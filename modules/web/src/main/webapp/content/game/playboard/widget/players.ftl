<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="playersInfo" title="game.player.label">
<table cellpadding="5" width="100%" border="1">
    <tbody>
        <#list board.playersHands as hand>
            <#assign p = playerManager.getPlayer(hand.getPlayerId())/>
        <tr class="player-info-${p.id} player-info">
            <td width="24px" height="24px" class="winner-icon ui-corner-left ui-table-left">
            <#--<img src="/game/player/image/view.html?pid=${p.id}" alt=""/>-->
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
                getPlayerInfoCells(pid, "td.winner-icon").html("<img src='/resources/images/scribble/winner.png'>");
            });
        };

        var showPlayerTimeout = function(pid, time) {
            updatePlayerInfo(pid, time);
        };

        var showPlayerRating = function(pid, ratingDelta, ratingFinal) {
            var iconClass;
            if (ratingDelta == 0) {
                ratingDelta = '+' + ratingDelta;
                iconClass = "icon-rating-same";
            } else if (ratingDelta > 0) {
                ratingDelta = '+' + ratingDelta;
                iconClass = "icon-rating-up";
            } else {
                ratingDelta = '' + ratingDelta;
                iconClass = "icon-rating-down";
            }
            updatePlayerInfo(pid, "<div class='rating'><div class='change " + iconClass + "'><sub>" + ratingDelta + "</sub></div><div class='value'>" + ratingFinal + "</div></div>");
        };

        var updatePlayerPoints = function(pid, points) {
            getPlayerInfoCells(pid, ".points").text(points);
        };

        var updatePlayerInfo = function(pid, info) {
            getPlayerInfoCells(pid, ".info").html(info);
        };

        var updateBoardState = function() {
            if (board.isBoardActive()) {
                selectActivePlayer(board.getPlayerTurn());
                showPlayerTimeout(board.getPlayerTurn(), board.getRemainedTime());
            } else {
                $.each(board.getPlayerRatings(), function(i, rating) {
                    showPlayerRating(rating.playerId, rating.ratingDelta, rating.newRating);
                    updatePlayerPoints(rating.playerId, rating.points);
                });
                selectWonPlayers(board.getWonPlayers());
            }
        };

        updateBoardState();
        <#if board.gameActive>
            board.bind('gameMoves',
                    function(event, move) {
                        var playerInfo = board.getPlayerInfo(move.player);
                        if (move.type = 'make') {
                            updatePlayerPoints(playerInfo.playerId, playerInfo.points);
                        }
                    })
                    .bind('gameState',
                    function(event, type, state) {
                        updateBoardState();
                    });
        </#if>
    };
</script>
</@wm.widget>
