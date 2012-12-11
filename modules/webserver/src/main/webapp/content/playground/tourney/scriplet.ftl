<#include "/core.ftl">

<#macro tourneyName tourneyId link=false>
    <#if link><a href="/playground/tourney/view?t=${tourneyId.number?string}"></#if>
${tourneyId.number?string}${gameMessageSource.getNumeralEnding(tourneyId.number, locale)} <@message code="tourney.label"/>
    <#if link></a></#if>
</#macro>

<#macro roundName roundId link=false final=false>
    <#assign divisionId=roundId.divisionId/>
    <#assign tourneyId=divisionId.tourneyId/>
    <#if link><a
            href="/playground/tourney/view?t=${tourneyId.number}&l=${divisionId.language.ordinal()}&s=${divisionId.section.ordinal()}&r=${roundId.round?string}"></#if>
    <#if final><@message code="tourney.round.final.label"/><#else><@message code="tourney.round.label"/> ${roundId.round?string}</#if>
    <#if link></a></#if>
</#macro>

<#macro groupName groupId link=false>
    <#assign roundId=groupId.roundId/>
    <#assign divisionId=roundId.divisionId/>
    <#assign tourneyId=divisionId.tourneyId/>
    <#if link><a
            href="/playground/tourney/view?t=${tourneyId.number}&l=${divisionId.language.ordinal()}&s=${divisionId.section.ordinal()}&r=${roundId.round?string}&g=${groupId.group}"></#if>
    <@message code="tourney.group.label"/> ${groupId.group?string}
    <#if link></a></#if>
</#macro>

<#macro languageName language>
    <@message code="language.${language?lower_case}"/>
</#macro>

<#macro sectionName section>
    <@message code="tourney.section.${section.name()?lower_case}.label"/>
</#macro>

<#macro sectionInfo section short=true>
    <#if section.anyRating>
        <#if short>
            <@message code="tourney.rating.any.label"/>
        <#else>
            <@message code="tourney.rating.any.description"/>
        </#if>
    <#else>
        <#if short>
            <@message code="tourney.rating.limit.label" args=[section.topRating]/>
        <#else>
            <@message code="tourney.rating.limit.description" args=[section.topRating]/>
        </#if>
    </#if>
</#macro>
