<#-- @ftlvariable name="context" type="wisematches.playground.propose.GameProposal" -->
<#import "../../utils.ftl" as notify>

<p>
    You have received new challenge #${context.id} "${context.gameSettings.title!""}" from player
    <strong><@notify.player player=context.initiator/></strong>
<#if context.commentary?has_content>
    with the comment: </p><p style="padding-left: 10px; font-style: italic;">${context.commentary}.
</#if>
</p>
<p>
    You can <@notify.link href="/playground/scribble/accept?p=${context.id}">accept the challenge</@notify.link> and
    start new game.
</p>
<p>
    If you don't want player with the initiator you
    can <@notify.link href="/playground/scribble/decline?p=${context.id}">decline the challenge</@notify.link>.
</p>
<p>
    If also can accept or decline the challenge in <@notify.link href="/playground/join">playground</@notify.link>.
    Please use appropriate context buttons.
</p>
<p>
    If you don't want receive new challenges from the initiator, you
    can <@notify.link href="playground/profile/view?p=${context.initiator.id}&blacklist=true">add
    him/her to your ignore list</@notify.link>. In this case you won't receive new challenges and private messages
    from the player and he/she won't be able join to your games.
</p>