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
            <td>${playerManager.getPlayer(playerMove.playerId).nickname!""}</td>
            <td>
                <#if playerMove.class.simpleName == "MakeWordMove">
                    <#assign word=playerMove.word/>
                    <a href="javascript: board.selectHistoryWord({row: ${word.position.row}, column: ${word.position.column}, direction: '${word.direction}', length: ${word.tiles?size}})">${word.toStringWord()}</a>
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

    board.bind('playerMoved', function(event, move) {
        var word = move.word;
        var link = '<a href="javascript: board.selectHistoryWord(' +
                '{row: ' + word.position.row + ', column: ' + word.position.column +
                ', direction: \'' + word.direction + '\', length: ' + word.tiles.length + '})">' +
                word.text +
                '</a>';
        movesHistoryTable.fnAddData([(1 + movesHistoryTable.fnGetData().length), board.getPlayerInfo(move.playerTurn).nickname, link, move.points]);
    });
</script>

</@wm.widget>
