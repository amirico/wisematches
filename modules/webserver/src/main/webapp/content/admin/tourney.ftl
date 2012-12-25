<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="words" type="wisematches.playground.scribble.Word[]" -->
<#-- @ftlvariable name="scoreEngine" type="wisematches.playground.scribble.score.ScoreEngine" -->

<#include "/core.ftl"/>

<form action="/admin/tourney" method="post">
    <label>
        Group:
        <input type="text" name="group" size="10" value="">
    </label>
    <button type="submit">Check and finalize group</button>
</form>
