<#-- @ftlvariable name="context.board" type="wisematches.playground.GameBoard" -->
<#-- @ftlvariable name="context.expirationType" type="wisematches.playground.propose.ProposalExpirationType" -->
<#import "../../utils.ftl" as util>

<p>У вас есть вызов которые будет автоматически отклонен через <strong><em>${gameMessageSource.formatMillis(context.expirationType.remainedTime, locale)}</em></strong>.</p>
<br>
<#include "initiated_ru.ftl"/>