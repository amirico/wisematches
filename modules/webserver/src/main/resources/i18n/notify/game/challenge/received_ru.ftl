<#-- @ftlvariable name="context" type="wisematches.playground.propose.ChallengeGameProposal" -->
<#import "../../utils.ftl" as notify>

<p>
    Вы получили вызов #${context.id} "${context.gameSettings.title!""}" от игрока
    <strong><@notify.player player=context.initiator/></strong>
<#if context.comment?has_content>
    с коментарием: </p><p style="padding-left: 10px; font-style: italic;">${context.comment}.
</#if>
</p>
<p>
    Вы можете <@notify.link href="/playground/scribble/accept?p=${context.id}">принять вызов</@notify.link> и начать
    игру.
</p>
<p>
    Если вы не хотите играть с этим игроком, вы
    можете <@notify.link href="/playground/scribble/decline?p=${context.id}">отказать от игры</@notify.link>.
</p>
<p>
    Вы таже можете принять/отказать от игры использую страницу <@notify.link href="/playground/join">ожидание
    противников</@notify.link>.
</p>
<p>
    Если вы не хотите в будущем получать новые вызовы от этого игрока, вы можете
<@notify.link href="playground/profile/view?p=${context.initiator.id}&blacklist=true">добавить его/ее в свой черный
    список</@notify.link>.
    В этом случае вы не будите получать вызовы и личные сообщения. Так же этот игрок не сможет присоединяться к вашим
    играм.
</p>