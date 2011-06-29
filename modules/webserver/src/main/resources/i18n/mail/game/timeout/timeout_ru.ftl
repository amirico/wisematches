<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../macro.ftl" as mail>

<@mail.html subject="Время вашего хода истекает">
<p> Врея вашего хода в игре <@mail.board board=context/>
</p>

<p>
    У вас есть <em>${timeLimitMessage}</em> что бы завершить ваш ход. В противном случае игра будет прервана и вам
    засчитано поражение.
</p>

</@mail.html>