<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">

<@wm.widget id="bankInfo" title="Bank's Info">
<table width="100%" border="0">
    <tr>
        <td>Total tiles:</td>
        <td width="100%">${board.getBankCapacity()}</td>
    </tr>
    <tr>
        <td colspan="2">
            <div class="ui-widget-content ui-widget-separator"></div>
        </td>
    </tr>
    <tr>
        <td>Tiles in bank:</td>
        <td>21</td>
    </tr>
    <tr>
        <td>Tiles on board:</td>
        <td>34</td>
    </tr>
    <tr>
        <td>Tiles in hands:</td>
        <td>34</td>
    </tr>
</table>
</@wm.widget>
