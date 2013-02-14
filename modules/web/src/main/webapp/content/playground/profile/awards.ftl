<#-- @ftlvariable name="country" type="wisematches.personality.membership.Country" -->
<#-- @ftlvariable name="player" type="wisematches.core.Personality" -->
<#-- @ftlvariable name="profile" type="wisematches.core.personality.player.profile.PlayerProfile" -->
<#-- @ftlvariable name="awards" type="wisematches.server.services.award.Award[]" -->
<#include "/core.ftl">

<table width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top">
        <#if !principal??><#include "/content/assistance/navigation.ftl"/></#if>
        </td>
        <td valign="top">
            <table class="profile shadow ui-state-default" width="960px">
                <tr>
                    <td class="info" width="200px">
                    <#include "scriplet/info.ftl">
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
                            <@message code="profile.awards.description"/>
                                (<a href="/info/awards"><@message code="info.awards.label"/></a>)
                            </div>
                        </div>

                        <div class="edit">
                            <button class="wm-ui-button"
                                    onclick="wm.util.url.redirect('/playground/profile/view?p=${player.id}')">
                            <@message code="profile.view"/>
                            </button>
                        </div>

                        <div class="awards ui-layout-table" style="padding-top: 10px;">
                        <#list awards as a>
                            <#if (a_index%2)==0>
                            <div></#if>
                            <div>
                                <div class="award-full">
                                    <@wm.award.image a.code a.weight/>
                                    <div>
                                        <p>
                                        ${gameMessageSource.formatDate(a.awardedDate, locale)}
                                        </p>

                                        <p><@message code="awards.${a.code}.label"/></p>
                                        <#if a.relationship??>
                                            <#if a.relationship.code==1>
                                                <p><@wm.tourney.tourney a.relationship.id, true/></p>
                                            </#if>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                            <#if (a_index%2)!=0></div></#if>
                        </#list>
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>