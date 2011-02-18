<#-- @ftlvariable name="pageName" type="java.lang.String" -->
<#include "/core.ftl">

<#assign headerTitle="game.header"/>

<@wisematches.html scripts=["/content/game/scribble.js"] styles=["/content/game/scribble.css"] title=headerTitle>
    <#include "header.ftl">
    <#include "pages/${pageName}.ftl">
</@wisematches.html>

