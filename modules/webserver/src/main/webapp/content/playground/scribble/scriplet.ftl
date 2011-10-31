<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="boardSettings" type="wisematches.playground.scribble.settings.BoardSettings" -->
<#-- @ftlvariable name="ratings" type="java.util.Collection<wisematches.playground.GameRatingChange>" -->
<#include "/core.ftl">

<#macro tileToJS tile><#if tile?has_content>{number: ${tile.number}, letter: '${tile.letter?string}', cost: ${tile.cost}, wildcard: ${tile.wildcard?string} }</#if></#macro>

<@wm.jstable/>

<#if !player??><#assign player=principal></#if>

<link rel="stylesheet" type="text/css" href="/content/playground/scribble/scribble.css"/>
<script type="text/javascript" src="/content/playground/scribble/scribble.js"></script>

<script type="text/javascript">
    var scribbleGame = {
        id: ${board.boardId},
        readOnly: ${viewMode?string},
        language:'${board.gameSettings.language}',
        daysPerMove: ${board.gameSettings.daysPerMove},
        startedMillis: ${gameMessageSource.getTimeMillis(board.startedTime)},
        startedMessage:'${gameMessageSource.formatDate(board.startedTime, locale)}',

        state:{
            active: ${board.gameActive?string},
            spentTimeMillis: ${(gameMessageSource.getSpentMinutes(board)*1000*60)},
            spentTimeMessage:'${gameMessageSource.formatSpentTime(board, locale)}',
        playerTurn:<#if board.playerTurn??>${board.playerTurn.playerId}<#else>null</#if>,
        <#if board.gameActive>
            remainedTimeMillis: ${(gameMessageSource.getRemainedMinutes(board)*1000*60)},
            remainedTimeMessage:'${gameMessageSource.formatRemainedTime(board, locale)}'
        </#if>
        <#if !board.gameActive>
            winners:[<#list board.wonPlayers as winner>${winner.playerId}<#if winner_has_next>,</#if></#list>],
            resolution:'${board.gameResolution}',
            finishTimeMillis: ${gameMessageSource.getTimeMillis(board.finishedTime)},
            finishTimeMessage:'${gameMessageSource.formatDate(board.finishedTime, locale)}'
        </#if>},

        players:[
        <#list board.playersHands as hand>
            <#assign p = playerManager.getPlayer(hand.playerId)/>
            {playerId: ${hand.playerId}, nickname:'${gameMessageSource.getPlayerNick(p, locale)}', membership:'${p.membership!""}', points: ${hand.points}}<#if hand_has_next>,</#if>
        </#list>],

    <#if ratings??>
        ratings:[
            <#list ratings as rating>
                {playerId: ${rating.playerId}, oldRating: ${rating.oldRating}, newRating: ${rating.newRating}, ratingDelta: ${rating.ratingDelta}, points: ${rating.points}}<#if rating_has_next>,</#if>
            </#list>
        ],
    </#if>

        board:{
            bonuses:[
            <#list board.getScoreEngine().getScoreBonuses() as bonus>
                {row: ${bonus.row}, column: ${bonus.column}, type:'${bonus.type.displayName}'}<#if bonus_has_next>,</#if>
            </#list>
            ],
            moves:[<#list board.gameMoves as move>
                <#assign playerMove = move.playerMove/>
                {number: ${move.moveNumber}, points: ${move.points}, player: ${playerMove.playerId},
                    <#if playerMove.class.simpleName == "MakeWordMove">
                        <#assign word=playerMove.word/>
                        type:'make',
                        word:{
                            position:{ row: ${word.position.row}, column: ${word.position.column}},
                            direction:'${word.direction}', text:'${word.text}',
                            tiles:[ <#list word.tiles as tile><@tileToJS tile/><#if tile_has_next>,</#if></#list> ]
                        }
                    <#elseif playerMove.class.simpleName == "ExchangeTilesMove">
                        type:'exchange'
                    <#else>
                        type:'pass'
                    </#if>
                }<#if move_has_next>,</#if>
            </#list>]
        },

        bank:{
            capacity: ${board.bankCapacity},
            tilesInfo:[
                <#list board.lettersDistribution.letterDescriptions as tbi>{letter:'${tbi.letter}', cost: ${tbi.cost}, count: ${tbi.count}}<#if tbi_has_next>,</#if></#list>]
        }

    <#assign playerHand=board.getPlayerHand(player.getId())!""/>
    <#if playerHand?has_content>
        ,
        privacy:{
            handTiles:[<#list playerHand.tiles as tile><@tileToJS tile/><#if tile_has_next>,</#if></#list>]
        }
    </#if>
    };

    wm.i18n.extend({'2l':'<@message code="game.play.bonus.2l"/>', '3l':'<@message code="game.play.bonus.3l"/>', '2w':'<@message code="game.play.bonus.2w"/>', '3w':'<@message code="game.play.bonus.3w"/>'});
</script>

<script type="text/javascript">
    if (typeof(scribbleController) == "undefined") {
        scribbleController = new wm.scribble.AjaxController();
    }
    var board = new wm.scribble.Board(scribbleGame, ${player.id}, "wildcardSelectionPanel", scribbleController);
</script>

<#if !viewMode>
<div id="wildcardSelectionPanel" class="${boardSettings.tilesClass}" title="<@message code="game.play.wildcard.label"/>" style="display: none;">
    <div><@message code="game.play.wildcard.description"/></div>
    <div style="position: relative; height: ${(((board.lettersDistribution?size)/15)?ceiling)*22}px;"></div>
</div>
</#if>
