<#-- @ftlvariable name="headerTitle" type="java.lang.String" -->
<#-- @ftlvariable name="originalTemplateName" type="java.lang.String" -->
<#include "/core.ftl">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><@message code=headerTitle!"title.default"/></title>

    <meta http-equiv="Content-type" content="text/html; charset=UTF-8"/>

    <link rel="stylesheet" type="text/css" href="/jquery/css/redmond/jquery-ui.custom.css"/>
    <script type="text/javascript" src="/jquery/js/jquery.min.js"></script>
    <script type="text/javascript" src="/jquery/js/json.min.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.blockUI.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.freeow.min.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.tools.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/content/wisematches.css"/>
    <script type="text/javascript" src="/content/wisematches.js"></script>

    <link rel="stylesheet" type="text/css" href="/content/personality/account/account.css"/>
<#if principal??>
    <link rel="stylesheet" type="text/css" href="/content/playground/game.css"/>
</#if>
<#include "templates/analytics.ftl">
</head>
<body>
<#include 'templates/unsupportedBrowser.ftl'/>

<#if principal??><#include "playground/header.ftl"/><#else><#include "personality/header.ftl"/></#if>

<div id="notification-block"></div>

<#if principal??>
<div id="header-separator" style="height: 20px;"></div>
</#if>

<div style="padding: 5px">
<#include "${originalTemplateName}"/>
</div>

<#if !errorCode??>
    <#if principal??><#include "playground/footer.ftl"/><#else><#include "personality/footer.ftl"/></#if>
</#if>

</body>
</html>