<#-- @ftlvariable name="country" type="wisematches.personality.profile.countries.Country" -->
<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="profile" type="wisematches.personality.profile.PlayerProfile" -->
<#-- @ftlvariable name="tourneyAwards" type="wisematches.playground.tourney.regular.TourneyAward[]" -->
<#include "/core.ftl">

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
                            </div>
                        </div>

                        <div class="edit">
                            <button onclick="wm.util.url.redirect('/playground/profile/view?p=${player.id}')">
                            <@message code="profile.view"/>
                            </button>
                        </div>

                        <div class="awards ui-layout-table" style="padding-top: 10px">
                        <#list tourneyAwards as a>
                            <#if (a_index%2)==0>
                            <div></#if>
                            <div>
                                <div class="award-full">
                                    <img src="/resources/images/tourney/${a.medal.name()?lower_case}.png"
                                         alt="<@message code="tourney.medal.${a.medal.name()?lower_case}.label"/>">

                                    <div>
                                        <p>${gameMessageSource.formatDate(a.awardedDate, locale)}</p>

                                        <p><@wm.tourney.tourney a.tourney, true/></p>

                                        <p><@wm.tourney.language a.language/>,
                                            <@wm.tourney.section a.section/> <@message code="tourney.level.label"/></p>
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

<script type="text/javascript">
    $(".profile button").button();
</script>
