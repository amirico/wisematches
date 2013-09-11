<#-- @ftlvariable name="context.board" type="wisematches.playground.GameBoard" -->
<#-- @ftlvariable name="context.expirationType" type="wisematches.playground.scribble.expiration.ScribbleExpirationType" -->
<#import "../../utils.ftl" as util>

<p> Врея вашего хода в игре <@util.board board=context.board/> истекает.</p>

<p>
    У вас есть
    <strong><em>${messageSource.formatTimeMillis(context.expirationType.remainedTime, locale)}</em></strong>
    что бы завершить ваш ход. В противном случае игра будет
    прервана и вам засчитано поражение.
</p>