<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "..//notice/macro.ftl" as notice>

<@notice.html subject="A turn have been moved to an opponent">
<p>
    A turn in game <@notice.board board=context/> has been moved to the
    player <@notice.player pid=context.playerTurn.playerId/>
</p>

<p>
    Please note that more than one game has been updated but only one email has been sent.
</p>
</@notice.html>