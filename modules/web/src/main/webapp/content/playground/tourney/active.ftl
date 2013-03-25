<#-- @ftlvariable name="announce" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="divisionsTree" type="wisematches.playground.tourney.regular.TourneyTree" -->

<#include "/core.ftl">

<@wm.ui.table.dtinit/>

<#macro divisionsInfo tourney language>
    <#list divisionsTree.getDivisions(tourney) as d>
        <#if d.language = language>
        <div>
            <@wm.tourney.section d.section/>,
            <@wm.tourney.round {"round":d.activeRound, "divisionId":d.id}, true/>
        </div>
        </#if>
    </#list>
</#macro>

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
                        <th rowspan="2"><@message code="tourney.tourney.label"/></th>
                        <th rowspan="2"><@message code="tourney.started.label"/></th>
                        <th colspan="${Language.values()?size?string}"
                            class="ui-state-default"
                            style="font-size: small; border-bottom: 1px solid !important; white-space: nowrap">
                            <@message code="tourney.active.rs"/>
                        </th>
                        <th rowspan="2" width="100%"></th>
                    </tr>
                    <tr>
                        <#list Language.values()?reverse as l>
                            <th><@wm.tourney.language l/></th>
                        </#list>
                    </tr>
                    </thead>
                    <tbody>
                        <#list divisionsTree.tourneys as tourney>
                        <td>
                            <@wm.tourney.tourney tourney.id, true/>
                        </td>
                        <td>
                        ${messageSource.formatDate(tourney.startedDate, locale)}
                        </td>
                            <#list Language.values()?reverse as l>
                            <td>
                                <@divisionsInfo tourney=tourney language=l/>
                            </td>
                            </#list>
                        <td>&nbsp;</td>
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
    wm.ui.dataTable('#tourneyWidget #activeTourneys table', {
        "bSortClasses": false,
        "aoColumns": [
            { "bSortable": true },
            { "bSortable": true },
        <#list Language.values() as l>
            { "bSortable": true },
        </#list>
            { "bSortable": false }
        ]
    });
</script>
