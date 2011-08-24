<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "/notice/macro.ftl" as notice>

<@notice.html subject="Ваша игра начата">

<p> Игра <@notice.board board=context/>, которую вы ожидаете, началась.</p>

<p>
    <#if context.playerTurn.playerId == principal.id>
        <strong>Ход в этой игре передан вам</strong>. У вас есть
        <em>${gameMessageSource.formatRemainedTime(context, locale)}</em> для выполнения хода или игра
        будет прервана и вам будет засчитан проигрыш.
        <#else>
            Ход был передан игроку <@notice.player pid=context.playerTurn.playerId/>.
    </#if>
</p>
</@notice.html>