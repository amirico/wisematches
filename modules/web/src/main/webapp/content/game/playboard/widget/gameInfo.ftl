<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">

<#setting datetime_format="MMM dd, yyyy HH:mm">
<#assign active=board.gameState == "ACTIVE"/>

<@wm.widget id="gameInfo" title="Game Info">
<table width="100%" border="0">
    <tr>
        <td><b>Game state:</b></td>
        <td width="100%"><#if active>In Progress<#else>Finished</#if></td>
    </tr>
    <tr>
        <td colspan="2">
            <div class="ui-widget-content ui-widget-separator"></div>
        </td>
    </tr>
    <tr>
        <td><b>Game started:</b></td>
        <td width="100%">${board.startedTime?datetime?string?cap_first}</td>
    </tr>
    <#if active>
        <tr>
            <td><b>Last move time:</b></td>
            <td width="100%">${board.lastMoveTime?datetime?string?cap_first}</td>
        </tr>
        <#else>
            <tr>
                <td><b>Game finished:</b></td>
                <td width="100%">${board.finishedTime?datetime?string?cap_first}</td>
            </tr>
    </#if>
    <tr>
        <td colspan="2">
            <div class="ui-widget-content ui-widget-separator"></div>
        </td>
    </tr>
    <tr>
        <td valign="top"><b>Tiles count in:</b></td>
        <td width="100%">
            <table>
                <tr>
                    <td><i>board</i></td>
                    <td><i>hands</i></td>
                    <td><i>bank</i></td>
                </tr>
                <tr>
                    <td align="center">44</td>
                    <td align="center">124</td>
                    <td align="center">12</td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</@wm.widget>