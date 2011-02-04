<#-- @ftlvariable name="headerTitle" type="java.lang.String" -->
<#include "/core.ftl">

<#macro html styles=[] scripts=[] i18n=[] headers=[] title='wisematches.title'>
<html>
<head>
    <title id="page-title"><@message code="${title}"/></title>
    <meta http-equiv="Content-type" content="text/html; charset=UTF-8">

    <link rel="stylesheet" type="text/css" href="/content/common/common.css">
    <link rel="stylesheet" type="text/css" href="/ext/resources/css/ext-all.css">
    <#list styles as style>
        <link rel="stylesheet" href="${style}">
    </#list>

    <#list i18n as i>
        <script type="text/javascript" src="/i18n/${i}/<@message code="locale"/>.js"></script>
    </#list>
    <script type="text/javascript" src="/ext/debug/ext-base-debug.js"></script>
    <script type="text/javascript" src="/ext/debug/ext-all-debug.js"></script>

    <script type="text/javascript" src="/ext/ext-ux-wm.js"></script>
    <script type="text/javascript" src="/content/common/common.js"></script>

    <!-- Some extension. TODO: add only for a few pages where it's really required. &ndash;&gt; -->
    <script type="text/javascript" src="/content/account/account.js"></script>

    <#list scripts as script>
        <script type="text/javascript" src="${script}"></script>
    </#list>

    <#list headers as header>
        <#include "${header}">
    </#list>
</head>
<body>
    <#nested>
</body>
</html>
</#macro>

<#macro field path>
<@spring.bind path/>
<div class="<#if spring.status.error>x-form-invalid-msg field-error<#else>field-ok</#if>">
    <#assign status=spring.status>
    <#assign statusValue=spring.stringStatusValue>

    <#nested >

    <#list status.errorMessages as msg>
        <div class="x-form-invalid-msg error-msg">${msg}</div>
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