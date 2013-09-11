<#-- @ftlvariable name="tourney" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="divisions" type="wisematches.playground.tourney.regular.TourneyDivision[]" -->

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
                        <strong><@message code="tourney.started.label"/></strong> ${messageSource.formatDate(tourney.startedDate, locale)}
                    </#if>
                    <#if tourney.finishedDate??>
                        ,
                        <strong><@message code="tourney.finished.label"/></strong> ${messageSource.formatDate(tourney.finishedDate, locale)}
                    </#if>
                </td>
            </tr>
        </table>
    </@wm.ui.table.toolbar>

    <@wm.ui.table.content wrap=true>
        <#list TourneySection.values() as s>

            <#list divisions as d>
                <#if d.language== language && d.section ==s>
                    <#assign division=d/>
                </#if>
            </#list>

            <#if division?has_content>
                <div class="division">
                    <div class="division-name">
                        <strong><@wm.tourney.section s/> <@message code="tourney.level.label"/></strong>: <span
                            class="sample">(<@wm.tourney.rating s, false/>)</span>
                    </div>
                    <div class="division-rounds">
                        <#assign activeRound=division.activeRound!"">

                        <#list 1..division.roundsCount as r>
                            <#assign final=false/>
                            <#if !r_has_next>
                                <#assign final=!activeRound?has_content || activeRound.final/>
                            </#if>
                            <#assign roundId={"divisionId": division.id, "round": r}/>
                            <@wm.tourney.round roundId, true, final/><#if r_has_next>, <#else></#if>
                        </#list>

                        <#if activeRound?has_content>
                        <#--TODO: Issue 242-->
                            (<@message code="tourney.completed.label"/>:
                        ${((activeRound.finishedGamesCount / activeRound.totalGamesCount)*100)?round}%)
                        <#else>
                            <span class="sample">
                                — <@messageLower code="tourney.finished.label"/> ${messageSource.formatDate(division.finishedDate, locale)}
                                </span>
                        </#if>
                    </div>
                    <#if division.finishedDate??>
                        <div class="division-winners">
                            <dl>
                                <dt><@message code="tourney.winners.label"/>:</dt>
                                <#list TourneyPlace.values() as p>
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