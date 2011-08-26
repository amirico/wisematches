<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../utils.ftl" as notify>

<p>
    A turn in game <@notify.board board=context/> has been moved to the
    player <@notify.player player=context.playerTurn.playerId/>
</p>

<p>
    Please note that more than one game has been updated but only one email has been sent.
</p>