<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="boardInfoJSON" type="java.lang.String" -->
<#-- @ftlvariable name="boardSettings" type="wisematches.playground.scribble.settings.BoardSettings" -->
<#include "/core.ftl">

<@wm.ui.table.dtinit/>

<#if principal??>
    <#if !player??><#assign player=principal></#if>
</#if>

<script type="text/javascript">
    var boardInfo = ${boardInfoJSON};

    wm.i18n.extend({
        'L2': '<@message code="game.play.bonus.2l"/>', 'L3': '<@message code="game.play.bonus.3l"/>',
        'W2': '<@message code="game.play.bonus.2w"/>', 'W3': '<@message code="game.play.bonus.3w"/>',
        'board.captions': '<@message code="game.play.captions"/>'});
</script>

<script type="text/javascript">
    if (typeof(scribbleController) == "undefined") {
        scribbleController = new wm.scribble.AjaxController();
    }
    var boardSettings = {
    <#if boardSettings??>
        clearByClick: ${boardSettings.clearByClick?string},
        showCaptions:${boardSettings.showCaptions?string}
    </#if>
    };

    var board = new wm.scribble.Board(boardInfo, <#if player??>${player.id}<#else>0</#if>, "wildcardSelectionPanel", scribbleController, boardSettings);
</script>

<#if !viewMode>
<div id="wildcardSelectionPanel" class="${boardSettings.tilesClass}" title="<@message code="game.play.wildcard.label"/>"
     style="display: none;">
    <div><@message code="game.play.wildcard.description"/></div>
    <div style="position: relative; height: 70px;"></div>
</div>
</#if>
