<#-- @ftlvariable name="accountBodyPageName" type="java.lang.String" -->
<#include "/core.ftl">

<#assign headerTitle="account.header"/>

<@wisematches.html
scripts=["/dwr/interface/problemsReportService.js"]
styles=["/content/info/info.css", "/content/account/account.css"] >
    <#include "../common/header.ftl">
    <#include "pages/${accountBodyPageName}.ftl">
    <#include "../common/footer.ftl">
</@wisematches.html>