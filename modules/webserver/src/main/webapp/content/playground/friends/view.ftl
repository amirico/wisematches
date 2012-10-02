<#-- @ftlvariable name="friends" type="java.util.Collection<wisematches.playground.friends.FriendRelation>" -->
<#include "/core.ftl">

<@wm.jstable/>
<#include "/content/playground/messages/scriplet.ftl">

<@wm.playground id="friendsWidget">
    <@wm.dtHeader>
        <@message code="game.menu.friends.label"/>
    </@wm.dtHeader>

    <@wm.dtToolbar align="left">
    <button type="submit" style="margin-left: 0" onclick="wm.friends.removeSelected();">
        <@message code="messages.delete.selected"/>
    </button>
    </@wm.dtToolbar>

    <@wm.dtContent>
    <table id="friends" width="100%" class="display">
        <thead>
        <tr>
            <th>
                <input title="select all friends" type="checkbox" id="removeAll" name="removeAll" value="true"
                       onchange="wm.friends.selectAll()">
            </th>
            <th nowrap="nowrap"><@message code="friends.column.player"/></th>
            <th nowrap="nowrap"><@message code="friends.column.since"/></th>
            <th width="100%"><@message code="friends.column.comment"/></th>
        </tr>
        </thead>
        <tbody>
            <#list friends as f>
            <tr id="friend${f.friend}" class="friend ui-state-default">
                <td class="friend-checkbox">
                    <input type="checkbox" name="removeList" value="${f.friend}">
                </td>
                <td nowrap="nowrap">
                    <div class="friend-name"><@wm.player player=playerManager.getPlayer(f.friend)/></div>
                </td>
                <td nowrap="nowrap">
                    <div class="friends-registered">
                    ${gameMessageSource.formatDate(f.registered, locale)} ${gameMessageSource.formatTime(f.registered, locale)}
                    </div>
                </td>
                <td width="100%">
                    <div class="friend-text">
                    ${f.comment}
                    </div>

                    <div class="friend-controls">
                        <@privateMessage pid=f.friend><@message code="messages.send.label"/></@privateMessage>
                        &nbsp;
                        &nbsp;
                        &nbsp;
                        <a href="#"
                           onclick="wm.friends.remove([${f.friend}])"><@message code="messages.delete.single"/></a>
                    </div>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
    </@wm.dtContent>

    <@wm.dtFooter/>
</@wm.playground>

<script type="text/javascript">
    wm.friends = $.extend({}, wm.friends, new function () {
        var widget = $("#friendsWidget");
        $("#friendsWidget button").button();

        wm.ui.dataTable('#friends', {
            "bSortClasses":false,
            "aoColumns":[
                { "bSortable":false },
                { "bSortable":true },
                { "bSortable":true },
                { "bSortable":false }
            ]
        });

        this.selectAll = function () {
            $(".friend-checkbox input").prop("checked", $("#removeAll").prop("checked"));
            return false;
        };

        this.removeSelected = function () {
            var selected = new Array();
            $(".friend-checkbox input:checked").each(function (index, el) {
                selected.push($(el).val());
            });
            wm.friends.remove(selected);
            return false;
        };

        this.remove = function (persons) {
            wm.ui.lock(widget, "<@message code="friends.status.removing"/>");
            $.ajax('remove.ajax', {
                type:'post',
                contentType:'application/x-www-form-urlencoded',
                data:{'persons[]':persons}
            })
                    .success(function (response) {
                        if (response.success) {
                            var dataTable = $('#friends').dataTable();
                            $.each(persons, function (i, v) {
                                dataTable.fnDeleteRow($("#friends #friend" + v).get(0));
                            });
                            wm.ui.unlock(widget, "<@message code="friends.status.removed"/>");
                        } else {
                            wm.ui.unlock(widget, response.summary, true);
                        }
                    });
            return false;
        };
    });
</script>
