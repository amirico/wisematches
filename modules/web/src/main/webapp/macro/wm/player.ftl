<#include "/macro/message.ftl"/>

<#macro name player showType=true showState=true showLink=true waiting=false>
    <#assign l=showLink && player.playerType.member/>
<span class="player ${player.playerType.name()?lower_case}<#if waiting> waiting</#if>">
    <#if showState && player.playerType.member><@state player/></#if>
    <#if l><@link player><@nick player/><#if showType><@icon player/></#if></@link><#else><@nick player/><#if showType><@icon player/></#if></#if>
</span>
</#macro>

<#macro state p>
<div class="state <#if playerStateManager.isPlayerOnline(p)>online<#else>offline</#if>"></div></#macro>

<#macro link p><a href="/playground/profile/view?p=${p.id}"><#nested/></a></#macro>

<#macro nick p>
<div class="nickname">${gameMessageSource.getPlayerNick(p, locale)}</div></#macro>

<#macro icon p>
<div class="icon<#if p.playerType.member> ${p.membership.code}</#if>"></div></#macro>