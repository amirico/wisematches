<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../utils.ftl" as notify>

<p> Врея вашего хода в игре <@notify.board board=context/>
</p>

<p>
    У вас есть <strong><em>${timeLimitMessage}</em></strong> что бы завершить ваш ход. В противном случае игра будет
    прервана и вам
    засчитано поражение.
</p>