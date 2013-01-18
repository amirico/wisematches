<#-- @ftlvariable name="infoId" type="java.lang.String" -->
<#-- @ftlvariable name="infoModel" type="freemarker.ext.dom.NodeModel" -->
<#-- @ftlvariable name="reverseOrder" type="java.lang.Boolean" -->

<#global reverse=reverseOrder/>
<#global level=0/>

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
    <#assign level=level+1/>
    <#if .node?children??>
    <ol>
        <#assign nodes=.node?children/>
        <#if reverse><#assign nodes=nodes?reverse/><#global reverse=false/></#if>
        <#list nodes as item>
            <li><#visit item></li>
        </#list>
    </ol>
    </#if>
    <#assign level=level-1/>
</#macro>

<#macro item>
<div class="info-item info-${infoId}-item info-item-level${level}">
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
