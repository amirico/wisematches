<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">

<#macro tileToJS tile><#if tile?has_content>{number: ${tile.number?string.computer}, letter: '${tile.letter?string}', cost: ${tile.cost}, wildcard: ${tile.wildcard?string} }</#if></#macro>

<script type="text/javascript">
    var scribbleGame = {
        id: ${board.boardId?string.computer},
        readOnly: ${viewMode?string},
        daysPerMove: ${board.gameSettings.daysPerMove?string.computer},

        state: {
            active: ${board.gameActive?string},
        playerTurn: <#if board.playerTurn??>${board.playerTurn.playerId?string.computer}<#else>null</#if>,
        <#if board.gameActive>
            remainedTimeMillis: ${gameMessageSource.getRemainedTimeMillis(board)?string.computer},
            remainedTimeMessage: '${gameMessageSource.getRemainedTime(board, locale)}'
            <#else>
                winners: [<#list board.wonPlayers as winner>${winner.playerId}<#if winner_has_next>,</#if></#list>],
                resolution: '${board.gameResolution}',
                resolutionMessage: '${gameMessageSource.formatGameResolution(board.gameResolution, locale)}',
                finishTimeMessage: '${gameMessageSource.formatDate(board.finishedTime, locale)}'
        </#if>},

        players: [
        <#list board.playersHands as hand>
            <#assign p = playerManager.getPlayer(hand.getPlayerId())/>
            {playerId: ${hand.playerId?string.computer}, index: ${hand.playerIndex?string.computer}, nickname: '${gameMessageSource.getPlayerNick(p, locale)}', membership: '${p.membership!""}',
                points: ${hand.points?string.computer} <#if !board.gameActive>, rating: ${hand.rating?string.computer}, ratingDelta: ${hand.ratingDelta?string.computer}</#if>}<#if hand_has_next>,</#if>
        </#list>],

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
            tilesInfo: [<#list board.getTilesBankInfo() as tbi>{letter:'${tbi.getLetter()}', cost: ${tbi.cost}, count: ${tbi.count}}<#if tbi_has_next>,</#if></#list>]
        }

    <#assign playerHand=board.getPlayerHand(player.getId())!""/>
    <#if playerHand??>
        , privacy: {
        handTiles: [<#list playerHand.tiles as tile><@tileToJS tile/><#if tile_has_next>,</#if></#list>]
    }
    </#if>
    };
</script>