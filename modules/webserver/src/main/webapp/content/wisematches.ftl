<#-- @ftlvariable name="headerTitle" type="java.lang.String" -->
<#-- @ftlvariable name="originalTemplateName" type="java.lang.String" -->
<#include "/core.ftl">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><@message code=headerTitle!"title.default"/></title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="Content-Language" content="${locale}"/>

    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>

    <link rel="stylesheet" type="text/css"
          href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/themes/redmond/jquery-ui.css"/>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>

    <link rel="stylesheet" type="text/css" href="<@s "css/jquery.dataTables_themeroller-1.9.4.css"/>">
    <script type="text/javascript" src="<@s "js/jquery.dataTables-1.9.4.min.js"/>"></script>

    <link rel="stylesheet" type="text/css" href="<@s "css/jquery.cluetip-1.2.7.css"/>"/>
    <script type="text/javascript" src="<@s "js/jquery.cluetip-1.2.7.min.js"/>"></script>

    <link rel="stylesheet" type="text/css" href="<@s "css/jquery.jscrollpane-2.0.0.css"/>"/>
    <script type="text/javascript" src="<@s "js/jquery.mousewheel-3.0.6.js"/>"></script>
    <script type="text/javascript" src="<@s "js/jquery.mousewheelIntent-1.2.0.js"/>"></script>
    <script type="text/javascript" src="<@s "js/jquery.jscrollpane-2.0.0.min.js"/>"></script>

    <script type="text/javascript" src="<@s "js/json2-2.1.8.min.js"/>"></script>
    <script type="text/javascript" src="<@s "js/jquery.timers-1.2.0.js"/>"></script>
    <script type="text/javascript" src="<@s "js/jquery.blockUI-2.5.4.js"/>"></script>
    <script type="text/javascript" src="<@s "js/jquery.freeow-1.0.2.min.js"/>"></script>
    <script type="text/javascript" src="<@s "js/jquery.hoverIntent-0.0.6.min.js"/>"></script>

    <link rel="stylesheet" type="text/css" href="<@s "css/wisematches-4.1.3.css"/>"/>
    <script type="text/javascript" src="<@s "js/wisematches-4.1.3.js"/>"></script>

<#include 'templates/localization.ftl'/>
<#include "templates/analytics.ftl">
</head>
<body>
<#include 'templates/unsupportedBrowser.ftl'/>

<div id="wisematchesHeader">
<#if principal??><#include "playground/header.ftl"/><#else><#include "personality/header.ftl"/></#if>
</div>

<div id="notification-block"></div>

<#if principal??>
<div id="header-separator" style="height: 20px;"></div>
</#if>

<div id="wisematchesContent" style="padding: 5px; display: none;">
<#include "${originalTemplateName}"/>
</div>

<div id="wisematchesFooter" style="display: none;">
<#if principal?? && principal?has_content><#include "playground/footer.ftl"/><#else><#include "personality/footer.ftl"/></#if>
</div>

<script type="text/javascript">
    $("#wisematchesHeader #gameToolbar").show();
    $("#wisematchesFooter").show();
    $("#wisematchesContent").show();
</script>
</body>
</html>