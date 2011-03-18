<#include "/core.ftl">
<#include "playboardModel.ftl">

<#macro tileToJS tile><#if tile?has_content>{number: ${tile.number}, letter: '${tile.letter?string}', cost: ${tile.cost}, wildcard: ${tile.wildcard?string} }</#if></#macro>

<script type="text/javascript">
    var gameInfo = {
        boardId: ${board.getBoardId()},
        daysPerMove: ${board.getGameSettings().daysPerMove},
        gameState: '${board.getGameState()}',
        boardViewer: ${player.getId()},
        playerTurn: ${board.getPlayerTurn().getPlayerId()},
        bankCapacity: ${board.bankCapacity},
        lastMoveTime: ${lastMoveMillis?string.computer},
        <#if board.gameState != "ACTIVE">wonPlayer:${board.getWonPlayer().getPlayerId()},</#if>
        bonuses: [
        <#list board.getScoreEngine().getScoreBonuses() as bonus>
            {row: ${bonus.row}, column: ${bonus.column}, type: '${bonus.type.displayName}'}<#if bonus_has_next>,</#if>
        </#list>
        ],
        players: [
        <#list board.getPlayersHands() as hand>
            <#assign p = playerManager.getPlayer(hand.getPlayerId())/>
            {id: ${hand.getPlayerId()}, points: ${hand.getPoints()}, index: ${hand.getPlayerIndex()}, nickname: '${gameMessageSource.getPlayerNick(p, locale)}', membership: '${p.membership!""}'}<#if hand_has_next>,</#if>
        </#list>
        ],
        handTiles:[
        <#assign playerHand=board.getPlayerHand(player.getId())!""/>
        <#if playerHand??>
            <#list playerHand.tiles as tile><@tileToJS tile/><#if tile_has_next>,</#if></#list>
        </#if>
        ],
        moves: [
        <#list board.gameMoves as move>
            <#assign playerMove = move.playerMove/>
            {number: ${move.moveNumber}, points: ${move.points}, player: ${playerMove.playerId},
                <#if playerMove.class.simpleName == "MakeWordMove">
                    <#assign word=playerMove.word/>
                    type: 'make',
                    word: {
                        position: { row: ${word.position.row}, column: ${word.position.column}},
                        direction: '${word.direction}', text: '${word.text}',
                        tiles: [ <#list word.tiles as tile><@tileToJS tile/><#if tile_has_next>,</#if></#list> ]
                    }
                    <#elseif playerMove.class.simpleName == "ExchangeTilesMove">
                        type: 'exchange'
                    <#else>
                        type: 'pass'
                </#if>
            }<#if move_has_next>,</#if>
        </#list>
        ]
    };

    var board = new wm.scribble.Board();
    board.initializeGame(gameInfo);
</script>

<table id="playboard" cellpadding="5" align="center">
    <tr>
        <td style="vertical-align: top; width: 250px">
        <#include "widget/progress.ftl"/>
        <#include "widget/legend.ftl"/>
            <div style="height: 10px"></div>
        <#include "widget/history.ftl"/>
        </td>

        <td style="vertical-align: top;">
        <@wm.widget id="scribbleBoard" title="<center>${board.gameSettings.title} #${board.boardId}</center>">
            <div id="boardActionsToolbar" style="float: right; padding-top: 3px">
                <div style="display: inline-block; margin: 0;">
                    <button id="makeTurnButton" class="icon-make-turn" onclick="board.makeTurn()">
                    <@message code="game.play.make"/>
                    </button>
                    <button id="clearSelectionButton" class="icon-clear-word" onclick="board.clearSelection()">
                    <@message code="game.play.clear"/>
                    </button>
                    <button id="exchangeTilesButton" class="icon-exchange-tiles" onclick="board.exchangeTiles()">
                    <@message code="game.play.exchange"/>
                    </button>
                    <button id="passTurnButton" class="icon-pass-turn" onclick="board.passTurn()">
                    <@message code="game.play.pass"/>
                    </button>
                </div>
            </div>
        </@wm.widget>
        </td>

        <td style="vertical-align: top;">
        <#include "widget/players.ftl"/>
            <div style="height: 10px"></div>
        <#include "widget/selection.ftl"/>
        <#--<div style="height: 10px"></div>-->
        <#--<#include "widget/memory.ftl"/>-->
        </td>
    </tr>
</table>

<script type="text/javascript">
    $("#boardActionsToolbar div").buttonset();
    $("#boardActionsToolbar button").button("disable");

    $("#scribbleBoard").prepend(board.getBoardElement());

    board.bind('tileSelected', function(event, tile) {
        $("#clearSelectionButton").button("enable");
    });
    board.bind('tileDeselected', function(event, tile) {
        if (board.getSelectedTiles().length == 0) {
            $("#clearSelectionButton").button("disable");
        }
    });

    <#if board.getPlayerTurn().getPlayerId() == player.getId()>
    $("#passTurnButton").button("enable");
    $("#exchangeTilesButton").button("enable");
    board.bind('wordChanged', function(event, word) {
        $("#makeTurnButton").button(word == null ? "disable" : "enable");
    });
    </#if>
</script>


