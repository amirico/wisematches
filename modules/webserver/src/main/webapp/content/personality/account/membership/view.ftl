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

            <div class="ui-layout-table">
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
                    <div class="center sample"><@message code="membership.subscription.notallowed"/></div>
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