<#-- @ftlvariable name="serverHostName" type="java.lang.String" -->
<#-- @ftlvariable name="ratingManager" type="wisematches.playground.RatingManager" -->
<#import "/spring.ftl" as spring/>
<#macro board board><@link href="playground/scribble/board?b=${board.boardId}">#${board.boardId} (${gameMessageSource.getBoardTitle(board, locale)})</@link></#macro>

<#macro player player showRating=false>
    <#assign person=player>
    <#if player?is_number><#assign person=personalityManager.getPlayer(player?number)><#else><#assign person=personalityManager.getPlayer(player)></#if>
    <#assign computerPlayer=(person.playerType == "GUEST") || (person.playerType == "ROBOT")/>
    <#if computerPlayer>
    <em>${gameMessageSource.getPlayerNick(person, locale)}</em>
    <#else>
        <@link href="playground/profile/view?p=${person.id}">
        <em>${gameMessageSource.getPlayerNick(person, locale)}</em></@link>
    </#if>
    <#if showRating> (${ratingManager.getRating(person)?string})</#if>
</#macro>

<#macro link href target=""><#local content><#nested></#local><a
        href="http://${serverHostName}/${href}"<#if target?has_content>
        target="${target}"</#if>><#if content?has_content>${content?string}<#else>http://${serverHostName}
    /${href}</#if></a></#macro>

<#macro mailto box><a href="mailto:${box}@${serverHostName}">${box}@${serverHostName}</a></#macro>