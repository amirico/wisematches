<#-- @ftlvariable name="context" type="wisematches.playground.propose.ChallengeGameProposal" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="Challenge to game has been received">
<p>
    You have received new challenge from <b><@mail.player pid=context.getInitiator().id/></b>. You can
    accept the challenge and start new game using
<@mail.link href="/playground/scribble/join?p=${context.id}">this link</@mail.link> or
    join the game in <@mail.link href="/playground/scribble/join">playground</@mail.link>.
</p>
</@mail.html>