<#-- @ftlvariable name="announce" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="divisionsIterator" type="wisematches.playground.tourney.regular.TourneyEntryIterator<wisematches.playground.tourney.regular.TourneyDivision>" -->

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
                        <th class="ui-state-default colgroup" colspan="${(TourneyPlace.values()?size)?string}">
                            <@message code="tourney.winners.label"/>
                        </th>
                        <th rowspan="2" width="100%"></th>
                    </tr>
                    <tr>
                        <#list TourneyPlace.values() as p>
                            <th>
                                <@message code="tourney.place.${p.place}.label"/>
                            </th>
                        </#list>
                    </tr>
                    </thead>
                    <tbody>
                        <#list divisionsIterator.itemsEntry as e>
                            <#assign tourney=e.key/>
                            <#list e.value as division>
                            <tr>
                                <td>
                                    <@wm.tourney.tourney tourney.id, true/>

                                    <span class="sample">
                                        (<@message code="tourney.started.label"/>:
                                    ${messageSource.formatDate(tourney.startedDate, locale)},
                                        <@message code="tourney.finished.label"/>:
                                    ${messageSource.formatDate(tourney.finishedDate, locale)})
                                    </span>
                                </td>

                                <td>
                                    <@wm.tourney.language division.language/>
                                </td>
                                <td>
                                    <@wm.tourney.section division.section/>
                                </td>

                                <#list TourneyPlace.values() as p>
                                    <td>
                                        <#list p.filter(division.tourneyWinners) as w>
                                            <div>
                                                <@wm.player.name personalityManager.getMember(w.player)/>
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
    wm.ui.dataTable('#finishedTourneys table', {
        "fnDrawCallback": wm.ui.table.groupColumnDrawCallback('#finishedTourneys'),
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
