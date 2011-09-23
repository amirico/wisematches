<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../utils.ftl" as notify>

<#assign move=context.getGameMoves().get(context.gameMovesCount - 1)/>

<p>State of the game <@notify.board board=context/> has been changed:
    player <@notify.player player=move.playerMove.playerId/> has
<#switch move.playerMove.class.simpleName>
    <#case "ExchangeTilesMove">
        exchanged ${move.tilesIds.size} tile(s)
        <#break>
    <#case "MakeWordMove">
        made word '<em>${move.playerMove.word.text}</em>' to ${move.points} points
        <#break>
    <#case "PassTurnMove">
        passed a turn
        <#break>
</#switch>
    and turn has been transferred to
<#if principal.id==context.playerTurn.playerId>you<#else><@notify.player player=context.playerTurn.playerId/></#if>.
</p>

<#if principal.id==context.playerTurn.playerId>
<p>
    You have <em>${gameMessageSource.formatRemainedTime(context, locale)}</em> to make a turn or game will be timed out
    and you will be defeated.
</p>
</#if>

<p>
    Please note that more than one game has been updated but only one email has been sent.
</p>