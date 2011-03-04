<#include "/core.ftl">

<#macro player player showType=true showRating=true>
    <#assign computerPlayer=(!(player.getMembership()??))/>
<span class="player <#if computerPlayer>computer<#else>member</#if>">
    <#if computerPlayer>
        <span class="nickname"><@wisematches.message code="game.player.${player.nickname}"/></span>
        <#if showType><span class="mod ${player.nickname} <#if player.id == 1>guest<#else>robot</#if>"></span></#if>
        <#if showRating><span class="rating">(${player.rating?string.computer})</span></#if>
        <#else>
            <a href="/game/profile.html?playerId=${player.id}">
                <span class="nickname">${player.nickname}</span>
                <#if showType && player.getMembership() != "BASIC"><span
                        class="mod ${player.getMembership().name()?lower_case}"></span></#if>
                <#if showRating><span class="rating">(${player.rating?string.computer})</span></#if>
            </a>
    </#if>
</span>
</#macro>