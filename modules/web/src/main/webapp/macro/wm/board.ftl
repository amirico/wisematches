<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->

<#include "/macro/message.ftl"/>
<#import "/macro/wm/tourney.ftl" as tourney/>

<#macro name board link=true number=true>
    <#assign title=messageSource.getBoardTitle(board, locale)/>
    <#if link><@href board.boardId>${title}<#if number> #${board.boardId}</#if></@href><#else>${title} <#if number> #${board.boardId}</#if></#if>
</#macro>

<#macro relationship relationship link=true>
    <#if relationship??>
        <#if relationship.code=1><@tourney.tourney relationship.id, link/></#if>
    </#if>
</#macro>

<#macro href boardId>
<a href="/playground/scribble/board?b=${boardId}"><#nested/></a>
</#macro>