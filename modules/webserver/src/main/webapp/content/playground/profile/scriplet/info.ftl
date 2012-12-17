<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="profile" type="wisematches.personality.profile.PlayerProfile" -->
<#-- @ftlvariable name="country" type="wisematches.personality.profile.countries.Country" -->

<#include "/core.ftl">

<#include "/content/templates/addthis.ftl"/>

<div class="personality">
    <div class="photo">
        <img class="shadow" style="width: 200px; height: 200px;"
             src="/playground/profile/image/view?pid=${player.id}"
             alt="Photo">
    </div>
    <div>
        <strong>${player.nickname}</strong>
    </div>
    <div>
    <#if principal?? && principal.id == player.id>
    <@addthis title="share.profile.my.label" description="share.profile.my.description" args=[principal.nickname]/>
<#elseif principal??>
        <@addthis title="share.profile.other.label" description="share.profile.other.description" args=[principal.nickname]/>
    </#if>
    </div>
    <div style="padding-top: 4px">
    <#if profile.gender??>
        <@message code="gender." + profile.gender.name()?lower_case/>,
    </#if>
    <#if profile.birthday??>${gameMessageSource.getAge(profile.birthday)} <@message code="profile.edit.years"/>
        ,</#if>
    </div>
<#if country??>
    <div>${country.name},</div>
</#if>
    <div>${player.timeZone.displayName}</div>
<#if profile.comments?? && profile.comments?has_content>
    <div class="quotation">&laquo; ${profile.comments} &raquo;</div>
</#if>
</div>
