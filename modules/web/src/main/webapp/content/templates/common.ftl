<#include "/core.ftl">

<#macro player player showType=true showRating=true>
    <#assign computerPlayer=(!(player.getMembership()??))/>
<span class="player <#if computerPlayer>computer<#else>member</#if>">
    <#if computerPlayer>
        <span class="nickname"><@wm.message code="game.player.${player.nickname}"/></span>
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

<#macro field path id="">
<@spring.bind path/>
<div <#if id?has_content>id="${id}"</#if> class="<#if spring.status.error>field-error<#else>field-ok</#if>">
    <#assign status=spring.status>
    <#assign statusValue=spring.stringStatusValue>

    <#nested >

    <#list status.errorMessages as msg>
        <div class="ui-state-error-text error-msg">${msg}</div>
    </#list>
</div>
</#macro>

<#macro fieldInput path attributes="" fieldType="text" size=30 value="">
<@field path=path>
<input type="${fieldType}" id="${spring.status.expression}" name="${spring.status.expression}" size="${size}"
       value="<#if fieldType!="password"><#if spring.stringStatusValue?has_content>${spring.stringStatusValue}<#else><@message code=value/></#if></#if>" ${attributes}/>
</@field>
</#macro>

<#macro captcha path>
    <#if captchaService??><@field path>${captchaService.createCaptchaScript(locale)}</@field></#if>
</#macro>

<#macro widget title id="" class="" style="">
<div class="ui-widget" <#if style??>style="${style}"</#if>>
    <div class="ui-widget-header ui-corner-top">${title}</div>
    <div <#if id??>id="${id}"</#if> class="ui-widget-content">
        <#nested/>
    </div>
</div>
</#macro>

<#macro topRoundPanel id="" class="" style="">
<@abstractPanel id="${id}" class="ui-corner-top ${class}" style="${style}">
    <#nested>
</@abstractPanel>
</#macro >

<#macro bottomRoundPanel id="" class="" style="">
<@abstractPanel id="${id}" class="ui-corner-bottom ${class}" style="${style}">
    <#nested>
</@abstractPanel>
</#macro >

<#macro roundPanel id="" class="" style="">
<@abstractPanel id="${id}" class="ui-corner-all ${class}" style="${style}">
    <#nested>
</@abstractPanel>
</#macro >

<#macro abstractPanel class style id="">
<div <#if id?has_content>id="${id}"</#if> class="ui-widget-content ${class!}"
     <#if style?has_content>style="${style}"</#if>>
    <#nested>
</div>
</#macro>