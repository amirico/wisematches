<#include "/core.ftl">

<@wm.playground id="playersWidget">
<div>
    <div class="ui-widget-header ui-corner-top shadow"
         style="border-top-color: #CCC; border-left-color: #CCC; border-right-color: #CCC; border-bottom-width:0">
        <@message code="search.label"/>
    </div>

<#--
    <div class="ui-widget-header ui-corner-all shadow"></div>

    <div class="ui-widget-content ui-corner-all shadow">
-->
    <div style="border-right: 1px solid #CCC;border-left: 1px solid #CCC;">
        <#assign scriplet=false/>
    <#include "scriplet.ftl"/>
    </div>

    <div class="ui-corner-bottom"
         style="height: 3px; border-bottom: 1px solid #CCC; border-right: 1px solid #CCC;border-left: 1px solid #CCC;">
    </div>


<#---->    </div>
</div>
</@wm.playground>