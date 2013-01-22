<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#include "/core.ftl">

<#assign playerInGame=principal?? && board.getPlayerHand(principal.id)??/>

<@wm.ui.widget class="playersInfo" title="game.player.label" help="board.players">
<div>
    <table cellpadding="5" width="100%" border="1">
        <tbody>
            <#list board.players as hand>
                <#assign p = playerManager.getPlayer(hand.getPlayerId())/>
            <tr class="player-info-${p.id} player-info">
                <td width="24px" height="24px" class="winner-icon ui-corner-left ui-table-left">&nbsp;</td>
                <td class="nickname ui-table-middle">
                    <@wm.player.name p/>
                </td>
                <td width="20px" class="points ui-table-middle">${hand.points}</td>
                <td width="60px" class="info ui-corner-right ui-table-right"></td>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
    <#if playerInGame>
    <div class="createChallenge <#if board.active>ui-helper-hidden</#if>" style="width: 100%; padding-top: 6px">
        <button class="wm-ui-button"><@message code="game.challenge.label"/></button>
    </div>
    </#if>
</@wm.ui.widget>
<script type="text/javascript">
    new wm.scribble.Players(board);

    <#if playerInGame>
    $(".createChallenge button").click(function () {
        wm.util.url.redirect('/playground/scribble/create?t=board&p=' + board.getBoardId());
    });
    board.bind('gameState', function (event, type, state) {
        if (type === 'finished') {
            $(".createChallenge").show();
        }
    });
    </#if>
</script>