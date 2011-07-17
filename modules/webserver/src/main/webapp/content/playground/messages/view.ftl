<#-- @ftlvariable name="messages" type="java.util.Collection<wisematches.playground.message.Message>" -->
<#include "/core.ftl">

<@wm.jstable/>
<#include "/content/playground/messages/scriplet.ftl">
<#include "/content/playground/blacklist/scriplet.ftl">

<@wm.playground id="messagesWidget">
<div>
    <div style="float: left;">
        <button type="submit" style="margin-left: 0" onclick="wm.messages.removeSelected();">
        <@message code="messages.delete.selected"/>
        </button>
    </div>

    <div style="float: right;">
        <a href="/playground/blacklist/view"><@message code="messages.blacklist"/></a>
    </div>
</div>

<table id="messages" width="100%" class="display">
    <thead>
    <tr>
        <th>
            <input title="select all messages" type="checkbox" id="removeAll" name="removeAll" value="true"
                   onchange="wm.messages.selectAll()">
        </th>
        <th nowrap="nowrap"><@message code="messages.column.from"/></th>
        <th width="100%"><@message code="messages.column.message"/></th>
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
                    <@replayMessage pid=m.id><@message code="messages.replay"/></@replayMessage>
                    <#--<#if m.original != 0>-->
                    <#--&nbsp;-->
                    <#--<a title="This message has been replied to"-->
                    <#--href="#">Previous Message</a>-->
                    <#--</#if>-->
                        &nbsp;&nbsp;&nbsp;
                        <a href="#"
                           onclick="wm.messages.reportAbuse(${m.id});"><@message code="messages.abuse"/></a>
                        &nbsp;
                    <@blacklist pid=m.sender><@message code="messages.ignore"/></@blacklist>
                        &nbsp;
                    </#if>
                    <a href="#"
                       onclick="wm.messages.remove([${m.id}])"><@message code="messages.delete.single"/></a>
                </div>
            </td>
        </tr>
        </#list>
    </tbody>
</table>

<div>
    <button style="margin-left: 0" onclick="wm.messages.removeSelected();">
    <@message code="messages.delete.selected"/>
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

    wm.messages = $.extend({}, wm.messages, new function() {
        this.reportAbuse = function(id) {
            wm.ui.showStatus("<@message code="messages.status.abuse.sending"/>");
            $.post('/playground/messages/abuse.ajax?m=' + id, function(result) {
                if (result.success) {
                    wm.ui.showStatus("<@message code="messages.status.abuse.sent"/>");
                } else {
                    wm.ui.showStatus(result.summary, true);
                }
            });
            return false;
        };

        this.selectAll = function() {
            $(".message-checkbox input").prop("checked", $("#removeAll").prop("checked"));
            return false;
        };

        this.removeSelected = function() {
            var selected = new Array();
            $(".message-checkbox input:checked").each(function(index, el) {
                selected.push($(el).val());
            });
            wm.messages.remove(selected);
            return false;
        };

        this.remove = function(msgs) {
            wm.ui.showStatus("<@message code="messages.status.remove.sending"/>", false, true);
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
                            wm.ui.showStatus("<@message code="messages.status.remove.sent"/>");
                        } else {
                            wm.ui.showStatus(result.summary, true);
                        }
                    })
                    .error(function(jqXHR, textStatus, errorThrown) {
                        wm.ui.showStatus(textStatus, true);
                    });
            return false;
        };
    });
</script>
