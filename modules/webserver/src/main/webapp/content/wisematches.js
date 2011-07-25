/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

if (wm == null) var wm = {};

wm.util = new function() {
    this.createMatrix = function(size) {
        var m = new Array(size);
        for (var i = 0; i < size; i++) {
            m[i] = new Array(size);
        }
        return m;
    };
};

wm.util.url = new function() {
    this.redirect = function(url) {
        window.location = url;
    };

    this.extend = function(sourceUrl, parameterName, parameterValue, replaceDuplicates) {
        if ((sourceUrl == null) || (sourceUrl.length == 0)) sourceUrl = document.location.href;
        var urlParts = sourceUrl.split("?");
        var newQueryString = "";
        if (urlParts.length > 1) {
            var parameters = urlParts[1].split("&");
            for (var i = 0; (i < parameters.length); i++) {
                var parameterParts = parameters[i].split("=");
                if (!(replaceDuplicates && parameterParts[0] == parameterName)) {
                    if (newQueryString == "")
                        newQueryString = "?";
                    else
                        newQueryString += "&";
                    newQueryString += parameterParts[0] + "=" + parameterParts[1];
                }
            }
        }
        if (newQueryString == "")
            newQueryString = "?";
        else
            newQueryString += "&";
        newQueryString += parameterName + "=" + parameterValue;

        return urlParts[0] + newQueryString;
    };
};

wm.ui = new function() {
    var activeWindows = true;

    var alertTemplate = function(title, message) {
        var e;
        e = ['<div>','<div class="content">','<h2>' + title + '</h2>','<p>' + message + '</p>','</div>','<span class="icon"></span>','<span class="close"></span>','</div>'].join("");
        return e;
    };

    var statusTemplate = function(title, message) {
        return '<div><div class="content">' + message + '</div></div>';
    };

    $.blockUI.defaults.css = {
        padding:    0,
        margin:        0,
        width:        '30%',
        top:        '40%',
        left:        '35%',
        textAlign:    'center',
        'border-width': '3px'
    };

    $.blockUI.defaults.overlayCSS = {
        backgroundColor: '#000',
        opacity:           0.2,
        cursor:               'wait'
    };

    $.ajaxSetup({
        type: 'post',
        dataType: 'json',
        contentType: 'application/json'
    });

    this.showConfirm = function(title, msg, approvedAction) {
        $('<div></div>').html(msg).dialog({
            title: title,
            draggable: false,
            modal: true,
            resizable: false,
            width: 400,
            buttons: {
                "Ok": function() {
                    $(this).dialog("close");
                    approvedAction(true);
                },
                "Cancel": function() {
                    $(this).dialog("close");
                    approvedAction(false);
                } }
        });
    };

    this.showWaitMessage = function(message) {
        $.blockUI({
            blockMsgClass: 'ui-corner-all ui-state-default',
            css: {
                padding: '15px',
                opacity: .85
            },
            message: message });
    };

    this.showMessage = function(opts) {
        opts = opts || {};
        var v = $.extend(opts, {
            message: '<div style="padding: 10px 24px; padding-bottom: 10px">' + opts.message + '</div><div class="closeButton"><a href="javascript: $.unblockUI()"><img src="/resources/images/close.png"></a></div>',
            blockMsgClass: 'ui-corner-all' + (opts.error ? ' ui-state-error' : ' ui-state-default'),
            draggable: false
        });
        $.blockUI(v);
        $('.blockOverlay').click($.unblockUI);
    };

    this.showAlert = function(title, message, type, error) {
        $("#alerts-widget-pane").freeow(title, message, {
            classes: [ error ? "ui-state-error" : "ui-state-highlight", "ui-corner-all", type],
            showStyle: {opacity: .95},
            template:alertTemplate,
            autoHideDelay: 10000
        });
        wm.ui.getAttention(title);
    };

    this.getAttention = function(title) {
        if (!activeWindows) {
            $(window).stopTime('attention-timer');
            var documentTitle = document.title;
            $(window).everyTime(500, 'attention-timer', function(i) {
                if (i % 2 == 0) {
                    document.title = "*** " + title + " ***";
                } else {
                    document.title = documentTitle;
                }
            });
        }
    };

    this.showStatus = function(message, error, stick) {
//        wm.ui.clearStatus();
        $("#status-widget-pane").empty();

        if (stick == undefined) {
            stick = false;
        }

        $("#status-widget-pane").freeow(null, message, {
            classes: [ error ? "ui-state-error" : "ui-state-highlight", "ui-corner-bottom"],
            template:statusTemplate,
            autoHide: !stick,
            autoHideDelay: 10000
        });
    };

    this.clearStatus = function() {
        var freeow = $("#status-widget-pane").children().data("freeow");
        if (freeow != null) {
            freeow.hide();
        } else {
            $("#status-widget-pane").empty();
        }
    };

    this.refreshImage = function(element) {
        var el = $(element);
        if (el.attr('src').indexOf("?") < 0) {
            el.attr('src', el.attr('src') + '?' + new Date().getTime());
        } else {
            el.attr('src', el.attr('src') + '&' + new Date().getTime());
        }
    };

    this.player = function(info) {
        return '<span class="player member"><a href="/playground/profile/view?p=' + info.playerId + '"><span class="nickname">' + info.nickname + '</span></a></span>';
    };

    $(document).ready(function() {
        $("body").prepend($("<div id='alerts-widget-pane' class='freeow-widget alerts-widget-pane'></div>"));
        $("body").prepend($("<div id='status-widget-pane' class='freeow-widget status-widget-pane'></div>"));

        var activeWindowTitle = document.title;
        $(window).bind("focus", function() {
            activeWindows = true;
            if (activeWindowTitle != undefined) {
                document.title = activeWindowTitle;
            }
            $(window).stopTime('attention-timer');
        });
        $(window).bind("blur", function() {
            activeWindows = false;
            activeWindowTitle = document.title;
        });
    });
};

