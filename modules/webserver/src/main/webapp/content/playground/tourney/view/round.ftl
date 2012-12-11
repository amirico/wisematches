<#-- @ftlvariable name="tourney" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="round" type="wisematches.playground.tourney.regular.TourneyRound" -->
<#-- @ftlvariable name="groups" type="wisematches.playground.tourney.regular.TourneyGroup[]" -->

<#include "/core.ftl">
<#include "../scriplet.ftl">

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
            <div style="padding-left: 10px">
            ${gameMessageSource.formatDate(round.startedDate, locale)}
                <#if round.finishedDate??>— ${gameMessageSource.formatDate(round.finishedDate, locale)}</#if>
            </div>
        </div>
    </@wm.dtToolbar>

    <@wm.dtContent wrap=true>
        <#list groups as g>
            Group #${g.group}:

            <table style="border-spacing: 1px">
                <tr>
                    <td>#</td>
                    <#list g.players as p>
                        <td>vs. #${p_index}</td>
                    </#list>
                    <td>Score</td>
                </tr>
                <#list g.players as p>
                    <tr>
                        <td><@wm.player playerManager.getPlayer(p)/></td>
                        <#list g.players as o>
                            <td>
                                <#if o=p>
                                    -
                                <#else>
                                ${g.getGameId(p, o)}
                                </#if>
                            </td>
                        </#list>
                        <td>${g.getScores(p)}</td>
                    </tr>
                </#list>
            </table>
        </#list>
    </@wm.dtContent>

    <@wm.dtFooter/>
</div>
</@wm.playground>