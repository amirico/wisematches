<#-- @ftlvariable name="context" type="wisematches.playground.propose.ChallengeGameProposal" -->
<#import "../utils.ftl" as notify>

<p>
    Вы получили вызов от игрока <b><@notify.player player=context.initiator/></b>. Вы можете принять вызов и
    начать новую игру используя <@notify.link href="/playground/scribble/join?p=${context.id}">эту ссылку</@notify.link>
    или
    присоединиться к игре в <@notify.link href="/playground/scribble/join">игровой зоне</@notify.link>.
</p>