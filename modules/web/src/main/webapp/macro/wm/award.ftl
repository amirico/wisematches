<#include "/core.ftl"/>

<#macro image descriptor weight link=true>
    <#assign name=descriptor.name?lower_case/>
    <#assign path=name?replace(".", "/")/>
<span class="award">
    <#if weight?? && weight?has_content>
        <#assign type=(weight.name())?lower_case/>
        <#if link><a href="/playground/award/view?c=${descriptor.code}&w=${weight}"></#if><img
            src="<@wm.ui.static "images/awards/${path}/${type}.png"/>"
            title="<@message code="awards.${name}.label"/>: <@message code="awards.${type}.label"/>"><#if link></a>
    </#if>
    <#else>
        <img src="<@wm.ui.static "images/awards/${path}/gray.png"/>"
             title="<@message code="awards.${name}.label"/>">
    </#if>
</span>
</#macro>
