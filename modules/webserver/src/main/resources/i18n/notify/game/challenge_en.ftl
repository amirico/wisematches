<#-- @ftlvariable name="context" type="wisematches.playground.propose.ChallengeGameProposal" -->
<#import "/notice/macro.ftl" as notice>

<@notice.html subject="Challenge to game has been received">
<p>
    You have received new challenge from <b><@notice.player pid=context.getInitiator().id/></b>. You can
    accept the challenge and start new game using
<@notice.link href="/playground/scribble/join?p=${context.id}">this link</@notice.link> or
    join the game in <@notice.link href="/playground/scribble/join">playground</@notice.link>.
</p>
</@notice.html>