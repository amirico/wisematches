<#-- @ftlvariable name="id" type="java.lang.String" -->
<#-- @ftlvariable name="informationHolder" type="freemarker.ext.dom.NodeModel" -->

<div id="info-${id}">
    <div id="info-${id}" class="info-header">
    <#if informationHolder.label[0]??>
        <div id="info-${id}-label" class="info-label">${informationHolder.label}</div>
    </#if>

    <#if informationHolder.description[0]??>
        <div id="info-${id}-description" class="info-description">${informationHolder.description}</div>
    </#if>
    </div>

    <div id="info-${id}-items" class="info-items">
    <#visit informationHolder.items>
    </div>
</div>

<#macro items>
    <#if .node?children??>
    <ol>
        <#list .node?children as item>
            <li><#visit item></li>
        </#list>
    </ol>
    </#if>
</#macro>

<#macro item>
<div class="info-item info-${id}-item">
    <#if .node.image[0]??>
        <div class="info-image info-${id}-image">
            <img src="${.node.image}"/>
        </div>
    </#if>
    <#if .node.label[0]??>
        <div class="info-label info-${id}-label">${.node.label}</div>
    </#if>
    <#if .node.description[0]??>
        <div class="info-description info-${id}-description">${.node.description}</div>
    </#if>
    <#if .node.links[0]??>
        <div class="info-links info-${id}-links">${.node.links}</div>
    </#if>

    <#if .node.items[0]??>
        <#visit .node.items[0]>
    </#if>
    <div class="space-line"></div>
</div>
</#macro>
