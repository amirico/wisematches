<#-- @ftlvariable name="headerTitle" type="java.lang.String" -->
<#-- @ftlvariable name="originalTemplateName" type="java.lang.String" -->
<#include "/core.ftl">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><@message code=headerTitle!"title.default"/></title>

    <meta http-equiv="Content-type" content="text/html; charset=UTF-8"/>

    <link rel="stylesheet" type="text/css" href="/jquery/css/redmond/jquery-ui.custom.css"/>
    <link rel="stylesheet" type="text/css" href="/jquery/css/table_jui.css"/>
    <link rel="stylesheet" type="text/css" href="/jquery/css/ColReorder.css"/>
    <link rel="stylesheet" type="text/css" href="/jquery/css/ColVis.css"/>

    <script type="text/javascript" src="/jquery/js/jquery.min.js"></script>
    <script type="text/javascript" src="/jquery/js/json.min.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.blockUI.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.freeow.min.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="/jquery/js/ColReorder.min.js"></script>
    <script type="text/javascript" src="/jquery/js/ColVis.min.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.timers.js"></script>

    <link rel="stylesheet" type="text/css" href="/content/wisematches.css"/>
    <script type="text/javascript" src="/i18n/${locale}.js"></script>
    <script type="text/javascript" src="/content/wisematches.js"></script>

    <link rel="stylesheet" type="text/css" href="/content/account/account.css"/>

    <link rel="stylesheet" type="text/css" href="/content/game/game.css"/>
    <script type="text/javascript" src="/content/game/scribble.js"></script>
</head>
<body>
<div id="freeow" class="freeow freeow-top-right"></div>
<@security.authorize ifAllGranted="user">
    <#include "game/header.ftl"/>
</@security.authorize>
<@security.authorize ifNotGranted="user">
    <#include "account/header.ftl"/>
</@security.authorize>

<#include "${originalTemplateName}"/>

<#if !errorCode??>
<@security.authorize ifAllGranted="user">
    <#include "game/footer.ftl"/>
</@security.authorize>
<@security.authorize ifNotGranted="user">
    <#include "account/footer.ftl"/>
</@security.authorize>
</#if>
</body>
</html>