<#-- @ftlvariable name="notificationsView" type="wisematches.server.web.controllers.personality.settings.view.NotificationsTreeView" -->

<#include "/core.ftl">

<table class="common-settings ui-widget-content ui-state-default shadow ui-corner-all" style="background-image: none;"
       width="100%">

<#list notificationsView.sections as section>
    <tr>
        <td colspan="2">
            <h2 style="margin-bottom: 0;">
                <@message code="account.modify.notice.section.${section}"/>
            </h2>
        </td>
    </tr>

    <#assign subs=notificationsView.getSubsections(section)/>
    <#list subs as sub>
        <#if (subs?size > 1)>
            <tr>
                <td colspan="2">
                    <h4 style="margin: 0;">
                        <@message code="account.modify.notice.section.${sub}"/>
                    </h4>
                </td>
            </tr>
        </#if>

        <#list notificationsView.getCodes(sub) as code>
            <#assign scope=notificationsView.getScope(code)!""/>
            <tr>
                <td>
                    <select id="field${code}" name="${code}">
                        <option value="" <#if !scope?has_content>selected="selected"</#if>>
                            <@message code="account.modify.notice.disabled.label"/>
                        </option>
                        <option value="external"
                                <#if scope?has_content && !scope.internal && scope.external>selected="selected"</#if>>
                            <@message code="account.modify.notice.external.label"/>
                        </option>
                        <#if code != "playground.message.received">
                            <option value="internal"
                                    <#if scope?has_content && scope.internal && !scope.external>selected="selected"</#if>>
                                <@message code="account.modify.notice.internal.label"/>
                            </option>
                            <option value="global"
                                    <#if scope?has_content && scope.internal && scope.external>selected="selected"</#if>>
                                <@message code="account.modify.notice.global.label"/>
                            </option>
                        </#if>
                    </select>
                </td>
                <td>
                    <div>
                        <label for="field${code}">
                            <@message code="account.modify.notice.${code?lower_case}.label"/>
                        </label>
                    </div>
                    <div class="sample">
                        <@message code="account.modify.notice.${code?lower_case}.description"/>
                    </div>
                </td>
            </tr>
        </#list>
    </#list>

    <#if section_has_next>
        <tr>
            <td colspan="2" class="ui-state-default shadow"></td>
        </tr>
    </#if>
</#list>

    <tr>
        <td colspan="2" class="ui-state-default shadow"></td>
    </tr>
</table>
