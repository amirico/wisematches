<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="playersInfo" title="Players Info">
<table cellpadding="5">
    <#list board.playersHands as hand>
        <#assign p = playerManager.getPlayer(hand.getPlayerId())/>
        <#assign active=(board.getPlayerTurn() == hand)/>
        <#assign playerStyle="ui-state-active"/>
        <tr id="playerInfo${hand.getPlayerId()}" class="playerInfo <#if active>active<#else>passive</#if>">
            <td class="ui-corner-left ${playerStyle} ui-table-left">
                <img align="top" src="/resources/images/player/noPlayerIcon.png" width="31" height="28" alt=""/>
            </td>
            <td class="${playerStyle} ui-table-middle"><@wm.player player=p showRating=false/></td>
            <td class="${playerStyle} ui-table-middle" align="center" width="40">${hand.getPoints()}</td>
            <td class="ui-corner-right ${playerStyle} ui-table-right" align="left" width="60">3d 21m</td>
        </tr>
    </#list>
</table>
</@wm.widget>
