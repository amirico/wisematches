<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<link rel="stylesheet" type="text/css" href="/content/playground/game.css"/>
<script type="text/javascript">
    var scribbleController = new function () {
        this.execute = function (widget, type, params, data, callback) {
        }
    };
</script>

<#include "/content/playground/scribble/scriplet.ftl"/>

<table width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top">
        <#include "/content/info/navigation.ftl"/>
        </td>
        <td valign="top" align="center">
            <div id="board${board.boardId}" class="${boardSettings.tilesClass}">
                <table id="playboard" class="playboard" cellpadding="5" align="center">
                    <tr>
                        <td style="vertical-align: top; width: 250px">
                        <#include "/content/playground/scribble/widget/progress.ftl"/>
                    <#include "/content/playground/scribble/widget/history.ftl"/>
                        </td>

                        <td style="vertical-align: top;">
                        <#assign boardName><@wm.board.name board, false/></#assign>
                        <@wm.ui.widget class="scribbleBoard" style="width: 100%" title="<center>${boardName}</center>"  help="board.playboard"/>
                    <#include "/content/playground/scribble/widget/controls.ftl"/>
                        <#if playerHand?has_content><#include "/content/playground/scribble/widget/annotation.ftl"/></#if>
                        </td>

                        <td style="vertical-align: top; width: 280px">
                        <#include "/content/playground/scribble/widget/players.ftl"/>
                            <#include "/content/playground/scribble/widget/selection.ftl"/>
                                            <#include "/content/playground/scribble/widget/thesaurus.ftl"/>
                        </td>
                    </tr>
                </table>
            </div>
        </td>
    </tr>
</table>

<script type="text/javascript">
    $("#board${board.boardId} .scribbleBoard .ui-widget-content").prepend(board.getBoardElement());
</script>