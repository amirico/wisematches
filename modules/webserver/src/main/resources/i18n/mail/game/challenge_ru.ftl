<#-- @ftlvariable name="context" type="wisematches.playground.propose.ChallengeGameProposal" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="Получен вызов от другого игрока">
<p>
    Вы получили вызов от игрока <b><@mail.player pid=context.getInitiator().id/></b>. Вы можете принять вызов и
    начать новую игру используя <@mail.link href="/playground/scribble/join?p=${context.id}">эту ссылку</@mail.link> или
    присоединиться к игре в <@mail.link href="/playground/scribble/join">игровой зоне</@mail.link>.
</p>
</@mail.html>