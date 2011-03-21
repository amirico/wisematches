<#-- @ftlvariable name="tilesBankInfo" type="char[][]" -->
<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">
<#include "playboardModel.ftl">

<#macro tileToJS tile><#if tile?has_content>{number: ${tile.number}, letter: '${tile.letter?string}', cost: ${tile.cost}, wildcard: ${tile.wildcard?string} }</#if></#macro>

<script type="text/javascript">
    var scribbleBoard = {
        boardId: ${board.getBoardId()},
        daysPerMove: ${board.getGameSettings().daysPerMove},
        gameState: '${board.getGameState()}',
        boardViewer: ${player.getId()},
        playerTurn: ${board.getPlayerTurn().getPlayerId()},
        bankCapacity: ${board.bankCapacity},
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
    board.initializeGame(scribbleBoard);
</script>

<div id="asdqwesq" style="display: none;">qweqwrwe</div>

<table id="playboard" cellpadding="5" align="center">
    <tr>
        <td style="vertical-align: top; width: 250px">
        <#include "widget/progress.ftl"/>
        <#include "widget/legend.ftl"/>
            <div style="height: 10px"></div>
        <#include "widget/history.ftl"/>
        </td>

        <td style="vertical-align: top;">
        <@wm.widget id="scribbleBoard" style="width: 100%" title="<center>${board.gameSettings.title} #${board.boardId}</center>"/>
        <#if !viewMode>
            <div id="boardActionsToolbar" class="ui-widget-content ui-corner-all" style="border-top: 0" align="center">
                <div>
                    <button id="makeTurnButton" class="icon-make-turn"
                            onclick="board.makeTurn()">
                    <@message code="game.play.make"/>
                    </button>
                    <button id="clearSelectionButton" class="icon-clear-word"
                            onclick="board.clearSelection()">
                    <@message code="game.play.clear"/>
                    </button>
                    <button id="exchangeTilesButton" class="icon-exchange-tiles"
                            onclick="board.exchangeTiles()">
                    <@message code="game.play.exchange"/>
                    </button>
                </div>
                <div>
                    <button id="passTurnButton" class="icon-pass-turn"
                            onclick="board.passTurn()">
                    <@message code="game.play.pass"/>
                    </button>
                    <button id="resignGameButton" class="icon-resign-game" onclick="board.resign()">
                    <@message code="game.play.resign"/>
                    </button>
                </div>
            </div>
        </#if>
        </td>

        <td style="vertical-align: top; width: 280px">
        <#include "widget/players.ftl"/>
            <div style="height: 10px"></div>
        <#include "widget/selection.ftl"/>
            <div style="height: 10px"></div>
        <#include "widget/memory.ftl"/>
        </td>
    </tr>
</table>

<script type="text/javascript">
    $("#scribbleBoard").prepend(board.getBoardElement());

    <#if !viewMode>
    $("#boardActionsToolbar button").button({disabled: true});
    $("#resignGameButton").button("enable"); // resign available in any time

    if (board.isPlayerActive()) {
        $("#passTurnButton").button("enable");
        $("#exchangeTilesButton").button("enable");
    }
    board.bind('wordChanged', function(event, word) {
        $("#makeTurnButton").button(word == null || !board.isPlayerActive() ? "disable" : "enable").removeClass("ui-state-hover");
    });
    board.bind('playerMoved', function(event, gameMove) {
        board.clearSelection();
        $("#passTurnButton").button(board.isPlayerActive() ? "enable" : "disable").removeClass("ui-state-hover");
        $("#exchangeTilesButton").button(board.isPlayerActive() ? "enable" : "disable").removeClass("ui-state-hover");
    });

    board.bind("tileSelected", function(event, tile) {
        if (board.getSelectedTiles().length != 0) {
            $("#clearSelectionButton").button('enable');
        }
    });
    board.bind("tileDeselected", function(event, tile) {
        if (board.getSelectedTiles().length == 0) {
            $("#clearSelectionButton").button('disable').removeClass("ui-state-hover");
        }
    });
    </#if>
</script>
