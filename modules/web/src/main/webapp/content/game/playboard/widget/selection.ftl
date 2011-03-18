<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="moveInfo" title="Move Info">
<table>
    <tr>
        <td height="22" valign="bottom"><b>Tiles:</b></td>
        <td height="22" valign="bottom" style="padding-left: 5px">
            <div id="selectedTilesInfo" style="position: relative; height: 22px;">
                no tiles selected
            </div>
        </td>
    </tr>
    <tr>
        <td height="22" valign="bottom"><b>Word:</b></td>
        <td height="22" valign="bottom" style="padding-left: 5px">
            <div id="selectedWordInfo" style="position: relative; height: 22px;">
                no word selected
            </div>
        </td>
    </tr>
    <tr>
        <td height="22" valign="bottom"><b>Points:</b></td>
        <td height="22" valign="bottom">
            <div id="selectedWordCost" style="position: relative; padding-left: 5px">
                no word selected
            </div>
        </td>
    </tr>
</table>
</@wm.widget>
<div id="selectionActionsToolbar" class="ui-widget-content ui-corner-bottom" style="border-top: 0" align="right">
    <div style="margin: 0;">
        <button id="clearSelectionButton" class="icon-clear-word" onclick="board.clearSelection()">
            Сбросить
        </button>
        <button id="checkWordButton" class="icon-check-word">
            Проверить
        </button>
    </div>
</div>

<script type="text/javascript">
    $("#selectionActionsToolbar div").buttonset();
    $("#selectionActionsToolbar button").button("disable");

    board.bind("wordChanged", function(event, word) {
        var swi = $("#selectedWordInfo").empty();
        var swc = $("#selectedWordCost").empty();
        if (word != null) {
            swc.text(board.getScoreEngine().getWordBonus(word).formula);
            $.each(word.tiles, function(i, t) {
                wm.scribble.tile.createTileWidget(t).offset({left: (i * 22), top: 0}).appendTo(swi);
            });
            $("#checkWordButton").button('enable');
        } else {
            swi.text('no word selected');
            swc.text('no word selected');
            $("#checkWordButton").button('disable').removeClass("ui-state-hover");
        }
    });

    board.bind("tileSelected", function(event, tile) {
        var length = $("#selectedTilesInfo div").length;
        if (length == 0) {
            $("#selectedTilesInfo").empty();
            $("#clearSelectionButton").button('enable');
        }
        wm.scribble.tile.createTileWidget(tile).offset({left: (length * 22), top: 0}).appendTo('#selectedTilesInfo');
    });

    board.bind("tileDeselected", function(event, tile) {
        var tiles = $("#selectedTilesInfo div");
        var updateOffset = false;
        $.each(tiles, function(i, tileWidget) {
            var v = $(tileWidget);
            if (v.data('tile').number == tile.number) {
                updateOffset = true;
                v.remove();
            } else if (updateOffset) {
                v.css('left', (i - 1) * 22);
            }
        });
        if (tiles.length == 1) {
            $("#selectedTilesInfo").text('no tiles selected');
            $("#clearSelectionButton").button('disable').removeClass("ui-state-hover");
        }
    });
</script>
