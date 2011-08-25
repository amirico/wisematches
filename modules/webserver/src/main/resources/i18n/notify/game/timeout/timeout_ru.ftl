<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "..//notice/macro.ftl" as notice>

<@notice.html subject="Время вашего хода истекает">
<p> Врея вашего хода в игре <@notice.board board=context/>
</p>

<p>
    У вас есть <em>${timeLimitMessage}</em> что бы завершить ваш ход. В противном случае игра будет прервана и вам
    засчитано поражение.
</p>

</@notice.html>