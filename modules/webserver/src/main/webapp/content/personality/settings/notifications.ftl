<#-- @ftlvariable name="notificationMask" type="wisematches.server.web.services.notice.NotificationMask" -->
<#-- @ftlvariable name="notificationDescriptions" type="java.util.Collection<wisematches.server.web.services.notice.NotificationDescription>" -->

<#include "/core.ftl">

<#assign lastGroup=""/>
<table class="common-settings ui-widget-content ui-state-default shadow ui-corner-all" style="background-image: none;"
       width="100%">
<#list notificationDescriptions?sort_by('group') as desc>
    <#if (desc_index !=0 && lastGroup!=desc.group)>
        <tr>
            <td colspan="2" class="ui-state-default shadow"></td>
        </tr>
    </#if>
    <#if lastGroup!=desc.group>
        <tr>
            <td colspan="2">
                <h2 style="margin-bottom: 0;">
                <@message code="account.modify.notice.group.${desc.group?lower_case}"/>
                </h2>
            </td>
        </tr>
    </#if>
    <#assign lastGroup=desc.group/>
    <tr>
        <td style="padding-top: 4px; width: 10px;">
            <input id="field${desc.name}" name="${desc.name}" type="checkbox"
                   <#if notificationMask.isEnabled(desc.name)>checked="checked"</#if> value="true">
        </td>
        <td>
            <div>
                <label for="field${desc.name}">
                <@message code="account.modify.notice.${desc.name?lower_case}.label"/>
                </label>
            </div>
            <div class="sample">
            <@message code="account.modify.notice.${desc.name?lower_case}.description"/>
            </div>
        </td>
    </tr>
</#list>
    <tr>
        <td colspan="2" class="ui-state-default shadow"></td>
    </tr>

    <tr>
        <td></td>
        <td align="left">
            <button name="save" type="submit" value="submit"><@message code="account.modify.save"/></button>
        </td>
    </tr>
</table>
