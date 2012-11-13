<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->

<#include "/core.ftl">

<div class="annotation ui-widget">
    <div class="header ui-widget-header ui-corner-all shadow">
        <div class="quickInfo ajax">
            <a class="ui-icon ui-icon-info" href="#" onclick="return false"
               rel="/info/tip.ajax?s=board.annotations"></a>
        </div>
        <div class="controls">
        <#if board.gameActive>
            <div class="ui-helper-hidden create-comment">
                <a class="action" href="#"
                   onclick="comments.create(); return false;"><@message code="game.comment.add"/></a>
            </div>
        </#if>
            <div class="loading-image" style="width: 150px">&nbsp;</div>
        </div>
        <div class="label">
            <b><@message code="game.comment.label"/></b>
            <span class="new ui-helper-hidden ui-state-hover">(<span
                    class="value">7</span> <@message code="game.comment.new"/>)</span>
        </div>
    </div>

    <div class="editor ui-widget-content ui-corner-all shadow ui-helper-hidden">
        <div>
            <label><textarea style="width:  100%; height: 100px;"></textarea></label>
        </div>

        <table width="100%">
            <tr>
                <td width="100%" align="left" valign="top">
                    <div class="ui-state-error-text"></div>
                </td>
                <td nowrap="nowrap" align="right" valign="top">
                    <button onclick="comments.save(); return false;"><@message code="game.comment.submit"/></button>
                    <button onclick="comments.cancel(); return false;"><@message code="game.comment.cancel"/></button>
                </td>
            </tr>
        </table>
    </div>

    <div class="content ui-helper-hidden">
        <div class="items ui-widget-content ui-corner-all shadow"></div>

        <div class="status ui-widget-content ui-corner-all ui-state-default shadow">
            <div class="controls">
                <a href='#' onclick="comments.load(); return false;">&raquo; load older <span>0</span></a>
            </div>
            <div class="progress">qwe</div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var comments = new wm.scribble.Comments(board, scribbleController, {
        "ago": "<@message code='game.comment.ago'/>",
        "of": "<@message code='separator.of'/>",
        "empty": "<@message code='game.comment.err.empty'/>",
        "saved": "<@message code='game.comment.saved'/>",
        "saving": "<@message code='game.comment.saving'/>",
        "loading": "<@message code='game.comment.loading'/>"
    });
</script>