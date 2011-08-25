<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "/notice/macro.ftl" as notice>

<@notice.html subject="Game has been started">

<p> A game &lt;@notify.board board=context/&gt; you are waiting for has been started</p>

<p>
    <#if context.playerTurn.playerId == principal.id>
        <strong>It's you turn in the game</strong>. You have
        <em>${gameMessageSource.formatRemainedTime(context, locale)} </em> to make a turn or game will be timed out and
        you will be defeated.
        <#else>
            Turn was transmitted to player <@notice.player pid=context.playerTurn.playerId/>.
    </#if>
</p>
</@notice.html>