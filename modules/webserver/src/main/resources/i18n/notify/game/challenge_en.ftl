<#-- @ftlvariable name="context" type="wisematches.playground.propose.ChallengeGameProposal" -->
<#import "../utils.ftl" as notify>

<p>
    You have received new challenge from <b><@notify.player player=context.initiator/></b>. You can
    accept the challenge and start new game using
<@notify.link href="/playground/scribble/join?p=${context.id}">this link</@notify.link> or
    join the game in <@notify.link href="/playground/scribble/join">playground</@notify.link>.
</p>