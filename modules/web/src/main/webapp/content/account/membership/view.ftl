<#-- @ftlvariable name="restrictionManager" type="wisematches.playground.restriction.RestrictionManager" -->
<#include "/core.ftl">

<#assign showGuestLimitations=!principal?? || principal.type.visitor/>
<#assign memberships=[Membership.BASIC, Membership.SILVER, Membership.GOLD, Membership.PLATINUM]/>

<table width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top">
        <#include "/content/assistance/navigation.ftl"/>
        </td>
        <td valign="top">
            <div id="membershipView">
                <div class="ui-widget-header ui-corner-all shadow">
                <@message code="membership.title.label"/>
                </div>
                <div class="ui-widget-content ui-state-default ui-background-no ui-corner-all shadow">
                    <div style="text-align: justify">
                    <@message code="membership.description"/>
                    </div>

                    <div>
                    <@message code="membership.level.your"/>:
                        <strong>
                        <#if principal??>
                            <#if principal.type.visitor>
                                <@message code="membership.name.guest"/>
                            <#else>
                                <@message code="membership.name.${principal.membership.name()?lower_case}"/>
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
                        <#if showGuestLimitations>
                            <div class="header ui-state-hover">
                                <div class="player visitor">
                                    <div class="icon"></div>
                                    <div class="nickname"><@message code="membership.name.guest"/></div>
                                </div>
                            </div>
                        </#if>
                        <#list memberships as m>
                            <div class="header ui-state-hover">
                                <div class="player ${m.name()?lower_case}">
                                    <div class="icon"></div>
                                    <div class="nickname"><@message code="membership.name.${m.name()?lower_case}"/></div>
                                </div>
                            </div>
                        </#list>
                        </div>

                        <div>
                            <div class="header subscribe ui-state-hover"></div>
                        <#if showGuestLimitations>
                            <div class="header subscribe ui-state-hover"></div>
                        </#if>
                        <#list memberships as m>
                            <div class="header subscribe ui-state-hover">
                                <#if m.basic>
                                    <#if !principal?? || principal.type.visitor>
                                        <button onclick="wm.util.url.redirect('/account/create')">
                                            <@message code="membership.register"/>
                                        </button>
                                    </#if>
                                <#else>
                                    <#if (!principal?? || !principal.type.member || principal.type.ordinal() >= m.ordinal())>
                                        <button disabled="disabled"><@message code="membership.subscribe"/></button>
                                    <#else>
                                        <button disabled="disabled"
                                                onclick="wm.util.url.redirect('/account/membership/subscribe')"><@message code="membership.subscribe"/></button>
                                    </#if>
                                </#if>
                            </div>
                        </#list>
                        </div>

                    <#list restrictionManager.restrictionNames as name>
                        <div>
                            <div><@message code="membership.description.${name?lower_case}"/></div>
                            <#if showGuestLimitations>
                                <div class="center">${restrictionManager.getDefaultThreshold(name)}</div>
                            </#if>
                            <#list memberships as m>
                                <div class="center">${restrictionManager.getRestrictionThreshold(name, m)}</div>
                            </#list>
                        </div>
                    </#list>
                        <div>
                            <div><@message code="membership.description.ads"/></div>
                        <#if showGuestLimitations>
                            <div class="center"><@message code="membership.yes"/></div>
                        </#if>
                        <#list memberships as m>
                            <div class="center">
                                <#if m.basic><@message code="membership.yes"/><#else><@message code="membership.no"/></#if>
                            </div>
                        </#list>
                        </div>
                    </div>

                    <div><@message code="membership.warning"/></div>

                    <div class="sample"><@message code="membership.testing"/></div>
                </div>
            </div>
        </td>
    </tr>
</table>