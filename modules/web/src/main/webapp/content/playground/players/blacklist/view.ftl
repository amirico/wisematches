<#-- @ftlvariable name="blacklist" type="java.util.Collection<wisematches.server.services.blacklist.BlacklistRecord>" -->
<#include "/core.ftl">

<@wm.ui.table.dtinit/>

<@wm.ui.playground id="blacklistWidget">
    <@wm.ui.table.header>
        <@message code="game.menu.players.label"/> > <@message code="game.menu.blacklist.label"/>
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar align="left">
    &nbsp;
    </@wm.ui.table.toolbar>

    <@wm.ui.table.content>
    <table id="blacklist" width="100%" class="display">
        <thead>
        <tr>
            <th>
                <input title="select all player" type="checkbox" id="removeAll" name="removeAll" value="true"
                       onchange="wm.blacklist.selectAll()">
            </th>
            <th><@message code="blacklist.column.player"/></th>
            <th><@message code="blacklist.column.since"/></th>
            <th width="100%"><@message code="blacklist.column.comment"/></th>
        </tr>
        </thead>
        <tbody>
            <#list blacklist as r>
            <tr id="blacklist${r.whom}" class="blacklist ui-state-default">
                <td class="blacklist-checkbox">
                    <input type="checkbox" name="persons" value="${r.whom}">
                </td>
                <td>
                    <div class="blacklist-from"><@wm.player.name personalityManager.getPlayer(r.whom)/></div>
                </td>
                <td>
                    <div class="blacklist-date">
                    ${gameMessageSource.formatDate(r.since, locale)} ${gameMessageSource.formatTime(r.since, locale)}
                    </div>
                </td>
                <td width="100%">
                    <div class="blacklist-text">
                    ${gameMessageSource.stringToHTMLString(r.message)}
                    </div>

                    <div class="blacklist-controls">
                        <a href="#" onclick="wm.blacklist.remove([${r.whom}])">
                            <@message code="blacklist.trustworthy.single"/>
                        </a>
                    </div>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
    </@wm.ui.table.content>

    <@wm.ui.table.statusbar align="left">
    <button class="wm-ui-button" type="submit" style="margin-left: 0" onclick="wm.blacklist.removeSelected();">
        <@message code="blacklist.trustworthy.selected"/>
    </button>
    </@wm.ui.table.statusbar>

    <@wm.ui.table.footer/>
</@wm.ui.playground>

<script type="text/javascript">
    wm.blacklist = new function () {
        var widget = $("#blacklistWidget");
        wm.ui.dataTable('#blacklist', {
            "bSortClasses": false,
            "aoColumns": [
                { "bSortable": false },
                { "bSortable": true },
                { "bSortable": true },
                { "bSortable": false }
            ]
        });

        this.selectAll = function () {
            $(".blacklist-checkbox input").prop("checked", $("#removeAll").prop("checked"));
            return false;
        };

        this.remove = function (ids) {
            wm.ui.lock(widget, "<@message code="blacklist.status.removing"/>");
            $.ajax('remove.ajax', {
                type: 'post',
                contentType: 'application/x-www-form-urlencoded',
                data: {'persons[]': ids}
            })
                    .success(function (response) {
                        if (response.success) {
                            var dataTable = $('#blacklist').dataTable();
                            $.each(ids, function (i, v) {
                                dataTable.fnDeleteRow($("#blacklist #blacklist" + v).get(0));
                            });
                            wm.ui.unlock(widget, "<@message code="blacklist.status.removed"/>");
                        } else {
                            wm.ui.unlock(widget, response.summary, true);
                        }
                    });
            return false;
        };

        this.removeSelected = function () {
            var selected = [];
            $(".blacklist-checkbox input:checked").each(function (index, el) {
                selected.push($(el).val());
            });
            if (selected.length != 0) {
                wm.blacklist.remove(selected);
            }
            return false;
        };
    };
</script>
