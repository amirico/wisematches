<#-- @ftlvariable name="friends" type="java.util.Collection<wisematches.server.web.services.friends.FriendRelation>" -->
<#include "/core.ftl">

<@wm.jstable/>
<#include "/content/playground/messages/scriplet.ftl">

<@wm.playground id="friendsWidget">
<div>
    <div>
        <button type="submit" style="margin-left: 0" onclick="wm.friends.removeSelected();">
        <@message code="messages.delete.selected"/>
        </button>
    </div>
</div>

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
                <@privateMessage pid=f.friend><@message code="messages.label"/></@privateMessage>
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

<div>
    <button style="margin-left: 0" onclick="wm.friends.removeSelected();">
    <@message code="messages.delete.selected"/>
    </button>
</div>
</@wm.playground>

<script type="text/javascript">
    $("#friendsWidget button").button();

    $('#friends').dataTable({
        "bJQueryUI": true,
        "bSortClasses": false,
        "aoColumns": [
            { "bSortable": false },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": false }
        ],
        "sPaginationType": "full_numbers"
    });

    wm.friends = $.extend({}, wm.friends, new function() {
        this.selectAll = function() {
            $(".friend-checkbox input").prop("checked", $("#removeAll").prop("checked"));
            return false;
        };

        this.removeSelected = function() {
            var selected = new Array();
            $(".friend-checkbox input:checked").each(function(index, el) {
                selected.push($(el).val());
            });
            wm.friends.remove(selected);
            return false;
        };

        this.remove = function(persons) {
            wm.ui.showStatus("<@message code="friends.status.remove.sending"/>", false, true);
            $.ajax('remove.ajax', {
                type: 'post',
                contentType: 'application/x-www-form-urlencoded',
                data:  {'persons[]': persons}
            })
                    .success(function(response) {
                        if (response.success) {
                            var dataTable = $('#friends').dataTable();
                            $.each(persons, function(i, v) {
                                dataTable.fnDeleteRow($("#friends #friend" + v).get(0));
                            });
                            wm.ui.showStatus("<@message code="friends.status.remove.sent"/>");
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
