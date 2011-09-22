<#include "/core.ftl">

<@wm.playground id="playersWidget">
<div>
    <div class="ui-widget-header ui-corner-all shadow"><@message code="search.label"/></div>

    <div class="ui-widget-content ui-corner-all shadow">
        <#assign scriplet=false/>
        <#include "scriplet.ftl"/>
    </div>
</div>
</@wm.playground>