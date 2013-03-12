<#-- @ftlvariable name="viewMode" type="boolean" -->
<#-- @ftlvariable name="boardInfo" type="wisematches.server.web.servlet.sdo.scribble.BoardInfo" -->
<#-- @ftlvariable name="boardSettings" type="wisematches.playground.scribble.settings.BoardSettings" -->
<#include "/core.ftl">

<#include "scriplet.ftl"/>

<#if !principal??>
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
        <#if !principal??><#include "/content/assistance/navigation.ftl"/></#if>
        </td>
        <td valign="top" align="center">
            <div id="board${boardInfo.id}" class="${boardSettings.tilesClass}">
                <table class="playboard" cellpadding="0" cellspacing="0" align="center">
                    <tr>
                        <td style="vertical-align: top; width: 250px">
                        <#include "widget/progress.ftl"/>
                            <#if !viewMode><#if boardSettings.enableShare><#include "widget/share.ftl"/></#if></#if>
                            <#include "widget/history.ftl"/>
                        </td>

                        <td style="vertical-align: top; padding-left: 5px; padding-right: 5px;">
                        <@wm.ui.widget class="scribbleBoard" style="width: 100%" title="<center>${boardInfo.settings.title}</center>"  help="board.playboard"/>
            <#include "widget/controls.ftl"/>
            <#if boardInfo.handTiles?has_content><#include "widget/annotation.ftl"/></#if>
                        </td>

                        <td style="vertical-align: top; width: 280px">
                        <#include "widget/players.ftl"/>
                            <#if viewMode><#if boardSettings.enableShare><#include "widget/share.ftl"/></#if></#if>
                        <#if !viewMode>
                            <#include "widget/selection.ftl"/>
                            <#include "widget/dictionary.ftl"/>
                            <#include "widget/memory.ftl"/>
                        <#else>
                        </#if>
                            <#if principal??><#include "widget/help.ftl"/></#if>
                        </td>
                    </tr>
                </table>
            </div>
        </td>
    </tr>
</table>

<script type="text/javascript">
    $("#board${boardInfo.id}").find(".scribbleBoard .ui-widget-content").prepend(board.getBoardElement());

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
