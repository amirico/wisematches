<#-- @ftlvariable name="serverHostName" type="java.lang.String" -->
<#macro board board><@link href="playground/scribble/board?b=${board.boardId}">#${board.boardId} (${board.gameSettings.title})</@link></#macro>

<#macro player player><#assign person=player><#if player?is_number><#assign person=playerManager.getPlayer(player?number)></#if><@link href="playground/profile/view?p=${person.id}">
<em>${gameMessageSource.getPlayerNick(person, locale)}</em></@link></#macro>

<#macro link href target=""><#local content><#nested></#local><a href="http://${serverHostName}/${href}"
                                                                 <#if target?has_content>target="${target}"</#if>><#if content?has_content>${content?string}<#else>
    http://${serverHostName}/${href}</#if></a></#macro>

<#macro mailto box><a href="mailto:${box}@${serverHostName}">${box}@${serverHostName}</a></#macro>