<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../utils.ftl" as notify>

<p> It's your turn in a game <@notify.board board=context/></p>

<p>
    You have <em>${gameMessageSource.formatRemainedTime(context, locale)}</em> to make a turn or game will be timed out
    and you will be defeated.
</p>

<p>
    Please note that more than one game has been updated but only one email has been sent.
</p>