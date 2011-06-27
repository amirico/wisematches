<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="Ваша игра начата">

<p> Игра #${context.boardId} (${context.gameSettings.title}), которую вы ожидаете, началась.</p>
<br>

<p>
    <#if context.playerTurn.playerId == principal.id>
        <strong>Ход в этой игре передан вам</strong>. У вас есть
        <em>${gameMessageSource.formatRemainedTime(context, locale)}</em> для выполнения хода или игра
        будет прервана и вам будет засчитан проигрыш.
        <#else>
            Ход был передан игроку <em>${playerManager.getPlayer(context.playerTurn.playerId).nickname}</em>.
    </#if>
</p>
</@mail.html>