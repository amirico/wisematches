<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="moveInfo" title="Move Info">
<table>
    <tr>
        <td height="22" valign="bottom"><b>Tiles:</b></td>
        <td height="22" valign="bottom" style="padding-left: 5px">
            <div id="selectedTilesInfo" style="position: relative; height: 22px;">
                <span class="sample">no tiles selected</span>
            </div>
        </td>
    </tr>
    <tr>
        <td height="22" valign="bottom"><b>Word:</b></td>
        <td height="22" valign="bottom" style="padding-left: 5px">
            <div id="selectedWordInfo" style="position: relative; height: 22px;">
                <span class="sample">no tiles selected</span>
            </div>
        </td>
    </tr>
    <tr>
        <td height="22" valign="bottom"><b>Points:</b></td>
        <td height="22" valign="bottom">
            <div id="selectedWordCost" style="position: relative; padding-left: 5px">
                (1x<b>2</b> + 2 + 7 + 9)x<b>2</b>=12
            </div>
        </td>
    </tr>
</table>
</@wm.widget>

<script type="text/javascript">
    function updateMoveInfo(event, tile, tiles, word) {
        var sti = $("#selectedTilesInfo");
        if (tiles.length == 0) {
            sti.empty();
            sti.text('no tiles selected');
        } else {
            if (tiles.length == 1) {
                sti.empty();
            }
            if (sti.children("#selectedTile" + tile.number).length == 0) {
                var newTile = createTileWidget(tile);
                newTile.attr('id', 'selectedTile' + tile.number);
                newTile.offset({left: ((tiles.length - 1) * 22), top: 0});
                newTile.appendTo(sti);
            }
        }

        var swi = $("#selectedWordInfo").empty();
        if (word != null) {
            for (var i = 0, len = word.tiles.length; i < len; i++) {
                var newTile = createTileWidget(word.tiles[i]);
                newTile.offset({left: (i * 22), top: 0});
                newTile.appendTo(swi);
            }
        } else {
            swi.text('no word selected');
        }
    }

    $(document).ready(function() {
        $("#scribble").bind('selected', updateMoveInfo).bind('deselected', updateMoveInfo);
    });
</script>
