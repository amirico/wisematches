<#include "/core.ftl">

<#macro editor id code value="" view="" classes="">
    <#assign qwe=view/>
    <#if (!view?has_content) && (value?has_content)><#assign qwe=value/></#if>
<div id="${id}" class="ui-editor-item ${classes}">
    <div class="ui-editor-label ${classes}"><@message code="${code}.label"/></div>
    <div label="<@message code="${code}.description"/>"
         class=" ui-editor-view<#if !qwe?has_content> sample</#if> ${classes}
    "><#if qwe?has_content>${qwe}<#else><@message code="${code}.description"/></#if></div>
    <input name="${id}" type="hidden" value="${value}">
</div>
</#macro>

<#macro player player showType=true showRating=true>
    <#assign computerPlayer=(player.membership == "GUEST") || (player.membership == "ROBOT")/>
<span class="player <#if computerPlayer>computer<#else>member</#if>">
    <#if !computerPlayer><a href="/playground/profile/view?p=${player.id}"></#if><span
        class="nickname">${gameMessageSource.getPlayerNick(player, locale)}</span><#if showType && player.getMembership() != "BASIC">
    <span class="mod ${player.membership!""?lower_case}"></span></#if><#if !computerPlayer></a></#if>
    <#if showRating><span class="rating">(${player.rating})</span></#if>
</span>
</#macro>

<#macro info>
<img class="help-tooltip" style="vertical-align: text-bottom" src="/resources/images/help.png" width="16" height="16"
     title="<#nested>"/>
</#macro>

<#macro field path id="" class="">
<@spring.bind path/>
<div <#if id?has_content>id="${id}"</#if>
     class="<#if spring.status.error>field-error<#else>field-ok</#if><#if class?has_content> ${class}</#if>">
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
    <div class="ui-widget-header ui-corner-all"><@message code=title/></div>
    <div <#if id??>id="${id}"</#if> class="ui-widget-content ui-corner-all">
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