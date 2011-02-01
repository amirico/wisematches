<#-- @ftlvariable name="infoId" type="java.lang.String" -->
<#include "/core.ftl">

<#assign headerTitle="account.header" />

<@wisematches.html styles=["/content/info/info.css", "/content/account/account.css"] title=headerTitle>
    <#include "../common/header.ftl">
    <#include "pages/${infoId}.ftl">
    <#include "../common/footer.ftl">
</@wisematches.html>