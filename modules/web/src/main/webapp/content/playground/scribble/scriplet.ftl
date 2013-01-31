<#-- @ftlvariable name="player" type="wisematches.core.Personality" -->
<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="tiles" type="wisematches.playground.scribble.Tile[]" -->
<#-- @ftlvariable name="boardSettings" type="wisematches.playground.scribble.settings.BoardSettings" -->
<#include "/core.ftl">

<#macro tileToJS tile><#if tile?has_content>{number: ${tile.number}, letter: '${tile.letter?string}', cost: ${tile.cost}, wildcard: ${tile.wildcard?string} }</#if></#macro>

<@wm.ui.table.dtinit/>

<#if principal??>
    <#if !player??><#assign player=principal></#if>
</#if>

<script type="text/javascript">
    <#if player??>
        <#assign playerHand=board.getPlayerHand(player.getId())!""/>
        <#if playerHand?has_content>
            <#assign tiles=playerHand.tiles/>
        </#if>
    </#if>

    var scribbleGame = {
        id: ${board.boardId},
        readOnly: ${viewMode?string},
        language: '${board.settings.language}',
        daysPerMove: ${board.settings.daysPerMove},
        startedMillis: ${gameMessageSource.getTimeMillis(board.startedTime)},
        startedMessage: '${gameMessageSource.formatDate(board.startedTime, locale)}',

        state: {
            active: ${board.active?string},
            spentTimeMillis: ${(gameMessageSource.getSpentMinutes(board)*1000*60)},
            spentTimeMessage: '${gameMessageSource.formatSpentTime(board, locale)}',
        playerTurn:<#if board.playerTurn??>${board.playerTurn.playerId}<#else>null</#if>,
        <#if board.active>
            remainedTimeMillis: ${(gameMessageSource.getRemainedMinutes(board)*1000*60)},
            remainedTimeMessage: '${gameMessageSource.formatRemainedTime(board, locale)}'
        </#if>
        <#if !board.active>
            winners: [<#list board.wonPlayers as winner>${winner.playerId}<#if winner_has_next>,</#if></#list>],
            resolution: '${board.resolution}',
            finishTimeMillis: ${gameMessageSource.getTimeMillis(board.finishedTime)},
            finishTimeMessage: '${gameMessageSource.formatDate(board.finishedTime, locale)}'
        </#if>},

        players: [
        <#list board.players as hand>
            <#assign p = playerManager.getPlayer(hand.playerId)/>
            {playerId: ${hand.playerId}, nickname: '${gameMessageSource.getPlayerNick(p, locale)}', membership: '${p.playerType!""}', points: ${hand.points}}<#if hand_has_next>,</#if>
        </#list>],

    <#if !board.active>
        ratings: [
            <#list board.ratingChanges as rating>
                {playerId: ${rating.playerId}, oldRating: ${rating.oldRating}, newRating: ${rating.newRating}, ratingDelta: ${rating.ratingDelta}, points: ${rating.points}}<#if rating_has_next>,</#if>
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
            capacity: ${board.bankCapacity},
            tilesInfo: [
                <#list board.lettersDistribution.letterDescriptions as tbi>{letter: '${tbi.letter}', cost: ${tbi.cost}, count: ${tbi.count}, wildcard: ${(tbi.cost==0)?string}}<#if tbi_has_next>,</#if></#list>]
        }

    <#if tiles??>
        ,
        privacy: {
            handTiles: [<#list tiles as tile><@tileToJS tile/><#if tile_has_next>,</#if></#list>]
        }
    </#if>
    };

    wm.i18n.extend({
        '2l': '<@message code="game.play.bonus.2l"/>', '3l': '<@message code="game.play.bonus.3l"/>',
        '2w': '<@message code="game.play.bonus.2w"/>', '3w': '<@message code="game.play.bonus.3w"/>',
        'board.captions': '<@message code="game.play.captions"/>'});
</script>

<script type="text/javascript">
    if (typeof(scribbleController) == "undefined") {
        scribbleController = new wm.scribble.AjaxController();
    }
    var boardSettings = {
    <#if boardSettings??>
        clearByClick: ${boardSettings.clearByClick?string},
        showCaptions:${boardSettings.showCaptions?string}
    </#if>
    };
    var board = new wm.scribble.Board(scribbleGame, <#if player??>${player.id}<#else>0</#if>, "wildcardSelectionPanel", scribbleController, boardSettings);
</script>

<#if !viewMode>
<div id="wildcardSelectionPanel" class="${boardSettings.tilesClass}" title="<@message code="game.play.wildcard.label"/>"
     style="display: none;">
    <div><@message code="game.play.wildcard.description"/></div>
    <div style="position: relative; height: ${(((board.lettersDistribution?size)/15)?ceiling)*22}px;"></div>
</div>
</#if>
