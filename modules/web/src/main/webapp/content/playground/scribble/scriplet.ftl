<#-- @ftlvariable name="principal" type="wisematches.core.Personality" -->
<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="tiles" type="wisematches.playground.scribble.Tile[]" -->
<#-- @ftlvariable name="boardSettings" type="wisematches.playground.scribble.settings.BoardSettings" -->
<#include "/core.ftl">

<#macro tileToJS tile><#if tile?has_content>{number: ${tile.number}, letter: '${tile.letter?string}', cost: ${tile.cost}, wildcard: ${tile.wildcard?string} }</#if></#macro>

<@wm.ui.table.dtinit/>

<#if principal??>
    <#if !player??><#assign player=player></#if>
</#if>

<script type="text/javascript">
    <#if player??>
        <#assign playerHand=board.getPlayerHand(player)!""/>
        <#if playerHand?has_content>
            <#assign tiles=playerHand.tiles/>
        </#if>
    </#if>

    var scribbleGame = {
        id: ${board.boardId},
        readOnly: ${viewMode?string},
        language: '${board.settings.language}',
        daysPerMove: ${board.settings.daysPerMove},
        startedMillis: ${messageSource.getTimeMillis(board.startedDate)},
        startedMessage: '${messageSource.formatDate(board.startedDate, locale)}',

        state: {
            active: ${board.active?string},
            spentTimeMillis: ${(messageSource.getSpentMinutes(board)*1000*60)},
            spentTimeMessage: '${messageSource.formatSpentTime(board, locale)}',
        playerTurn:<#if board.playerTurn??>${board.playerTurn.id}<#else>null</#if>,
        <#if board.active>
            remainedTimeMillis: ${(messageSource.getRemainedMinutes(board)*1000*60)},
            remainedTimeMessage: '${messageSource.formatRemainedTime(board, locale)}'
        </#if>
        <#if !board.active>
            winners: [<#list board.wonPlayers as winner>${winner.id}<#if winner_has_next>,</#if></#list>],
            resolution: '${board.resolution}',
            finishTimeMillis: ${messageSource.getTimeMillis(board.finishedDate)},
            finishTimeMessage: '${messageSource.formatDate(board.finishedDate, locale)}'
        </#if>},

        players: [
        <#list board.players as p>
            <#assign hand=board.getPlayerHand(p)/>
            {playerId: ${p.id}, nickname: '${messageSource.getPersonalityNick(p, locale)}', points: ${hand.points}}<#if p_has_next>,</#if>
        </#list>],

    <#if !board.active>
        ratings: [
            <#list board.players as p>
                <#assign hand=board.getPlayerHand(p)/>
                {playerId: ${p.id}, oldRating: ${hand.oldRating}, newRating: ${hand.newRating}, points: ${hand.points}}<#if p_has_next>,</#if>
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
                {number: ${move.moveNumber}, points: ${move.points}, player: ${move.player.id},
                    <#if move.class.simpleName == "MakeTurn">
                        <#assign word=move.word/>
                        type: 'make',
                        word: {
                            position: { row: ${word.position.row}, column: ${word.position.column}},
                            direction: '${word.direction}', text: '${word.text}',
                            tiles: [ <#list word.tiles as tile><@tileToJS tile/><#if tile_has_next>,</#if></#list> ]
                        }
                    <#elseif move.class.simpleName == "ExchangeMove">
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
                <#list board.distribution.letterDescriptions as tbi>{letter: '${tbi.letter}', cost: ${tbi.cost}, count: ${tbi.count}, wildcard: ${(tbi.cost==0)?string}}<#if tbi_has_next>,</#if></#list>]
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
    <div style="position: relative; height: ${(((board.distribution?size)/15)?ceiling)*22}px;"></div>
</div>
</#if>
