<#-- @ftlvariable name="blacklist" type="java.util.Collection<wisematches.playground.blacklist.BlacklistRecord>" -->
<#include "/core.ftl">

<@wm.jstable/>

<@wm.playground id="blacklistWidget">
    <@wm.dtHeader>
        <@message code="game.menu.blacklist.label"/>
    </@wm.dtHeader>

    <@wm.dtToolbar align="left">
    <button type="submit" style="margin-left: 0" onclick="wm.blacklist.removeSelected();">
        <@message code="blacklist.trustworthy.selected"/>
    </button>
    </@wm.dtToolbar>

    <@wm.dtContent>
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
                    <div class="blacklist-from"><@wm.player player=playerManager.getPlayer(r.whom)/></div>
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
    </@wm.dtContent>

    <@wm.dtFooter/>
</@wm.playground>

<script type="text/javascript">
    wm.blacklist = new function () {
        var widget = $("#blacklistWidget");
        $("#blacklistWidget button").button();

        wm.ui.dataTable('#blacklist', {
            "bSortClasses":false,
            "aoColumns":[
                { "bSortable":false },
                { "bSortable":true },
                { "bSortable":true },
                { "bSortable":false }
            ]
        });

        this.selectAll = function () {
            $(".blacklist-checkbox input").prop("checked", $("#removeAll").prop("checked"));
            return false;
        };

        this.remove = function (ids) {
            wm.ui.lock(widget, "<@message code="blacklist.status.removing"/>");
            $.ajax('remove.ajax', {
                type:'post',
                contentType:'application/x-www-form-urlencoded',
                data:{'persons[]':ids}
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
            var selected = new Array();
            $(".blacklist-checkbox input:checked").each(function (index, el) {
                selected.push($(el).val());
            });
            wm.blacklist.remove(selected);
            return false;
        };
    };
</script>
