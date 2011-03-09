<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="scribbleBoard" title="Scribble Board">
<div id="scribble">
    <div id="container">
        <div id="bonuses">
            <#list board.getScoreEngine().getScoreBonuses() as bonus>
                <div class="cell bonus-cell-${bonus.getType().displayName}"
                     style="left: ${bonus.column*22}px; top: ${bonus.row*22}px"></div>
                <div class="cell bonus-cell-${bonus.getType().displayName}"
                     style="left: ${bonus.column*22}px; top: ${22+(13-bonus.row)*22}px"></div>
                <div class="cell bonus-cell-${bonus.getType().displayName}"
                     style="left: ${22 + (13-bonus.column)*22}px; top: ${bonus.row*22}px"></div>
                <div class="cell bonus-cell-${bonus.getType().displayName}"
                     style="left: ${22+(13-bonus.column)*22}px; top: ${22+(13-bonus.row)*22}px"></div>
            </#list>
        </div>

        <div id="board">
            <#list 0..14 as i>
    <#list 0..14 as j>
                <#assign tile=board.getBoardTile(i, j)!"">
                <#if tile?has_content><@tileObject tile=tile i=i j=j/></#if>
            </#list>
</#list>
        </div>

        <div id="hand">
            <#assign playerHand=board.getPlayerHand(player.getId())!""/>
<#if playerHand??>
            <#list playerHand.tiles as tile>
                <#if tile?has_content><@tileObject tile=tile i=tile_index j=0/></#if>
            </#list>
        </#if>
        </div>
    </div>
</div>

<div id="boardActionsToolbar" style="float: right; padding-top: 3px">
    <div style="display: inline-block; margin: 0;">
        <button id="makeTurnButton" class="icon-make-turn" onclick="makeTurn()">Ходить</button>
        <button id="clearSelectionButton" class="icon-clear-word" onclick="clearSelection()">Сбросить</button>
        <button id="exchangeTilesButton" class="icon-exchange-tiles" onclick="exchangeTiles()">Обменять</button>
        <button id="passTurnButton" class="icon-pass-turn" onclick="passTurn()">Пропустить</button>
    </div>
</div>
</@wm.widget>

<script type="text/javascript">
    $("#boardActionsToolbar div").buttonset();
    $("#boardActionsToolbar button").button("disable");

    if (playerId == playerMove) {
        $("#exchangeTilesButton").button("enable");
        $("#passTurnButton").button("enable");
    }

    function clearSelection() {
        board.clearSelection();
    }

    function makeTurn() {
        alert("Make Turn");
    }

    function passTurn() {
        alert("Pass Turn");
    }

    function exchangeTiles() {
        alert("Exchange Tiles");
    }

    function boardStateChanged(event, tile, tiles, word) {
        $("#makeTurnButton").button((word == null ? "disable" : "enable"));
        $("#clearSelectionButton").button((tiles.length == 0 ? "disable" : "enable"));
    }

    $(document).ready(function() {
        $("#scribble").bind('selected', boardStateChanged).bind('deselected', boardStateChanged);
    });
</script>