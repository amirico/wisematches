<#-- @ftlvariable name="infoId" type="java.lang.String" -->
<#-- @ftlvariable name="infoModel" type="freemarker.ext.dom.NodeModel" -->

<div id="info-${infoId}">
    <div id="info-${infoId}-header" class="info-header">
    <#if infoModel.label[0]??>
        <div id="info-${infoId}-label" class="info-label">${infoModel.label}</div>
    </#if>

    <#if infoModel.description[0]??>
        <div id="info-${infoId}-description" class="info-description">${infoModel.description}</div>
    </#if>
    </div>

    <div id="info-${infoId}-items" class="info-items">
    <#visit infoModel.items>
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
<div class="info-item info-${infoId}-item">
    <#if .node.image[0]??>
        <div class="info-image info-${infoId}-image">
            <img src="${.node.image}"/>
        </div>
    </#if>
    <#if .node.label[0]??>
        <div class="info-label info-${infoId}-label">${.node.label}</div>
    </#if>
    <#if .node.description[0]??>
        <div class="info-description info-${infoId}-description">${.node.description}</div>
    </#if>
    <#if .node.links[0]??>
        <div class="info-links info-${infoId}-links">${.node.links}</div>
    </#if>

    <#if .node.items[0]??>
        <#visit .node.items[0]>
    </#if>
    <div class="space-line"></div>
</div>
</#macro>
