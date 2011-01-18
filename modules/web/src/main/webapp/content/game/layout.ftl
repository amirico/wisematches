<#-- @ftlvariable name="pageName" type="java.lang.String" -->

<#include "/core.ftl">

<@wisematches.html i18n=["login"]
scripts=["/content/game/game.js", "/dwr/interface/problemsReportService.js"] styles=["/content/game/game.css"] >
    <#include "header.ftl">
    <#include "pages/${pageName}.ftl">
<#--<#include "modelConverter.ftl">-->
<#--<#include "footer.ftl">-->
</@wisematches.html>

