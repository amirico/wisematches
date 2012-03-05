<#-- @ftlvariable name="blacklist" type="java.util.Collection<wisematches.playground.blacklist.BlacklistRecord>" -->
<#include "/core.ftl">

<@wm.jstable/>

<@wm.playground id="blacklistWidget">
<div>
    <div style="float: left;">
        <button type="submit" style="margin-left: 0" onclick="wm.blacklist.removeSelected();">
        <@message code="blacklist.trustworthy.selected"/>
        </button>
    </div>
</div>

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

<div>
    <button style="margin-left: 0" onclick="wm.blacklist.removeSelected();">
    <@message code="blacklist.trustworthy.selected"/>
    </button>
</div>
</@wm.playground>

<script type="text/javascript">
    $("#blacklistWidget button").button();

    wm.ui.dataTable('#blacklist', {
        "bSortClasses": false,
        "aoColumns": [
            { "bSortable": false },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": false }
        ],
        "sPaginationType": "full_numbers"
    });

    wm.blacklist = new function() {
        this.selectAll = function() {
            $(".blacklist-checkbox input").prop("checked", $("#removeAll").prop("checked"));
            return false;
        };

        this.remove = function(ids) {
            wm.ui.showStatus("Removing messages. Please wait...", false, true);
            $.ajax('remove.ajax', {
                type: 'post',
                contentType: 'application/x-www-form-urlencoded',
                data:  {'persons[]': ids}
            })
                    .success(function(response) {
                        if (response.success) {
                            var dataTable = $('#blacklist').dataTable();
                            $.each(ids, function(i, v) {
                                dataTable.fnDeleteRow($("#blacklist #blacklist" + v).get(0));
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

        this.removeSelected = function() {
            var selected = new Array();
            $(".blacklist-checkbox input:checked").each(function(index, el) {
                selected.push($(el).val());
            });
            wm.messages.remove(selected);
            return false;
        };
    };
</script>
