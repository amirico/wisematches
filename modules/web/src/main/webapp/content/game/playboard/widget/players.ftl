<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="playersInfo" title="game.player.label">
<table cellpadding="5" width="100%" border="1">
    <tbody>
        <#list board.playersHands as hand>
            <#assign p = playerManager.getPlayer(hand.getPlayerId())/>
        <tr class="player-info-${p.id} player-info">
            <td width="36px" class="icon ui-corner-left ui-table-left">
                <img src="/game/player/image/view.html?pid=${p.id}" alt=""/>
            </td>
            <td class="nickname ui-table-middle">
            <@wm.player player=p showRating=false/>
            </td>
            <td width="20px" class="points ui-table-middle">${hand.points}</td>
            <td width="60px" class="info ui-corner-right ui-table-right"></td>
        </tr>
        </#list>
    </tbody>
</table>

<script type="text/javascript">
    wm.scribble.players = new function() {
        var getPlayerInfoCells = function(pid, name) {
            return $("#playersInfo .player-info-" + pid + " " + name);
        };

        var selectActivePlayer = function(pid) {
            $("#playersInfo .player-info td").removeClass("ui-state-active");
            $("#playersInfo .player-info .info").text("");
            getPlayerInfoCells(pid, "td").addClass("ui-state-active");
        };

        var selectWonPlayers = function(pids) {
            $("#playersInfo .player-info td").removeClass("ui-state-active");
            $.each(pids, function(i, pid) {
                getPlayerInfoCells(pid, "td").addClass("ui-state-highlight");
            });
        };

        var showPlayerTimeout = function(pid, time) {
            updatePlayerInfo(pid, time);
        };

        var showPlayerRating = function(pid, delta) {
            if (delta == 0) {
                updatePlayerInfo(pid, "<div class='rating'><div>1212</div><div class='icon-rating-same'></div><div>1212</div></div>");
            } else if (delta > 0) {
                updatePlayerInfo(pid, "<div class='rating'><div>1212</div><div class='icon-rating-up'></div><div>1234</div></div>");
            } else {
                updatePlayerInfo(pid, "<div class='rating'><div>1212</div><div class='icon-rating-down' style='size: 8px; color: #a52a2a;'>+ 12 =</div><div>1203</div></div>");
            }
        };

        var updatePlayerPoints = function(pid) {
            getPlayerInfoCells(pid, ".points").text(board.getPlayerInfo(pid).points);
        };

        var updatePlayerInfo = function(pid, info) {
            getPlayerInfoCells(pid, ".info").html(info);
        };

        var updateBoardState = function() {
            if (board.isBoardActive()) {
                selectActivePlayer(board.getPlayerTurn());
                showPlayerTimeout(board.getPlayerTurn(), board.getRemainedTime());
            } else {
                $.each(board.getPlayerHands(), function(i, hand) {
                    showPlayerRating(hand.playerId, hand.ratingDelta);
                    updatePlayerPoints(hand.playerId);
                });
                selectWonPlayers(board.getWonPlayers());
            }
        };

        updateBoardState();

        board.bind('gameMoves',
                function(event, move) {
                    if (move.type = 'make') {
                        updatePlayerPoints(move.player);
                    }
                })
                .bind('gameState',
                function(event, type, state) {
                    updateBoardState();
                });

    };
</script>
</@wm.widget>
