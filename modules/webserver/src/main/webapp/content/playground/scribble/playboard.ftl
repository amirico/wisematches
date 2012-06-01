<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="boardSettings" type="wisematches.playground.scribble.settings.BoardSettings" -->
<#include "/core.ftl">

<#include "scriplet.ftl"/>

<div id="board${board.boardId}" class="${boardSettings.tilesClass}">
    <table class="playboard" cellpadding="0" cellspacing="0" align="center">
        <tr>
            <td style="vertical-align: top; width: 250px">
            <#include "widget/progress.ftl"/>
        <#include "widget/history.ftl"/>
        <#include "widget/help.ftl"/>
            </td>

            <td style="vertical-align: top; padding-left: 5px; padding-right: 5px;">
            <@wm.widget class="scribbleBoard" style="width: 100%" title="<center>${board.gameSettings.title} #${board.boardId}</center>"  help="board.playboard"/>
            <#include "widget/controls.ftl"/>
            <#if playerHand?has_content><#include "widget/annotation.ftl"/></#if>
            </td>

            <td style="vertical-align: top; width: 280px">
            <#include "widget/players.ftl"/>
                <#include "widget/selection.ftl"/>
                <#include "widget/thesaurus.ftl"/>
                <#if !viewMode><#include "widget/memory.ftl"/></#if>
            </td>
        </tr>
    </table>
</div>
<div><#include "/content/playground/footer.ftl"/></div>


<script type="text/javascript">
    $("#board${board.boardId} .scribbleBoard .ui-widget-content").prepend(board.getBoardElement());

    <#if !viewMode>
    $(document).ready(function () {
        var monitoring = new wm.scribble.Monitoring(board);

        monitoring.addMonitoringBean('board', board.getMonitoringBean());
        if (comments != null && comments != undefined) {
            monitoring.addMonitoringBean('comments', comments.getMonitoringBean());
        }
        monitoring.startMonitoring();
    });
    </#if>
</script>
