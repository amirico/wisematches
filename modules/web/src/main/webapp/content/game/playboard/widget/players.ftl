<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="playersInfo" title="game.player.label">
<table cellpadding="5" width="100%">
    <#list board.playersHands as hand>
        <#assign p = playerManager.getPlayer(hand.getPlayerId())/>
        <#if board.gameState == 'ACTIVE'>
            <#assign active=(board.getPlayerTurn() == hand)/>
            <#assign winner = false/>
            <#assign finished = false/>
            <#assign playerStyle="ui-state-active"/>
            <#else>
                <#assign active=(board.getWonPlayers() == hand) || board.gameState == 'DREW'/>
                <#assign finished = true/>
                <#assign playerStyle="ui-state-highlight"/>
        </#if>
        <tr id="playerInfo${hand.getPlayerId()}" class="playerInfo <#if !active>passive</#if>">
            <td class="player-icon ui-corner-left ${playerStyle} ui-table-left">
                <img src="/game/player/image/view.html?pid=${hand.getPlayerId()}" width="31" height="28" alt=""/>
            </td>
            <td class="player-name ${playerStyle} ui-table-middle">
            <@wm.player player=p showRating=false/>
                <#if (finished && active)>
                    <div class="winner"><@message code="game.player.winner"/></div>
                </#if>
            </td>
            <td class="player-points ${playerStyle} ui-table-middle" align="center">${hand.getPoints()}</td>
            <td class="player-time ui-corner-right ${playerStyle} ui-table-right" align="left">
                <#if finished>
                    <div class="player-rating">
                        <#if hand.ratingDelta==0>
                            <div class="icon-rating-same"></div>
                            <div>+0</div>
                            <#elseif (hand.ratingDelta>0)>
                                <div class="icon-rating-up"></div>
                                <div>+${hand.ratingDelta?string.computer}</div>
                            <#else>
                                <div class="icon-rating-down"></div>
                                <div>${hand.ratingDelta?string.computer}</div>
                        </#if>
                    </div>
                    <#else>
                        <#if active>${gameMessageSource.getRemainedTime(board, locale)}</#if>
                </#if>
            </td>
        </tr>
    </#list>
</table>

<script type="text/javascript">
    wm.scribble.players = new function() {
        var lastPlayerTurn = board.getPlayerTurn();

        var updatePlayerState = function(id, active) {
            var v = $("#playerInfo" + id);
            if (active) {
                v.removeClass("passive");
            } else {
                v.addClass("passive");
            }
        };

        var updatePlayerPoints = function(id, points, finite) {
            var v = $("#playerInfo" + id + " .player-points");
            if (finite === true) {
                v.text(points);
            } else {
                v.text(parseInt(v.text()) + points);
            }
        };

        var updatePlayerTime = function(id, timeMessage) {
            $("#playerInfo" + id + " .player-time").html(timeMessage);
        };

        board.bind('gameMoves',
                function(event, move) {
                    if (move.type = 'make') {
                        updatePlayerPoints(move.player, move.points);
                    }
                })
                .bind('gameInfo',
                function(event, info) {
                    if (lastPlayerTurn != info.playerTurn) {
                        updatePlayerState(lastPlayerTurn, false);
                        updatePlayerState(info.playerTurn, true);
                        updatePlayerTime(lastPlayerTurn, "");

                        lastPlayerTurn = info.playerTurn;
                    }
                    updatePlayerTime(info.playerTurn, info.remainedTimeMessage);
                })
                .bind('gameFinalization',
                function(event, status) {
                    updatePlayerState(lastPlayerTurn, false);
                    $("#playersInfo td").removeClass("ui-state-active").addClass("ui-state-highlight");
                    $.each(status.playerHands, function(i, hand) {
                        var winner = hand.playerId == status.winner || status.state == 'DREW';
                        if (winner) {
                            $("#playerInfo" + hand.playerId + " .player-name").append($('<div class="winner"></div>').text('<@message code="game.player.winner"/>'));
                        }
                        updatePlayerState(hand.playerId, winner);
                        updatePlayerPoints(hand.playerId, hand.points, true);
                        if (hand.ratingDelta == 0) {
                            updatePlayerTime(hand.playerId, "<div class='player-rating'><div class='icon-rating-same'></div><div>+0</div></div>");
                        } else if (hand.ratingDelta > 0) {
                            updatePlayerTime(hand.playerId, "<div class='player-rating'><div class='icon-rating-up'></div><div>+" + hand.ratingDelta + "</div></div>");
                        } else {
                            updatePlayerTime(hand.playerId, "<div class='player-rating'><div class='icon-rating-down'></div><div>" + hand.ratingDelta + "</div></div>");
                        }
                    });
                });
    };
</script>
</@wm.widget>
