<#-- @ftlvariable name="sections" type="wisematches.playground.tourney.regular.TourneySection[]" -->
<#-- @ftlvariable name="languages" type="wisematches.personality.Language[]" -->
<#-- @ftlvariable name="announce" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="winnerPlaces" type="wisematches.playground.tourney.TourneyPlace[]" -->
<#-- @ftlvariable name="divisionsTree" type="wisematches.playground.tourney.regular.TourneyTree" -->

<#include "/core.ftl">

<@wm.ui.table.dtinit/>

<@wm.ui.playground id="tourneyWidget">
<table id="tourney" width="100%">
    <tr>
    <td width="100%" valign="top">
        <div id="finishedTourneys">
            <@wm.ui.table.header align="left">
                <@message code="tourney.tourney.label"/> > <@message code="tourney.all.finished.label"/>
            </@wm.ui.table.header>

            <@wm.ui.table.toolbar>
                <div class="wm-ui-buttonset">
                    <a href="/playground/tourney"><@message code="tourney.participated.label"/></a>
                    <a href="/playground/tourney/active"><@message code="tourney.all.active.label"/></a>
                </div>
            </@wm.ui.table.toolbar>

            <@wm.ui.table.content>
                <table width="100%" class="display">
                    <thead>
                    <tr>
                        <th rowspan="2"><@message code="tourney.tourney.label"/></th>
                        <th rowspan="2"><@message code="tourney.language.label"/></th>
                        <th rowspan="2"><@message code="tourney.level.label"/></th>
                        <th colspan="${(winnerPlaces?size)?string}"
                            class="ui-state-default"
                            style="font-size: small; border-bottom: 1px solid !important; white-space: nowrap">
                            <@message code="tourney.winners.label"/>
                        </th>
                        <th rowspan="2" width="100%"></th>
                    </tr>
                    <tr>
                        <#list winnerPlaces as p>
                            <th>
                                <@message code="tourney.place.${p.place}.label"/>
                            </th>
                        </#list>
                    </tr>
                    </thead>
                    <tbody>
                        <#list divisionsTree.tourneys as tourney>
                            <#assign divisions=divisionsTree.getDivisions(tourney)/>
                            <#list divisions as d>
                            <tr>
                                <td>
                                    <@wm.tourney.tourney tourney.id, true/>

                                    <span class="sample">
                                        (<@message code="tourney.started.label"/>:
                                    ${gameMessageSource.formatDate(tourney.startedDate, locale)},
                                        <@message code="tourney.finished.label"/>:
                                    ${gameMessageSource.formatDate(tourney.finishedDate, locale)})
                                    </span>
                                </td>

                                <td>
                                    <@wm.tourney.language d.language/>
                                </td>
                                <td>
                                    <@wm.tourney.section d.section/>
                                </td>

                                <#list winnerPlaces as p>
                                    <td>
                                        <#list p.filter(d.tourneyWinners) as w>
                                            <div>
                                                <@wm.player.name player=playerManager.getPlayer(w.player)/>
                                            </div>
                                        </#list>
                                    </td>
                                </#list>

                                <td>&nbsp;</td>
                            </tr>
                            </#list>
                        </#list>
                    </tbody>
                </table>
            </@wm.ui.table.content>

            <@wm.ui.table.footer/>
        </div>
        <#if announce??>
            <td valign="top">
                <#include "announce.ftl">
            </td>
        </#if>
    </tr>
</table>
</@wm.ui.playground>

<script type="text/javascript">
    wm.ui.dataTable('#tourneyWidget #finishedTourneys table', {
        "fnDrawCallback": function (oSettings) {
            if (oSettings.aiDisplay.length == 0) {
                return;
            }

            var nTrs = $('#tourneyWidget').find('#finishedTourneys tbody tr');
            var iColspan = nTrs[0].getElementsByTagName('td').length;
            var sLastGroup = "";
            for (var i = 0; i < nTrs.length; i++) {
                var iDisplayIndex = oSettings._iDisplayStart + i;
                var sGroup = oSettings.aoData[ oSettings.aiDisplay[iDisplayIndex] ]._aData[0];
                if (sGroup != sLastGroup) {
                    var nGroup = document.createElement('tr');
                    var nCell = document.createElement('td');
                    nCell.colSpan = iColspan;
                    nCell.className = "group";
                    nCell.innerHTML = sGroup;
                    nGroup.appendChild(nCell);
                    nTrs[i].parentNode.insertBefore(nGroup, nTrs[i]);
                    sLastGroup = sGroup;
                }
            }
        },
        "aoColumnDefs": [
            { "bVisible": false, "aTargets": [ 0 ] }
        ],
        "aaSortingFixed": [
            [ 0, 'asc' ]
        ],
        "bSortClasses": false,
        "aoColumns": [
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": false },
            { "bSortable": false },
            { "bSortable": false },
            { "bSortable": false }
        ]
    });
</script>
