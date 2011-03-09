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
            <td>${move.getMoveNumber()}</td>
            <td>Player ID: ${playerMove.getPlayerId()}</td>
            <td>${playerMove.getClass().getSimpleName()}</td>
            <td>${move.getPoints()}</td>
        </tr>
        </#list>
    </tbody>
</table>
</@wm.widget>

<script type="text/javascript">
    $("#movesHistory table").dataTable({
        "bJQueryUI": true,
        "bFilter": false,
        "bSort": false,
        "bSortClasses": false,
        "sDom": 't'
    });
</script>
