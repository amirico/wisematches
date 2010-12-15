<#include "/core.ftl">

<#macro infoPanel id items numbered=true>
<div id="info-${id}">
    <div id="info-${id}-header">
        <h2><@spring.message code="info.${id}.label"/></h2>
    </div>
    <div id="info-${id}-text">
    <@spring.message code="info.${id}.text"/>
    </div>

    <div id="info-${id}-items">
        <#list items as name>
            <div class="info-${id}-item">
                <div class="info-${id}-image">
                    <img src="<@spring.message code="info.${id}.${name}.image"/>"/>
                </div>
                <div class="info-${id}-name">
                    <#if numbered>${name_index+1}.</#if><@spring.message code="info.${id}.${name}.label"/>
                </div>
                <div class="info-${id}-description">
                <@spring.message code="info.${id}.${name}.description"/>
                </div>
                <div class="space-line"></div>
            </div>
        </#list>
    </div>
</div>
</#macro>