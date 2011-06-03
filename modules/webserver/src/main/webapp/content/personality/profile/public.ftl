<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="profile" type="wisematches.personality.profile.PlayerProfile" -->
<#include "/core.ftl">

<div class="info">
    <div class="photo">
        <img style="width: 200px; height: 200px;"
             src="/resources/images/player/noPlayer200.png" alt="Photo">
    </div>
    <div><strong>${player.nickname}</strong></div>
    <div <#if !profile.gender??>class="undefined"</#if>>
    ${profile.gender!"gender undefined"},
    </div>
    <div <#if !profile.birthday??>class="undefined"</#if>>
    ${profile.birthday!"ages undefined"},
    </div>
    <div <#if !profile.countryCode??>class="undefined"</#if>>
    ${profile.countryCode!"country undefined"},
    </div>
    <div>
    ${player.timeZone.displayName}
    </div>
    <div class="<#if !profile.comments??>undefined </#if>quotation">
        &laquo; ${profile.comments!""} &raquo;
    </div>
</div>
