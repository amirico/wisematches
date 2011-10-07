<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="ratings" type="java.util.Collection<wisematches.playground.GameRatingChange>" -->

<#assign principal=testPlayer/>
<#include "/core.ftl">

<style type="text/css">
    #board0 table {
        width: 100%;
    }

    #board0 table td {
        vertical-align: top;
    }
</style>

<#include "/content/playground/scribble/scriplet.ftl"/>

<div id="board${board.boardId}">
    <div id="info-move">
        <div id="info-move-header" class="info-header">
            <div id="info-move-label" class="info-label">How To Move</div>

            <div id="info-move-description" class="info-description">
            ASda sd asd ;ajsd ;aksdj al;sdfj asdfja shdfhqweuiof hasdkf hahefow fhalfhqweof hasfd
            </div>
        </div>
    </div>

    <table>
        <tr>
            <td>
            <@wm.widget id="scribbleBoard" style="display: inline-block; float: left; padding-right: 15px;" title="Scribble Board Example"/>
            </td>
            <td>
            <@message code="game.tip.board.playboard"/>
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td>
            <@message code="game.tip.board.players"/>
            </td>
            <td>
            <#include "/content/playground/scribble/widget/players.ftl"/>
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td width="270px">
            <#include "/content/playground/scribble/widget/progress.ftl"/>
            </td>
            <td>
            <@message code="game.tip.board.progress"/>
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td>
            <@message code="game.tip.board.history"/>
            </td>
            <td>
            <#include "/content/playground/scribble/widget/history.ftl"/>
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td width="270px">
            <#include "/content/playground/scribble/widget/selection.ftl"/>
            </td>
            <td>
            <@message code="game.tip.board.selection"/>
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td>
            <@message code="game.tip.board.memory"/>
            </td>
            <td>
            <#include "/content/playground/scribble/widget/memory.ftl"/>
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td>
            <#include "/content/playground/scribble/widget/controls.ftl"/>
            </td>
            <td>
            <@message code="game.tip.board.controls"/>
            </td>
        </tr>
    </table>
</div>

<script type="text/javascript">
    $("#scribbleBoard").prepend(board.getBoardElement());
</script>
