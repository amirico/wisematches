<#-- @ftlvariable name="restrictionManager" type="wisematches.playground.restriction.RestrictionManager" -->
<#include "/core.ftl">

<#assign memberships=[Membership.BASIC, Membership.SILVER, Membership.GOLD, Membership.PLATINUM]/>

<table width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top">
        <#if !principal??><#include "/content/assistance/navigation.ftl"/></#if>
        </td>
        <td valign="top">

            <div class="profile">
                <div id="membershipView">
                    <div class="ui-widget-header ui-corner-all shadow">
                    <@message code="membership.title.label"/>
                    </div>
                    <div class="ui-widget-content ui-state-default ui-background-no ui-corner-all shadow">
                        <div>
                        <@message code="membership.description"/>
                        </div>

                        <div>
                        <@message code="membership.level.your"/>:
                            <strong>
                            <#if principal??>
                                <#if principal.playerType.guest>
                                    <@message code="membership.name.guest"/>
                                <#else>
                                    <@message code="membership.name.${principal.playerType.code}"/>
                                </#if>
                            <#else>
                                <@message code="membership.unregistered.label"/>
                                <span class="sample">(<@message code="membership.unregistered.description"/>)</span>
                            </#if>
                            </strong>
                        </div>

                        <div class="levels ui-layout-table shadow ui-corner-all">
                            <div>
                                <div class="header ui-state-hover"><@message code="membership.level"/></div>
                            <#list memberships as m>
                                <div class="header ui-state-hover">
                                    <div class="player personality">
                                        <div class="icon ${m.code}"></div>
                                        <div class="nickname"><@message code="membership.name.${m.name()?lower_case}"/></div>
                                    </div>
                                </div>
                            </#list>
                            </div>

                            <div>
                                <div class="header subscribe ui-state-hover"></div>
                            <#list memberships as m>
                                <div class="header subscribe ui-state-hover">
                                    <#if m.default>
                                        <#if !principal?? || principal.playerType.guest>
                                            <button onclick="wm.util.url.redirect('/account/create')">
                                                <@message code="membership.register"/>
                                            </button>
                                        </#if>
                                    <#else>
                                        <#if (!principal?? || !principal.playerType.personality || principal.playerType.ordinal() >= m.ordinal())>
                                            <button disabled="disabled"><@message code="membership.subscribe"/></button>
                                        <#else>
                                            <button onclick="wm.util.url.redirect('/account/membership/subscribe')"><@message code="membership.subscribe"/></button>
                                        </#if>
                                    </#if>
                                </div>
                            </#list>
                            </div>

                        <#list restrictionManager.restrictionNames as name>
                            <div>
                                <div><@message code="membership.description.${name?lower_case}"/></div>
                                <#list memberships as m>
                                    <div class="center">${restrictionManager.getRestrictionThreshold(name, m)}</div>
                                </#list>
                            </div>
                        </#list>
                            <div>
                                <div><@message code="membership.description.ads"/></div>
                            <#list memberships as m>
                                <div class="center">
                                    <#if m.default><@message code="membership.yes"/><#else><@message code="membership.no"/></#if>
                                </div>
                            </#list>
                            </div>
                        </div>

                        <div>
                        <@message code="membership.warning"/>
                        </div>

                        <div class="sample">
                        <@message code="membership.testing"/>
                        </div>
                    </div>
                </div>
            </div>
        </td>
    </tr>
</table>