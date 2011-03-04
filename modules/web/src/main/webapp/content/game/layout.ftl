<#-- @ftlvariable name="pageName" type="java.lang.String" -->
<#include "/core.ftl">

<#assign headerTitle="game.header"/>

<@wisematches.html title=headerTitle>
    <#include "header.ftl">
    <#include "pages/${pageName}.ftl">

<#--
<div style="text-align:center;">
    <span class="copyrights"><@message "copyrights.label"/></span>
</div>
-->
</@wisematches.html>