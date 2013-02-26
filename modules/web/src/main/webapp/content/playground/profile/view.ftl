<#-- @ftlvariable name="player" type="wisematches.core.Member" -->
<#-- @ftlvariable name="profile" type="wisematches.core.personality.player.profile.PlayerProfile" -->
<#include "/core.ftl">

<table width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top">
        <#if !principal??><#include "/content/assistance/navigation.ftl"/></#if>
        </td>
        <td valign="top">
            <table class="profile shadow ui-state-default">
                <tr>
                    <td class="info" width="200px" style="padding: 0; margin: 0">
                    <#include "scriplet/info.ftl">
                    <#include "scriplet/awards.ftl">
                    </td>

                    <td class="content shadow ui-state-default">
                        <div class="title">
                            <div class="player-name">
                            <#if (profile.realName?? && profile.realName?has_content)>
                                <strong>${profile.realName}</strong>
                            <#else>
                                <strong>${player.nickname}</strong>
                            </#if>
                            </div>
                            <div class="registered">
                            <@message code="profile.registered"/>:
                            ${messageSource.formatDate(profile.creationDate, locale)}
                            </div>
                        </div>
                    <#if principal?? && principal.id == player.id>
                        <div class="edit">
                            <button class="wm-ui-button"
                                    onclick="wm.util.url.redirect('/playground/profile/edit')"><@message code="profile.edit"/></button>
                        </div>
                    </#if>

                    <#include "statistics.ftl"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>