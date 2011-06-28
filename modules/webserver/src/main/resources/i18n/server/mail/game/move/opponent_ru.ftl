<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../macro.ftl" as mail>

<@mail.html subject="Ход был передан следующему противника">
<p>
    Ход в игре <@mail.board board=context/> был передан следующему
    противнику <@mail.player pid=context.playerTurn.playerId/>
</p>

<p>
    Обратите внимание, что состояние более одной игры могло быть изменено но только одно письмо было послано.
</p>
</@mail.html>