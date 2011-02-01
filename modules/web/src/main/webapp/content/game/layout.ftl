<#-- @ftlvariable name="pageName" type="java.lang.String" -->
<#include "/core.ftl">

<#assign headerTitle="game.header"/>

<@wisematches.html i18n=["login"] scripts=["/content/game/game.js"] styles=["/content/game/game.css"] title=headerTitle>
    <#include "header.ftl">
    <#include "pages/${pageName}.ftl">
<#--<#include "modelConverter.ftl">-->
<#--<#include "footer.ftl">-->
</@wisematches.html>

