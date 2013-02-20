<#-- @ftlvariable name="tourney" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="sections" type="wisematches.playground.tourney.regular.TourneySection[]" -->
<#-- @ftlvariable name="winnerPlaces" type="wisematches.playground.tourney.TourneyPlace[]" -->
<#-- @ftlvariable name="divisionsTree" type="wisematches.playground.tourney.regular.TourneyTree" -->

<#include "/core.ftl">

<@wm.ui.playground id="tourneyWidget">
<div id="divisions">
    <@wm.ui.table.header align="left">
        <@message code="tourney.tourney.label"/> > <@wm.tourney.tourney tourney, false/>
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar align="left">
        <table width="100%">
            <tr>
                <td class="tourney-name ui-state-active" style="width: 100%">
                    <@wm.tourney.tourney tourney, false/>
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
    </@wm.ui.table.toolbar>

    <@wm.ui.table.content wrap=true>
        <#list sections as s>
            <#assign division=divisionsTree.getDivision(tourney, language, s)!""/>
            <#if division?has_content>
                <div class="division">
                    <div class="division-name">
                        <strong><@wm.tourney.section s/> <@message code="tourney.level.label"/></strong>: <span
                            class="sample">(<@wm.tourney.rating s, false/>)</span>
                    </div>
                    <div class="division-rounds">
                        <#list divisionsTree.getRounds(division) as r>
                            <@wm.tourney.round r.id, true, r.final/><#if r_has_next>, <#else></#if>
                        </#list>
                        <#if division.finishedDate??>
                            <span class="sample">
                                — <@messageLower code="tourney.finished.label"/> ${gameMessageSource.formatDate(division.finishedDate, locale)}
                                </span>
                        <#else>
                            <#assign activeRound=divisionsTree.getRound(division, division.activeRound)/>
                        <#-- TODO: Issue 242 -->
                            (<@message code="tourney.completed.label"/>:
                        ${((activeRound.finishedGamesCount / activeRound.totalGamesCount)*100)?round}%)
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
                                                <@wm.player.name personalityManager.getMember(p.player)/>
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
    </@wm.ui.table.content>

    <@wm.ui.table.footer/>
</div>
</@wm.ui.playground>