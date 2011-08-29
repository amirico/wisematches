<#-- @ftlvariable name="context" type="wisematches.playground.propose.ChallengeGameProposal" -->
<#import "../../utils.ftl" as notify>

<p>
    Ваш вызов #${context.id} "${context.gameSettings.title!""}" был отклонен игроком либо его время ожидания истекло.
</p>
<p>
    Если вы не знакомы с этим игроком мы не рекомендуем ваш отправлять новый вызов.
</p>