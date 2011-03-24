<#-- @ftlvariable name="tilesBankInfo" type="char[][]" -->
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
    $(document).ready(function() {
        board.startBoardMonitoring();
    });
</script>

<table id="playboard" cellpadding="5" align="center">
    <tr>
        <td style="vertical-align: top; width: 250px">
        <#--<#include "widget/progress.ftl"/>-->
        <#--<#include "widget/legend.ftl"/>-->
            <#--<div style="height: 10px"></div>-->
        <#--<#include "widget/history.ftl"/>-->
        </td>

        <td style="vertical-align: top;">
        <#include "widget/board.ftl"/>
        </td>

        <td style="vertical-align: top; width: 280px">
        <#--<#include "widget/players.ftl"/>-->
            <#--<div style="height: 10px"></div>-->
        <#--<#include "widget/selection.ftl"/>-->
            <#--<div style="height: 10px"></div>-->
        <#--<#include "widget/memory.ftl"/>-->
        </td>
    </tr>
</table>
