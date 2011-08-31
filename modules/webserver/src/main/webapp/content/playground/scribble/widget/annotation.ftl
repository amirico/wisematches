<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->

<#include "/core.ftl">

<style type="text/css">
    .annWidget {
        padding-top: 10px;
        max-width: 445px;
    }

    .annCtrls {
        float: right;
    }

    .annCtrls a {
        color: #1D5987;
        border-bottom: 1px dashed;
        text-decoration: none
    }

    .annPlayer {
        display: inline-block;
    }

    .annTS {
        font-size: 10px;
        color: gray;
        line-height: normal;
        display: inline-block;
        float: right;
    }

    .annSeparator {
        padding-top: 0;
        margin-left: -5px;
        margin-right: -5px;
        border-width: 0;
        border-bottom-width: 1px;
    }

    .annMsg {
        padding: 0;
        margin: 0;
        position: relative;
        text-align: justify;
        line-height: normal;
    }

    .annItem.collapsed {
        position: relative;
    }

    .annItem.collapsed .annInfo {
        display: none;
    }

    .annItem.collapsed .annMsg {
        overflow: hidden;
        white-space: nowrap;
    }

    .annItem.collapsed .annMsg span {
        right: 0;
        top: 0;
        width: 60px;
        height: 100%;
        position: absolute;
        background: url(/resources/images/scribble/board/textFading.png) repeat-y;
    }

    .annStatus, .annStatus div {
        margin: 0;
        padding: 0;
        /*font-size: 10px;*/
        text-align: right;
        font-weight: bold;
        line-height: normal;
    }

    .annCount {
        float: left;
    }
</style>

<div class="annWidget ui-widget">
    <div class="ui-widget-header ui-corner-all shadow">
        <div class="annCtrls">
            <div class="ui-helper-hidden">
                <a href="#" onclick="comments.addNew(); return false;"><@message code="game.comment.new"/></a>
            </div>
            <div class="loading-image" style="width: 150px">&nbsp;</div>
        </div>
        <div><b><@message code="game.comment.label"/></b></div>
    </div>

    <div class="annArea ui-widget-content ui-corner-all shadow ui-helper-hidden">
        <div>
            <textarea style="width:  100%; height: 100px;"></textarea>
        </div>

        <div style="text-align: right;">
            <button onclick="comments.save()"><@message code="game.comment.submit"/></button>
            <button onclick="comments.cancelNew()"><@message code="game.comment.cancel"/></button>
        </div>
    </div>

    <div class="annView ui-widget-content ui-corner-all shadow ui-helper-hidden">
        <div class="annComments"></div>
        <div class="annStatus">
            <div class="annCount">&nbsp;</div>
            <div class="annLoad">&nbsp;</div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var Asd = function(board) {
        var comments = {};

        var overlayCSS = {
            '-moz-border-radius': '5px',
            '-webkit-border-radius': '5px',
            'border-radius': '5px',
            backgroundColor:'#DFEFFC'
        };

        var element = function(clazz) {
            return $("#board" + board.getBoardId() + " " + clazz);
        };

        this.addNew = function() {
            element(".annArea textarea").val("");
            element(".annArea").slideDown('fast');
        };

        this.cancelNew = function() {
            closeEditor();
        };

        this.save = function() {
            element(".annArea").block({ message: null, overlayCSS: overlayCSS });
            var msg = element(".annArea textarea").val();
            $.post('/playground/scribble/comment/add.ajax?b=' + board.getBoardId(), $.toJSON({text: msg}), function(result) {
                if (result.success) {
                    closeEditor();
                } else {
                    wm.ui.showStatus(result.summary, true);
                }
                element(".annArea").unblock();
            });
        };

        var closeEditor = function() {
            element(".annArea").slideUp('fast');
        };

        var addComment = function(comment, collapsed) {
            var item = $('<div class="annItem ' + (collapsed ? 'collapsed' : '') + '"></div>');
            var info = $('<div class="annInfo"></div>').appendTo(item);
            var time = $('<div class="annTS"></div>').appendTo(info).html('(' + comment.elapsed + ' ' + 'ago' + ')');
            var player = $('<div class="annPlayer"></div>').appendTo(info).html(wm.ui.player(board.getPlayerInfo(comment.person)));

            var msg = $('<div class="annMsg"></div>').appendTo(item).html(comment.text + "<span></span>");

            if (collapsed) {
                item.hover(function() {
                    $(this).find(".annInfo").slideDown('fast');
                    $(this).toggleClass("collapsed");
                }, function() {
                    $(this).find(".annInfo").slideUp('fast');
                    $(this).toggleClass("collapsed");
                });
            }

            element(".annView .annComments").append(item);
            element(".annView .annComments").append($('<div class="annSeparator ui-widget-content"></div>'));
        };

        $.post('/playground/scribble/comment/load.ajax?b=' + board.getBoardId(), function(result) {
            if (result.success) {
                var count = 0;
                var asd = true;
                var visible = new Array();

                comments.length = result.data.comments.length;
                $.each(result.data.comments, function(i, a) {
                    comments[a.id] = a.read;
                    if (!a.read && asd) {
                        visible.push(a.id);
                    } else {
                        if (count < 3) {
                            visible.push(a.id);
                        }
                        asd = false;
                        count++;
                    }
                });

//                alert("Visible comments: " + visible.length);
                if (visible.length != 0) {
                    $.post('/playground/scribble/comment/get.ajax?b=' + board.getBoardId(), $.toJSON(visible), function(result) {
                        if (result.success) {
                            $.each(result.data.comments, function(i, a) {
                                addComment(a, comments[a.id]);
                            });
                            element(".annView .annStatus .annCount").html(visible.length + " of " + comments.length);
                            if (comments.length > visible.length) {
                                if (comments.length - visible.length > 5) {
                                    element(".annView .annStatus .annLoad").html("<a href='a'>load next 5</a>");
                                } else {
                                    element(".annView .annStatus .annLoad").html("<a href='a'>load next " + (comments.length - visible.length) + "</a>");
                                }
                            }
                            element(".annView").show();
                            element(".annCtrls div").toggle();
                            element(".annWidget").unblock();
                        } else {
                            wm.ui.showStatus(result.summary, true);
                        }
                    });
                }
            } else {
                wm.ui.showStatus(result.summary, true);
            }
        });

        element(".annArea button").button();
    };

    var comments = new Asd(board);
    //    var comments = new wm.scribble.Comments(board, {
    <#--"ago": "<@message code='game.comment.ago'/>"-->
    //    });
</script>