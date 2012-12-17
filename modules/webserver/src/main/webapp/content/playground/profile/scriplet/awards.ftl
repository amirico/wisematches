<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="tourneyCareer" type="wisematches.playground.tourney.TourneyCareer" -->
<#-- @ftlvariable name="tourneyMedals" type="wisematches.playground.tourney.TourneyMedal[]" -->

<#include "/core.ftl">

<div class="awards">
<#list tourneyMedals as m>
    <#assign count=tourneyCareer.getMedalsCount(m)/>

    <div class="award<#if count==0> ui-state-disabled</#if>">
        <img src="/resources/images/tourney/${m.name()?lower_case}.png"
             alt="<@message code="tourney.medal.${m.name()?lower_case}.label"/>">

        <p>${count?string}</p>
    </div>
</#list>
    <div style="text-align: right">
        <a href="/playground/profile/awards?p=${player.id?string}"><@message code="profile.awards.label"/></a>
    </div>
</div>
