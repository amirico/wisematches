<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="Game has been started">

<p> A game <@mail.board board=context/> you are waiting for has been started</p>

<p>
    <#if context.playerTurn.playerId == principal.id>
        <strong>It's you turn in the game</strong>. You have
        <em>${gameMessageSource.formatRemainedTime(context, locale)} </em> to make a turn or game will be timed out and
        you will be defeated.
        <#else>
            Turn was transmitted to player <@mail.player pid=context.playerTurn.playerId/>.
    </#if>
</p>
</@mail.html>