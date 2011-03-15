<#-- @ftlvariable name="playerMove.word" type="wisematches.server.gameplaying.scribble.Word" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="movesHistory" title="History Moves">
<table width="100%">
    <thead>
    <tr>
        <th>#</th>
        <th>Player</th>
        <th>Word</th>
        <th>Cost</th>
    </tr>
    </thead>
    <tbody>
        <#list board.getGameMoves() as move>
            <#assign playerMove = move.getPlayerMove()/>
        <tr>
            <td>${move.getMoveNumber() + 1}</td>
            <td>${gameMessageSource.getPlayerNick(playerManager.getPlayer(playerMove.playerId), locale)}</td>
            <td>
                <#if playerMove.class.simpleName == "MakeWordMove">
                    <#assign word=playerMove.word/>
                    <a href="javascript: board.selectHistoryWord({row: ${word.position.row}, column: ${word.position.column}, direction: '${word.direction}', length: ${word.tiles?size}})">${word.text}</a>
                    <#else>
                        This move not supported
                </#if>
            </td>
            <td>${move.getPoints()}</td>
        </tr>
        </#list>
    </tbody>
</table>

<script type="text/javascript">
    var movesHistoryTable = $("#movesHistory table").dataTable({
        "bJQueryUI": true,
        "bSort": true,
        "bSortClasses": false,
        "aaSorting": [
            [0,'desc']
        ],
        "bPaginate": false,
        "sScrollY": "300px",
        "bStateSave": true,
        "sDom": 't'
    });

    board.bind('playerMoved', function(event, gameMove) {
        var move = gameMove.move;
        var playerMove = move.playerMove;
        var word = playerMove.word;
        var link = '<a href="javascript: board.selectHistoryWord(' +
                '{row: ' + word.position.row + ', column: ' + word.position.column +
                ', direction: \'' + word.direction + '\', length: ' + word.tiles.length + '})">' +
                word.text +
                '</a>';
        movesHistoryTable.fnAddData([1 + move.moveNumber, board.getPlayerInfo(playerMove.playerId).nickname, link, move.points]);
    });
</script>

</@wm.widget>
