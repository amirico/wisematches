<#-- @ftlvariable name="notificationMask" type="wisematches.server.web.services.notice.NotificationMask" -->
<#-- @ftlvariable name="notificationDescriptions" type="java.util.Collection<wisematches.server.web.services.notice.NotificationDescription>" -->

<#include "/core.ftl">

<#assign lastSeries=""/>
<table class="common-settings ui-widget-content ui-state-default shadow ui-corner-all" style="background-image: none;"
       width="100%">
<#list notificationDescriptions?sort_by("series") as desc>
    <#if (desc_index !=0 && lastSeries!=desc.series)>
        <tr>
            <td colspan="2" class="ui-state-default shadow"></td>
        </tr>
    </#if>
    <#if lastSeries!=desc.series>
        <tr>
            <td colspan="2"><h4 style="margin-bottom: 0;">${desc.series?upper_case}</h4></td>
        </tr>
    </#if>
    <#assign lastSeries=desc.series/>
    <tr>
        <td style="padding-top: 4px; width: 10px;">
            <input id="field${desc.name}" name="${desc.name}" type="checkbox"
                   <#if notificationMask.isEnabled(desc.name)>checked="checked"</#if>>
        </td>
        <td>
            <div><label for="field${desc.name}">${desc.name}</label></div>
            <div class="sample">This is description for ${desc.name}</div>
        </td>
    </tr>
</#list>
    <tr>
        <td colspan="2" class="ui-state-default shadow"></td>
    </tr>
</table>
