<#-- @ftlvariable name="country" type="wisematches.personality.profile.countries.Country" -->
<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="profile" type="wisematches.personality.profile.PlayerProfile" -->
<#-- @ftlvariable name="awards" type="wisematches.playground.award.Award[]" -->
<#include "/core.ftl">

<#macro awardImage code weight>
    <#if weight?? && weight?has_content><#assign a=weight.name()?lower_case/><#else><#assign a="default"/></#if>
<img src="/resources/images/awards/${code?replace(".", "/")?lower_case}/${a}.png" alt="">
</#macro>

<#if !principal??>
<link rel="stylesheet" type="text/css" href="/content/playground/game.css"/>
</#if>

<table width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top">
        <#if !principal??><#include "/content/info/navigation.ftl"/></#if>
        </td>
        <td valign="top">
            <table class="profile shadow ui-state-default" width="960px">
                <tr>
                    <td class="info" width="200px">
                    <#include "scriplet/info.ftl">
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
                                    <@awardImage a.code a.weight/>
                                    <div>
                                        <p>${gameMessageSource.formatDate(a.awardedDate, locale)}</p>

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