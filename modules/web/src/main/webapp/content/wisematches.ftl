<#-- @ftlvariable name="originalTemplateName" type="java.lang.String" -->
<#-- @ftlvariable name="headerTitle" type="java.lang.String" -->
<#include "/core.ftl">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><@message code="wisematches.title"/></title>

    <meta http-equiv="Content-type" content="text/html; charset=UTF-8"/>

    <link rel="stylesheet" type="text/css" href="/jquery/css/redmond/jquery-ui.custom.css"/>
    <script type="text/javascript" src="/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/jquery/json.min.js"></script>
    <script type="text/javascript" src="/jquery/jquery-ui.min.js"></script>
    <script type="text/javascript" src="/jquery/jquery.blockUI.js"></script>
    <script type="text/javascript" src="/jquery/jquery.freeow.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/jquery/css/table_jui.css"/>
    <link rel="stylesheet" type="text/css" href="/jquery/css/table_col_reorder.css"/>
    <link rel="stylesheet" type="text/css" href="/jquery/css/table_cov_vis.css"/>
    <script type="text/javascript" src="/jquery/jquery.table.min.js"></script>
    <script type="text/javascript" src="/jquery/jquery.table_col_vis.min.js"></script>
    <script type="text/javascript" src="/jquery/jquery.table_col_reorder.min.js"></script>
    <script type="text/javascript" src="/jquery/jquery.timers.js"></script>

    <link rel="stylesheet" type="text/css" href="/content/wisematches.css"/>
    <script type="text/javascript" src="/i18n/${locale}.js"></script>
    <script type="text/javascript" src="/content/wisematches.js"></script>

    <link rel="stylesheet" type="text/css" href="/content/account/account.css"/>

    <link rel="stylesheet" type="text/css" href="/content/game/game.css"/>
    <link rel="stylesheet" type="text/css" href="/content/game/playboard/scribble.css"/>
    <script type="text/javascript" src="/content/game/playboard/scribble.js"></script>
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

<@security.authorize ifNotGranted="user">
    <#include "account/footer.ftl"/>
</@security.authorize>
</body>
</html>