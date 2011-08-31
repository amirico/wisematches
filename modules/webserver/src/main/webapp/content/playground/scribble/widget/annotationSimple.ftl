<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->

<#include "/core.ftl">

<div class="annotationWidget ui-widget">
    <div class="annotationControls ui-widget-header ui-corner-all shadow">
        <div style="float: right">
            <a href="#" onclick="comments.addNew();"><@message code="game.comment.new"/></a>
        </div>
        <div><b><@message code="game.comment.label"/></b></div>
    </div>

    <div class="annotationArea ui-widget-content ui-corner-all shadow ui-helper-hidden">
        <div>
            <textarea style="width:  100%; height: 100px;"></textarea>
        </div>

        <div style="text-align: right;">
            <button onclick="comments.save()"><@message code="game.comment.submit"/></button>
            <button onclick="comments.cancelNew()"><@message code="game.comment.cancel"/></button>
        </div>
    </div>

    <div class="annotationView ui-widget-content ui-corner-all shadow ui-helper-hidden">
        <div>
            <div style="float: right;">
                <div class="annotationPrev" role="button" style="display: inline-block;"
                     onclick="comments.prev()">
                    <div class="ui-icon ui-icon-circle-arrow-w">
                    </div>
                </div>
                <div class="annotationPosition" style="display: inline-block; vertical-align: top;"></div>
                <div class="annotationNext" role="button"
                     style="display: inline-block;" onclick="comments.next()">
                    <div class="ui-icon ui-icon-circle-arrow-e"></div>
                </div>
            </div>

            <div class="annotationPlayer" style="display: inline-block;"></div>
            <div class="annotationTimestamp" style="display: inline-block;"></div>
        </div>

        <div class="annotationSeparator ui-widget-content"></div>

        <div class="annotationMessage"></div>
    </div>
</div>

<script type="text/javascript">
    var comments = new wm.scribble.Comments(board, {
        "ago": "<@message code='game.comment.ago'/>"
    });
</script>