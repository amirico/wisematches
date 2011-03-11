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
    var gameInfo = {
        bonuses: [
        <#list board.getScoreEngine().getScoreBonuses() as bonus>
            {row: ${bonus.row}, column: ${bonus.column}, type: '${bonus.type.displayName}'}<#if bonus_has_next>,</#if>
        </#list>
        ],
        boardTiles: [
        <#assign separatorRequired=false/>
        <#list 0..14 as i>
            <#list 0..14 as j>
                <#assign tile=board.getBoardTile(i, j)!"">
                <#if tile?has_content>
                    <#if separatorRequired>,</#if>{number: ${tile.number}, letter: '${tile.letter?string?upper_case}', cost: ${tile.cost}, wildcard: ${tile.wildcard?string}, row: ${i}, column: ${j} }
                    <#assign separatorRequired=true/>
                </#if>
            </#list>
        </#list>
        ],
        handTiles:[
        <#assign playerHand=board.getPlayerHand(player.getId())!""/>
        <#if playerHand??>
            <#list playerHand.tiles as tile>
                <#if tile?has_content>
                    {number: ${tile.number}, letter: '${tile.letter?string?upper_case}', cost: ${tile.cost}, wildcard: ${tile.wildcard?string} }<#if tile_has_next>,</#if>
                </#if>
            </#list>
        </#if>
        ],
        players: [
        <#list board.getPlayersHands() as hand>
            <#assign p = playerManager.getPlayer(hand.getPlayerId())/>
            {id: ${hand.getPlayerId()}, points: ${hand.getPoints()}, index: ${hand.getPlayerIndex()}, nickname: '${p.nickname}', membership: '${p.membership!""}'}<#if hand_has_next>,</#if>
        </#list>
        ],
        playerTurn: ${board.getPlayerTurn().getPlayerId()}
    };

    var board = new wm.scribble.Board();
    board.initializeGame(gameInfo);
    <#--var playerId = ${player.getId()};-->
    <#--var playerMove = <#if board.getPlayerTurn()??>${board.getPlayerTurn().getPlayerId()}<#else>undefined</#if>;-->
</script>

<div id="console" style="height: 100px; width: 100%; border: 1px solid red; overflow:auto; ">
</div>

<table id="playboard" cellpadding="5" align="center">
    <tr>
        <td style="vertical-align: top; width: 250px">
        <#include "widget/bankInfo.ftl"/>
        <#--<div style="height: 10px"></div>-->
        <#--<#include "widget/boardLegend.ftl"/>-->
            <div style="height: 10px"></div>
        <#include "widget/movesHistory.ftl"/>
        </td>

        <td style="vertical-align: top;">
        <@wm.widget id="scribbleBoard" title="Scribble Board">
            <div id="boardActionsToolbar" style="float: right; padding-top: 3px">
                <div style="display: inline-block; margin: 0;">
                    <button id="makeTurnButton" class="icon-make-turn" onclick="board.makeTurn()">Ходить</button>
                    <button id="clearSelectionButton" class="icon-clear-word" onclick="board.clearSelection()">Сбросить
                    </button>
                    <button id="exchangeTilesButton" class="icon-exchange-tiles" onclick="board.exchangeTiles()">
                        Обменять
                    </button>
                    <button id="passTurnButton" class="icon-pass-turn" onclick="board.passTurn()">Пропустить</button>
                </div>
            </div>
        </@wm.widget>
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
    $("#boardActionsToolbar div").buttonset();
    $("#boardActionsToolbar button").button("disable");

    $("#scribbleBoard").prepend(board.getBoardElement());

    board.bind('tileSelected', function(event, tile) {
        $("#clearSelectionButton").button("enable");
    });
    board.bind('tileDeselected', function(event, tile) {
        if (board.getSelectedTiles().length == 0) {
            $("#clearSelectionButton").button("disable");
        }
    });
    board.bind('wordChanged', function(event, word) {
        $("#makeTurnButton").button(word == null ? "disable" : "enable");
    });
</script>

