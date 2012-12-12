<#-- @ftlvariable name="tourney" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="round" type="wisematches.playground.tourney.regular.TourneyRound" -->
<#-- @ftlvariable name="groups" type="wisematches.playground.tourney.regular.TourneyGroup[]" -->

<#-- @ftlvariable name="currentPage" type="int" -->
<#-- @ftlvariable name="groupsCount" type="int" -->

<#include "/core.ftl">
<#include "../scriplet.ftl">

<#macro pages count current>
[
    <#list 0..(count-1)/30 as p>
        <#assign first=p*30+1/>
        <#assign last=p*30+30/>
        <#if (last>count)><#assign last=count/></#if>
        <#if p != current><a
                href="/playground/tourney/view?t=${tourney.number}&l=${round.division.language.ordinal()}&s=${round.division.section.ordinal()}&r=${round.round}&p=${p}"></#if>
    ${first}..${last}<#if p != current></a></#if>
        <#if p_has_next>|</#if>
    </#list>
]
</#macro>

<#macro successColor s>
    <#if s.points=0>#AA0033<#elseif s.points=1>#FF9900<#else>#008000</#if>
</#macro>

<@wm.playground id="tourneyWidget">
<div id="divisions">
    <@wm.dtHeader align="left">
        <@message code="tourney.tourney.label"/> > <@tourneyName tourney, false/>
    </@wm.dtHeader>

    <@wm.dtToolbar align="left">
        <div>
            <div class="tourney-name ui-state-active" style="display: block">
                <@tourneyName tourney, false/>
            </div>
            <div>
                <@message code="language.${round.division.language.name()?lower_case}"/>,
                <@sectionName round.division.section/> <@message code="tourney.level.label"/>,
                <@roundName round.id, true/>
            </div>
            <div class="sample" style="padding-left: 10px">
                (${gameMessageSource.formatDate(round.startedDate, locale)}
                <#if round.finishedDate??>— ${gameMessageSource.formatDate(round.finishedDate, locale)}</#if>)
            </div>
        </div>
    </@wm.dtToolbar>

    <@wm.dtContent wrap=true>
        <div class="pages" style="padding-bottom: 10px">
            <@pages groupsCount, currentPage/>
        </div>

        <#list groups as g>
            <div class="group">
                <div class="group-name">
                    <@message code="tourney.group.label"/> #${g.group}
                    <span class="sample">(<@message code="tourney.completed.label"/>
                    ${((g.finishedGamesCount / g.totalGamesCount)*100)?round}%)</span>:
                </div>
                <div class="group-content">
                    <table>
                        <tr>
                            <td class="ui-state-hover">#</td>
                            <#list g.players as p>
                                <td class="ui-state-hover">vs. #${p_index+1}</td>
                            </#list>
                            <td class="ui-state-hover"><@message code="tourney.games.label"/></td>
                            <td class="ui-state-hover"><@message code="tourney.score.label"/></td>
                        </tr>
                        <#list g.players as p>
                            <tr>
                                <td class="ui-state-default" style="padding-right: 40px">
                                ${p_index+1}. <@wm.player playerManager.getPlayer(p)/>
                                </td>
                                <#assign games=0/>
                                <#list g.players as o>
                                    <#assign success=g.getPlayerSuccess(p, o)!""/>
                                    <td class="ui-state-default">
                                        <#if o=p>
                                            —
                                        <#else>
                                            <#assign gameId=g.getGameId(p, o)/>
                                            <a href="/playground/scribble/board?b=${gameId}">
                                                <#if success?has_content>
                                                    <#assign games=games+1/>
                                                ${success.points/2}
                                                    <span style="color: <@successColor success/>">
                                                        <@message code="tourney.success.${success.name()?lower_case}.label"/>
                                                    </span>
                                                <#else>
                                                    #${gameId}
                                                </#if>
                                            </a>
                                        </#if>
                                    </td>
                                </#list>
                                <td class="ui-state-default">${games} / ${g.totalGamesCount}</td>
                                <td class="ui-state-default">${(g.getPlayerScores(p)/2)?round}</td>
                            </tr>
                        </#list>
                    </table>
                </div>
            </div>
        </#list>

        <div class="pages" style="padding-top: 10px">
            <@pages groupsCount, currentPage/>
        </div>
    </@wm.dtContent>

    <@wm.dtFooter/>
</div>
</@wm.playground>