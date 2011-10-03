<#include "/core.ftl">

<div class="boardInfoWidget" style="padding-top: 10px;">
    <div class="ui-widget-content ui-corner-all shadow" align="center">
    <#--<a class="action" href="#" onclick="wm.game.help.showHelp('/info/legend'); return false;">Показать Легенду</a> |-->
    <#--<a class="action" href="#" onclick="wm.game.help.showHelp('/info/moves'); return false;">Как ходить?</a> |-->
        <a class="action" href="#"
           onclick="wm.game.help.showHelp('/info/rules', this); return false;"><@message code="game.help.rules"/></a>
    </div>
</div>