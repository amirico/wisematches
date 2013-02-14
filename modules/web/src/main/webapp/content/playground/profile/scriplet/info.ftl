<#-- @ftlvariable name="player" type="wisematches.core.Personality" -->
<#-- @ftlvariable name="profile" type="wisematches.core.personality.player.profile.PlayerProfile" -->
<#-- @ftlvariable name="country" type="wisematches.personality.membership.Country" -->

<#include "/core.ftl">

<#include "/content/addthis.ftl"/>

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
    <#assign m=player.playerType/>
        <div class="player member">
        <#if !m.default>
            <div class="membership ${m.code}"></div></#if>
            <a href="/account/playerType">
                <div class="nickname"><@message code="membership.name.${m.code}"/> <@message code="member.label"/></div>
            </a>
        </div>
    </div>
    <div style="padding-top: 4px; padding-bottom: 4px;">
    <#if principal?? && principal.id == player.id>
    <@addthis title="share.profile.my.label" description="share.profile.my.description" args=[principal.nickname]/>
<#elseif principal??>
        <@addthis title="share.profile.other.label" description="share.profile.other.description" args=[principal.nickname]/>
    </#if>
    </div>
    <div>
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
