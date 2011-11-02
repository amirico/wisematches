<#-- @ftlvariable name="messages" type="java.util.Collection<wisematches.playground.message.impl.HibernateMessage>" -->
<#include "/core.ftl">

<@wm.jstable/>

<@wm.playground id="messagesWidget">
<div>
    <div style="float: left;">
        <button type="submit" style="margin-left: 0" onclick="wm.messages.removeSelected();">
            <@message code="messages.delete.selected"/>
        </button>
    </div>
</div>

<table id="messages" width="100%" class="display">
    <thead>
    <tr>
        <th>
            <input title="select all messages" type="checkbox" id="removeAll" name="removeAll" value="true"
                   onchange="wm.messages.selectAll()">
        </th>
        <th nowrap="nowrap"><@message code="messages.column.to"/></th>
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
                <div class="message-from"><@wm.player player=playerManager.getPlayer(m.recipient)/></div>
                <div class="message-date">
                ${gameMessageSource.formatDate(m.creationDate, locale)} ${gameMessageSource.formatTime(m.creationDate, locale)}
                </div>
            </td>
            <td width="100%">
                <div class="message-text">
                ${gameMessageSource.stringToHTMLString(m.text)}
                </div>

                <div class="message-controls">
                    <a href="#" onclick="wm.messages.remove([${m.id}])"><@message code="messages.delete.single"/></a>
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
        "bJQueryUI":true,
        "bSortClasses":false,
        "aaSorting":[
        ],
        "aoColumns":[
            { "bSortable":false },
            { "bSortable":false },
            { "bSortable":false }
        ],
        "sPaginationType":"full_numbers"
    });

    wm.messages = $.extend({}, wm.messages, new function() {
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
            $.ajax('remove.ajax?sent=true', {
                type:'post',
                contentType:'application/x-www-form-urlencoded',
                data:{'messages[]':msgs}
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
