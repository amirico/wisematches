<#include "/core.ftl">

<div class="boardInfoWidget">
    <div class="ui-widget-content ui-corner-all shadow" align="center">
        <div>
            <a href="/info/move" target="_blank"><@messageCapFirst code="info.rules.move.label"/></a>
            |
            <a href="/info/rules" target="_blank"><@messageCapFirst code="game.help.rules"/></a>
        </div>
        <div>
            <a href="#" class="action"
               onclick="settings.showSettings(); return false;"><@messageCapFirst code="account.modify.board.label"/></a>
        </div>
    </div>
</div>

<script type="text/javascript">
    var settings = new wm.scribble.Settings(board, {'title':'<@message code="account.modify.board.label"/>', 'apply':'<@message code="game.settings.apply"/>'});
</script>
