<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "..//notice/macro.ftl" as notice>

<@notice.html subject="Ход был передан следующему противника">
<p>
    Ход в игре <@notice.board board=context/> был передан следующему
    противнику <@notice.player pid=context.playerTurn.playerId/>
</p>

<p>
    Обратите внимание, что состояние более одной игры могло быть изменено но только одно письмо было послано.
</p>
</@notice.html>