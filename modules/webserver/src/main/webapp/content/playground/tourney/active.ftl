<#-- @ftlvariable name="sections" type="wisematches.playground.tourney.regular.TourneySection[]" -->
<#-- @ftlvariable name="languages" type="wisematches.personality.Language[]" -->
<#-- @ftlvariable name="announce" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="divisionsTree" type="wisematches.playground.tourney.regular.TourneyTree" -->

<#include "/core.ftl">
<#include "scriplet.ftl">

<@wm.jstable/>

<#macro divisionsInfo tourney language>
    <#list divisionsTree.getDivisions(tourney) as d>
        <#if d.language = language>
        <div>
            <@sectionName section=d.section/>,
            <@roundName roundId={"round":d.activeRound, "divisionId":d.id} link=true/>
        </div>
        </#if>
    </#list>
</#macro>

<@wm.playground id="tourneyWidget">
<table id="tourney" width="100%">
    <tr>
    <td width="100%" valign="top">
        <div id="activeTourneys">
            <@wm.dtHeader align="left">
                <@message code="tourney.tourney.label"/> > <@message code="tourney.all.active.label"/>
            </@wm.dtHeader>

            <@wm.dtToolbar>
                <div>
                    <a href="/playground/tourney/finished"><@message code="tourney.all.finished.label"/></a>
                </div>
            </@wm.dtToolbar>

            <@wm.dtContent>
                <table width="100%" class="display">
                    <thead>
                    <tr>
                        <th rowspan="2"><@message code="tourney.tourney.label"/></th>
                        <th rowspan="2"><@message code="tourney.started.label"/></th>
                        <th colspan="${languages?size?string}"
                            class="ui-state-default"
                            style="font-size: small; border-bottom: 1px solid !important; white-space: nowrap">
                            <@message code="tourney.active.rs"/>
                        </th>
                        <th rowspan="2" width="100%"></th>
                    </tr>
                    <tr>
                        <#list languages?reverse as l>
                            <th><@languageName language=l/></th>
                        </#list>
                    </tr>
                    </thead>
                    <tbody>
                        <#list divisionsTree.tourneys as tourney>
                        <td>
                            <@tourneyName tourneyId=tourney.id link=true/>
                        </td>
                        <td>
                        ${gameMessageSource.formatDate(tourney.startedDate, locale)}
                        </td>
                            <#list languages?reverse as l>
                            <td>
                                <@divisionsInfo tourney=tourney language=l/>
                            </td>
                            </#list>
                        <td>&nbsp;</td>
                        </#list>
                    </tbody>
                </table>
            </@wm.dtContent>

            <@wm.dtFooter/>
        </div>
        <#if announce??>
            <td valign="top">
                <#include "announce.ftl">
            </td>
        </#if>
    </tr>
</table>
</@wm.playground>

<script type="text/javascript">
    wm.ui.dataTable('#tourneyWidget #activeTourneys table', {
        "bSortClasses": false,
        "aoColumns": [
            { "bSortable": true },
            { "bSortable": true },
        <#list languages as l>
            { "bSortable": true },
        </#list>
            { "bSortable": false }
        ]
    });
</script>
