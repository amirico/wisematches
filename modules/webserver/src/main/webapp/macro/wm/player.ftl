<#macro name player showType=true showState=true hideLink=false>
    <#assign computerPlayer=(player.membership == "GUEST") || (player.membership == "ROBOT")/>
<span class="player <#if computerPlayer>computer<#else>member</#if>">
    <#if showState && playerStateManager.isPlayerOnline(player)>
        <div class="online"></div></#if>
    <span>
    <#if !computerPlayer && !hideLink><a href="/playground/profile/view?p=${player.id}"></#if><span
            class="nickname">${gameMessageSource.getPlayerNick(player, locale)}</span>
        <#if showType && player.getMembership() != "BASIC">
            <span class="mod ${player.membership!""?lower_case}"></span></#if><#if !computerPlayer && !hideLink>
    </a></#if>
    </span>
</span>
</#macro>