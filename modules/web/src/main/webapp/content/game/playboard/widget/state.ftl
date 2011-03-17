<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">
<#include "../playboardModel.ftl">

<#assign active=board.gameState == "ACTIVE"/>
<@wm.widget id="gameInfo" title="game.state.label">
<table width="100%" border="0">
    <tr>
        <td><b><@message code="game.state.status.label"/>:</b></td>
        <td width="100%">${gameMessageSource.formatGameState(board.getGameState(), locale)}</td>
    </tr>
    <tr>

        <td colspan="2">
            <div class="ui-widget-content ui-widget-separator"></div>
        </td>
    </tr>
    <tr>
        <td><b><@message code="game.state.started.label"/>:</b></td>
        <td width="100%">${gameMessageSource.formatDate(board.startedTime, locale)}</td>
    </tr>
    <#if active>
        <tr>
            <td><b><@message code="game.state.moved.label"/>:</b></td>
            <td width="100%">${gameMessageSource.formatDate(board.lastMoveTime, locale)}</td>
        </tr>
        <#else>
            <tr>
                <td><b><@message code="game.state.finished.label"/>:</b></td>
                <td width="100%">${gameMessageSource.formatDate(board.finishedTime, locale)}</td>
            </tr>
    </#if>
    <tr>
        <td colspan="2">
            <div class="ui-widget-content ui-widget-separator"></div>
        </td>
    </tr>
    <#assign boardTilesCount=0/>
    <#assign bankTilesCount=board.bankCapacity/>
    <#assign bankRemainedCount=board.bankRemained/>
    <#list 0..bankTilesCount-1 as n>
        <#if board.isBoardTile(n)><#assign boardTilesCount=boardTilesCount+1/></#if>
    </#list>
    <tr>
        <td valign="top"><b>Tiles count in:</b></td>
        <td width="100%">
            <table>
                <tr>
                    <td><i>bank</i></td>
                    <td><i>board</i></td>
                    <td><i>hands</i></td>
                </tr>
                <tr>
                    <td align="center">${bankRemainedCount}</td>
                    <td align="center">${boardTilesCount}</td>
                    <td align="center">${bankTilesCount - boardTilesCount - bankRemainedCount}</td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</@wm.widget>

<script type="text/javascript">
    board.bind('playerMoved', function(event, gameMove) {
//        var moveType = gameMove.moveType;
//        var playerMove = gameMove.move.playerMove;
//        if (moveType = 'MakeWordMove') {
//            var v = $("#playerInfo" + playerMove.playerId + " .player-points");
//            v.text(parseInt(v.text()) + gameMove.move.points);
//        }
//        $("#playerInfo" + playerMove.playerId).addClass("passive");
//        $("#playerInfo" + playerMove.playerId + " .player-time").text("");
//
//        $("#playerInfo" + gameMove.nextPlayerId).removeClass("passive");
//        $("#playerInfo" + gameMove.nextPlayerId + " .player-time").text(gameMove.remainedTimeMessage);
    });
</script>