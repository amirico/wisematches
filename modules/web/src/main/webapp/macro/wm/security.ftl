<#-- @ftlvariable name="restriction" type="wisematches.playground.restriction.Restriction" -->

<#include "/macro/message.ftl"/>

<#macro observed nbsp=false>
    <#if !restriction??><#nested><#elseif nbsp>&nbsp;</#if>
</#macro>

<#macro info code style="">
    <#if restriction??>
    <div class="restriction ui-state-error-text" <#if style?has_content>style="${style}"</#if>>
        <@message code="${code}" args=[restriction.threshold, restriction.violation]/>
    </div>
    </#if>
</#macro>

<#macro authorize granted><#if springSecurityContext.hasRole(granted)><#nested/></#if></#macro>