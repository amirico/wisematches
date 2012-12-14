<#include "/macro/message.ftl"/>

<#macro tourney tourneyId link=false>
    <#assign id=tourneyId/>
    <#if !(id?is_number)><#assign id=tourneyId.number/></#if>
    <#if link><a
            href="/playground/tourney/view?t=${id?string}"></#if>${id?string}${gameMessageSource.getNumeralEnding(id, locale)} <@message code="tourney.label"/><#if link></a></#if>
</#macro>

<#macro round roundId link=false final=false>
    <#assign divisionId=roundId.divisionId/>
    <#assign tourneyId=divisionId.tourneyId/>
    <#if link><a
            href="/playground/tourney/view?t=${tourneyId.number}&l=${divisionId.language.ordinal()}&s=${divisionId.section.ordinal()}&r=${roundId.round?string}"></#if>
    <#if final><@message code="tourney.round.final.label"/><#else><@message code="tourney.round.label"/> ${roundId.round?string}</#if>
    <#if link></a></#if>
</#macro>

<#macro group groupId link=false>
    <#assign roundId=groupId.roundId/>
    <#assign divisionId=roundId.divisionId/>
    <#assign tourneyId=divisionId.tourneyId/>
    <#if link><a
            href="/playground/tourney/view?t=${tourneyId.number}&l=${divisionId.language.ordinal()}&s=${divisionId.section.ordinal()}&r=${roundId.round?string}&g=${groupId.group}"></#if>
    <@message code="tourney.group.label"/> ${groupId.group?string}
    <#if link></a></#if>
</#macro>

<#macro language value>
    <@message code="language.${value?lower_case}"/>
</#macro>

<#macro section value>
    <@message code="tourney.section.${value.name()?lower_case}.label"/>
</#macro>

<#macro rating section short=true>
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