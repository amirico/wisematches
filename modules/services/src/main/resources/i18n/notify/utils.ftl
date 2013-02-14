<#ftl strip_whitespace=true strip_text=true>
<#-- @ftlvariable name="personality" type="wisematches.core.Personality" -->
<#-- @ftlvariable name="serverDescriptor" type="wisematches.server.services.ServerDescriptor" -->

<#macro board board><@link href="playground/scribble/board?b=${board.boardId}">#${board.boardId} (${messageSource.getBoardTitle(board, locale)})</@link></#macro>

<#macro player personality showRating=false>
    <#if personality.traceable>
    <em>${messageSource.getPersonalityNick(personality, locale)}</em>
    <#else>
        <@link href="playground/profile/view?p=${personality.id}">
        <em>${messageSource.getPersonalityNick(personality, locale)}</em></@link>
    </#if>
    <#if showRating> (${statisticsManager.getRating(personality)?string})</#if>
</#macro>

<#macro mailto box><a href="mailto:${box}@${serverDescriptor.mailHostName}">${box}
    @${serverDescriptor.mailHostName}</a></#macro>-->

<#macro link href target=""><#local content><#nested></#local><a
        href="http://${serverDescriptor.webHostName}/${href}"<#if target?has_content>
        target="${target}"</#if>><#if content?has_content>${content?string}<#else>http://${serverDescriptor.webHostName}
    /${href}</#if></a></#macro>
