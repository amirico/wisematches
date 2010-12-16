<#-- @ftlvariable name="informationHolder" type="freemarker.ext.dom.NodeModel" -->
<#import "../custom-el.ftl" as cust>

<#if informationHolder.label!>
<div id="terms-header">${informationHolder.label}</div>
</#if>

<#if informationHolder.description!>
<div id="terms-description">${informationHolder.description}</div>
</#if>

<div id="terms-content">
<#recurse informationHolder>

    </#recurse>
<#--
<#list informationHolder.items as item>
    <ul class="terms-section">
        <li>
            <span class="terms-section-header">${item.label}</span>
            <ul>
                <#list item.items as subItem>
                    <li class="terms-section-item">
                    ${subItem.description}
                    </li>
                </#list>
            </ul>
        </li>
    </ul>
</#list>
-->
</div>
