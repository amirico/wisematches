/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

if (wm == null) var wm = {};

// Define I18N object inside WM namespace.
if (wm.i18n == null) wm.i18n = new function() {
    var translations = null;
    var locale = null;

    var lookup = function(key) {
        return getTranslations()[key];
    };

    var getLocale = function() {
        if (locale == null) {
            locale = lookup('locale') || 'en';
        }
        return locale;
    };

    var getTranslations = function() {
        if (translations == null) {
            translations = unifyTranslations(lang);
        }
        return translations;
    };

    var unifyTranslations = function(hash, key_prefix) {
        var prefix = key_prefix || "";
        if (prefix.length != 0) {
            prefix += ".";
        }

        var result = {};
        for (var key in hash) {
            var value = hash[key];
            if (typeof value == "object") {
                var result_for_key = unifyTranslations(value, prefix + key);
                for (key in result_for_key) {
                    result[key] = result_for_key[key];
                }
            } else {
                result[prefix + key] = value;
            }
        }
        return result;
    };

    var getValue = function(key, options) {
        var value = lookup(key);
        if (value == null) return "???" + key + "???";

        if (options != null) {
            for (key in options) {
                value = value.replace("{" + key + "}", options[key]);
            }
        }
        return value;
    };

    /**
     * Copies all the properties of config to obj.
     * @param {String} key the name of text constant
     * @param {String} options possible values to be replaced
     * @return {String} returns obj the appropriate localized text by specified key.
     * @member wm.i18n
     */
    this.t = function(key, options) {
        return getValue(key, options);
    };

    /**
     * Returns two letters name of current locale.
     * @return {String} returns current locale.
     * @member wm.i18n
     */
    this.locale = function() {
        return getLocale();
    }
};
if (_ == null) var _ = wm.i18n.t;

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

    this.showGrowl = function(title, message, type, opts) {
        opts = opts || {};
        var v = $.extend(opts, {
            classes: [ opts.error ? "ui-state-error" : "ui-state-highlight", "ui-corner-all", type],
            showStyle: {opacity: .95},
            autoHideDelay: 10000
        });
        $("#notify-widget-pane").freeow(title, message, v);
    }
};

wm.ui.editor = {};

wm.ui.editor.Controller = function(view, editorsInfo) {
    var activeElement;
    var activeEditor;

    var editorDialog = $("<div class='ui-widget-editor ui-widget-content'><div class='ui-layout-table'><div>" +
            "<div class='ui-editor-label'></div>" +
            "<div><div class='ui-editor-content'></div><div class='ui-editor-controls'>" +
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

        $("#header").block({
            message: "Saving profile...",
            css: {
                top: 0,
                borderWidth: 1,
                borderTopWidth: 0
            },
            showOverlay: false,
            blockMsgClass: 'ui-state-highlight ui-corner-bottom'
        });

        var values = {};
        $.each($(view).find('input').serializeArray(), function(i, field) {
            values[field.name] = field.value;
        });
        alert($.toJSON(values));

        $.ajax({
            url: 'save',
            cache: false,
            data: $.toJSON(values),
            error: function(jqXHR, textStatus, errorThrown) {
            },
            success: function(data, textStatus, jqXHR) {
                if (!data.success) {
                    $("#header").block({
                        message: "Profile saving error: " + data.summary,
                        css: {
                            top: 0,
                            borderWidth: 1,
                            borderTopWidth: 0
                        },
                        timeout: 10000,
                        showOverlay: false,
                        blockMsgClass: 'ui-state-error ui-corner-bottom'
                    });
                } else {
                    $("#header").block({
                        message: "Profile saved",
                        css: {
                            top: 0,
                            borderWidth: 1,
                            borderTopWidth: 0
                        },
                        timeout: 5000,
                        showOverlay: false,
                        blockMsgClass: 'ui-state-highlight ui-corner-bottom'
                    });
                }
                $("#header").unblock();
                $.unblockUI();
                saveButton.removeAttr('disabled');
                cancelButton.removeAttr('disabled');
            }
        });
    };

    var revertEditing = function() {
        $.unblockUI();
        return false;
    };

    var createNewEditor = function(editorInfo) {
        if (editorInfo.type == 'text') {
            return new wm.ui.editor.TextEditor();
        } else if (editorInfo.type == 'select') {
            return new wm.ui.editor.SelectEditor(editorInfo.values);
        } else if (editorInfo.type == 'date') {
            return new wm.ui.editor.DateEditor(editorInfo.opts || {});
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
        editorLabel.empty();
        editorContent.empty();

        activeEditor = null;
        activeElement = null;
    };

    var openEditor = function(view, editor) {
        activeElement = view;
        activeEditor = editor;

        var viewInfo = getViewInfo(view);

        editorLabel.text(viewInfo.label);
        editorContent.append(editor.createEditor(viewInfo.value));

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

wm.ui.editor.TextEditor = function() {
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

wm.ui.editor.DateEditor = function(ops) {
    var editor = $("<div></div>").datepicker(ops);

    this.createEditor = function(currentValue) {
        return editor.datepicker("setDate", currentValue);
    };

    this.getValue = function() {
        return $.datepicker.formatDate('dd-mm-yy', editor.datepicker("getDate"));
    };

    this.getDisplayValue = function() {
        return $.datepicker.formatDate('DD, MM d, yy', editor.datepicker("getDate"));
    };
};

wm.ui.editor.SelectEditor = function(values) {
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

$(document).ready(function() {
    $("[title]").tooltip({ position: "bottom right", opacity: 0.97});
    var notifications = $(".notification");
    if (notifications.size() > 0) {
        $("#header-separator").slideUp('slow');
        notifications.appendTo($("#notification-block")).slideDown('slow');
    }
});