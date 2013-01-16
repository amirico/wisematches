<#include "/macro/message.ftl"/>

<#macro name player showType=true showState=true showLink=true waiting=false>
    <#assign s=showState && playerStateManager.isPlayerOnline(player)/>
    <#assign l=showLink && player.membership.member/>
    <#assign m=showType && player.membership.paidMember/>
    <#assign n=gameMessageSource.getPlayerNick(player, locale)/>
<span class="player ${player.membership.name()?lower_case} <#if waiting>waiting</#if>"><#if s>
    <div class="state online"></div></#if>
    <#if l><a href="/playground/profile/view?p=${player.id}"></#if>
    <div class="nickname">${n}</div><#if m>
        <div class="membership"
             title="<@message code="membership.name.${player.membership.name()?lower_case}"/> <@message code="membership.player.label"/>">
        </div></#if><#if l></a></#if>
</span>
</#macro>
