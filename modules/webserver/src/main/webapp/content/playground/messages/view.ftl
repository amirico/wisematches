<#-- @ftlvariable name="messages" type="java.util.Collection<wisematches.playground.message.Message>" -->
<#include "/core.ftl">

<#include "/content/playground/ignores/scriplet.ftl">

<style type="text/css">
    .message {
        font-weight: normal;
        border: none;
    }

    .message-text {
        text-align: justify;
    }

    .message-date {
        white-space: nowrap;
    }

    .message-controls {
        float: right;
        font-weight: bold;
    }

    #messages tr .message-controls a {
        color: #cccccc;
        text-decoration: underline;
    }

    #messages tr:hover .message-controls a {
        color: inherit;
        text-decoration: underline;
    }

    #messages tr.odd td, #messages tr.even td {
        vertical-align: top;
        border-bottom-style: solid;
    }
</style>

<@wm.jstable/>

<@wm.playground id="messagesWidget">
<div>
    <div style="float: left;">
        <button type="submit" style="margin-left: 0" onclick="wm.messages.removeSelected();">
            Delete Selected
        </button>
    </div>

    <div style="float: right;">
        <a href="/playground/messages/sent">Sent messages</a>
        •
        <a href="/playground/players/ignores">My ignore list</a>
    </div>
</div>

<table id="messages" width="100%" class="display">
    <thead>
    <tr>
        <th>
            <input title="select all messages" type="checkbox" id="removeAll" name="removeAll" value="true"
                   onchange="wm.messages.selectAll()">
        </th>
        <th>From/Date</th>
        <th width="100%">Message</th>
    </tr>
    </thead>
    <tbody>
        <#list messages as m>
        <tr id="message${m.id}" class="message ui-state-default">
            <td class="message-checkbox">
                <input type="checkbox" name="removeList" value="${m.id}">
            </td>
            <td>
                <#if m.sender != 0>
                    <div class="message-from"><@wm.player player=playerManager.getPlayer(m.sender)/></div>
                </#if>
                <div class="message-date">
                ${gameMessageSource.formatDate(m.created, locale)} ${gameMessageSource.formatTime(m.created, locale)}
                </div>
            </td>
            <td width="100%">
                <div class="message-text">
                ${gameMessageSource.stringToHTMLString(m.text)}
                </div>

                <div class="message-controls">
                    <#if !m.notification>
                        <a title="Replay to the message"
                           href="/playground/messages/replay?m=${m.id}">Replay</a>
                        <#if m.original != 0>
                            &nbsp;
                            <a title="This message has been replied to"
                               href="#">Previous Message</a>
                        </#if>
                        &nbsp;&nbsp;&nbsp;
                        <a title="Report abuse" href="#" onclick="wm.messages.reportAbuse(${m.id});">Abuse</a>
                        &nbsp;
                    <@ignore_link pid=m.sender>Ignore</@ignore_link>
                        &nbsp;
                    </#if>
                    <a title="Delete the message"
                       href="#" onclick="wm.messages.remove([${m.id}])">Delete</a>
                </div>
            </td>
        </tr>
        </#list>
    </tbody>
</table>

<div>
    <button style="margin-left: 0" onclick="wm.messages.removeSelected();">
        Delete Selected
    </button>
</div>
</@wm.playground>

<script type="text/javascript">
    $("#messagesWidget button").button();

    $('#messages').dataTable({
        "bJQueryUI": true,
        "bSortClasses": false,
        "aoColumns": [
            { "bSortable": false },
            { "bSortable": false },
            { "bSortable": false }
        ],
        "sPaginationType": "full_numbers"
    });

    wm.messages = new function() {
        this.reportAbuse = function(id) {
            wm.ui.showStatus("Sending abuse report. Please wait...");
            $.post('/playground/messages/abuse.ajax?m=' + id, function(result) {
                if (result.success) {
                    wm.ui.showStatus("Abuse report has been sent");
                } else {
                    wm.ui.showStatus(result.summary, true);
                }
            });
        };

        this.selectAll = function() {
            $(".message-checkbox input").prop("checked", $("#removeAll").prop("checked"));
        };

        this.removeSelected = function() {
            var selected = new Array();
            $(".message-checkbox input:checked").each(function(index, el) {
                selected.push($(el).val());
            });
            wm.messages.remove(selected);
        };

        this.remove = function(msgs) {
            wm.ui.showStatus("Removing messages. Please wait...", false, true);
            $.ajax('remove.ajax', {
                type: 'post',
                contentType: 'application/x-www-form-urlencoded',
                data:  {'messages[]': msgs}
            })
                    .success(function(response) {
                        if (response.success) {
                            var dataTable = $('#messages').dataTable();
                            $.each(msgs, function(i, v) {
                                dataTable.fnDeleteRow($("#messages #message" + v).get(0));
                            });
                            wm.ui.showStatus("Message has been removed");
                        } else {
                            wm.ui.showStatus("Message can't be removed", true, false);
                        }
                    })
                    .error(function(jqXHR, textStatus, errorThrown) {
                        wm.ui.showStatus("Message can't be removed", true, false);
                    });
            return false;
        };
    };
</script>
