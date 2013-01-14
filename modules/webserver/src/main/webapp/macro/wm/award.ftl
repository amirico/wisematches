<#include "/macro/message.ftl"/>

<#macro image code weight>
    <#assign name=code?lower_case/>
    <#assign path=name?replace(".", "/")/>
    <#if weight?? && weight?has_content>
        <#assign type=(weight.name())?lower_case/>
    <img src="/resources/images/awards/${path}/${type}.png"
         title="<@message code="awards.${name}.label"/>: <@message code="awards.${type}.label"/>">
    <#else>
    <img src="/resources/images/awards/${path}/default.png"
         title="<@message code="awards.${name}.label"/>">
    </#if>
</#macro>
