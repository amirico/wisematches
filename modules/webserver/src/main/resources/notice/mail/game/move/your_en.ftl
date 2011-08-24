<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "..//notice/macro.ftl" as notice>

<@notice.html subject="It's your turn">
<p> It's your turn in a game <@notice.board board=context/></p>

<p>
    You have <em>${gameMessageSource.formatRemainedTime(context, locale)}</em> to make a turn or game will be timed out
    and you will be defeated.
</p>

<p>
    Please note that more than one game has been updated but only one email has been sent.
</p>
</@notice.html>