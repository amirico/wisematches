<#-- @ftlvariable name="tourney" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="sections" type="wisematches.playground.tourney.regular.TourneySection[]" -->
<#-- @ftlvariable name="languages" type="wisematches.personality.Language[]" -->
<#-- @ftlvariable name="winnerPlaces" type="wisematches.playground.tourney.regular.WinnerPlace[]" -->
<#-- @ftlvariable name="divisionsTree" type="wisematches.playground.tourney.regular.TourneyTree" -->

<#include "/core.ftl">
<#include "scriplet.ftl">

<@wm.playground id="tourneyWidget">
<div id="divisions">
    <@wm.dtHeader align="left">
        <@message code="tourney.tourney.label"/> > <@tourneyName tourneyId=tourney link=false/>
    </@wm.dtHeader>

    <@wm.dtToolbar align="left">
        <table width="100%">
            <tr>
                <td class="tourney-name ui-state-active" style="width: 100%">
                    <@tourneyName tourneyId=tourney link=false/>
                </td>
                <td rowspan="2" class="sample" style="text-align: right; vertical-align: bottom; white-space: nowrap">
                    <@message code="tourney.limitation.language.label"/>
                    <strong><@message code="language.${language.name()?lower_case}"/></strong>.
                </td>
            </tr>
            <tr>
                <td>
                    <#if tourney.startedDate??>
                        <strong><@message code="tourney.started.label"/></strong> ${gameMessageSource.formatDate(tourney.startedDate, locale)}
                    </#if>
                    <#if tourney.finishedDate??>
                        ,
                        <strong><@message code="tourney.finished.label"/></strong> ${gameMessageSource.formatDate(tourney.finishedDate, locale)}
                    </#if>
                </td>
            </tr>
        </table>
    </@wm.dtToolbar>

    <@wm.dtContent wrap=true>
        <#list sections as s>
            <#assign division=divisionsTree.getDivision(tourney, language, s)!""/>
            <#if division?has_content>
                <div class="division">
                    <div class="division-name">
                        <strong><@sectionName section=s/> <@message code="tourney.level.label"/></strong>: <span
                            class="sample">(<@sectionInfo section=s short=false/>)</span>
                    </div>
                    <div class="division-rounds">
                        <#list divisionsTree.getRounds(division) as r>
                            <@roundName r.id, true, r.final/><#if r_has_next>, </#if>
                        </#list>
                        <#if division.finishedDate??>
                            <span class="sample"> - <@message code="tourney.finished.label"/> ${gameMessageSource.formatDate(division.finishedDate, locale)}</span>
                        </#if>
                    </div>
                    <#if division.finishedDate??>
                        <div class="division-winners">
                            <dl>
                                <dt><@message code="tourney.winners.label"/>:</dt>
                                <#list winnerPlaces as p>
                                    <#assign players=p.filter(division.tourneyWinners)/>
                                    <#if players?has_content>
                                        <dd>
                                            <@message code="tourney.place.${p.place}.label"/>:
                                            <#list players as p>
                                                <@wm.player player=playerManager.getPlayer(p.player)/>
                                                <#if p_has_next>, </#if>
                                            </#list>
                                        </dd>
                                    </#if>
                                </#list>
                            </dl>
                        </div>
                    </#if>
                </div>
            </#if>
        </#list>
    </@wm.dtContent>

    <@wm.dtFooter/>
</div>
</@wm.playground>