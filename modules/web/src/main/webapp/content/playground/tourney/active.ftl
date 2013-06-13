<#-- @ftlvariable name="announce" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="divisionsIterator" type="wisematches.playground.tourney.regular.TourneyEntryIterator<wisematches.playground.tourney.regular.TourneyDivision>" -->

<#include "/core.ftl">

<@wm.ui.table.dtinit/>

<@wm.ui.playground id="tourneyWidget">
<table id="tourney" width="100%">
    <tr>
    <td width="100%" valign="top">
        <div id="activeTourneys">
            <@wm.ui.table.header align="left">
                <@message code="tourney.tourney.label"/> > <@message code="tourney.all.active.label"/>
            </@wm.ui.table.header>

            <@wm.ui.table.toolbar>
                <div class="wm-ui-buttonset">
                    <a href="/playground/tourney"><@message code="tourney.participated.label"/></a>
                    <a href="/playground/tourney/finished"><@message code="tourney.all.finished.label"/></a>
                </div>
            </@wm.ui.table.toolbar>

            <@wm.ui.table.content>
                <table width="100%" class="display">
                    <thead>
                    <tr>
                        <th><@message code="tourney.tourney.label"/></th>
                        <th><@message code="tourney.language.label"/></th>
                        <th><@message code="tourney.level.label"/></th>
                        <th><@message code="tourney.started.label"/></th>
                        <th><@message code="tourney.round.label"/></th>
                    </tr>
                    </thead>
                    <tbody>
                        <#list divisionsIterator.itemsEntry as e>
                            <#assign tourney=e.key/>
                            <#list e.value as division>
                            <tr>
                                <td>
                                    <@wm.tourney.tourney tourney.id, true/> <@wm.tourney.dates tourney "sample"/>
                                </td>
                                <td style="padding-left: 30px">
                                    <@wm.tourney.language division.language/>
                                </td>
                                <td>
                                    <@wm.tourney.section division.section/>
                                </td>
                                <td>
                                ${messageSource.formatDate(division.startedDate, locale)}
                                </td>
                                <td>
                                    <#assign activeRound=division.activeRound!"">
                                    <#list 1..division.roundsCount as r>
                                        <#assign final=false/>
                                        <#if !r_has_next>
                                            <#assign final=!activeRound?has_content || activeRound.final/>
                                        </#if>
                                        <#assign roundId={"divisionId": division.id, "round": r}/>
                                        <@wm.tourney.round roundId, true, final/><#if r_has_next>, <#else></#if>
                                    </#list>
                                    (<@message code="tourney.completed.label"/>:
                                ${((activeRound.finishedGamesCount / activeRound.totalGamesCount)*100)?round}%)
                                </td>
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
    wm.ui.dataTable('#activeTourneys table', {
        "fnDrawCallback": wm.ui.table.groupColumnDrawCallback('#activeTourneys'),
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
            { "bSortable": true },
            { "bSortable": true }
        ]
    });
</script>
