<#include "/core.ftl">

<@wm.ui.widget class="playersInfo" title="game.player.label" help="board.players">
<div>
    <table cellpadding="5" width="100%" border="1">
        <tbody>
        <#--
            <#list boardInfo.players as p>
                <#assign hand=boardInfo.outcomes.scores[p_index]/>
            <tr class="player-info-${p.id} player-info">
                <td width="24px" height="24px" class="winner-icon ui-corner-left ui-table-left">
                    <div></div>
                </td>
                <td class="nickname ui-table-middle">
                    <@wm.player.name p/>
                </td>
                <td width="20px" class="points ui-table-middle">${hand.points}</td>
                <td width="60px" class="info ui-corner-right ui-table-right"></td>
            </tr>
            </#list>
-->
        </tbody>
    </table>
</div>
<div class="createChallenge ui-helper-hidden"
     style="width: 100%; padding-top: 6px">
    <button class="wm-ui-button"><@message code="game.challenge.label"/></button>
</div>
</@wm.ui.widget>
<script type="text/javascript">
    new wm.scribble.Players(board);
</script>