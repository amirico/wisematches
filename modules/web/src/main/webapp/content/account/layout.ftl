<#-- @ftlvariable name="accountBodyPageName" type="java.lang.String" -->

<#include "/core.ftl">

<@wisematches.html title="account.header" scripts=["/dwr/interface/problemsReportService.js"] styles=["/content/info/info.css", "/content/account/account.css"] >
    <#include "header.ftl">
    <#include "/content/account/pages/${accountBodyPageName}.ftl">
    <#include "/content/common/footer.ftl">
</@wisematches.html>

