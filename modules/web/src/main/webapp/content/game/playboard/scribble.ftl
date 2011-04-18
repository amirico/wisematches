<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">

<#include "scribblejs.ftl"/>

<script type="text/javascript">
    var board = new wm.scribble.Board(scribbleGame, ${player.id?string.computer}, "wildcardSelectionPanel");
</script>

<#if !viewMode>
<div id="wildcardSelectionPanel" title="<@message code="game.play.wildcard.label"/>" style="display: none;">
    <div><@message code="game.play.wildcard.description"/></div>
    <div style="position: relative; height: ${(((board.tilesBankInfo?size)/15)?ceiling)*22}px;"></div>
</div>
</#if>

<table id="playboard" cellpadding="5" align="center">
    <tr>
        <td style="vertical-align: top; width: 250px">
        <#include "widget/progress.ftl"/>
        <#include "widget/legend.ftl"/>
        <#include "widget/history.ftl"/>
        </td>

        <td style="vertical-align: top;">
        <@wm.widget id="scribbleBoard" style="width: 100%" title="<center>${board.gameSettings.title} #${board.boardId}</center>"/>
        <#if !viewMode><#include "widget/controls.ftl"/></#if>
        </td>

        <td style="vertical-align: top; width: 280px">
        <#include "widget/players.ftl"/>
<#if !viewMode>
            <#include "widget/selection.ftl"/>
            <#include "widget/memory.ftl"/>
        </#if>
        </td>
    </tr>
</table>

<script type="text/javascript">
    $("#scribbleBoard").prepend(board.getBoardElement());

    <#if board.gameActive>
    $(document).ready(function() {
        board.startBoardMonitoring(function(state, message, error) {
        });
    });
    </#if>
</script>
