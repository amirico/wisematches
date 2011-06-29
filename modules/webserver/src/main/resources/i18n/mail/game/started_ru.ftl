<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="Ваша игра начата">

<p> Игра <@mail.board board=context/>, которую вы ожидаете, началась.</p>

<p>
    <#if context.playerTurn.playerId == principal.id>
        <strong>Ход в этой игре передан вам</strong>. У вас есть
        <em>${gameMessageSource.formatRemainedTime(context, locale)}</em> для выполнения хода или игра
        будет прервана и вам будет засчитан проигрыш.
        <#else>
            Ход был передан игроку <@mail.player pid=context.playerTurn.playerId/>.
    </#if>
</p>
</@mail.html>