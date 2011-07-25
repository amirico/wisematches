<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->

<#include "/core.ftl">

<div id="annotationWidget" class="ui-widget" style="padding-top: 10px; max-width: 450px;">
    <div id="annotationControls" class="ui-widget-header ui-corner-all shadow">
        <div style="float: right">
            <a href="#" onclick="wm.scribble.annotations.addNew();"
               style="color: #1D5987; border-bottom: 1px dashed; text-decoration: none">add new note</a>
        </div>
        <div><b>Board Notes</b> <span id="annotationCount"></span></div>
    </div>

    <div id="annotationArea" class="ui-widget-content ui-corner-all shadow ui-helper-hidden">
        <div>
            <textarea style="width:  100%; height: 100px;"></textarea>
        </div>

        <div style="text-align: right;">
            <button onclick="wm.scribble.annotations.save()">Submit</button>
            <button onclick="wm.scribble.annotations.cancelNew()">Cancel</button>
        </div>
    </div>

    <div id="annotationView" class="ui-widget-content ui-corner-all shadow ui-helper-hidden">
        <div>
            <div style="float: right;">
                <div id="annotationPrev"
                     style="background: none; border: none; display: inline-block; cursor: pointer"
                     onclick="wm.scribble.annotations.prev()">
                    <div class="ui-icon ui-icon-circle-arrow-w"></div>
                </div>
                <div id="annotationPosition" style="display: inline-block; vertical-align: top;">
                    0 of 0
                </div>
                <div id="annotationNext"
                     style="background: none; border: none; display: inline-block; cursor: pointer"
                     onclick="wm.scribble.annotations.next()">
                    <div class="ui-icon ui-icon-circle-arrow-e"></div>
                </div>
            </div>

            <div id="annotationPlayer"></div>
        </div>

        <div class="ui-widget-content"
             style="padding-top: 0; margin-left: -5px; margin-right: -5px;border-width: 0; border-bottom-width:  1px;"></div>

        <div id="annotationMessage" style="padding-top: 10px; text-align: justify;"></div>
    </div>
</div>

<script type="text/javascript">
    wm.scribble.annotations = new function() {
        var comments;
        var position = 0;
        var commentsCache = new Array();

        var overlayCSS = {
            '-moz-border-radius': '5px',
            '-webkit-border-radius': '5px',
            'border-radius': '5px',
            backgroundColor:'#DFEFFC'
        };

        this.addNew = function() {
            $("#annotationView").hide();
            $("#annotationArea").slideDown('slow');
            return false;
        };

        this.cancelNew = function() {
            $("#annotationArea").slideUp('slow');
        };

        this.save = function() {
            wm.ui.showStatus("Adding new comment. Please wait...");
            var msg = $("#annotationArea textarea").val();
            $.post('/playground/scribble/comment/add.ajax?b=' + board.getBoardId(), $.toJSON({text: msg}), function(result) {
                if (result.success) {
                    wm.ui.showStatus("Comment has been added");
                } else {
                    wm.ui.showStatus(result.summary, true);
                }
            });
        };

        var showComment = function(pos) {
            var comment = commentsCache['comment' + pos];
            if (comment == undefined) {
                $("#annotationWidget").block({ message: 'Loading a comment...', overlayCSS: overlayCSS });
//                $("#annotationPlayer").html("Loading a comment...");
//                $("#annotationMessage").html('<div class="loading-image" style="height: 50px"></div>');
                $.post('/playground/scribble/comment/get.ajax?b=' + board.getBoardId() + "&m=" + comments[pos], function(result) {
                    if (result.success) {
                        commentsCache['comment' + pos] = result.data;
                        showComment(pos);
                    } else {
                        wm.ui.showStatus(result.summary, true);
                    }
                    $("#annotationWidget").unblock();
                });
            } else {
                position = pos;
                $("#annotationMessage").html(comment.text);
                $("#annotationPlayer").html(board.getPlayerInfo(comment.pid).nickname);
                $("#annotationPosition").html((position + 1) + " of " + comments.length);
            }
        };

        this.next = function() {
            if (position + 1 < comments.length) {
                showComment(position + 1);
            }
            return false;
        };

        this.prev = function() {
            if (position - 1 >= 0) {
                showComment(position - 1);
            }
            return false;
        };

        var updateView = function(ids) {
            comments = ids;

            $("#annotationCount").html("(" + comments.length + ")");
            if (comments.length > 0) {
                $("#annotationView").show();
                showComment(comments.length - 1);
            }
        };

        $.post('/playground/scribble/comment/load.ajax?b=' + board.getBoardId(), function(result) {
            if (result.success) {
                updateView(result.data.comments);
            } else {
                wm.ui.showStatus(result.summary, true);
            }
        })
    };

    $("#annotationWidget button").button();

    $(document).ready(function() {
        $("#annotationNext").hover(function() {
                    $("#annotationNext").addClass("ui-state-hover");
                },
                function() {
                    $("#annotationNext").removeClass("ui-state-hover");
                });

        /*
        $("#annotationPrev").hover(function() {
                    $("#annotationPrev").addClass("ui-state-hover");
                },
                function() {
                    $("#annotationPrev").removeClass("ui-state-hover");
                });
*/


        /*
                $("#annotationControls div").hover(
                        function() {
                            $(this).parent().removeClass();
                        },
                        function() {

                        })
        */
    });
</script>