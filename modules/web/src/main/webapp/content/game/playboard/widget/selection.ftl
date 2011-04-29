<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.server.playground.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="moveInfo" title="game.selection.label" style="padding-top: 10px;">
<table width="100%">
    <tr>
        <td height="22" valign="bottom"><b><@message code="game.selection.tiles"/>:</b></td>
        <td height="22" width="100%" valign="bottom" style="padding-left: 5px">
            <div id="selectedTilesInfo" style="position: relative; height: 22px;">
            <@message code="game.selection.notiles"/>
            </div>
        </td>
    </tr>
    <tr>
        <td height="22" valign="bottom"><b><@message code="game.selection.word"/>:</b></td>
        <td height="22" valign="bottom" style="padding-left: 5px">
            <div id="selectedWordInfo" style="position: relative; height: 22px;">
            <@message code="game.selection.noword"/>
            </div>
        </td>
    </tr>
    <tr>
        <td height="22" valign="bottom"><b><@message code="game.selection.points"/>:</b></td>
        <td height="22" valign="bottom">
            <div id="selectedWordCost" style="position: relative; padding-left: 5px">
            <@message code="game.selection.noword"/>
            </div>
        </td>
    </tr>
</table>
<table width="100%">
    <tr>
        <td valign="middle" nowrap="nowrap">
            <div id="wordStatus">
                <span id="wordStatusIcon"></span>
                <span id="wordStatusMessage"></span>
            </div>
        </td>
        <td valign="middle" nowrap="nowrap" align="right">
            <button id="checkWordButton" onclick="moveInfo.checkSelectedWord()">
            <@message code="game.selection.check"/>
            </button>
        </td>
    </tr>
</table>
</@wm.widget>

<script type="text/javascript">
    var moveInfo = new wm.scribble.Selection(board, {
                "checking": "<@message code="game.selection.checking"/>",
                "valid": "<@message code="game.selection.valid"/>",
                "invalid": "<@message code="game.selection.invalid"/>"
            });
</script>
