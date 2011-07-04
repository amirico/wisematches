<#-- @ftlvariable name="memberships" type="java.util.Collection<wisematches.personality.Membership>" -->
<#-- @ftlvariable name="restrictionDescriptions" type="java.util.Collection<wisematches.server.web.services.restriction.RestrictionDescription>" -->
<#include "/core.ftl">

<div class="profile">
    <div class="membership">
        <div class="ui-widget-header ui-corner-all shadow">
        <@message code="membership.label"/>
        </div>
        <div class="ui-state-default ui-background-no ui-corner-all shadow">
            <div>
            <@message code="membership.description"/>
            </div>

            <div>
            <@message code="membership.level.your"/>:
                <strong><@message code="membership.name.${principal.membership.name()?lower_case}"/></strong>
            </div>

            <div class="ui-layout-table shadow" style="width: 100%">
                <div>
                    <div><@message code="membership.level"/></div>
                <#list memberships as m>
                    <div class="center">
                        <strong><@message code="membership.name.${m.name()?lower_case}"/> </strong>
                    </div>
                </#list>
                </div>

                <div>
                    <div></div>
                <#list memberships as m>
                    <#if principal.membership == "GUEST" && m == "BASIC">
                        <div class="center">
                            <button onclick="wm.util.url.redirect('/account/create')">
                            <@message code="membership.register"/>
                            </button>
                        </div>
                        <#elseif (principal.membership.ordinal() < m.ordinal())>
                            <div class="center sample">
                                <button disabled="disabled"><@message code="membership.subscribe"/></button>
                            </div>
                        <#else>
                            <div></div>
                    </#if>
                </#list>
                </div>

            <#list restrictionDescriptions as d>
                <div>
                    <div><@message code="membership.description.${d.name?lower_case}"/></div>
                    <#list memberships as m>
                        <div class="center">${d.getRestriction(m)}</div>
                    </#list>
                </div>
            </#list>
                <div>
                    <div><@message code="membership.description.ads"/></div>
                <#list memberships as m>
                    <div class="center">
                        <#if m.adsVisible><@message code="membership.yes"/><#else><@message code="membership.no"/></#if>
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