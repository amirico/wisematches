<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="awardTypes" type="wisematches.playground.award.AwardType[]" -->
<#-- @ftlvariable name="awardWeights" type="wisematches.playground.award.AwardWeight[]" -->
<#-- @ftlvariable name="awardsSummary" type="wisematches.playground.award.AwardsSummary" -->
<#-- @ftlvariable name="awardDescriptors" type="java.util.Collection<wisematches.playground.award.AwardDescriptor>" -->

<#include "/core.ftl">

<#macro awardImage code weight>
    <#if weight??><#assign a=weight.name()?lower_case/><#else><#assign a="default"/></#if>
<img src="/resources/images/awards/${code?replace(".", "/")?lower_case}/${a}.png" alt="">
</#macro>

<div class="awards">
<#list awardDescriptors as d>
    <#if awardsSummary.hasAwards(d.code)>
        <#if d.type.medal>
            <#list awardsSummary.getAwardWeights(d.code) as w>
                <div class="award medal">
                    <@awardImage d.code w/>

                    <p>${awardsSummary.getAwardsCount(d.code, w)}</p>
                </div>
            </#list>
        <#elseif d.type.badge>
            <div class="award badge">
                <@awardImage d.code awardsSummary.getHighestWeight(d.code)/>
                <p>&nbsp;</p>
            </div>
        </#if>
    </#if>
</#list>
<#--

<#list awardsSummary.awardNames as code>
    <#assign descriptor=awardDescriptors.get(code)/>
    <#if descriptor.type.medal>
        <div class="award medal">
        </div>
    <#elseif descriptor.type.ribbon>
        <#assign weidght=awardsSummary.getHighestWeight(code)/>
        <div class="award ribbon">
        </div>
    </#if>
</#list>
-->

<#--<#list awardTypes as t>-->
<#--<div>-->
<#--<h1>${t.name()}</h1>-->
<#--<#list t.filter(awardDescriptors) as d>-->
<#--<#list awardWeights?reverse as w>-->
<#--<div class="award">-->
<#--${d.code}.${w.name()} ${awardsSummary.getAwardsCount(d.code, w)}-->
<#--</div>-->
<#--</#list>-->
<#--</#list>-->
<#--</div>-->
<#--</#list>-->

<#--
<#assign totalCount=0/>
<#list tourneyMedals as m>
    <#assign count=tourneyCareer.getMedalsCount(m)/>
    <#assign totalCount=totalCount+count/>

    <#if count!=0>
        <div class="award">
            <img src="/resources/images/tourney/${m.name()?lower_case}.png"
                 alt="<@message code="tourney.medal.${m.name()?lower_case}.label"/>">

            <p>${count?string}</p>
        </div>
    </#if>
</#list>

<#if totalCount != 0>
    <div style="text-align: right">
        <a href="/playground/profile/awards?p=${player.id?string}"><@message code="profile.awards.label"/></a>
    </div>
</#if>
-->
</div>
