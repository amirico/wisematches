<#include "/core.ftl">

<@wm.ui.playground id="playersWidget">
    <@wm.ui.table.header>
        <@message code="search.label"/>
    </@wm.ui.table.header>

    <#assign scriplet=false/>
    <#include "scriplet.ftl"/>
</@wm.ui.playground>