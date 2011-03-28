<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="playersInfo" title="game.player.label">
<table cellpadding="5" width="100%">
    <#list board.playersHands as hand>
        <#assign p = playerManager.getPlayer(hand.getPlayerId())/>
        <#assign active=(board.getPlayerTurn() == hand)/>
        <#assign playerStyle="ui-state-active"/>
        <tr id="playerInfo${hand.getPlayerId()}" class="playerInfo <#if !active>passive</#if>">
            <td class="player-icon ui-corner-left ${playerStyle} ui-table-left">
                <img align="top" src="/game/player/image/view.html?pid=${hand.getPlayerId()}" width="31" height="28"
                     alt=""/>
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
    var lastPlayerTurn = board.getPlayerTurn();
    board.bind('gameMoves',
            function(event, move) {
                if (move.type = 'make') {
                    var v = $("#playerInfo" + move.player + " .player-points");
                    v.text(parseInt(v.text()) + move.points);
                }
            })
            .bind('gameInfo',
            function(event, info) {
                if (lastPlayerTurn != info.playerTurn) {
                    $("#playerInfo" + lastPlayerTurn).addClass("passive");
                    $("#playerInfo" + lastPlayerTurn + " .player-time").text("");
                    $("#playerInfo" + info.playerTurn).removeClass("passive");
                    lastPlayerTurn = info.playerTurn;
                }
                $("#playerInfo" + info.playerTurn + " .player-time").text(info.remainedTimeMessage);
            });
</script>
</@wm.widget>
