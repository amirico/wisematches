/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

if (wm == null) var wm = {};

STATE = {
    DEFAULT:{
        class:'ui-state-highlight'
    },
    INFO:{
        class:'ui-state-active'
    },
    ERROR:{
        class:'ui-state-error'
    }
};

wm.i18n = new function () {
    var values = {};

    var lookup = function (key) {
        return values[key] || null;
    };

    var getValue = function (key, defaultValue, options) {
        var value = lookup(key);
        if (value == null) return defaultValue;

        if (options != null) {
            for (key in options) {
                value = value.replace("{" + key + "}", options[key]);
            }
        }
        return value;
    };

    this.extend = function (hash) {
        $.extend(values, hash);
    };

    this.value = function (key, defaultValue, options) {
        return getValue(key, defaultValue, options);
    };

    this.locale = function () {
        return this.value('locale');
    }
};

wm.util = new function () {
    this.createMatrix = function (size) {
        var m = new Array(size);
        for (var i = 0; i < size; i++) {
            m[i] = new Array(size);
        }
        return m;
    };
};

wm.util.url = new function () {
    this.redirect = function (url) {
        window.location = url;
    };

    this.remove = function (sourceUrl, parameterName) {
        if ((sourceUrl == null) || (sourceUrl.length == 0)) sourceUrl = document.location.href;
        var split = sourceUrl.split("#");
        var urlParts = split[0].split("?");
        var newQueryString = "";
        if (urlParts.length > 1) {
            var parameters = urlParts[1].split("&");
            for (var i = 0; (i < parameters.length); i++) {
                var parameterParts = parameters[i].split("=");
                if (parameterParts[0] != parameterName) {
                    if (newQueryString == "")
                        newQueryString = "?";
                    else
                        newQueryString += "&";
                    newQueryString += parameterParts[0] + "=" + parameterParts[1];
                }
            }
        }
        return urlParts[0] + newQueryString + (split[1] != undefined ? "#" + split[1] : "");
    };

    this.extend = function (sourceUrl, parameterName, parameterValue, replaceDuplicates) {
        if ((sourceUrl == null) || (sourceUrl.length == 0)) sourceUrl = document.location.href;

        var split = sourceUrl.split("#");
        var urlParts = split[0].split("?");
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
        return urlParts[0] + newQueryString + (split[1] != undefined ? "#" + split[1] : "");
    };
};

