<#-- @ftlvariable name="headerTitle" type="java.lang.String" -->
<#-- @ftlvariable name="originalTemplateName" type="java.lang.String" -->
<#include "/core.ftl">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><@message code=headerTitle!"title.default"/></title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="Content-Language" content="${locale}"/>

    <script src="http://code.jquery.com/jquery.min.js"></script>

    <link rel="stylesheet" type="text/css"
          href="http://code.jquery.com/ui/1.9.2/themes/redmond/jquery-ui.css"/>
    <script src="http://code.jquery.com/ui/1.9.2/jquery-ui.min.js"></script>

    <script type="text/javascript" src="/jquery/js/json.min.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.hoverIntent.min.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.blockUI.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.freeow.min.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.timers.js"></script>
    <script type="text/javascript" src="/jquery/js/jquery.form.js"></script>

    <link rel="stylesheet" type="text/css" href="/jquery/css/jquery.cluetip.css"/>
    <script type="text/javascript" src="/jquery/js/jquery.cluetip.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/content/wisematches.css"/>
    <script type="text/javascript" src="/content/wisematches.js"></script>

    <link rel="stylesheet" type="text/css" href="/content/personality/account.css"/>
    <script type="text/javascript" src="/content/personality/account.js"></script>

<#if principal??>
    <link rel="stylesheet" type="text/css" href="/content/playground/game.css"/>
    <script type="text/javascript" src="/content/playground/game.js"></script>

    <link rel="stylesheet" type="text/css" href="/jquery/css/table_jui.css"/>
</#if>
<#include 'templates/localization.ftl'/>
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

<#if principal?? && principal?has_content><#include "playground/footer.ftl"/><#else><#include "personality/footer.ftl"/></#if>
</body>
</html>                                 .