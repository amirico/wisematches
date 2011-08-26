<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../utils.ftl" as notify>

<p>
    Ход в игре <@notify.board board=context/> был передан следующему
    противнику <@notify.player player=context.playerTurn.playerId/>
</p>

<p>
    Обратите внимание, что состояние более одной игры могло быть изменено но только одно письмо было послано.
</p>