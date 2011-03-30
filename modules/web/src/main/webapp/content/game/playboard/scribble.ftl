<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">

<script type="text/javascript">
    wm.scribble.wildcard = new function() {
        var wildcardSelectionDialog = null;

        this.replaceHandler = function(tile, replacer) {
            if (wildcardSelectionDialog == null) {
                wildcardSelectionDialog = $('#wildcardSelectionPanel').dialog({
                    title: '<@message code="game.play.wildcard.label"/>',
                    autoOpen: false,
                    draggable: false,
                    modal: true,
                    resizable: false,
                    width: 400
                });

                var panel = $($("#wildcardSelectionPanel div").get(1)).empty();
                $.each(scribbleGame.bank.tilesInfo, function(i, bti) {
                    var row = Math.floor(i / 15);
                    var col = (i - row * 15);
                    var t = wm.scribble.tile.createTileWidget({number:0, letter: bti.letter, cost: 0}).offset({top: row * 22, left: col * 22});
                    t.hover(
                            function() {
                                wm.scribble.tile.selectTile(this);
                            },
                            function() {
                                wm.scribble.tile.deselectTile(this);
                            }).click(
                            function() {
                                wildcardSelectionDialog.replacer($(this).data('tile').letter);
                                wildcardSelectionDialog.dialog("close");
                            }).appendTo(panel);
                });
            }
            wildcardSelectionDialog.replacer = replacer;
            wildcardSelectionDialog.dialog("open");
        }
    };
</script>

<#include "scribblejs.ftl"/>

<script type="text/javascript">
    var board = new wm.scribble.Board(scribbleGame, ${player.id?string.computer}, wm.scribble.wildcard.replaceHandler);
</script>

<div id="wildcardSelectionPanel" style="display: none;">
    <div><@message code="game.play.wildcard.description"/></div>
    <div style="position: relative; height: ${(((board.tilesBankInfo?size)/15)?ceiling)*22}px;"></div>
</div>

<table id="playboard" cellpadding="5" align="center">
    <tr>
        <td style="vertical-align: top; width: 250px">
        <#include "widget/progress.ftl"/>
        <#include "widget/legend.ftl"/>
            <div style="height: 10px"></div>
        <#include "widget/history.ftl"/>
        </td>

        <td style="vertical-align: top;">
        <@wm.widget id="scribbleBoard" style="width: 100%" title="<center>${board.gameSettings.title} #${board.boardId}</center>"/>
        <#if !viewMode><#include "widget/controls.ftl"/></#if>
        </td>

        <td style="vertical-align: top; width: 280px">
        <#include "widget/players.ftl"/>
            <div style="height: 10px"></div>
        <#include "widget/selection.ftl"/>
            <div style="height: 10px"></div>
        <#include "widget/memory.ftl"/>
        </td>
    </tr>
</table>

<script type="text/javascript">
    $("#scribbleBoard").prepend(board.getBoardElement());

    $(document).ready(function() {
        board.startBoardMonitoring(function(state, message, error) {
        });
    });
</script>