wm.ui = new function () {
    var activeWindows = true;

    $.blockUI.defaults.message = null;

    $.blockUI.defaults.css = {
        padding:0,
        margin:0,
        width:'30%',
        top:'40%',
        left:'35%',
        textAlign:'center',
        'border-width':'3px'
    };

    $.blockUI.defaults.overlayCSS = {
        opacity:0.2,
        cursor:'wait',
        '-moz-border-radius':'5px',
        '-webkit-border-radius':'5px',
        'border-radius':'5px',
        backgroundColor:'#DFEFFC'
    };

    $.ajaxSetup({
        type:'post',
        dataType:'json',
        contentType:'application/json'
    });

    var alertTemplate = function (title, message) {
        var e;
        e = ['<div>', '<div class="content">', '<h2>' + title + '</h2>', '<p>' + message + '</p>', '</div>', '<span class="icon"></span>', '<span class="close"></span>', '</div>'].join("");
        return e;
    };

    var messageTemplate = function (title, message) {
        return '<div style="padding: 10px 24px; padding-bottom: 10px">' + message + '</div><div class="closeButton"><a href="#"><img src="/resources/images/close.png"></a></div>';
    };

    var statusTemplate = function (title, message) {
        return '<div><div class="content">' + message + '</div></div>';
    };

    var showStatus = function (message, severity, stick) {
        $("#status-widget-pane").empty();

        if (stick == undefined) {
            stick = false;
        }

        var opts = {
            classes:[ severity.class, "ui-corner-all shadow"],
            template:statusTemplate,
            autoHide:!stick,
            autoHideDelay:10000
        };
        if (stick) {
            opts = $.extend(opts, {onClick:function () {
            }, onHover:function () {
            }});
        }
        $("#status-widget-pane").freeow(null, message, opts);
    };

    var clearStatus = function () {
        var freeow = $("#status-widget-pane").children().data("freeow");
        if (freeow != null) {
            freeow.hide();
        } else {
            $("#status-widget-pane").empty();
        }
    };

    this.lock = function (element, message) {
        if (element != null && element != undefined) {
            element.block({message:null});
        } else {
            $.blockUI({message:null});
        }
        showStatus(message, STATE.DEFAULT, true);
    };

    this.unlock = function (element, message, error) {
        if (error == null || error == undefined) {
            error = false;
        }

        if (element != null && element != undefined) {
            element.unblock();
        } else {
            $.unblockUI();
        }

        if (message == null || message == undefined) {
            clearStatus();
        } else {
            showStatus(message, error ? STATE.ERROR : STATE.INFO, false);
        }
    };


    this.message = function (element, message, error) {
        var v = {
            message:messageTemplate(null, message),
            blockMsgClass:'ui-corner-all shadow' + (error ? ' ui-state-error' : ' ui-state-default'),
            draggable:false
        };

        if (element != undefined && element != null) {
            element.block(v);
        } else {
            $.blockUI(v);
        }

        var processClose = function () {
            if (element != undefined && element != null) {
                element.unblock();
            } else {
                $.unblockUI();
            }
        };
        $('.closeButton').click(processClose);
        $('.blockOverlay').click(processClose);
    };

    this.confirm = function (title, msg, approvedAction) {
        $('<div></div>').html(msg).dialog({
            title:title,
            draggable:false,
            modal:true,
            resizable:false,
            width:400,
            buttons:[
                {
                    text:wm.i18n.value('button.ok', 'Ok'),
                    click:function () {
                        $(this).dialog("close");
                        approvedAction(true);
                    }
                },
                {
                    text:wm.i18n.value('button.cancel', 'Cancel'),
                    click:function () {
                        $(this).dialog("close");
                        approvedAction(false);
                    }
                }
            ]
        });
    };


    this.notification = function (title, message, type, error) {
        $("#alerts-widget-pane").freeow(title, message, {
            classes:[ error ? "ui-state-error" : "ui-state-highlight", "ui-corner-all", "shadow", type],
            showStyle:{opacity:.95},
            template:alertTemplate,
            autoHideDelay:10000
        });

        if (!activeWindows) {
            $(window).stopTime('attention-timer');
            var documentTitle = document.title;
            $(window).everyTime(500, 'attention-timer', function (i) {
                if (i % 2 == 0) {
                    document.title = "*** " + title + " ***";
                } else {
                    document.title = documentTitle;
                }
            });
        }
    };


    this.refreshImage = function (element) {
        var el = $(element);
        if (el.attr('src').indexOf("?") < 0) {
            el.attr('src', el.attr('src') + '?' + new Date().getTime());
        } else {
            el.attr('src', el.attr('src') + '&' + new Date().getTime());
        }
    };

    this.player = function (info, hideLink) {
        var id = (info.playerId != undefined ? info.playerId : info.id);
        var html = '<span class="player ' + (id > 1000 ? 'member' : 'computer') + '">';
        if (info.online) {
            html += '<div class="online"></div> ';
        }
        if (!hideLink && id > 1000) {
            html += '<a href="/playground/profile/view?p=' + id + '">';
        }
        html += '<span class="nickname">' + info.nickname + '</span>';
        if (!hideLink && id > 1000) {
            html += '</a>';
        }
        if (id < 1000) {
            html += ' <span class="mod ROBOT"></span>';
        }
        html += '</span>';
        return html;
    };

    $(document).ready(function () {
        var $status = $("<div id='status-widget-pane' class='freeow-widget status-widget-pane'></div>").appendTo($("body"));
        var $alerts = $("<div id='alerts-widget-pane' class='freeow-widget alerts-widget-pane'></div>").appendTo($("body"));

        var $window = $(window);
        var $header = $("#header");
        var windowScroll = function () {
            var height = $header.outerHeight(true);
            var scrollY = $window.scrollTop();
            if (height - scrollY >= 0) {
                $status.css({top:height - scrollY});
                $alerts.css({top:height - scrollY});
            } else if ($status.offset().top != 0) {
                $status.css({top:0});
                $alerts.css({top:0});
            }
        };
        $window.scroll(windowScroll).resize(windowScroll);
        windowScroll();

        var activeWindowTitle = document.title;
        $(window).bind("focus", function () {
            activeWindows = true;
            if (activeWindowTitle != undefined) {
                document.title = activeWindowTitle;
            }
            $(window).stopTime('attention-timer');
        });
        $(window).bind("blur", function () {
            activeWindows = false;
            activeWindowTitle = document.title;
        });
    });
};

