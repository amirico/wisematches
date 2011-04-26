<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="playersInfo" title="game.player.label">
<table cellpadding="5" width="100%" border="1">
    <tbody>
        <#list board.playersHands as hand>
            <#assign p = playerManager.getPlayer(hand.getPlayerId())/>
        <tr class="player-info-${p.id} player-info">
            <td width="24px" height="24px" class="winner-icon ui-corner-left ui-table-left">&nbsp;</td>
            <td class="nickname ui-table-middle">
            <@wm.player player=p showRating=false/>
            </td>
            <td width="20px" class="points ui-table-middle">${hand.points}</td>
            <td width="60px" class="info ui-corner-right ui-table-right"></td>
        </tr>
        </#list>
    </tbody>
</table>
</@wm.widget>

<script type="text/javascript">
    new wm.scribble.Players(board);
</script>