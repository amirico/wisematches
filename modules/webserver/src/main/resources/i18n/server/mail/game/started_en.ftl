<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="Game has been started">

<p> A Game Board #${context.boardId} (${context.gameSettings.title} you are waiting for has been started</p>
<br>

<p>
    <#if context.playerTurn.playerId == principal.id>
        <strong>It's you turn in this game</strong>. You have
        <em>${gameMessageSource.formatRemainedTime(context, locale)} </em> to make a turn or game will be timed out and
        you will be defeated.
        <#else>
            Turn was transmitted to player <em>${playerManager.getPlayer(context.playerTurn.playerId).nickname}</em>.
    </#if>
</p>
</@mail.html>