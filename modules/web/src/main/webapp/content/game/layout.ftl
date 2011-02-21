<#-- @ftlvariable name="pageName" type="java.lang.String" -->
<#include "/core.ftl">

<#assign headerTitle="game.header"/>

<@wisematches.html styles=["/content/game/scribble.css"]  scripts=["/content/game/scribble.js"] title=headerTitle>
    <#include "header.ftl">
    <#include "pages/${pageName}.ftl">
</@wisematches.html>