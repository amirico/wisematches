<#-- @ftlvariable name="resourceTemplate" type="java.lang.String" -->
<#include "/core.ftl">

<table id="info-page">
    <tr>
        <td style="vertical-align: top;">
        <#include "navigation.ftl"/>
        </td>
        <td id="info-content" style="vertical-align: top; width: 100%">
        <#if resourceTemplate??><#include resourceTemplate><#else><#include "/content/info/resources.ftl"></#if>
        </td>
    </tr>
</table>