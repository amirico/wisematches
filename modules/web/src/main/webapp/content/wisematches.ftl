<#-- @ftlvariable name="headerTitle" type="java.lang.String" -->
<#include "/core.ftl">

<#macro html title='wisematches.title'>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><@message code="${title}"/></title>

    <meta http-equiv="Content-type" content="text/html; charset=UTF-8"/>

    <link rel="stylesheet" type="text/css" href="/jquery/css/redmond/jquery-ui.custom.css"/>
    <link rel="stylesheet" type="text/css" href="/jquery/css/table_jui.css"/>
    <link rel="stylesheet" type="text/css" href="/jquery/css/table_col_reorder.css"/>
    <link rel="stylesheet" type="text/css" href="/jquery/css/table_cov_vis.css"/>
    <link rel="stylesheet" type="text/css" href="/content/wisematches.css"/>
    <link rel="stylesheet" type="text/css" href="/content/account/account.css"/>
    <link rel="stylesheet" type="text/css" href="/content/game/scribble.css"/>
    <link rel="stylesheet" type="text/css" href="/content/game/scribble-board.css"/>

    <script type="text/javascript" src="/jquery/jquery.js"></script>
    <script type="text/javascript" src="/jquery/jquery-ui.min.js"></script>
    <script type="text/javascript" src="/jquery/dataTables.min.js"></script>
    <script type="text/javascript" src="/jquery/dataTables-colVis.min.js"></script>
    <script type="text/javascript" src="/jquery/dataTables-colReorder.min.js"></script>
    <script type="text/javascript" src="/jquery/jquery.blockUI.js"></script>

    <script type="text/javascript" src="/i18n/${locale}.js"></script>
    <script type="text/javascript" src="/content/wisematches.js"></script>
    <script type="text/javascript" src="/content/account/account.js"></script>
    <script type="text/javascript" src="/content/game/scribble-board.js"></script>
</head>
<body>
    <#nested>
</body>
</html>
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