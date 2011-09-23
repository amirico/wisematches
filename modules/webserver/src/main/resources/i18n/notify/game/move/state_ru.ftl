<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../utils.ftl" as notify>

<#assign move=context.getGameMoves().get(context.gameMovesCount - 1)/>

<p>Состояние игры <@notify.board board=context/> было изменено:
    игрок <@notify.player player=move.playerMove.playerId/>
<#switch move.playerMove.class.simpleName>
    <#case "ExchangeTilesMove">
        обменял ${move.tilesIds.size} фишек
        <#break>
    <#case "MakeWordMove">
        составил слово '<em>${move.playerMove.word.text}</em>' за ${move.points} очков
        <#break>
    <#case "PassTurnMove">
        пропустил ход
        <#break>
</#switch>
    и ход был передан
<#if principal.id==context.playerTurn.playerId>вам<#else><@notify.player player=context.playerTurn.playerId/></#if>.
</p>

<#if principal.id==context.playerTurn.playerId>
<p>
    У вас есть <em>${gameMessageSource.formatRemainedTime(context, locale)}</em> чтобы совершить ход. В противном
    случае игра будет прарвана и вам засчитано поражение.
</p>
</#if>

<p>
    Обратите внимание, что состояние более одной игры могло быть изменено но только одно письмо было отправлено.
</p>