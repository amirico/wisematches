<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../utils.ftl" as notify>

<p> Your move time is running out for board <@notify.board board=context/>
</p>

<p>
    You have <strong><em>${timeLimitMessage}</em></strong> to make a turn or game will be terminated and you will be
    defeated.
</p>