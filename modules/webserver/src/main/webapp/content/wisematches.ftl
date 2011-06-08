<#-- @ftlvariable name="headerTitle" type="java.lang.String" -->
<#-- @ftlvariable name="originalTemplateName" type="java.lang.String" -->
<#include "/core.ftl">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><@message code=headerTitle!"title.default"/></title>

    <meta http-equiv="Content-type" content="text/html; charset=UTF-8"/>

    <link rel="stylesheet" type="text/css" href="/jquery/css/table_jui.css"/>
    <link rel="stylesheet" type="text/css" href="/jquery/css/redmond/jquery-ui.custom.css"/>
    <link rel="stylesheet" type="text/css" href="/jquery/css/ColReorder.css"/>
    <link rel="stylesheet" type="text/css" href="/jquery/css/ColVis.css"/>

    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript" src="/jquery/js/jquery.min.js"></script>
    <script type="text/javascript" src="/jquery/js/json.min.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.blockUI.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.freeow.min.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="/jquery/js/ColReorder.min.js"></script>
    <script type="text/javascript" src="/jquery/js/ColVis.min.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.timers.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.tools.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/content/wisematches.css"/>
    <script type="text/javascript" src="/i18n/${locale}.js"></script>
    <script type="text/javascript" src="/content/wisematches.js"></script>

    <link rel="stylesheet" type="text/css" href="/content/playground/game.css"/>
    <link rel="stylesheet" type="text/css" href="/content/personality/account/account.css"/>

    <script type="text/javascript" src="/content/playground/scribble/scribble.js"></script>
    <link rel="stylesheet" type="text/css" href="/content/playground/scribble/scribble.css"/>
<#include "templates/analytics.ftl">
</head>
<body>
<@security.authorize ifAllGranted="user">
    <#include "playground/header.ftl"/>
</@security.authorize>
<@security.authorize ifNotGranted="user">
    <#include "personality/header.ftl"/>
</@security.authorize>

<div id="notification-block"></div>

<@security.authorize ifAllGranted="user">
<div id="header-separator" style="height: 20px;"></div>
</@security.authorize>

<#include "${originalTemplateName}"/>

<#if !errorCode??>
<@security.authorize ifAllGranted="user">
    <#include "playground/footer.ftl"/>
</@security.authorize>
<@security.authorize ifNotGranted="user">
    <#include "personality/footer.ftl"/>
</@security.authorize>
</#if>
</body>
</html>