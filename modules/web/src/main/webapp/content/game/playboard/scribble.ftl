<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">

<#macro tileToJS tile><#if tile?has_content>{number: ${tile.number}, letter: '${tile.letter?string}', cost: ${tile.cost}, wildcard: ${tile.wildcard?string} }</#if></#macro>

<script type="text/javascript">
    var scribbleBoard = {
        boardId: ${board.getBoardId()},
        gameState: '${board.getGameState()}',
        daysPerMove: ${board.getGameSettings().daysPerMove},
        boardViewer: ${player.getId()},
        playerTurn: ${board.getPlayerTurn().getPlayerId()},
        bankCapacity: ${board.bankCapacity},
        bankTilesInfo: [<#list board.getTilesBankInfo() as tbi>{letter:'${tbi.getLetter()}', cost: ${tbi.cost}, count: ${tbi.count}}<#if tbi_has_next>,</#if></#list>],
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

    var wildcardSelectionDialog = null;
    var board = new wm.scribble.Board(scribbleBoard, function(tile, replacer) {
        if (wildcardSelectionDialog == null) {
            wildcardSelectionDialog = $('#wildcardSelectionPanel').dialog({
                title: '<@message code="game.play.wildcard.label"/>',
                autoOpen: false,
                draggable: false,
                modal: true,
                resizable: false,
                width: 400
            });

            var panel = $($("#wildcardSelectionPanel div").get(1)).empty();
            $.each(scribbleBoard.bankTilesInfo, function(i, bti) {
                var row = Math.floor(i / 15);
                var col = (i - row * 15);
                var t = wm.scribble.tile.createTileWidget({number:0, letter: bti.letter, cost: 0}).offset({top: row * 22, left: col * 22});
                t.hover(
                        function() {
                            wm.scribble.tile.selectTile(this);
                        },
                        function() {
                            wm.scribble.tile.deselectTile(this);
                        }).click(
                        function() {
                            wildcardSelectionDialog.replacer($(this).data('tile').letter);
                            wildcardSelectionDialog.dialog("close");
                        }).appendTo(panel);
            });
        }
        wildcardSelectionDialog.replacer = replacer;
        wildcardSelectionDialog.dialog("open");
    });
</script>

<div id="wildcardSelectionPanel" style="display: none;">
    <div><@message code="game.play.wildcard.description"/></div>
    <div style="position: relative; height: ${(((board.tilesBankInfo?size)/15)?ceiling)*22}px;"></div>
</div>

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
        <#if !viewMode><#include "widget/controls.ftl"/></#if>
        </td>

        <td style="vertical-align: top; width: 280px">
        <#include "widget/players.ftl"/>
            <div style="height: 10px"></div>
        <#include "widget/selection.ftl"/>
            <div style="height: 10px"></div>
        <#--<#include "widget/memory.ftl"/>-->
        </td>
    </tr>
</table>

<script type="text/javascript">
    $("#scribbleBoard").prepend(board.getBoardElement());

    $(document).ready(function() {
        board.startBoardMonitoring(function(state, message, error) {
        });
    });
</script>