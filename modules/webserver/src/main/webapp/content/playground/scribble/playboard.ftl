<#-- @ftlvariable name="viewMode" type="boolean" -->
<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="boardSettings" type="wisematches.playground.scribble.settings.BoardSettings" -->
<#include "/core.ftl">

<#include "scriplet.ftl"/>

<#if viewMode> <!-- mock controller for view mode -->
<script type="text/javascript">
    var scribbleController = new function () {
        this.execute = function (widget, type, params, data, callback) {
        }
    };
</script>
</#if>

<table width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top">
        <#if !principal??><#include "/content/info/navigation.ftl"/></#if>
        </td>
        <td valign="top" align="center">
            <div id="board${board.boardId}" class="${boardSettings.tilesClass}">
                <table class="playboard" cellpadding="0" cellspacing="0" align="center">
                    <tr>
                        <td style="vertical-align: top; width: 250px">
                        <#include "widget/progress.ftl"/>
            <#if boardSettings.enableShare>
                            <#include "widget/share.ftl"/>
                        </#if>
        <#include "widget/history.ftl"/>
        <#if !viewMode><#include "widget/help.ftl"/></#if>
                        </td>

                        <td style="vertical-align: top; padding-left: 5px; padding-right: 5px;">
                        <#assign boardName><@wm.board.name board, false/></#assign>
            <@wm.ui.widget class="scribbleBoard" style="width: 100%" title="<center>${boardName}</center>"  help="board.playboard"/>
            <#include "widget/controls.ftl"/>
            <#if playerHand?has_content><#include "widget/annotation.ftl"/></#if>
                        </td>

                        <td style="vertical-align: top; width: 280px">
                        <#include "widget/players.ftl"/>
                <#include "widget/selection.ftl"/>
                <#include "widget/thesaurus.ftl"/>
                <#if !viewMode>
                            <#include "widget/memory.ftl"/>
                        <#elseif principal??>
                            <#include "widget/help.ftl"/>
                        </#if>
                        </td>
                    </tr>
                </table>
            </div>
        <#if principal??>
            <div><#include "/content/playground/footer.ftl"/></div>
        </#if>
        </td>
    </tr>
</table>

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
