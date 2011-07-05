<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../macro.ftl" as mail>

<@mail.html subject="A turn have been moved to an opponent">
<p>
    A turn in game <@mail.board board=context/> has been moved to the
    player <@mail.player pid=context.playerTurn.playerId/>
</p>

<p>
    Please note that more than one game has been updated but only one email has been sent.
</p>
</@mail.html>