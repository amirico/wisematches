<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="templateName" type="java.lang.String" -->
<#include "/core.ftl">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><@message code=title!"title.default"/></title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="Content-Language" content="${locale}"/>

    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>

    <link rel="stylesheet" type="text/css"
          href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/themes/redmond/jquery-ui.css"/>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>

    <link rel="stylesheet" type="text/css" href="<@wm.ui.static "css/jquery.dataTables_themeroller-1.9.4.css"/>">
    <script type="text/javascript" src="<@wm.ui.static "js/jquery.dataTables-1.9.4.min.js"/>"></script>

    <link rel="stylesheet" type="text/css" href="<@wm.ui.static "css/jquery.cluetip-1.2.7.css"/>"/>
    <script type="text/javascript" src="<@wm.ui.static "js/jquery.cluetip-1.2.7.min.js"/>"></script>

    <link rel="stylesheet" type="text/css" href="<@wm.ui.static "css/jquery.jscrollpane-2.0.0.css"/>"/>
    <script type="text/javascript" src="<@wm.ui.static "js/jquery.mousewheel-3.0.6.js"/>"></script>
    <script type="text/javascript" src="<@wm.ui.static "js/jquery.mousewheelIntent-1.2.0.js"/>"></script>
    <script type="text/javascript" src="<@wm.ui.static "js/jquery.jscrollpane-2.0.0.min.js"/>"></script>

    <script type="text/javascript" src="<@wm.ui.static "js/json2-2.1.8.min.js"/>"></script>
    <script type="text/javascript" src="<@wm.ui.static "js/jquery.timers-1.2.0.js"/>"></script>
    <script type="text/javascript" src="<@wm.ui.static "js/jquery.blockUI-2.5.4.js"/>"></script>
    <script type="text/javascript" src="<@wm.ui.static "js/jquery.freeow-1.0.2.min.js"/>"></script>
    <script type="text/javascript" src="<@wm.ui.static "js/jquery.hoverIntent-0.0.6.min.js"/>"></script>

    <link rel="stylesheet" type="text/css" href="<@wm.ui.static "css/wisematches-5.0.0.css"/>"/>
    <script type="text/javascript" src="<@wm.ui.static "js/wisematches-5.0.0.js"/>"></script>

<#include "analytics.ftl">
<#include 'localization.ftl'/>
</head>
<body>
<#include 'browser.ftl'/>

<div id="wisematchesHeader">
<#if player??><#include "playground/header.ftl"/><#else><#include "account/header.ftl"/></#if>
</div>

<div id="notification-block"></div>

<#if player??>
<div id="header-separator" style="height: 20px;"></div>
</#if>

<div id="wisematchesContent" style="padding: 5px; display: none;">
<#include "${templateName}"/>
</div>

<div id="wisematchesFooter" style="display: none;">
<#if player?? && player?has_content><#include "playground/footer.ftl"/><#else><#include "account/footer.ftl"/></#if>
</div>

<script type="text/javascript">
    $("#wisematchesHeader").find("#gameToolbar").show();
    $("#wisematchesFooter").show();
    $("#wisematchesContent").show();
</script>
</body>
</html>