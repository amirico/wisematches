<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../utils.ftl" as notify>

<p>A game <@notify.board board=context/> has been finished.</p>

<p>
<#switch context.gameResolution>
    <#case "RESIGNED">
        <#if context.playerTurn.playerId == principal.id>
            You have resigned a game.
        <#else>
            The player <@notify.player player=context.playerTurn.playerId/> has
            resigned a game.
        </#if>
        <#break>
    <#case "TIMEOUT">
        <#if context.playerTurn.playerId == principal.id>
            Your move time run up and the game has been interrupted.
        <#else>
            The player <@notify.player player=context.playerTurn.playerId/> move
            time run up and the game has been interrupted.
        </#if>
        <#break>
    <#case "STALEMATE">
        There is no winner. Stalemate.
        <#break>
    <#default>
        The following <#if context.wonPlayers?size == 1>player has<#else>players have</#if> won the game:
        <#list context.wonPlayers as w>
            <@notify.player player=w.playerId/><#if w_has_next>, </#if></#list>
</#switch>
</p>

<#if context.ratedGame>
<p>
    Ratings of all players player (except robots) were changed:
<table>
    <tr>
        <td></td>
        <td align="center"><b>Points</b></td>
        <td align="center"><b>Old Rating</b></td>
        <td align="center"><b>New Rating</b></td>
    </tr>
    <#list context.ratingChanges as change>
        <tr>
            <td><@notify.player player=change.playerId/> <#if change.playerId==principal.id>(<b>You</b>)</#if></td>
            <td align="center">${change.points}</td>
            <td align="center">${change.oldRating}</td>
            <td align="center">${change.newRating}</td>
        </tr>
    </#list>
</table>
</p>
<p>
    You can read more about rating calculation here: <@notify.link href="info/rating"/>.
</p>
<#else>
<p>
    Because game wasn't rated your rating and ratings of your opponents was not changed and
    still is ${context.getRatingChange(context.getPlayerHand(principal.id)).newRating?string}.
</p>
</#if>
