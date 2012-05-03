<#-- @ftlvariable name="context.proposal" type="wisematches.playground.propose.GameProposal" -->
<#import "../../utils.ftl" as util>

<p>
    You have received new challenge from player
    <strong><@util.player player=context.proposal.initiator/></strong>
<#if context.proposal.commentary?has_content>
<blockquote>${context.proposal.commentary}</blockquote>
</#if>
</p>
<br>
<p>
    If you accept the proposal new game "${context.proposal.gameSettings.title!""}"
    with ${gameMessageSource.formatMinutes(context.proposal.gameSettings.daysPerMove*24*60, locale)} time limit.
</p>
<p>
    The ${gameMessageSource.getMessage("language.${context.proposal.gameSettings.language}", locale)} language
    will be used for the game. If you don't know the language we suggest not accept the challenge.
</p>
<br>
<p>
    You can <@util.link href="playground/scribble/accept?p=${context.proposal.id}">accept the challenge</@util.link> and
    start new game.
</p>
<p>
    If you don't want play with the initiator you
    can <@util.link href="playground/scribble/decline?p=${context.proposal.id}">decline the challenge</@util.link>.
</p>
<p>
    If also can accept or decline the challenge on <@util.link href="playground/join">playground</@util.link> page.
    Please use appropriate context.proposal buttons.
</p>
<p>
    If you don't want receive new challenges from the initiator, you
    can <@util.link href="playground/profile/view?p=${context.proposal.initiator.id}&blacklist=true">add
    him/her to your ignore list</@util.link>. In this case you won't receive new challenges and private messages
    from the player and he/she won't be able join to your games.
</p>