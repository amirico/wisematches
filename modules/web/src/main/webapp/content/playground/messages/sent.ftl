<#-- @ftlvariable name="messages" type="java.util.Collection<wisematches.server.services.message.impl.HibernateMessage>" -->
<#include "/core.ftl">

<@wm.ui.table.dtinit/>

<@wm.ui.playground id="messagesWidget">
    <@wm.ui.table.header>
        <@message code="game.menu.messages.label"/> > <@message code="messages.sent"/>
    </@wm.ui.table.header>


    <@wm.ui.table.toolbar align="right">
    <a class="wm-ui-button" href="/playground/blacklist/view"><@message code="messages.blacklist"/></a>
    <a class="wm-ui-button" href="/playground/messages/view"><@message code="messages.received"/></a>
    </@wm.ui.table.toolbar>

    <@wm.ui.table.content>
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
                    <div class="message-from"><@wm.player.name personalityManager.getPlayer(m.recipient)/></div>
                    <div class="message-date">
                    ${gameMessageSource.formatDate(m.creationDate, locale)} ${gameMessageSource.formatTime(m.creationDate, locale)}
                    </div>
                </td>
                <td width="100%">
                    <div class="message-text">
                    ${gameMessageSource.stringToHTMLString(m.text)}
                    </div>

                    <div class="message-controls">
                        <a href="#"
                           onclick="wm.messages.remove([${m.id}])"><@message code="messages.delete.single"/></a>
                    </div>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
    </@wm.ui.table.content>

    <@wm.ui.table.statusbar align="left">
    <button class="wm-ui-button" style="margin-left: 0" onclick="wm.messages.removeSelected();">
        <@message code="messages.delete.selected"/>
    </button>
    </@wm.ui.table.statusbar>

    <@wm.ui.table.footer/>
</@wm.ui.playground>

<script type="text/javascript">
    wm.messages = $.extend({}, wm.messages, new function () {
        var widget = $("#messagesWidget");
        wm.ui.dataTable('#messages', {
            "bSortClasses": false,
            "aaSorting": [
            ],
            "aoColumns": [
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false }
            ]
        });

        this.selectAll = function () {
            $(".message-checkbox input").prop("checked", $("#removeAll").prop("checked"));
            return false;
        };

        this.removeSelected = function () {
            var selected = new Array();
            $(".message-checkbox input:checked").each(function (index, el) {
                selected.push($(el).val());
            });
            if (selected.length != 0) {
                wm.messages.remove(selected);
            }
            return false;
        };

        this.remove = function (msgs) {
            wm.ui.lock(widget, "<@message code="messages.status.remove.sending"/>");
            $.ajax('remove.ajax?sent=true', {
                type: 'post',
                contentType: 'application/x-www-form-urlencoded',
                data: {'messages[]': msgs}
            })
                    .success(function (response) {
                        if (response.success) {
                            var dataTable = $('#messages').dataTable();
                            $.each(msgs, function (i, v) {
                                dataTable.fnDeleteRow($("#messages #message" + v).get(0));
                            });
                            wm.ui.unlock(widget, "<@message code="messages.status.remove.sent"/>");
                        } else {
                            wm.ui.unlock(widget, response.summary, true);
                        }
                    });
            return false;
        };
    });
</script>
