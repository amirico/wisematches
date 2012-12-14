<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->

<#include "/macro/message.ftl"/>

<#macro name board link=true number=true>
    <#assign title=gameMessageSource.getBoardTitle(board, locale)/>
    <#if link><@href board.boardId>${title}<#if number> #${board.boardId}</#if></@href><#else>${title} <#if number> #${board.boardId}</#if></#if>
</#macro>

<#macro href boardId>
<a href="/playground/scribble/board?b=${boardId}"><#nested/></a>
</#macro>