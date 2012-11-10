<#-- @ftlvariable name="timeZones" type="java.util.Collection<wisematches.server.web.controllers.personality.settings.form.TimeZoneInfo>" -->
<#-- @ftlvariable name="settings" type="wisematches.server.web.controllers.personality.settings.form.SettingsForm" -->
<#include "/core.ftl">

<table class="common-settings ui-widget-content ui-state-default shadow ui-corner-all" style="background-image: none;"
       width="100%">
    <tr>
        <td>
            <label for="language"><@message code="account.register.language.label"/>:</label>
        </td>
        <td>
        <@wm.field path="settings.language">
            <select id="language" name="language" style="width: 170px;">
                <#list ["en", "ru"] as l>
                    <option value="${l}" <#if (locale==l)>selected="selected"</#if>>
                        <@message code="language.${l}"/>
                    </option>
                </#list>
            </select>
        </@wm.field>
            <span class="sample"><@message code="account.register.language.description"/></span>
        </td>
    </tr>
</table>