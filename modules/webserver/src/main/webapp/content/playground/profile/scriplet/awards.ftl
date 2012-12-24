<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="tourneyCareer" type="wisematches.playground.tourney.TourneyCareer" -->
<#-- @ftlvariable name="tourneyMedals" type="wisematches.playground.tourney.TourneyPlace[]" -->

<#include "/core.ftl">

<div class="awards">
<#assign totalCount=0/>
<#list tourneyMedals as m>
    <#assign count=tourneyCareer.getMedalsCount(m)/>
    <#assign totalCount=totalCount+count/>

    <#if count!=0>
        <div class="award">
            <img src="/resources/images/tourney/${m.name()?lower_case}.png"
                 alt="<@message code="tourney.medal.${m.name()?lower_case}.label"/>">

            <p>${count?string}</p>
        </div>
    </#if>
</#list>

<#if totalCount != 0>
    <div style="text-align: right">
        <a href="/playground/profile/awards?p=${player.id?string}"><@message code="profile.awards.label"/></a>
    </div>
</#if>
</div>
