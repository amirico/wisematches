<#-- @ftlvariable name="context.proposal" type="wisematches.playground.propose.GameProposal" -->
<#import "../../utils.ftl" as notify>

<#assign proposal=context.proposal/>

<p>
    You have received new challenge #${proposal.id} "${proposal.gameSettings.title!""}" from player
    <strong><@notify.player player=proposal.initiator/></strong>
<#if proposal.commentary?has_content>
    with the comment: </p><p style="padding-left: 10px; font-style: italic;">${proposal.commentary}.
</#if>
</p>
<p>
    The game is limited by ${gameMessageSource.formatMinutes(proposal.gameSettings.daysPerMove * 24 * 60)} for one move
    and playing on ${gameMessageSource.getMessage("language"+proposal.gameSettings.language, locale)} language.
</p>
<p>
    You can <@notify.link href="/playground/scribble/accept?p=${proposal.id}">accept the challenge</@notify.link> and
    start new game.
</p>
<p>
    If you don't want player with the initiator you
    can <@notify.link href="/playground/scribble/decline?p=${proposal.id}">decline the challenge</@notify.link>.
</p>
<p>
    If also can accept or decline the challenge in <@notify.link href="/playground/join">playground</@notify.link>.
    Please use appropriate context buttons.
</p>
<p>
    If you don't want receive new challenges from the initiator, you
    can <@notify.link href="playground/profile/view?p=${proposal.initiator.id}&blacklist=true">add
    him/her to your ignore list</@notify.link>. In this case you won't receive new challenges and private messages
    from the player and he/she won't be able join to your games.
</p>
