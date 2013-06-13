<#include "/macro/message.ftl"/>

<#macro tourney tourneyId link=false>
    <#assign id=tourneyId/>
    <#if !(id?is_number)><#assign id=tourneyId.number/></#if>
    <#if link><a
            href="/playground/tourney/view?t=${id?string}"></#if>${id?string}${messageSource.getNumeralEnding(id, locale)} <@message code="tourney.label"/><#if link></a></#if></#macro>

<#macro round roundId link=false final=false>
    <#assign divisionId=roundId.divisionId/>
    <#assign tourneyId=divisionId.tourneyId/>
    <#if link><a
            href="/playground/tourney/view?t=${tourneyId.number}&l=${divisionId.language.ordinal()}&s=${divisionId.section.ordinal()}&r=${roundId.round?string}"></#if>
    <#if final><@message code="tourney.round.final.label"/><#else><@message code="tourney.round.label"/> ${roundId.round?string}</#if><#if link></a></#if></#macro>

<#macro group groupId link=false>
    <#assign roundId=groupId.roundId/>
    <#assign divisionId=roundId.divisionId/>
    <#assign tourneyId=divisionId.tourneyId/>
    <#if link><a
            href="/playground/tourney/view?t=${tourneyId.number}&l=${divisionId.language.ordinal()}&s=${divisionId.section.ordinal()}&r=${roundId.round?string}&g=${groupId.group}"></#if>
    <@message code="tourney.group.label"/> ${groupId.group?string}<#if link></a></#if></#macro>

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

<#macro dates tourney class>
<span class="${class}">(<#if tourney.startedDate??><@message code="tourney.started.label"/>:
${messageSource.formatDate(tourney.startedDate, locale)}</#if><#if tourney.finished??>,
    <@message code="tourney.finished.label"/>:
${messageSource.formatDate(tourney.finishedDate, locale)})</#if>)</span></#macro>

<#macro resolution r points><#assign name=r.name()?lower_case/><#if points>${r.points/2} </#if><span
        class="tourney-resolution-${name}"><#if points>
    (</#if><@message code="tourney.success.${name}.label"/><#if points>)</#if></span></#macro>
