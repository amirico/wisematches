<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">
<#include "../playboardModel.ftl">

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
        <td width="100%">${gameMessageSource.formatDate(board.startedTime, locale)}</td>
    </tr>
    <#if active>
        <tr>
            <td><b>Last move time:</b></td>
            <td width="100%">${gameMessageSource.formatDate(board.lastMoveTime, locale)}</td>
        </tr>
        <#else>
            <tr>
                <td><b>Game finished:</b></td>
                <td width="100%">${gameMessageSource.formatDate(board.finishedTime, locale)}</td>
            </tr>
    </#if>
    <tr>
        <td colspan="2">
            <div class="ui-widget-content ui-widget-separator"></div>
        </td>
    </tr>
    <#assign boardTilesCount=0/>
    <#assign bankTilesCount=board.bankCapacity/>
    <#list 0..bankTilesCount-1 as n>
        <#if board.isBoardTile(n)><#assign boardTilesCount=boardTilesCount+1/></#if>
    </#list>
    <tr>
        <td valign="top"><b>Tiles count in:</b></td>
        <td width="100%">
            <table>
                <tr>
                    <td><i>bank</i></td>
                    <td><i>board</i></td>
                    <td><i>hands</i></td>
                </tr>
                <tr>
                    <td align="center">${bankTilesCount}</td>
                    <td align="center">${boardTilesCount}</td>
                    <td align="center">${bankTilesCount - boardTilesCount}</td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</@wm.widget>

<script type="text/javascript">

</script>