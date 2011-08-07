<#-- @ftlvariable name="context" type="wisematches.playground.propose.ChallengeGameProposal" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="Challenge to game has been received">
<p>
    You have received new challenge from <b><@mail.player pid=context.getInitiator().id/></b>. You can
<@mail.link href="/playground/scribble/join?p=${context.id}">accept challenge</@mail.link> and start new game or
<@mail.link href="/playground/scribble/decline?p=${context.id}">decline it</@mail.link>.
</p>

<p>

</p>
</@mail.html>