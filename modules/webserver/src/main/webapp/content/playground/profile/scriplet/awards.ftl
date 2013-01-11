<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="awardsSummary" type="wisematches.playground.award.AwardsSummary" -->

<#include "/core.ftl">

<#macro awardImage code weight>
    <#if weight?? && weight?has_content><#assign a=weight.name()?lower_case/><#else><#assign a="default"/></#if>
<img src="/resources/images/awards/${code?replace(".", "/")?lower_case}/${a}.gif"
     title="<@message code="awards.${code}.label"/>">
</#macro>

<#assign totalCount=0/>
<div class="awards">
<#assign medals=awardsSummary.getAwards(AwardType.MEDAL)!""/>
<#assign badges=awardsSummary.getAwards(AwardType.BADGE)!""/>
<#assign ribbons=awardsSummary.getAwards(AwardType.RIBBON)!""/>

<#if medals?has_content>
    <div class="awards-section">
        <div class="ui-state-hover awards-type">
            <@message code="awards.medals.label"/>
        </div>
        <div class="awards-set">
            <#list medals as d>
                <#list awardsSummary.getAwardWeights(d.code) as w>
                    <#assign totalCount=totalCount + 1/>
                    <div class="award medal">
                        <@awardImage d.code w/>

                        <p>${awardsSummary.getAwardsCount(d.code, w)}</p>
                    </div>
                </#list>
            </#list>
        </div>
    </div>
</#if>

<#if badges?has_content>
    <div class="awards-section">
        <div class="ui-state-hover awards-type">
            <@message code="awards.badges.label"/>
        </div>
        <div class="awards-set">
            <#list badges as d>
                <#assign totalCount=totalCount + 1/>
                <div class="award badge">
                    <@awardImage d.code awardsSummary.getHighestWeight(d.code)/>
                </div>
            </#list>
        </div>
    </div>
</#if>

<#if ribbons?has_content>
    <div class="awards-section">
        <div class="ui-state-hover awards-type">
            <@message code="awards.ribbons.label"/>
        </div>
        <div class="awards-set">
            <#list ribbons as d>
                <#assign totalCount=totalCount + 1/>
                <div class="award ribbon">
                    <@awardImage d.code ""/>
                </div>
            </#list>
        </div>
    </div>
</#if>

<#if totalCount != 0>
    <div style="text-align: right">
        <a href="/playground/profile/awards?p=${player.id?string}"><@message code="profile.awards.label"/></a>
    </div>
</#if>
</div>
