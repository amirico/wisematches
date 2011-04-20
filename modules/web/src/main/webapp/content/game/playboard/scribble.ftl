<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#-- @ftlvariable name="ratings" type="java.util.Collection<wisematches.server.standing.rating.RatingChange>" -->

<#include "/core.ftl">

<#macro tileToJS tile><#if tile?has_content>{number: ${tile.number?string.computer}, letter: '${tile.letter?string}', cost: ${tile.cost}, wildcard: ${tile.wildcard?string} }</#if></#macro>

<script type="text/javascript">
    var scribbleGame = {
        id: ${board.boardId?string.computer},
        readOnly: ${viewMode?string},
        language: '${board.gameSettings.language}',
        daysPerMove: ${board.gameSettings.daysPerMove?string.computer},
        startedMillis: ${gameMessageSource.getTimeMillis(board.startedTime)?string.computer},
        startedMessage: '${gameMessageSource.formatDate(board.startedTime, locale)}',

        state: {
            active: ${board.gameActive?string},
            spentTimeMillis: ${(gameMessageSource.getSpentMinutes(board)*1000*60)?string.computer},
            spentTimeMessage: '${gameMessageSource.formatSpentTime(board, locale)}',
        playerTurn: <#if board.playerTurn??>${board.playerTurn.playerId?string.computer}<#else>null</#if>,
        <#if board.gameActive>
            remainedTimeMillis: ${(gameMessageSource.getRemainedMinutes(board)*1000*60)?string.computer},
            remainedTimeMessage: '${gameMessageSource.formatRemainedTime(board, locale)}'
        </#if>
        <#if !board.gameActive>
            winners: [<#list board.wonPlayers as winner>${winner.playerId}<#if winner_has_next>,</#if></#list>],
            resolution: '${board.gameResolution}',
            finishTimeMillis: ${gameMessageSource.getTimeMillis(board.finishedTime)?string.computer},
            finishTimeMessage: '${gameMessageSource.formatDate(board.finishedTime, locale)}'
        </#if>},

        players: [
        <#list board.playersHands as hand>
            <#assign p = playerManager.getPlayer(hand.playerId)/>
            {playerId: ${hand.playerId?string.computer}, nickname: '${gameMessageSource.getPlayerNick(p, locale)}', membership: '${p.membership!""}', points: ${hand.points?string.computer}}<#if hand_has_next>,</#if>
        </#list>],

    <#if ratings??>
        ratings: [
            <#list ratings as rating>
                {playerId: ${rating.playerId?string.computer}, boardId: ${rating.boardId?string.computer}, oldRating: ${rating.oldRating?string.computer}, newRating: ${rating.newRating?string.computer}, ratingDelta: ${rating.ratingDelta?string.computer}, points: ${rating.points?string.computer}}<#if rating_has_next>,</#if>
            </#list>
        ],
    </#if>

        board: {
            bonuses: [
            <#list board.getScoreEngine().getScoreBonuses() as bonus>
                {row: ${bonus.row}, column: ${bonus.column}, type: '${bonus.type.displayName}'}<#if bonus_has_next>,</#if>
            </#list>
            ],
            moves: [<#list board.gameMoves as move>
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
            </#list>]
        },

        bank: {
            capacity: ${board.bankCapacity?string.computer},
            tilesInfo: [
                <#list board.getTilesBankInfo() as tbi>{letter:'${tbi.getLetter()}', cost: ${tbi.cost}, count: ${tbi.count}}<#if tbi_has_next>,</#if></#list>]
        }

    <#assign playerHand=board.getPlayerHand(player.getId())!""/>
    <#if playerHand??>
        ,
        privacy: {
            handTiles: [<#list playerHand.tiles as tile><@tileToJS tile/><#if tile_has_next>,</#if></#list>]
        }
    </#if>
    };
</script>

<script type="text/javascript">
    var board = new wm.scribble.Board(scribbleGame, ${player.id?string.computer}, "wildcardSelectionPanel");
</script>

<#if !viewMode>
<div id="wildcardSelectionPanel" title="<@message code="game.play.wildcard.label"/>" style="display: none;">
    <div><@message code="game.play.wildcard.description"/></div>
    <div style="position: relative; height: ${(((board.tilesBankInfo?size)/15)?ceiling)*22}px;"></div>
</div>
</#if>

<table id="playboard" cellpadding="5" align="center">
    <tr>
        <td style="vertical-align: top; width: 250px">
        <#include "widget/progress.ftl"/>
        <#include "widget/legend.ftl"/>
        <#include "widget/history.ftl"/>
        </td>

        <td style="vertical-align: top;">
        <@wm.widget id="scribbleBoard" style="width: 100%" title="<center>${board.gameSettings.title} #${board.boardId}</center>"/>
        <#if !viewMode><#include "widget/controls.ftl"/></#if>
        </td>

        <td style="vertical-align: top; width: 280px">
        <#include "widget/players.ftl"/>
<#if !viewMode>
            <#include "widget/selection.ftl"/>
            <#include "widget/memory.ftl"/>
        </#if>
        </td>
    </tr>
</table>

<script type="text/javascript">
    $("#scribbleBoard").prepend(board.getBoardElement());

    <#if board.gameActive>
    $(document).ready(function() {
        board.startBoardMonitoring(function(state, message, error) {
        });
    });
    </#if>
</script>
