<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget class="boardChallenge" title="game.challenge.label" style="padding-top: 10px;" help="board.challenge">
<a href="/playground/scribble/create?t=board&p=${board.boardId}">Challenge opponents to new game</a>
</@wm.widget>