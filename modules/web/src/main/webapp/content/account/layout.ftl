<#-- @ftlvariable name="infoId" type="java.lang.String" -->
<#include "/core.ftl">

<#assign headerTitle="account.header" />

<@wisematches.html title=headerTitle>
    <#include "header.ftl">
    <#include "pages/${infoId}.ftl">
    <#include "footer.ftl">
</@wisematches.html>