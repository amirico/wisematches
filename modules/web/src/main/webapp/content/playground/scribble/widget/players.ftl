<#include "/core.ftl">

<@wm.ui.widget class="playersInfo" title="game.player.label" help="board.players">
<div>
    <table cellpadding="5" width="100%" border="1">
        <tbody></tbody>
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