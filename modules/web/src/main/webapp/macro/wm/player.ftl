<#include "/macro/message.ftl"/>

<#macro name player showType=true showState=true showLink=true waiting=false>
    <#assign l=showLink && player.type.player/>
<span class="player ${player.type.name()?lower_case}<#if player.type.robot> ${player.robotType.name()?lower_case}</#if><#if player.type.player> ${player.playerType.name()?lower_case}</#if><#if waiting> waiting</#if>">
    <#if showState && player.type.player><@state player/></#if>
    <#if l><@link player><@nick player/><#if showType><@icon player/></#if></@link><#else><@nick player/><#if showType><@icon player/></#if></#if>
</span>
</#macro>

<#macro state p>
<div class="state <#if playerStateManager.isPlayerOnline(p)>online<#else>offline</#if>"></div>
</#macro>

<#macro link p><a href="/playground/profile/view?p=${p.id}"><#nested/></a></#macro>

<#macro nick p>
<div class="nickname">${messageSource.getPersonalityNick(p, locale)}</div></#macro>

<#macro icon p>
<div class="icon<#if p.type.player> ${p.playerType.name()?lower_case}</#if>"></div></#macro>