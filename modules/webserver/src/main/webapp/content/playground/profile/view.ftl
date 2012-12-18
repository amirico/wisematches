<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="profile" type="wisematches.personality.profile.PlayerProfile" -->
<#include "/core.ftl">

<#if !principal??>
<link rel="stylesheet" type="text/css" href="/content/playground/game.css"/>
</#if>
<link rel="stylesheet" type="text/css" href="/content/playground/scribble/scribble.css"/>

<table width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top">
        <#if !principal??><#include "/content/info/navigation.ftl"/></#if>
        </td>
        <td valign="top">
            <table class="profile shadow ui-state-default">
                <tr>
                    <td class="info" width="200px">
                    <#include "scriplet/info.ftl"><#include "scriplet/awards.ftl">
                    </td>

                    <td class="content shadow ui-state-default">
                        <div class="title">
                            <div class="player">
                            <#if (profile.realName?? && profile.realName?has_content)>
                                <strong>${profile.realName}</strong>
                            <#else>
                                <strong>${player.nickname}</strong>
                            </#if>
                            </div>
                            <div class="registered">
                            <@message code="profile.registered"/>
                                : ${gameMessageSource.formatDate(profile.creationDate, locale)}
                            </div>
                        </div>
                    <#if principal?? && principal.id == player.id>
                        <div class="edit">
                            <button onclick="wm.util.url.redirect('/playground/profile/edit')"><@message code="profile.edit"/></button>
                        </div>
                    </#if>

                    <#include "statistics.ftl"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<script type="text/javascript">
    $(".profile button").button();
</script>