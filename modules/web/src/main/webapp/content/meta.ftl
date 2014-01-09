<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="titleExtension" type="java.lang.Object" -->

<#include "/core.ftl">

<#assign version="6.0.0"/>

<title><@message code=title!"title.default"/><#if titleExtension?has_content>${titleExtension}</#if></title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Content-Language" content="${locale}"/>

<script type="text/javascript" src="http://code.jquery.com/jquery-2.0.0.min.js"></script>
<link rel="stylesheet" type="text/css"
      href="http://code.jquery.com/ui/1.10.2/themes/redmond/jquery-ui.min.css"/>
<script type="text/javascript" src="http://code.jquery.com/ui/1.10.2/jquery-ui.min.js"></script>

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
<script type="text/javascript" src="<@wm.ui.static "js/jquery.textchange.min.js"/>"></script>
<script type="text/javascript" src="<@wm.ui.static "js/jquery.freeow-1.0.2.min.js"/>"></script>
<script type="text/javascript" src="<@wm.ui.static "js/jquery.hoverIntent-0.0.6.min.js"/>"></script>

<link rel="stylesheet" type="text/css" href="<@wm.ui.static "css/wisematches-${version}.css"/>"/>
<script type="text/javascript" src="<@wm.ui.static "js/wisematches-${version}.js"/>"></script>

<#include "analytics.ftl">
<#include 'localization.ftl'/>