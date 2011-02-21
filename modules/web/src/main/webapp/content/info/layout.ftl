<#-- @ftlvariable name="pageName" type="java.lang.String" -->
<#include "/core.ftl">

<#assign headerTitle="info.header"/>

<@wisematches.html title=headerTitle>
<@security.authorize ifAllGranted="user">
    <#include "/content/game/header.ftl">
</@security.authorize>
<@security.authorize ifNotGranted="user">
    <#include "/content/account/header.ftl">
</@security.authorize>

    <#include "helpCenter.ftl">

<@security.authorize ifNotGranted="user">
    <#include "/content/account/footer.ftl">
</@security.authorize>
</@wisematches.html>