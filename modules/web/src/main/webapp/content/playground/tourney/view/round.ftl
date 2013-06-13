<#-- @ftlvariable name="tourney" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="round" type="wisematches.playground.tourney.regular.TourneyRound" -->
<#-- @ftlvariable name="groups" type="wisematches.playground.tourney.regular.TourneyGroup[]" -->

<#-- @ftlvariable name="groupsCount" type="int" -->
<#-- @ftlvariable name="currentPage" type="int" -->

<#include "/core.ftl">

<#macro pages count current>
    <#if (count>30)>
    [
        <#list 0..(count-1)/30 as p>
            <#assign first=p*30+1/>
            <#assign last=p*30+30/>
            <#if (last>count)><#assign last=count/></#if>
            <#if p != current><a
                    href="/playground/tourney/view?t=${tourney.number}&l=${round.division.language.ordinal()}&s=${round.division.section.ordinal()}&r=${round.round}&h=${p}"></#if>
        ${first}..${last}<#if p != current></a></#if>
            <#if p_has_next>|</#if>
        </#list>
    ]
    </#if>
</#macro>

<#macro successColor s>
    <#if s.points=0>#AA0033<#elseif s.points=1>#FF9900<#else>#008000</#if>
</#macro>

<@wm.ui.playground id="tourneyWidget">
<div id="divisions">
    <@wm.ui.table.header align="left">
        <@message code="tourney.tourney.label"/> > <@wm.tourney.tourney tourney.id, false/>
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar align="left">
        <div>
            <div class="tourney-name ui-state-active" style="display: block">
                <@wm.tourney.tourney tourney.id, false/>
            </div>
            <div>
                <@message code="language.${round.division.language.name()?lower_case}"/>,
                <@wm.tourney.section round.division.section/> <@message code="tourney.level.label"/>,
                <@wm.tourney.round round.id, true/>
            </div>
            <div class="sample" style="padding-left: 10px">
                (${messageSource.formatDate(round.startedDate, locale)}
                <#if round.finishedDate??>— ${messageSource.formatDate(round.finishedDate, locale)}</#if>)
            </div>
        </div>
    </@wm.ui.table.toolbar>

    <@wm.ui.table.content wrap=true>
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
                                ${p_index+1}. <@wm.player.name personalityManager.getMember(p)/>
                                </td>
                                <#assign totalGames=0/>
                                <#assign finishedGames=0/>
                                <#list g.players as o>
                                    <#assign success=g.getPlayerSuccess(p, o)!""/>
                                    <td class="ui-state-default">
                                        <#if o=p>
                                            —
                                        <#else>
                                            <#assign gameId=g.getGameId(p, o)/>
                                            <#assign totalGames=totalGames+1/>
                                            <a href="/playground/scribble/board?b=${gameId}">
                                                <#if success?has_content>
                                                    <#assign finishedGames=finishedGames+1/>
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
                                <td class="ui-state-default">${finishedGames} / ${totalGames}</td>
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
    </@wm.ui.table.content>

    <@wm.ui.table.footer/>
</div>
</@wm.ui.playground>