<#macro daysAsString days>
${days?string.computer} <#if days=1>day<#else>days</#if>
</#macro>

<#macro hoursAsString hours>
${hours?string.computer} <#if hours=1>hour<#else>hours</#if>
</#macro>

<#macro minutesAsString minutes>
${minutes?string.computer} <#if minutes=1>minute<#else>minutes</#if>
</#macro>