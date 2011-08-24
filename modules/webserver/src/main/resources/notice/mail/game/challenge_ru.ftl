<#-- @ftlvariable name="context" type="wisematches.playground.propose.ChallengeGameProposal" -->
<#import "/notice/macro.ftl" as notice>

<@notice.html subject="Получен вызов от другого игрока">
<p>
    Вы получили вызов от игрока <b><@notice.player pid=context.getInitiator().id/></b>. Вы можете принять вызов и
    начать новую игру используя <@notice.link href="/playground/scribble/join?p=${context.id}">эту ссылку</@notice.link>
    или
    присоединиться к игре в <@notice.link href="/playground/scribble/join">игровой зоне</@notice.link>.
</p>
</@notice.html>