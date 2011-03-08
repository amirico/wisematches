<#--
    This is simple round panel with ExtJS styles. Two possible attributes are provided: additional class
    and style.
-->

<#macro widget title id="" class="" style="">
<div class="ui-widget" <#if style??>style="${style}"</#if>>
    <div class="ui-widget-header ui-corner-top">${title}</div>
    <div <#if id??>id="${id}"</#if> class="ui-widget-content">
        <#nested/>
    </div>
</div>
</#macro>

<#macro topRoundPanel id="" class="" style="">
<@abstractPanel id="${id}" class="ui-corner-top ${class}" style="${style}">
    <#nested>
</@abstractPanel>
</#macro >

<#macro bottomRoundPanel id="" class="" style="">
<@abstractPanel id="${id}" class="ui-corner-bottom ${class}" style="${style}">
    <#nested>
</@abstractPanel>
</#macro >

<#macro roundPanel id="" class="" style="">
<@abstractPanel id="${id}" class="ui-corner-all ${class}" style="${style}">
    <#nested>
</@abstractPanel>
</#macro >

<#macro abstractPanel class style id="">
<div <#if id?has_content>id="${id}"</#if> class="ui-widget-content ${class!}"
     <#if style?has_content>style="${style}"</#if>>
    <#nested>
</div>
</#macro>