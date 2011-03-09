<#-- @ftlvariable name="tilesBankInfo" type="char[][]" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<#macro tilePlain number cost letter i j>
<div <#if number??>id="tile${number}"</#if> class="tile cost${cost}"
     style="left: ${i*22}px; top: ${j*22}px; background-position: -${cost*22}px -${j*22}px">
    <span>${letter?upper_case}</span>
</div>
</#macro>

<#macro tileObject tile i j><@tilePlain number=tile.number cost=tile.cost letter=tile.letter i=i j=j/></#macro>

<script type="text/javascript">
    var board = new wm.scribble.Board();
    var playerId = ${player.getId()};
    var playerMove = <#if board.getPlayerTurn()??>${board.getPlayerTurn().getPlayerId()}<#else>undefined</#if>;

    function createTileWidget(tile) {
        return $("<div></div>").addClass("tile cost" + tile.cost).css('background-position', '-' + tile.cost * 22 + 'px 0').append($("<span></span>").text(tile.letter));
    }
</script>

<table id="playboard" cellpadding="5" align="center">
    <tr>
        <td style="vertical-align: top;">
        <#include "widget/bankInfo.ftl"/>
            <div style="height: 10px"></div>
        <#include "widget/boardLegend.ftl"/>
            <div style="height: 10px"></div>
        <#include "widget/movesHistory.ftl"/>
        </td>

        <td style="vertical-align: top;">
        <#include "widget/scribbleBoard.ftl"/>
        </td>

        <td style="vertical-align: top;">
        <#include "widget/playersInfo.ftl"/>
            <div style="height: 10px"></div>
        <#include "widget/moveInfo.ftl"/>
            <div style="height: 10px"></div>
        <#include "widget/memoryWords.ftl"/>
        </td>
    </tr>
</table>

<script type="text/javascript">
    board.init();
</script>
