<#--
    This is simple round panel with ExtJS styles. Two possible attributes are provided: additional class
    and style.
-->

<#macro topRoundPanel id="" class="" style="">
<@abstractPanel id="${id}" class="x-panel-round-top ${class}" style="${style}">
    <#nested>
</@abstractPanel>
</#macro >

<#macro bottomRoundPanel id="" class="" style="">
<@abstractPanel id="${id}" class="x-panel-round-bottom ${class}" style="${style}">
    <#nested>
</@abstractPanel>
</#macro >

<#macro roundPanel id="" class="" style="">
<@abstractPanel id="${id}" class="x-panel-round ${class}" style="${style}">
    <#nested>
</@abstractPanel>
</#macro >

<#macro abstractPanel class style id="">
<div <#if id?has_content>id="${id}"</#if> class="x-panel ${class!}" <#if style?has_content>style="${style}"</#if>>
    <div class="x-panel-tl">
        <div class="x-panel-tr">
            <div class="x-panel-tc"></div>
        </div>
    </div>
    <div class="x-panel-ml">
        <div class="x-panel-mr">
            <div class="x-panel-mc">
                <div class="x-panel-body">
                    <#nested>
                </div>
            </div>
        </div>
    </div>
    <div class="x-panel-bl x-panel-nofooter">
        <div class="x-panel-br">
            <div class="x-panel-bc"></div>
        </div>
    </div>
</div>
</#macro>