<#macro declination val one two three>
    <#assign v = (val?string.computer)>
${v}
    <#if (val>=5 && val<=20)>
    ${three}
        <#else>
            <#assign v = v[v?length-1]>
            <#if v =='1'>${one}<#elseif (v=='2') || (v =='3') || (v =='4')>${two}<#else>${three}</#if>
    </#if>
</#macro>

<#macro daysAsString days><@declination val=days one="день" two="дня" three="дней"/></#macro>

<#macro hoursAsString hours><@declination val=hours one="час" two="часа" three="часов"/></#macro>

<#macro minutesAsString minutes><@declination val=minutes one="минута" two="минуты" three="минут"/></#macro>
