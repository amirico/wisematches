<#-- @ftlvariable name="context.proposal" type="wisematches.playground.propose.GameProposal" -->
<#import "../../utils.ftl" as util>

<p>
    Вы получили вызов #${context.proposal.id} "${context.proposal.gameSettings.title!""}" от игрока
    <strong><@util.player player=context.proposal.initiator/></strong>
<#if context.proposal.commentary?has_content>
<blockquote>${context.proposal.commentary}</blockquote>
</#if>
</p>
<br>
<p>
    Если вы примете данное предложение новая игра "${context.proposal.gameSettings.title!""}"
    будет создана с ограничением
    в ${gameMessageSource.formatTimeMinutes(context.proposal.gameSettings.daysPerMove*24*60, locale)} на
    каждый ход.
</p>
<p>
    В игре будет
    использован ${gameMessageSource.getMessage("language.${context.proposal.gameSettings.language}", locale)}
    язык. Если не знаете этот язык мы не советуем вам принимать принимать это предложение.
</p>
<br>

<p>
    Вы можете принять/отказаться от игры использую страницу <@util.link href="playground/scribble/join">ожидание
    противников</@util.link>.
</p>
<p>
    Если вы не хотите в будущем получать новые вызовы от этого игрока, вы можете
<@util.link href="playground/profile/view?p=${context.proposal.initiator.id}&blacklist=true">добавить его/ее в свой
    черный
    список</@util.link>.
    В этом случае вы не будите получать вызовы и личные сообщения. Так же этот игрок не сможет присоединяться к вашим
    играм.
</p>