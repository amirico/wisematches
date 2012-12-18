<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.ui.widget class="moveInfo" title="game.selection.label" help="board.selection">
<table width="100%">
<#--
    <tr>
        <td height="22" valign="bottom"><b><@message code="game.selection.tiles"/>:</b></td>
        <td height="22" width="100%" valign="bottom" style="padding-left: 5px">
            <div class="selectedTilesInfo" style="position: relative; height: 22px;">
                <@message code="game.selection.notiles"/>
            </div>
        </td>
    </tr>
-->
    <tr>
        <td height="22" valign="bottom"><b><@message code="game.selection.word"/>:</b></td>
        <td height="22" width="100%" valign="bottom" style="padding-left: 5px">
            <div class="selectedWordInfo" style="position: relative; height: 22px;">
                <@message code="game.selection.noword"/>
            </div>
        </td>
    </tr>
    <tr>
        <td height="22" valign="bottom"><b><@message code="game.selection.points"/>:</b></td>
        <td height="22" width="100%" valign="bottom">
            <div class="selectedWordCost" style="position: relative; padding-left: 5px">
                <@message code="game.selection.noword"/>
            </div>
        </td>
    </tr>
</table>
</@wm.ui.widget>

<script type="text/javascript">
    var moveInfo = new wm.scribble.Selection(board);
</script>
