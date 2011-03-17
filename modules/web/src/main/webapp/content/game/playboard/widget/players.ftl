<#-- @ftlvariable name="lastMoveMillis" type="java.lang.Long" -->
<#-- @ftlvariable name="currentTimeMillis" type="java.lang.Long" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="playersInfo" title="Players Info">
<table cellpadding="5" width="100%">
    <#list board.playersHands as hand>
        <#assign p = playerManager.getPlayer(hand.getPlayerId())/>
        <#assign active=(board.getPlayerTurn() == hand)/>
        <#assign playerStyle="ui-state-active"/>
        <tr id="playerInfo${hand.getPlayerId()}" class="playerInfo <#if !active>passive</#if>">
            <td class="player-icon ui-corner-left ${playerStyle} ui-table-left">
                <img align="top" src="/resources/images/player/noPlayerIcon.png" width="31" height="28" alt=""/>
            </td>
            <td class="player-name ${playerStyle} ui-table-middle"><@wm.player player=p showRating=false/></td>
            <td class="player-points ${playerStyle} ui-table-middle" align="center">${hand.getPoints()}</td>
            <td class="player-time ui-corner-right ${playerStyle} ui-table-right" align="left">
                <#if active>${gameMessageSource.getRemainedTime(board, locale)}</#if>
            </td>
        </tr>
    </#list>
</table>

<script type="text/javascript">
    board.bind('playerMoved', function(event, gameMove) {
        if (gameMove.move.type = 'make') {
            var v = $("#playerInfo" + gameMove.move.player + " .player-points");
            v.text(parseInt(v.text()) + gameMove.move.points);
        }
        $("#playerInfo" + gameMove.move.player).addClass("passive");
        $("#playerInfo" + gameMove.move.player + " .player-time").text("");

        $("#playerInfo" + gameMove.game.playerTurn).removeClass("passive");
        $("#playerInfo" + gameMove.game.playerTurn + " .player-time").text(gameMove.game.remainedTimeMessage);
    });
</script>
</@wm.widget>