wm.ui.editor = new function () {
    var TextEditor = function () {
        var editor = $("<input>");

        this.createEditor = function (currentValue) {
            return editor.val(currentValue);
        };

        this.getValue = function () {
            return editor.val();
        };

        this.getDisplayValue = function () {
            return editor.val();
        };
    };

    var DateEditor = function (ops) {
        var editor = $("<div></div>").datepicker(ops);

        this.createEditor = function (currentValue) {
            return editor.datepicker("setDate", currentValue);
        };

        this.getValue = function () {
            return $.datepicker.formatDate(ops.dateFormat, editor.datepicker("getDate"));
        };

        this.getDisplayValue = function () {
            return $.datepicker.formatDate(ops.displayFormat, editor.datepicker("getDate"));
        };
    };

    var SelectEditor = function (values) {
        var editor = $('<select></select>');

        $.each(values, function (key, value) {
            editor.append($('<option value="' + key + '">' + value + '</option>'));
        });

        this.createEditor = function (currentValue) {
            return editor.val(currentValue);
        };

        this.getValue = function () {
            return editor.val();
        };

        this.getDisplayValue = function () {
            return editor.children("option:selected").text();
        };
    };

    this.Controller = function (view, committer, editorsInfo) {
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

        var commitEditing = function () {
            saveButton.attr('disabled', 'disabled');
            cancelButton.attr('disabled', 'disabled');

            setViewInfo(activeElement, {
                value:activeEditor.getValue(),
                view:activeEditor.getDisplayValue()
            });

            var values = {};
            $.each($(view).find('input').serializeArray(), function (i, field) {
                values[field.name] = field.value;
            });
            committer(activeElement.id, values, function (errorMsg) {
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

        var revertEditing = function () {
            setViewInfo(activeElement, {
                value:previousValue.value,
                view:previousValue.view
            });
            $.unblockUI();
            return false;
        };

        var createNewEditor = function (editorInfo) {
            if (editorInfo.type == 'text') {
                return new TextEditor();
            } else if (editorInfo.type == 'select') {
                return new SelectEditor(editorInfo.values);
            } else if (editorInfo.type == 'date') {
                return new DateEditor(editorInfo.opts || {});
            }
        };

        var setViewInfo = function (view, info) {
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

        var getViewInfo = function (view) {
            return {
                label:$(view).children(".ui-editor-label").text(),
                view:$(view).children(".ui-editor-view").html(),
                value:$(view).children("input").val()
            };
        };

        var closeEditor = function () {
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

        var openEditor = function (view, editor) {
            activeElement = view;
            activeEditor = editor;
            previousValue = getViewInfo(view);

            editorLabel.text(previousValue.label);
            editorContent.append(editor.createEditor(previousValue.value));

            var offset = $(view).offset();

            $.blockUI({
                message:editorDialog,
                centerX:false,
                centerY:false,
                fadeIn:false,
                fadeOut:false,
                blockMsgClass:'shadow',
                css:{
                    width:'auto',
                    left:offset.left + 5,
                    top:offset.top + 5
                },
                draggable:false,
                onUnblock:closeEditor
            });
        };

        saveButton.click(commitEditing);
        cancelButton.click(revertEditing);

        $.each($(view).find('.ui-editor-item'), function (i, v) {
            if (editorsInfo[v.id] != undefined) {
                $(v).click(function () {
                    openEditor(v, createNewEditor(editorsInfo[v.id]));
                });
            }
        });
    };
};

$(document).ready(function () {
    jQuery.fn.extend({
        serializeObject:function () {
            var arrayData, objectData;
            arrayData = this.serializeArray();
            objectData = {};

            $.each(arrayData, function () {
                var value;

                if (this.value != null) {
                    value = this.value;
                } else {
                    value = '';
                }

                if (objectData[this.name] != null) {
                    if (!objectData[this.name].push) {
                        objectData[this.name] = [objectData[this.name]];
                    }

                    objectData[this.name].push(value);
                } else {
                    objectData[this.name] = value;
                }
            });

            return objectData;
        }
    });

    $("[title]").cluetip({ showTitle:false});

    var notifications = $(".notification");
    if (notifications.size() > 0) {
        $("#header-separator").slideUp('slow');
        notifications.appendTo($("#notification-block")).slideDown('slow');
    }

    $(".quickInfo").addClass('ui-state-default').hover(
        function () {
            if (!$(this).hasClass('ui-state-active')) {
                $(this).attr('class', 'quickInfo ui-state-hover');
            }
        },
        function () {
            if (!$(this).hasClass('ui-state-active')) {
                $(this).attr('class', 'quickInfo ui-state-default');
            }
        });

    var activeQuickInfo = undefined;
    $(".quickInfo a").cluetip({
        width:340,
        showTitle:false,
        ajaxCache:true,
        activation:'click',
        closePosition:'bottom',
        closeText:wm.i18n.value('button.close'),
        arrows:false,
        sticky:true,
        ajaxProcess:function (E) {
            return E.summary;
        },
        ajaxSettings:{
            type:'post',
            dataType:'json',
            contentType:'application/json'
        },
        onActivate:function (e) {
            var element = $(this);
            if (activeQuickInfo != undefined) {
                activeQuickInfo.parent().attr('class', 'quickInfo ui-state-default');
            }
            activeQuickInfo = element;
            element.parent().attr('class', 'quickInfo ui-state-active');
            return true;
        },
        onHide:function (ct, ci) {
            $(this).parent().attr('class', 'quickInfo ui-state-default');
            activeQuickInfo = undefined;
        }
    });

    $(".data-table-toolbar a").button();
});