wm.ui.editor = new function() {
    var TextEditor = function() {
        var editor = $("<input>");

        this.createEditor = function(currentValue) {
            return editor.val(currentValue);
        };

        this.getValue = function() {
            return editor.val();
        };

        this.getDisplayValue = function() {
            return editor.val();
        };
    };

    var DateEditor = function(ops) {
        var editor = $("<div></div>").datepicker(ops);

        this.createEditor = function(currentValue) {
            return editor.datepicker("setDate", currentValue);
        };

        this.getValue = function() {
            return $.datepicker.formatDate(ops.dateFormat, editor.datepicker("getDate"));
        };

        this.getDisplayValue = function() {
            return $.datepicker.formatDate(ops.displayFormat, editor.datepicker("getDate"));
        };
    };

    var SelectEditor = function(values) {
        var editor = $('<select></select>');

        $.each(values, function(key, value) {
            editor.append($('<option value="' + key + '">' + value + '</option>'));
        });

        this.createEditor = function(currentValue) {
            return editor.val(currentValue);
        };

        this.getValue = function() {
            return editor.val();
        };

        this.getDisplayValue = function() {
            return editor.children("option:selected").text();
        };
    };

    this.Controller = function(view, committer, editorsInfo) {
        var activeElement;
        var activeEditor;
        var previousValue;

        var editorDialog = $("<div class='ui-widget-editor ui-widget-content'><div class='ui-layout-table'><div>" +
                "<div class='ui-editor-label'></div>" +
                "<div><div class='ui-editor-content'></div><div class='ui-editor-controls'>" +
                "<div class='ui-editor-error'></div>" +
                "<button class='ui-editor-save'>Save</button> " +
                "<button class='ui-editor-cancel'>Cancel</button>" +
                "</div></div>" +
                "</div></div></div>");

        var editorLabel = $(editorDialog).find('.ui-editor-label');
        var editorContent = $(editorDialog).find('.ui-editor-content');

        var saveButton = $(editorDialog).find('.ui-editor-save');
        var cancelButton = $(editorDialog).find('.ui-editor-cancel');

        var commitEditing = function() {
            saveButton.attr('disabled', 'disabled');
            cancelButton.attr('disabled', 'disabled');

            setViewInfo(activeElement, {
                value: activeEditor.getValue(),
                view: activeEditor.getDisplayValue()
            });

            var values = {};
            $.each($(view).find('input').serializeArray(), function(i, field) {
                values[field.name] = field.value;
            });
            committer(activeElement.id, values, function(errorMsg) {
                if (errorMsg != undefined) {
                    editorDialog.addClass('ui-state-error');
                    editorDialog.find(".ui-editor-error").html(errorMsg);

                    saveButton.removeAttr('disabled');
                    cancelButton.removeAttr('disabled');
                } else {
                    $.unblockUI();
                }
            });
        };

        var revertEditing = function() {
            setViewInfo(activeElement, {
                value: previousValue.value,
                view: previousValue.view
            });
            $.unblockUI();
            return false;
        };

        var createNewEditor = function(editorInfo) {
            if (editorInfo.type == 'text') {
                return new TextEditor();
            } else if (editorInfo.type == 'select') {
                return new SelectEditor(editorInfo.values);
            } else if (editorInfo.type == 'date') {
                return new DateEditor(editorInfo.opts || {});
            }
        };

        var setViewInfo = function(view, info) {
            var a = $(view).children(".ui-editor-view");
            if (info.value == "") {
                a.addClass('sample');
                a.html(a.attr('label'));
            } else {
                a.removeClass('sample');
                a.html(info.view);
            }
            $(view).children("input").val(info.value);
        };

        var getViewInfo = function(view) {
            return {
                label: $(view).children(".ui-editor-label").text(),
                view: $(view).children(".ui-editor-view").html(),
                value: $(view).children("input").val()
            };
        };

        var closeEditor = function() {
            saveButton.removeAttr('disabled');
            cancelButton.removeAttr('disabled');

            editorDialog.removeClass('ui-state-error');
            editorDialog.find(".ui-editor-error").html('');

            editorLabel.empty();
            editorContent.empty();

            activeEditor = null;
            activeElement = null;
            previousValue = null;
        };

        var openEditor = function(view, editor) {
            activeElement = view;
            activeEditor = editor;
            previousValue = getViewInfo(view);

            editorLabel.text(previousValue.label);
            editorContent.append(editor.createEditor(previousValue.value));

            var offset = $(view).offset();

            $.blockUI({
                message: editorDialog,
                centerX: false,
                centerY: false,
                fadeIn: false,
                fadeOut: false,
                blockMsgClass: 'shadow',
                css: {
                    width: 'auto',
                    left: offset.left + 5,
                    top: offset.top + 5
                },
                draggable: false,
                onUnblock: closeEditor
            });
        };

        saveButton.click(commitEditing);
        cancelButton.click(revertEditing);

        $.each($(view).find('.ui-editor-item'), function(i, v) {
            if (editorsInfo[v.id] != undefined) {
                $(v).click(function() {
                    openEditor(v, createNewEditor(editorsInfo[v.id]));
                });
            }
        });
    };
};

$(document).ready(function() {
    $("[title]").tooltip({ position: "bottom right", opacity: 0.97});
    var notifications = $(".notification");
    if (notifications.size() > 0) {
        $("#header-separator").slideUp('slow');
        notifications.appendTo($("#notification-block")).slideDown('slow');
    }
});