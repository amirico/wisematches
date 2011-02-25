<#-- @ftlvariable name="headerTitle" type="java.lang.String" -->
<#include "/core.ftl">

<#macro html title='wisematches.title'>
<html>
<head>
    <title id="page-title"><@message code="${title}"/></title>

    <meta http-equiv="Content-type" content="text/html; charset=UTF-8">

    <link rel="stylesheet" type="text/css" href="/content/wisematches.css">
    <link rel="stylesheet" type="text/css" href="/content/account/account.css">
    <link rel="stylesheet" type="text/css" href="/content/game/scribble.css">
    <link rel="stylesheet" type="text/css" href="/jquery/css/redmond/jquery-ui.custom.css"/>

    <script type="text/javascript" src="/i18n/${locale}.js"></script>

    <script type="text/javascript" src="/jquery/jquery.js"></script>
    <script type="text/javascript" src="/jquery/jquery-ui.min.js"></script>

    <script type="text/javascript" src="/content/wisematches.js"></script>
    <script type="text/javascript" src="/content/game/scribble.js"></script>
    <script type="text/javascript" src="/content/account/account.js"></script>
</head>
<body>
    <#nested>
</body>
</html>
</#macro>

<#macro field path>
<@spring.bind path/>
<div class="<#if spring.status.error>field-error<#else>field-ok</#if>">
    <#assign status=spring.status>
    <#assign statusValue=spring.stringStatusValue>

    <#nested >

    <#list status.errorMessages as msg>
        <div class="ui-state-error-text error-msg">${msg}</div>
    </#list>
</div>
</#macro>

<#macro fieldInput path attributes="" fieldType="text" size=30>
<@field path=path>
<input type="${fieldType}" id="${spring.status.expression}" name="${spring.status.expression}" size="${size}"
       value="<#if fieldType!="password">${spring.stringStatusValue}</#if>" ${attributes}>
</@field>
</#macro>

<#macro captcha path>
    <#if captchaService??><@field path>${captchaService.createCaptchaScript(locale)}</@field></#if>
</#macro>