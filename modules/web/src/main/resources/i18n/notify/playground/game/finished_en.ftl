<#-- @ftlvariable name="board" type="wisematches.playground.GameBoard" -->
<#-- @ftlvariable name="resolution" type="wisematches.playground.GameResolution" -->
<#-- @ftlvariable name="winners" type="java.util.Collection<wisematches.playground.GamePlayerHand>" -->
<#import "../../utils.ftl" as util>
<#assign board=context.board/>
<#assign resolution=context.resolution/>
<#assign winners=context.winners/>

<p>A game <@util.board board=board/> has been finished.</p>

<p>
<#switch resolution>
    <#case "RESIGNED">
        <#if board.playerTurn.playerId == principal.id>
            You have resigned a game.
        <#else>
            The player <@util.player player=board.playerTurn.playerId/> has
            resigned a game.
        </#if>
        <#break>
    <#case "TIMEOUT">
        <#if board.playerTurn.playerId == principal.id>
            Your move time run up and the game has been interrupted.
        <#else>
            The player <@util.player player=board.playerTurn.playerId/>'s move
            time run up and the game has been interrupted.
        </#if>
        <#break>
    <#case "STALEMATE">
        There is no winner. Stalemate.
        <#break>
    <#default>
        The following <#if winners?size == 1>player has<#else>players have</#if> won the game:
        <#list winners as w>
            <@util.player player=w.playerId/><#if w_has_next>, </#if></#list>
</#switch>
</p>

<#if board.rated>
<p>
    Ratings of all players player (except robots) were changed:
<table>
    <tr>
        <td></td>
        <td align="center"><b>Points</b></td>
        <td align="center"><b>Old Rating</b></td>
        <td align="center"><b>New Rating</b></td>
    </tr>
    <#list board.ratingChanges as change>
        <tr>
            <td><@util.player player=change.playerId/> <#if change.playerId==principal.id>(<b>You</b>)</#if></td>
            <td align="center">${change.points}</td>
            <td align="center">${change.oldRating}</td>
            <td align="center">${change.newRating}</td>
        </tr>
    </#list>
</table>
</h>
<p>
    You can read more about rating calculation here: <@util.link href="info/rating"/>.
</p>
<#else>
<p>
    Because game isn't rated your rating and ratings of your opponents was not changed and
    still is ${board.getRatingChange(board.getPlayerHand(principal.id)).newRating?string}.
</p>
</#if>
