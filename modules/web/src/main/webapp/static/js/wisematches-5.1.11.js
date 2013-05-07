/*
 * Copyright (c) 2013, WiseMatches (by Sergey Klimenko).
 */
wm = {};
wm.game = {};
wm.game.dict = {};
wm.game.tourney = {};
wm.game.settings = {};
wm.scribble = {};

STATE = {
    DEFAULT: {
        class: 'ui-state-highlight'
    },
    INFO: {
        class: 'ui-state-active'
    },
    ERROR: {
        class: 'ui-state-error'
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
    this.reload = function () {
        window.location.reload();
    };

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
    var statusWidgetPane;
    var alertsWidgetPane;
    var activeWindows = true;

    $.blockUI.defaults.message = null;

    $.blockUI.defaults.css = {
        padding: 0,
        margin: 0,
        width: '30%',
        top: '40%',
        left: '35%',
        textAlign: 'center',
        'border-width': '3px'
    };

    $.blockUI.defaults.overlayCSS = {
        opacity: 0.2,
        cursor: 'wait',
        '-moz-border-radius': '5px',
        '-webkit-border-radius': '5px',
        'border-radius': '5px',
        backgroundColor: '#DFEFFC'
    };

    $.ajaxSetup({
        type: 'post',
        dataType: 'json',
        contentType: 'application/json'
    });

    var stringify_aoData = function (aoData) {
        var o = {};
        var modifiers = ['mDataProp_', 'sSearch_', 'iSortCol_', 'bSortable_', 'bRegex_', 'bSearchable_', 'sSortDir_'];
        jQuery.each(aoData, function (idx, obj) {
            if (obj.name) {
                for (var i = 0; i < modifiers.length; i++) {
                    if (obj.name.substring(0, modifiers[i].length) == modifiers[i]) {
                        var index = parseInt(obj.name.substring(modifiers[i].length));
                        var key = 'a' + modifiers[i].substring(0, modifiers[i].length - 1);
                        if (!o[key]) {
                            o[key] = [];
                        }
                        o[key][index] = obj.value;
                        return;
                    }
                }
                o[obj.name] = obj.value;
            }
            else {
                o[idx] = obj;
            }
        });
        return JSON.stringify(o);
    };

    $.extend($.fn.dataTable.defaults, {
        "sUrl": "",
        "bJQueryUI": true,
        "sInfoPostFix": "",
        "sPaginationType": "full_numbers",
        "sDom": '<"data-table-top"<"ui-widget-content">><"data-table-content"t><"data-table-bottom"<"ui-widget-content"rlip>>',
        "fnInitComplete": function () {
            $('.dataTables_scrollBody').jScrollPane({showArrows: false});
        },
        "fnServerData": function (sSource, aoData, fnCallback) {
            $.post(sSource, stringify_aoData(aoData), function (result, b, c) {
                if (result.success) {
                    fnCallback(result.data, b, c);
                } else {
                    wm.ui.unlock(null, result.message, true);
                }
            })
        }});

    var alertTemplate = function (title, message) {
        var e;
        e = ['<div>', '<div class="content">', '<h2>' + title + '</h2>', '<p>' + message + '</p>', '</div>', '<span class="icon"></span>', '<span class="close"></span>', '</div>'].join("");
        return e;
    };

    var messageTemplate = function (title, message) {
        return '<div style="padding: 10px 24px; padding-bottom: 10px">' + message + '</div><div class="closeButton"><a href="#"><div class="wm-icon-close"/></a></div>';
    };

    var statusTemplate = function (title, message) {
        return '<div><div class="content">' + message + '</div></div>';
    };

    var showStatus = function (message, severity, stick) {
        statusWidgetPane.empty();

        if (stick == undefined) {
            stick = false;
        }

        var opts = {
            classes: [ severity.class, "ui-corner-all shadow"],
            template: statusTemplate,
            autoHide: !stick,
            autoHideDelay: 10000
        };
        if (stick) {
            opts = $.extend(opts, {onClick: function () {
            }, onHover: function () {
            }});
        }
        statusWidgetPane.freeow(null, message, opts);
    };

    var clearStatus = function () {
        var freeow = statusWidgetPane.children().data("freeow");
        if (freeow != null) {
            freeow.hide();
        } else {
            statusWidgetPane.empty();
        }
    };

    this.lock = function (element, message) {
        if (element != null && element != undefined) {
            element.block({message: null});
        } else {
            $.blockUI({message: null});
        }
        if (message != null && message != undefined) {
            showStatus(message, STATE.DEFAULT, true);
        }
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
            message: messageTemplate(null, message),
            blockMsgClass: 'ui-corner-all shadow' + (error ? ' ui-state-error' : ' ui-state-default'),
            draggable: false
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
            title: title,
            draggable: false,
            modal: true,
            resizable: false,
            width: 400,
            buttons: [
                {
                    text: wm.i18n.value('button.ok', 'Ok'),
                    click: function () {
                        $(this).dialog("close");
                        approvedAction(true);
                    }
                },
                {
                    text: wm.i18n.value('button.cancel', 'Cancel'),
                    click: function () {
                        $(this).dialog("close");
                        approvedAction(false);
                    }
                }
            ]
        });
    };


    this.notification = function (title, message, type, error) {
        alertsWidgetPane.freeow(title, message, {
            classes: [ error ? "ui-state-error" : "ui-state-highlight", "ui-corner-all", "shadow", type],
            showStyle: {opacity: .95},
            template: alertTemplate,
            autoHideDelay: 10000
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

    this.player = function (player, showLink, showState, showType, waiting) {
        showType = (showType !== undefined) ? showType : true;
        showState = (showState !== undefined) ? showState : true;
        showLink = (showLink !== undefined) ? showLink : true;
        waiting = (waiting !== undefined) ? waiting : false;

        var l = showLink && (player.membership != null);
        var html = '';
        html += '<span class="player';
        html += ' ' + player.type.toLowerCase();

        if (player.robotType != null) {
            html += ' ' + player.robotType.toLowerCase();
        }
        if (player.membership != null) {
            html += ' ' + player.membership.toLowerCase();
        }
        if (waiting) {
            html += ' waiting';
        }
        html += '">';
        if (showState && player.membership != null) {
            html += '<div class="state ' + (player.online ? 'online' : 'offline') + '"></div>';
        }
        if (l) {
            html += '<a href="/playground/profile/view?p=' + player.id + '">';
        }
        html += '<div class="nickname">' + player.nickname + '</div>';

        if (showType) {
            html += '<div class="icon"></div>';
        }

        if (l) {
            html += "</a>";
        }
        html += '</span>';
        return html;
    };

    /*
     this.player = function (player, hideLink) {
     var id = player.id;
     var html = '';

     html += '<span class="player';
     html += ' ' + player.type.toLowerCase();
     if (player.membership != null) {
     html += player.membership.toLowerCase();
     }
     if (player.robotType != null) {
     html += player.robotType.toLowerCase();
     }

     if (player.online) {
     html += '<div class="state online"></div> ';
     }
     if (!hideLink && id >= 1000) {
     html += '<a href="/playground/profile/view?p=' + id + '">';
     }
     html += '<div class="nickname">' + info.nickname + '</div>';
     if (info.membership.toLowerCase() != 'basic') {
     html += ' <div class="membership"></div>';
     }
     if (!hideLink && id > 1000) {
     html += '</a>';
     }
     html += '</span>';
     return html;
     };
     */

    $(document).ready(function () {
        var body = $("body");
        statusWidgetPane = $("<div id='status-widget-pane' class='freeow-widget status-widget-pane'></div>").appendTo(body);
        alertsWidgetPane = $("<div id='alerts-widget-pane' class='freeow-widget alerts-widget-pane'></div>").appendTo(body);

        var $window = $(window);
        var $header = $("#header");
        var windowScroll = function () {
            var height = $header.outerHeight(true);
            var scrollY = $window.scrollTop();
            if (height - scrollY >= 0) {
                statusWidgetPane.css({top: height - scrollY});
                alertsWidgetPane.css({top: height - scrollY});
            } else if (statusWidgetPane.offset().top != 0) {
                statusWidgetPane.css({top: 0});
                alertsWidgetPane.css({top: 0});
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
                value: activeEditor.getValue(),
                view: activeEditor.getDisplayValue()
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
                value: previousValue.value,
                view: previousValue.view
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
                label: $(view).children(".ui-editor-label").text(),
                view: $(view).children(".ui-editor-view").html(),
                value: $(view).children("input").val()
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

        $.each($(view).find('.ui-editor-item'), function (i, v) {
            if (editorsInfo[v.id] != undefined) {
                $(v).click(function () {
                    openEditor(v, createNewEditor(editorsInfo[v.id]));
                });
            }
        });
    };
};

wm.game.help = new function () {
    this.showHelp = function (section, ctx) {
        $('<div><div class="loading-image" style="height: 300px"></div></div>').load(section + '?plain=true').dialog({
            title: ctx != undefined ? $(ctx).text() : '',
            width: 650,
            height: 450,
            modal: true,
            resizable: true,
            buttons: [
                {
                    text: wm.i18n.value('button.close', 'Close'),
                    click: function () {
                        $(this).dialog("close");
                    }
                }
            ]
        });
        return false;
    };
};

wm.game.Proposal = function (widget, language) {
    this.accept = function (id) {
        wm.ui.lock(widget, language['accepting']);
        $.post("/playground/scribble/accept.ajax?p=" + id, function (result) {
            if (result.success) {
                if (result.data == undefined) {
                    wm.util.url.reload();
                } else {
                    wm.util.url.redirect('/playground/scribble/board?b=' + result.data);
                }
            } else {
                wm.ui.unlock(widget, result.message, true);
            }
        });
    };

    this.decline = function (id) {
        wm.ui.lock(widget, language['declining']);
        $.post("/playground/scribble/decline.ajax?p=" + id, function (result) {
            if (result.success) {
                wm.util.url.reload();
            } else {
                wm.ui.unlock(widget, result.message, true);
            }
        });
    };

    this.cancel = function (id) {
        wm.ui.lock(widget, language['cancelling']);
        $.post('/playground/scribble/decline.ajax?p=' + id, function (result) {
            if (result.success) {
                wm.ui.unlock(widget, language['cancelled']);
                wm.util.url.reload();
            } else {
                wm.ui.unlock(widget, result.message, true);
            }
        });
    }
};

wm.game.Create = function (maxOpponents, opponentsCount, playerSearch, language) {
    var attachPlayerSearchActions = function (a) {
        $(a).hover(
            function () {
                $(this).addClass("player-search-remove");
            },
            function () {
                $(this).removeClass("player-search-remove");
            }).click(function () {
                $(this).fadeOut('fast', function () {
                    $(this).remove();
                    if (opponentsCount == maxOpponents) {
                        $("#opponentsControl").fadeIn('slow');
                    }
                    opponentsCount--;
                });
            });
    };

    this.selectOpponent = function () {
        playerSearch.openDialog(insertPlayer, 'player-search-dlg');
        return false;
    };

    var insertPlayer = function (playerInfo) {
        var s = $('<div style="display: none;">' + wm.ui.player(playerInfo, false) + '<input type="hidden" name="opponents" value="' + playerInfo.id + '"/></div>');
        attachPlayerSearchActions(s);
        $("#opponentsList").append(s);
        $("#opponentsList .ui-state-error-text").remove();
        s.fadeIn('fast');
        opponentsCount++;
        if (opponentsCount == maxOpponents) {
            $("#opponentsControl").fadeOut('slow');
        }
    };

    $('#opponentsList div').each(function (i, a) {
        attachPlayerSearchActions(a);
    });

    $("#createTabRobot").change(function () {
        $(".create-form").slideUp();
        $("#robotForm").slideDown();
    });

    $("#createTabWait").change(function () {
        $(".create-form").slideUp();
        $("#waitingForm").slideDown();
    });

    $("#createTabChallenge").change(function () {
        $(".create-form").slideUp();
        $("#challengeForm").slideDown();
    });

    $(".player-search-action").hover(function () {
        $(this).addClass("ui-state-hover");
    }, function () {
        $(this).removeClass("ui-state-hover");
    });

    this.submitForm = function () {
        var gameWidget = $("#createGame");

        wm.ui.lock(gameWidget, language['waiting']);
        var serializeObject = $("#form").serializeObject();
        if (serializeObject.opponents != undefined && !$.isArray(serializeObject.opponents)) {
            serializeObject.opponents = [serializeObject.opponents];
        }
        $.post("create.ajax", JSON.stringify(serializeObject),
            function (response) {
                if (response.success) {
                    if (response.data == null || response.data.board == undefined) {
                        wm.util.url.redirect('/playground/scribble/active');
                    } else {
                        wm.util.url.redirect('/playground/scribble/board?b=' + response.data.board);
                    }
                } else {
                    wm.ui.unlock(gameWidget, response.message, true);
                }
            }, 'json');
    };
};

wm.game.History = function (pid, columns, language) {
    $.each(columns, function (i, a) {
        var index;
        if (a.mDataProp == 'title') {
            a.mRender = function (data, type, row) {
                var id = row.boardId;
                var title = row.title;
                if (id != 0) {
                    return "<a href='/playground/scribble/board?b=" + id + "'>" + title + "</a>";
                } else {
                    return title;
                }
            };
        } else if (a.mDataProp == 'players') {
            a.mRender = function (data, type, row) {
                var res = $.map(data, function (p, i) {
                    if (p.id != pid) {
                        return wm.ui.player(p);
                    }
                    return null;
                });
                return res.join(', ');
            };
        } else if (a.mDataProp == 'scores') {
            a.mRender = function (data, type, row) {
                var hand = $.grep(data, function (n, i) {
                    return row.players[i].id == pid;
                })[0];
                var change = hand.newRating - hand.oldRating;
                var res = '';
                res += '<div class="rating ' + (change < 0 ? 'down' : change == 0 ? 'same' : 'up') + '">';
                res += '<div class="change"><sub>' + (change < 0 ? '' : '+') + change + '</sub></div>';
                res += '&nbsp;' + hand.newRating;
                res += '</div>';
                return res;
            };
        }
    });

    wm.ui.dataTable('#history', {
        "bStateSave": false,
        "bFilter": false,
        "bSortClasses": false,
        "aaSorting": [
            [7, 'desc']
        ],
        "iDisplayStart": 0,
        "aoColumns": columns,
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": "/playground/scribble/history/load.ajax?p=" + pid,
        "oLanguage": language
    });
};

wm.game.Search = function (columns, scriplet, language) {
    var players;
    var callback;

    var search = this;
    var filterForm = $("#filterForm");

    $.each(columns, function (i, a) {
        if (a.mDataProp == 'player') {
            a.mRender = function (data, type, row) {
                return wm.ui.player(data, !scriplet);
            };
        }
    });

    var resultTable = wm.ui.dataTable('#searchResult', {
        "bSortClasses": false,
        "aoColumns": columns,
        "bProcessing": true,
        "bServerSide": true,
        "aaSorting": [
            [ 1, "desc" ],
            [ 2, "desc" ]
        ],
        "sAjaxSource": "/playground/players/load.ajax",
        "fnServerData": function (sSource, aoData, fnCallback) {
            $.fn.dataTable.defaults.fnServerData(sSource + "?" + filterForm.serialize(), aoData, function (res) {
                players = res.aaData;
                fnCallback(res);
            });
        }
    });

    var clearSearch = function () {
        filterForm.find('input').val("");
        filterForm.find('option[value=""]').attr('selected', 'selected');
        reloadContent();
    };

    var reloadContent = function () {
        resultTable.fnDraw();
    };

    this.closeDialog = function () {
        $("#searchPlayerWidget").dialog('close');

    };

    this.openDialog = function (c, clazz) {
        callback = c;
        clearSearch();
        $("#searchPlayerWidget").dialog({
            title: language['title'],
            dialogClass: clazz,
            modal: true,
            width: 800,
            buttons: [
                {
                    text: wm.i18n.value('button.close', 'Close'),
                    click: function () {
                        $(this).dialog("close");
                    }
                }
            ]
        });
        return false;
    };

    if (scriplet) {
        resultTable.find("tbody").click(function (event) {
            var p = $(event.target).closest('tr');
            search.closeDialog();
            var pos = resultTable.fnGetPosition(p.get(0));
            callback(players[pos]['player']);
        });
    }

    $("#searchTypes").buttonset().change(reloadContent);
    $("#searchFilterReset").button().click(clearSearch);
    $("#searchFilterDo").button().click(reloadContent);
    $("#searchFilterApply").button().click(reloadContent);

    $("#playersSearchFilter").keyup(function (e) {
        if (e.which == 13) {
            reloadContent();
        }
    });

    var visible = false;
    $("#playersSearchAdvanced").click(function () {
        $(".advanced-filter").slideToggle();
        visible = !visible;
        if (!visible) {
            clearSearch();
        }
    });
};

wm.game.Dictionary = function (lang, dictionaryManager, i18n) {
    var instance = this;
    var dictionary = $("#dictionary");

    var activeTopLetter;
    var activeSubLetter;

    var activeWordEntries;

    var topAlphabet = dictionary.find("#topAlphabet");
    var topAlphabetLinks = topAlphabet.find("a");

    var subAlphabet = dictionary.find("#subAlphabet");
    var subAlphabetLinks = subAlphabet.find("a");

    var searchInput = dictionary.find("#dictionarySearch");

    var wordsViewPanel = dictionary.find(".scroll-pane");
    var wordsViewTable = wordsViewPanel.find("table");
    var wordsViewStatus = dictionary.find("#wordsCount span");
    var wordsViewScrollPane = wordsViewPanel.jScrollPane({showArrows: false, horizontalGutter: 10, hideFocus: true}).data('jsp');

    var createViewItem = function (entry, index) {
        var def = "";
        if (def != "") {
            def += "<br>";
        }
        var attrs = "";
        $.each(entry.attributes, function (k, a) {
            attrs += " " + i18n[a];
        });

        var desc = $("<td>" + def + "<span class='sample'>" + attrs + "</span> " + entry.definition + "</td>");
        var link = $("<td></td>").append($("<a href='#'></a>").text(entry.word).click(function (e) {
            e.preventDefault();
            dictionaryManager.viewWordEntry(entry, false);
        }));
        return $("<tr></tr>").append(link).append(desc);
    };

    var loadWordEntries = function (prefix) {
        wm.ui.lock(wordsViewPanel, i18n['status.words.loading']);
        wordsViewStatus.text(i18n['status.words.loading']);

        if (prefix.length == 0) {
            wm.ui.unlock(wordsViewPanel);
        } else {
            $.post("/playground/dictionary/loadWordEntries.ajax?l=" + lang + "&p=" + prefix, null, function (response) {
                if (response.success) {
                    activeWordEntries = response.data;
                    wordsViewStatus.text(activeWordEntries.length);

                    $.each(activeWordEntries, function (index, entry) {
                        entry.language = lang;
                        wordsViewTable.append(createViewItem(entry, index));
                    });
                    wordsViewScrollPane.reinitialise();
                    wm.ui.unlock(wordsViewPanel);
                } else {
                    wm.ui.unlock(wordsViewPanel, response.message, true);
                }
            });
        }
    };

    var selectTopLetter = function (el) {
        var letter = el != null ? el.attr('href').substring(1) : '';
        if (letter != activeTopLetter) {
            activeTopLetter = letter;
            topAlphabet.find("a").removeClass("active-letter");
            subAlphabet.find("div").hide();
            dictionary.find("#subAlphabet" + letter).show();
            if (el != null) {
                el.addClass("active-letter");
            }
        }
        return letter;
    };

    var selectSubLetter = function (el) {
        var letter = el != null ? el.attr('href').substring(1) : '';
        if (letter != activeSubLetter) {
            activeSubLetter = letter;
            subAlphabet.find("a").removeClass("active-letter");
            if (el != null) {
                el.addClass("active-letter");
            }
        }
        return letter;
    };

    var clearTable = function () {
        wordsViewTable.empty();
        wordsViewStatus.text(0);
    };

    var initAlphabets = function (value) {
        if (value.length > 0) {
            var letter = value.charAt(0).toUpperCase();
            var el = topAlphabet.find('a[href$="#' + letter + '"]');
            if (el.length != 0) {
                selectTopLetter(el);

                if (value.length > 1) {
                    var subLetter = letter.toLowerCase() + value.charAt(1).toLowerCase();
                    selectSubLetter(subAlphabet.find('a[href$="#' + subLetter + '"]'));
                } else {
                    selectSubLetter(null);
                }
            } else {
                selectSubLetter(null);
                selectTopLetter(null);
            }
        } else {
            selectSubLetter(null);
            selectTopLetter(null);
        }
    };

    topAlphabetLinks.click(function (e) {
        clearTable();

        var a = selectTopLetter($(this)).toLowerCase();
        searchInput.val(a);
        window.location.hash = "#" + a;
        e.preventDefault();
    });

    subAlphabetLinks.click(function (e) {
        clearTable();
        var a = selectSubLetter($(this));
        searchInput.val(a);
        window.location.hash = "#" + a;
        loadWordEntries(a);
        e.preventDefault();
    });

    searchInput.bind("input propertychange", function (evt) {
        if (window.event && event.type == "propertychange" && event.propertyName != "value")
            return;
        clearTable();

        var input = $(this);
        var value = input.val();

        initAlphabets(value);
        window.location.hash = "#" + value;

        if (value.length >= 2) {
            window.clearTimeout(input.data("timeout"));
            input.data("timeout", setTimeout(function () {
                loadWordEntries(value);
            }, 800));
        }
    });

    var initByHash = function () {
        var hash = window.location.hash.substring(1);
        initAlphabets(hash);
        searchInput.val(hash);
        if (hash.length >= 2) {
            loadWordEntries(hash);
        }
    };

    var initSuggestions = function () {
        $.post('/playground/dictionary/loadRecentReclaims.ajax?l=' + lang, function (result) {
            function initSuggestionsView(sugggestions, view) {
                if (sugggestions.length > 0) {
                    $.each(sugggestions, function (i, e) {
                        e.language = lang;
                        var link = $('<a href="#"></a>').text(e.word);
                        link.click(function () {
                            dictionaryManager.viewWordEntry(e, true);
                        });
                        view.append(link);
                        if (i != sugggestions.length - 1) {
                            view.append(', ');
                        }
                    });
                    view.parent().show();
                }
            }

            if (result.success) {
                var added = [];
                var removed = [];
                var updated = [];
                $.each(result.data, function (i, e) {
                    if (e.resolutionType == "CREATE") {
                        added.push(e);
                    } else if (e.resolutionType == "REMOVE") {
                        removed.push(e);
                    } else if (e.resolutionType == "UPDATE") {
                        updated.push(e);
                    }
                });

                initSuggestionsView(added, $("#recentlyAdded").find("div"));
                initSuggestionsView(removed, $("#recentlyRemoved").find("div"));
                initSuggestionsView(updated, $("#recentlyUpdated").find("div"));
            } else {
                wm.ui.unlock(null, result.message, true);
            }
        });
    };

    initByHash();
    initSuggestions();

    $("#addNewWord").button({icons: {primary: 'ui-icon-circle-plus'}}).click(function () {
        dictionaryManager.createWordEntry('', lang);
    });
};

wm.game.dict.DictionaryManager = function (readOnly, i18n) {
    var instance = this;
    var globalReadOnly = readOnly;
    var wordEntryEditor = $("#wordEntryEditor");
    var wordEntryAction = wordEntryEditor.find("#action");

    var isAction = function (action) {
        return wordEntryAction.val() == action;
    };

    var setAction = function (action) {
        wordEntryAction.val(action);

        var title = wordEntryEditor.dialog('option', 'title');
        if ("UPDATE" == action) {
            title = i18n['title.edit'];
        } else if ("VIEW" == action) {
            title = i18n['title.view'];
        } else if ("CREATE" == action) {
            title = i18n['title.add'];
        } else if ("MODIFY" == action) {
            title = i18n['title.modify'];
        }
        wordEntryEditor.dialog('option', 'title', title);
    };

    var sendRequest = function (processor) {
        var v = wordEntryEditor.parent();
        wm.ui.lock(v);
        var serializeObject = wordEntryEditor.find("form").serializeObject();
        $.post("/playground/dictionary/" + processor + ".ajax", JSON.stringify(serializeObject), function (response) {
            if (response.success) {
                wm.ui.unlock(v, i18n['waiting'], false);
                wordEntryEditor.dialog("close");
            } else {
                wm.ui.unlock(v, response.message, true);
            }
        });
    };

    var startEditing = function (action) {
        setAction(action);
        wordEntryEditor.find(".view").hide();
        wordEntryEditor.find(".edit").show();
        wordEntryEditor.find(".warn").show();
        $("#wordEditorRemoveBtn").hide();
        $("#wordEditorChangeBtn").find("span").text(i18n['save']);
    };

    var resetEntryEditor = function () {
        if (isAction("CREATE")) {
            $("#wordEditorRemoveBtn").show();
            wordEntryEditor.find(".create").toggle();
        }
        setAction("VIEW");
        wordEntryEditor.find(".view").show();
        wordEntryEditor.find(".edit").hide();
        wordEntryEditor.find(".warn").hide();

        wordEntryEditor.find(".word-view").text("");
        wordEntryEditor.find(".word-input").val("");
        wordEntryEditor.find(".language-view").text("");
        wordEntryEditor.find(".language-input").val("");
        wordEntryEditor.find(".definition-view").text("");
        wordEntryEditor.find(".definition-input").val("");
        $("input[name=attributes]").prop('checked', false);
        wordEntryEditor.find(".attributes-view").text("");
    };

    this.createWordEntry = function (word, lang) {
        instance.viewWordEntry({word: word, language: lang});

        wordEntryEditor.find(".create").toggle();
        $("#wordEditorChangeBtn").find("span").text(i18n['add']);
        startEditing("CREATE");
    };

    this.viewWordEntry = function (entry, readOnly) {
        readOnly = (readOnly == undefined ? false : readOnly);
        var buttons = [];
        if (!globalReadOnly && !readOnly) {
            buttons.push({
                id: 'wordEditorChangeBtn',
                text: i18n['edit'],
                click: function () {
                    if (isAction("VIEW")) {
                        startEditing("UPDATE");
                    } else if (isAction("MODIFY")) {
                        sendRequest("update");
                    } else {
                        sendRequest("suggest");
                    }
                }
            });

            buttons.push({
                id: 'wordEditorRemoveBtn',
                text: i18n['remove'],
                click: function () {
                    wm.ui.confirm(i18n['remove.title'], i18n['remove.confirm'], function (approve) {
                        if (approve) {
                            setAction("REMOVE");
                            sendRequest("suggest");
                        }
                    });
                }
            });
        }

        buttons.push({
            id: 'wordEditorCancelBtn',
            text: wm.i18n.value('button.cancel', 'Cancel'),
            click: function () {
                $(this).dialog("close");
            }
        });

        var dialog = wordEntryEditor.dialog({
            title: i18n['title.view'],
            dialogClass: 'word-editor-dlg',
            draggable: false,
            modal: true,
            autoOpen: false,
            resizable: false,
            width: 700,
            buttons: buttons
        });

        resetEntryEditor();

        if (entry != null) {
            wordEntryEditor.find("input[name=id]").val(entry.id);
            wordEntryEditor.find(".word-view").text(entry.word);
            wordEntryEditor.find(".word-input").val(entry.word);
            wordEntryEditor.find(".language-view").text(wm.i18n.value('language.' + entry.language.toLowerCase()));
            wordEntryEditor.find(".language-input").val(entry.language);
            if (entry.definition != null && entry.definition != undefined) {
                wordEntryEditor.find(".definition-view").html(entry.definition.replace(/\n/g, '<br />'));
                wordEntryEditor.find(".definition-input").val(entry.definition);

                var attrs = "";
                $.each(entry.attributes, function (i, v) {
                    attrs += " " + i18n[v];
                    $("#" + v).prop('checked', true);
                });
                wordEntryEditor.find(".attributes-view").text(attrs);
            }
        }
        dialog.dialog("open");
    };

    this.getWordEntry = function (word, lang, callback) {
        $.post("/playground/dictionary/loadWordEntry.ajax?l=" + lang + "&w=" + word, function (response) {
            var entry = response.data;
            if (response.success) {
                if (entry != null) {
                    entry.language = lang;
                }
                callback(entry, null);
            } else {
                callback(null, response.message);
            }
        });
    };

    this.editWordEntry = function (entry) {
        instance.viewWordEntry(entry);
        startEditing("MODIFY");
    };
};

wm.game.tourney.Subscription = function (announce, subscribed, subscriptions, language) {
    var subscriptionView = $("#subscriptionView");
    var subscriptionForm = $("#subscriptionForm");
    var subscriptionDialog = $("#subscriptionDialog");
    var subscriptionDetails = $("#subscriptionDetails");

    var subscribe = function (comp, lang, section, callback) {
        var data = JSON.stringify({language: lang, section: section});
        wm.ui.lock(comp, language["register.subscribing"]);
        $.post("/playground/tourney/changeSubscription.ajax?t=" + announce, data,
            function (response) {
                if (response.success) {
                    subscriptions = response.data;
                    wm.ui.unlock(comp, language["register.subscribed"]);
                } else {
                    wm.ui.unlock(comp, response.message, true);
                }
                updateAnnounceView(true);
                callback(response.success);
            }, 'json');
    };

    var unsubscribe = function (comp, callback) {
        wm.ui.lock(comp, language["register.unsubscribing"]);
        $.post("/playground/tourney/changeSubscription.ajax?t=" + announce, JSON.stringify({}),
            function (response) {
                if (response.success) {
                    subscriptions = response.data;
                    wm.ui.unlock(comp, language["register.unsubscribed"]);
                } else {
                    wm.ui.unlock(comp, response.message, true);
                }
                updateAnnounceView(false);
                callback(response.success);
            }, 'json');
    };

    var updateAnnounceView = function (sub) {
        subscribed = sub;

        var announceAction = subscriptionView.find('button .ui-button-text');
        var announceSection = subscriptionView.find("#announceSection");
        var announceLanguage = subscriptionView.find("#announceLanguage");

        if (subscribed) {
            subscriptionView.find(".tourney-state").removeClass("ui-state-disabled");

            announceAction.text(language["register.refuse"]);
            announceSection.text(getSelectedValue('section', false));
            announceLanguage.text(getSelectedValue('language', false));
        } else {
            subscriptionView.find(".tourney-state").addClass("ui-state-disabled");
            announceAction.text(language["register.accept"]);
            announceSection.text(language["register.unspecified"]);
            announceLanguage.text(language["register.unspecified"]);
        }

        var tsum = 0;
        $.each(subscriptions, function (l, ss) {
            var sum = 0;
            $.each(ss, function (s, v) {
                sum += v;
                subscriptionView.find(".subscriptionDetails" + l + s).text(v);
            });
            subscriptionView.find(".subscriptionDetails" + l).text(sum);
            tsum += sum;
        });
    };

    var getSelectedValue = function (input, value) {
        var el;
        if (input == 'language') {
            el = subscriptionForm.find("select[name=language] option:selected");
        } else if (input == 'section') {
            el = subscriptionForm.find("input[name=section]:checked");
        }

        if (value) {
            return el.val();
        } else {
            if (input == 'section') {
                el = $(subscriptionForm.find("#subscriptionSectionLabel" + el.val() + " span").get(0));
            }
            return el.text();
        }
    };

    var showSubscriptionDialog = function () {
        subscriptionDialog.dialog({
            id: "jQueryDialog",
            title: language["register.title"],
            width: 550,
            minHeight: 350,
            modal: true,
            resizable: false,
            buttons: [
                {
                    class: "tourney-unsubscribed",
                    text: language["register.button"],
                    click: function () {
                        subscribe(subscriptionDialog.closest(".ui-dialog"),
                            getSelectedValue('language', true),
                            getSelectedValue('section', true),
                            function () {
                                subscriptionDialog.dialog("close");
                            });
                    }
                },
                {
                    text: wm.i18n.value('button.cancel', 'Cancel'),
                    click: function () {
                        subscriptionDialog.dialog("close");
                    }

                }
            ]
        });
    };

    subscriptionForm.find("#subscriptionLanguage").change(function () {
        var language = getSelectedValue('language', true);
        var find = subscriptionForm.find("#subscriptionSection .players");
        $.each(find, function (i, v) {
            v = $(v);
            v.text(subscriptions[language][v.attr('id').substring(26)]);
        });
    });

    subscriptionView.find('button').click(function () {
        if (subscribed) {
            unsubscribe(subscriptionView, function () {
            });
        } else {
            showSubscriptionDialog();
        }
    });
};

wm.game.settings.Board = function () {
    var prevSet = $(".tiles-set-prev");
    var nextSet = $(".tiles-set-next");
    var tileSetView = $(".tiles-set-view");

    var selected = 0;
    var tilesSet = ['tiles-set-classic', 'tiles-set-classic2'];

    $.each(tilesSet, function (i, v) {
        if (tileSetView.hasClass(v)) {
            selected = i;
        }
        return false;
    });

    var checkButtons = function () {
        if (selected == 0) {
            prevSet.attr('disabled', 'disabled');
        } else {
            prevSet.removeAttr('disabled');
        }

        if (selected == tilesSet.length - 1) {
            nextSet.attr('disabled', 'disabled');
        } else {
            nextSet.removeAttr('disabled');
        }
    };

    var changeTilesView = function (value) {
        $("#tilesClass").val(tilesSet[selected + value]);

        tileSetView.removeClass(tilesSet[selected]);
        tileSetView.addClass(tilesSet[selected + value]);

        selected = selected + value;
    };

    $(".tiles-set-nav").hover(
        function () {
            if ($(this).attr('disabled') == undefined) {
                $(this).removeClass('ui-state-default').addClass('ui-state-hover');
            }
        },
        function () {
            if ($(this).attr('disabled') == undefined) {
                $(this).removeClass('ui-state-hover').addClass('ui-state-default');
            }
        });

    prevSet.click(function () {
        if (selected > 0) {
            changeTilesView(-1);
            checkButtons();
            prevSet.removeClass('ui-state-hover').addClass('ui-state-default');
        }
        return false;
    });

    nextSet.click(function () {
        if (selected < tilesSet.length - 1) {
            changeTilesView(1);
            checkButtons();
            nextSet.removeClass('ui-state-hover').addClass('ui-state-default');
        }
        return false;
    });
    checkButtons();
};

wm.scribble.tile = new function () {
    var updateTileImage = function (tileWidget) {
        var k = 0;
        if (tileWidget.selected) {
            k += 22;
        }
        if (tileWidget.pinned) {
            k += 44;
        }
        var tile = $(tileWidget).data('tile');
        tileWidget.style.backgroundPosition = tile.cost * -22 + "px" + " -" + k + "px";
    };

    this.createTileWidget = function (tile) {
        return $("<div></div>").addClass("tile cost" + tile.cost).css('background-position', '-' + tile.cost * 22 + 'px 0').append($("<span></span>").html(tile.letter.toUpperCase())).data('tile', tile);
    };

    this.selectTile = function (tileWidget) {
        $(tileWidget).addClass("tile-selected").get(0).selected = true;
        updateTileImage(tileWidget);
    };

    this.deselectTile = function (tileWidget) {
        $(tileWidget).removeClass("tile-selected").get(0).selected = false;
        updateTileImage(tileWidget);
    };

    this.pinTile = function (tileWidget) {
        $(tileWidget).get(0).pinned = true;
        updateTileImage(tileWidget);
    };

    this.isTileSelected = function (tileWidget) {
        return $(tileWidget).get(0).selected === true;
    };

    this.getLetter = function (tileWidget) {
        return $(tileWidget).data('tile').letter;
    };

    this.setLetter = function (tileWidget, letter) {
        var v = $(tileWidget);
        v.data('tile').letter = letter;
        return v.children().get(0).innerText = letter.toUpperCase();
    };

    this.isTilePined = function (tileWidget) {
        return tileWidget != null && tileWidget != undefined && $(tileWidget).get(0).pinned === true;
    };
};

wm.scribble.AjaxController = function () {
    var encodeData = function (data) {
        if (data != null && data != undefined) {
            return JSON.stringify(data);
        }
        return data;
    };

    var getWidgetUrl = function (widget, type) {
        var url;
        if (widget == 'board') {
            url = '/playground/scribble/board';
        } else if (widget == 'memory') {
            url = '/playground/scribble/memory';
        } else if (widget == 'comments') {
            url = '/playground/scribble/comment';
        }
        return url + '/' + type + '.ajax';
    };

    this.execute = function (widget, type, params, data, callback) {
        var url = getWidgetUrl(widget, type);
        if (params != null && params != undefined) {
            url += "?" + params;
        }
        $.post(url, encodeData(data), callback);
    };
};

wm.scribble.Comments = function (board, controller, language) {
    var loadedComments = 0;
    var unreadComments = 0;
    var comments = [];

    var widget = board.getPlayboardElement('.annotation');
    var view = widget.find('.items');

    var editor = widget.find('.editor');
    var editorError = editor.find('.ui-state-error-text');

    var status = widget.find('.status');
    var newCount = widget.find('.new .value');

    var initWidget = function () {
        wm.ui.lock(widget, language['loading']);
        editor.find("button").button();
        editor.find("textarea").change(function () {
            showEditorError(null);
        });
        loadStatuses();
    };

    var changeUnreadMessages = function (diff) {
        unreadComments = unreadComments + diff;
        newCount.text(unreadComments);
        if (unreadComments != 0) {
            newCount.parent().fadeIn('slow');
        } else {
            newCount.parent().fadeOut('slow');
        }
    };

    var loadStatuses = function () {
        wm.ui.lock(widget);
        controller.execute('comments', 'load', 'b=' + board.getBoardId(), null, function (result) {
            if (result.success) {
                comments = result.data;

                var count = 0;
                var historyCount = 0;
                var unreadLoaded = true;
                $.each(result.data, function (i, a) {
                    if (!a.read && unreadLoaded) {
                        count++;
                    } else {
                        if (historyCount < 3) {
                            count++;
                        }
                        unreadLoaded = false;
                        historyCount++;
                    }
                });

                widget.find('.header .controls div').toggle();
                if (comments.length != 0) {
                    widget.find('.content').show();
                    updateStatus();
                    loadComments(count);
                } else {
                    wm.ui.unlock(widget);
                }
                controller.execute('comments', 'mark', 'b=' + board.getBoardId());
            } else {
                wm.ui.unlock(widget, result.message, true);
            }
        });
    };

    var loadComments = function (count) {
        if (loadedComments + count > comments.length) {
            count = comments.length - loadedComments;
        }

        if (count <= 0) {
            return false;
        }

        var read = [];
        var visible = [];
        for (var i = loadedComments; i < loadedComments + count; i++) {
            visible.push(comments[i].id);
            read['c' + comments[i].id] = comments[i].read;
        }

        wm.ui.lock(widget, language['loading']);
        controller.execute('comments', 'get', 'b=' + board.getBoardId(), visible, function (result) {
            if (result.success) {
                $.each(result.data, function (i, a) {
                    showComment(a, read['c' + a.id], false);
                });
                loadedComments += count;
                updateStatus();
                wm.ui.unlock(widget);
            } else {
                wm.ui.unlock(widget, result.message, true);
            }
        });
        return true;
    };

    var updateStatus = function () {
        if (comments.length == 0) {
            status.find('.controls').hide();
        } else {
            var c = getNextLoadCount();
            if (c == 0) {
                status.find('.controls').hide();
            } else {
                status.find('.controls span').html(c);
            }
            status.find('.progress').html("1.." + loadedComments + " " + language['of'] + " " + comments.length);
        }
    };

    var getNextLoadCount = function () {
        var c = comments.length - loadedComments;
        if (c > 5) {
            return 5;
        }
        return c;
    };

    var clearEditor = function () {
        editor.slideUp('fast');
        showEditorError(null);
        editor.find("textarea").val('');
    };

    var showEditorError = function (msg) {
        if (msg == undefined || msg == null) {
            editorError.html('');
        } else {
            editorError.html(msg);
        }
    };

    var registerItemControls = function (item) {
        item.hoverIntent({
            interval: 300,
            over: function () {
                $(this).find(".info").slideDown('fast');
                $(this).toggleClass("collapsed");
            },
            out: function () {
                $(this).find(".info").slideUp('fast');
                $(this).toggleClass("collapsed");
            }
        });
    };

    var showComment = function (comment, collapsed, top) {
        var item = $('<div class="item' + (collapsed ? ' collapsed' : '') + '"></div>');
        var info = $('<div class="info"></div>').appendTo(item);
        var time = $('<div class="time"></div>').appendTo(info).html('(' + comment.elapsed + ' ' + language['ago'] + ')');
        var player = $('<div class="sender"></div>').appendTo(info).html(wm.ui.player(board.getPlayer(comment.person).info, false, false, false));

        var msg = $('<div class="message"></div>').appendTo(item).html(comment.text + "<span></span>");

        if (collapsed) {
            registerItemControls(item);
        } else {
            changeUnreadMessages(1);
            item.click(function () {
                item.find(".info").slideUp('fast');
                item.addClass("collapsed");
                changeUnreadMessages(-1);
                registerItemControls(item.unbind('click', arguments.callee));
            });
        }

        if (top) {
            if (view.children().length != 0) {
                view.prepend($('<div class="separator ui-widget-content"></div>'));
            }
            view.prepend(item);
        } else {
            if (view.children().length != 0) {
                view.append($('<div class="separator ui-widget-content"></div>'));
            }
            view.append(item);
        }
    };

    this.create = function () {
        editor.slideDown('fast');
    };

    this.save = function () {
        var val = editor.find('textarea').val();
        if (val.trim().length == 0) {
            showEditorError(language['empty']);
            return false;
        }
        wm.ui.lock(widget, language['saving']);
        $.post('/playground/scribble/comment/add.ajax?b=' + board.getBoardId(), val, function (result) {
            if (result.success) {
                loadedComments += 1;
                comments.unshift({id: result.data.id, read: true});
                showComment(result.data, true, true);
                clearEditor();
                updateStatus();
                widget.find('.content').show();
                wm.ui.unlock(widget, language['saved']);
            } else {
                showEditorError();
                wm.ui.unlock(widget, result.message);
            }
        });
    };

    this.cancel = function () {
        clearEditor();
    };

    this.load = function () {
        loadComments(getNextLoadCount());
    };

    this.getMonitoringBean = function () {
        var processLoadedComments = function (response) {
            var a = response.data.commentChanges;
            if (a != null && a != undefined) {
                var b = a.comments;
                if (b != undefined && b.length > 0) {
                    $.each(b.reverse(), function (i, a) {
                        comments.unshift({id: a.id, read: false});
                        showComment(a, false, true);
                    });
                    loadedComments += b.length;
                    if (loadedComments == b.length) {
                        widget.find('.content').show();
                        updateStatus();
                    }
                    $.post('/playground/scribble/comment/mark.ajax?b=' + board.getBoardId());
                }
            }
        };

        return new function () {
            this.getParameters = function () {
                return "c=" + comments.length;
            };

            this.getCallback = function () {
                return processLoadedComments;
            };
        };
    };

    board.bind('resolution', function (event, oldResolution, newResolution) {
        if (newResolution != null) {
            widget.find('.create-comment').remove();
        }
    });

    $(document).ready(function () {
        initWidget();
    });
};

wm.scribble.Memory = function (board, controller, clearMemory, language) {
    var nextWordId = 0;
    var memoryWords = [];
    var memoryWordsCount = 0;

    var memoryWordWidget = board.getPlayboardElement('.memoryWordsWidget');
    var memoryWordTable = memoryWordWidget.find('.memoryWords');
    var addWordButton = memoryWordWidget.find('.memoryAddButton');
    var clearWordButton = memoryWordWidget.find('.memoryClearButton');

    var addWord = function (word, checkPlacement) {
        var valid = isWordValid(word, checkPlacement);
        if (!valid && clearMemory) {
            executeRequest('remove', word, function (data) {
            });
        } else {
            memoryWordsCount++;
            var id = nextWordId++;
            memoryWords[id] = word;
            memoryTable.fnAddData(createNewRecord(id, word, valid));
            clearWordButton.button(memoryWordsCount == 0 ? "disable" : "enable");
            board.clearSelection();
        }
    };

    var getWord = function (id) {
        return memoryWords[id];
    };

    var removeWord = function (id) {
        var word = memoryWords[id];
        if (word != null && word != undefined) {
            memoryWordsCount--;
            memoryWords[id] = null;

            var row = memoryWordTable.find('.memory-word-' + id).closest('tr').get(0);
            memoryTable.fnDeleteRow(memoryTable.fnGetPosition(row));

            clearWordButton.button(memoryWordsCount == 0 ? "disable" : "enable");
        }
    };

    var isWordValid = function (word, checkPlacement) {
        var valid = board.checkWord(word);
        if (valid && checkPlacement) {
            valid = !board.isWordPlaced(word);
        }
        return valid;
    };

    var createNewRecord = function (id, word, valid) {
        var scoreEngine = board.getScoreEngine();

        var text = word.text;
        var points = scoreEngine.getWordPoints(word).points.toString();

        var e = '<div class="memory-controls memory-word-' + id + '">';
        if (!valid) {
            e += '<span></span>';
            text = "<del>" + text + "</del>";
            points = "<del>" + points + "</del>";
        } else {
            e += '<a class="icon-memory-select" href="javascript: memoryWords.select(' + id + ')"></a>';
        }
        e += '<a class="icon-memory-remove" href="javascript: memoryWords.remove(' + id + ')"></a>';
        e += '</div>';
        return [text, points, e];
    };

    var executeRequest = function (type, data, successHandler) {
        wm.ui.lock(memoryWordWidget, language['changeMemory']);
        controller.execute('memory', type, 'b=' + board.getBoardId(), data, function (result) {
            if (result.success) {
                successHandler(result.data);
                wm.ui.unlock(memoryWordWidget);
            } else {
                wm.ui.unlock(memoryWordWidget, result.message, true);
            }
        });
    };

    var reloadMemoryWords = function () {
        memoryTable.fnClearTable();
        executeRequest('load', null, function (data) {
            $.each(data, function (i, word) {
                addWord(word, true);
            });
        });
    };

    var validateWords = function () {
        $.each(memoryWords, function (id, word) {
            if (word != null && word != undefined) {
                var valid = isWordValid(word, true);
                if (!valid && clearMemory) {
                    removeWord(id);
                    executeRequest('remove', word, function (data) {
                    });
                } else {
                    var row = memoryWordTable.find('.memory-word-' + id).closest('tr').get(0);
                    memoryTable.fnUpdate(createNewRecord(id, word, valid), memoryTable.fnGetPosition(row), 0);
                }
            }
        });
    };

    this.select = function (id) {
        var word = getWord(id);
        if (word != null && word != undefined) {
            board.selectWord(word);
        }
    };

    this.remove = function (id) {
        var word = getWord(id);
        if (word != null && word != undefined) {
            executeRequest('remove', word, function (data) {
                removeWord(id);
            });
        }
    };

    this.clear = function () {
        executeRequest('clear', null, function (data) {
            memoryTable.fnClearTable();
        });
    };

    this.remember = function () {
        var word = board.getSelectedWord();
        if (word != null || word != undefined) {
            executeRequest('add', word, function (data) {
                addWord(word, false);
            });
        }
    };


    var memoryTable = wm.ui.dataTable(memoryWordTable, {
        "bFilter": false,
        "bSort": true,
        "bSortClasses": true,
        "sDom": 't',
        "aaSorting": [
            [1, 'desc']
        ],
        "aoColumns": [
            null,
            { "sClass": "center"},
            { "bSortable": false }
        ],
        "oLanguage": language
    });

    var processWordSelected = function (event, word) {
        addWordButton.button(word == null ? "disable" : "enable");
    };

    var processGameFinished = function (event, oldResolution, newResolution) {
        memoryWordWidget.remove();
        board.unbind('selectedWord', processWordSelected);
    };

    var processNewMoves = function (event, moves) {
        validateWords();
    };

    addWordButton.button({disabled: true, icons: {primary: 'icon-memory-add'}}).click(this.remember);
    clearWordButton.button({disabled: true, icons: {primary: 'icon-memory-clear'}}).click(this.clear);

    board.bind('selectedWord', processWordSelected)
        .bind('resolution', processGameFinished)
        .bind('gameMoves', processNewMoves);

    $(document).ready(function () {
        reloadMemoryWords();
    });
};

wm.scribble.Selection = function (board) {
    var wordInfoElement = board.getPlayboardElement('.selectedWordInfo');
    var wordCostElement = board.getPlayboardElement('.selectedWordCost');
    var selectedTilesElement = board.getPlayboardElement('.selectedTilesInfo');

    var selectedWordInfo = wordInfoElement.text();
    var selectedWordCost = wordCostElement.text();
    var selectedTilesInfo = selectedTilesElement.text();

    board.bind("selectedTiles",
        function (event, tile, selected) {
            var tiles = selectedTilesElement.find('div');
            var length = board.getSelectedTiles().length;
            if (selected && length == 1) {
                selectedTilesElement.empty();
            }
            if (selected) {
                wm.scribble.tile.createTileWidget(tile).offset({left: ((length - 1) * 22), top: 0}).appendTo(selectedTilesElement);
            } else {
                var updateOffset = false;
                $.each(tiles, function (i, tileWidget) {
                    var v = $(tileWidget);
                    if (v.data('tile').number == tile.number) {
                        updateOffset = true;
                        v.remove();
                    } else if (updateOffset) {
                        v.css('left', (i - 1) * 22);
                    }
                });
            }
            if (length == 0) {
                selectedTilesElement.text(selectedTilesInfo);
            }
        })
        .bind('selectedWord',
        function (event, word) {
            if (word != null) {
                wordCostElement.empty().text(board.getScoreEngine().getWordPoints(word).formula);
                wordInfoElement.empty();
                $.each(word.tiles, function (i, t) {
                    wm.scribble.tile.createTileWidget(t).offset({left: (i * 22), top: 0}).appendTo(wordInfoElement);
                });
            } else {
                wordInfoElement.text(selectedWordInfo);
                wordCostElement.text(selectedWordCost);
            }
        });
};

wm.scribble.Dictionary = function (board, dictionaryManager, checkWords) {
    var instance = this;
    var checkTimer = undefined;
    var input = board.getPlayboardElement('.word-value');
    var controlButton = board.getPlayboardElement('.word-control');

    var wordEntry = undefined;
    var activeAction;

    const CHECK_WORD_TIMEOUT = 2000;

    var startAutoChecker = function () {
        stopAutoChecker();
        var val = input.val();
        checkTimer = $(instance).oneTime(CHECK_WORD_TIMEOUT, 'checkWord', function () {
            if (isAutoChecker()) {
                validateWord(val);
            }
        });
    };

    var stopAutoChecker = function () {
        if (isAutoChecker()) {
            $(instance).stopTime('checkWord');
            checkTimer = undefined;
        }
    };

    var isAutoChecker = function () {
        return (checkTimer != undefined);
    };

    var changeControlButton = function (icon) {
        activeAction = icon;
        controlButton.button("option", {
            icons: { primary: icon }
        });
        return controlButton;
    };

    var validateWord = function (text) {
        stopAutoChecker();
        if (text.length == 0) {
            return;
        }

        var parent = input.parent();
        wm.ui.lock(parent);
        dictionaryManager.getWordEntry(text, board.getLanguage(), function (we, message) {
            if (isAutoChecker()) {
                return;
            }

            wordEntry = we;
            if (wordEntry != null) {
                input.addClass('ui-state-valid');
                changeControlButton('ui-icon-script');
            } else {
                input.addClass('ui-state-error');
                changeControlButton('ui-icon-pencil');
            }
            wm.ui.unlock(parent);
        });
    };

    var validateInputValue = function () {
        input.removeClass('ui-state-error ui-state-valid');
        changeControlButton('ui-icon-search');
        if (input.val().length != 0) {
            controlButton.button('enable');
            if (checkWords) {
                startAutoChecker();
            }
        } else {
            controlButton.button('disable').removeClass("ui-state-hover");
            if (checkWords) {
                stopAutoChecker();
            }
        }
    };

    var checkWord = function () {
        validateWord(input.val());
    };

    board.bind('selectedWord',
        function (event, word) {
            if (word != null) {
                input.val(word.text);
            } else {
                input.val('');
            }
            validateInputValue();
        });

    input.keyup(function (e) {
        if (e.which == 13) {
            checkWord();
            e.preventDefault();
        } else {
            validateInputValue();
        }
    });

    controlButton.button({text: false}).click(function (e) {
        if (activeAction == 'ui-icon-search') {
            checkWord();
        } else if (activeAction == 'ui-icon-pencil') {
            dictionaryManager.createWordEntry(input.val(), board.getLanguage());
        } else if (activeAction == 'ui-icon-script') {
            dictionaryManager.viewWordEntry(wordEntry, false);
        }
        e.preventDefault();
    });
    changeControlButton('ui-icon-search');
};

wm.scribble.BankInfo = function (board, language) {
    var bankDialog = null;
    var colsCount = 8;

    var initBankDialog = function () {
        var rows = 0;
        var cols = 0;
        var tilesPanel = $(".tiles-bank .tiles");
        $.each(board.getBankTilesInfo(), function (i, bti) {
            var row = Math.floor(i / colsCount);
            var col = (i - row * colsCount);
            rows = Math.max(rows, row + 1);
            cols = Math.max(cols, col + 1);
            var t = wm.scribble.tile.createTileWidget({number: bti.count, letter: bti.letter, cost: bti.cost}).offset({top: row * 22, left: col * 22});
            t.hover(
                function () {
                    showTileInfo(bti);
                    wm.scribble.tile.selectTile(this);
                },
                function () {
                    wm.scribble.tile.deselectTile(this);
                }).click(
                function () {
                    selectActiveTile(bti);
                }).appendTo(tilesPanel);
        });
        tilesPanel.width(cols * 22).height(rows * 22);

        bankDialog = board.getPlayboardElement(".tiles-bank").dialog({
            title: language['title'],
            width: 500,
            height: 'auto',
            resizable: false,
            autoOpen: false,
            buttons: [
                {
                    text: wm.i18n.value('button.cancel', 'Cancel'),
                    click: function () {
                        $(this).dialog("close");
                    }
                }
            ]
        });
    };

    var showTileInfo = function (tile) {
        var activeBoardTiles = getBoardTiles(tile);

        $(".tiles-bank .tileView").empty().append(wm.scribble.tile.createTileWidget({number: 0, letter: tile.letter, cost: tile.cost}).offset({left: 0, top: 0}));
        $(".tiles-bank .tileCost").text(tile.cost);
        $(".tiles-bank .totalCount").text(tile.count);
        $(".tiles-bank .boardCount").text(activeBoardTiles.length);
    };

    var getBoardTiles = function (tile) {
        var res = [];
        for (var i = 0; i < 15; i++) {
            for (var j = 0; j < 15; j++) {
                var bt = board.getBoardTile(i, j);
                if (bt != null) {
                    if ((bt.wildcard && tile.wildcard) || (!bt.wildcard && bt.letter == tile.letter)) {
                        res.push(bt);
                    }
                }
            }
        }
        return res;
    };

    var selectActiveTile = function (tile) {
        if (tile != null) {
            board.selectLetters(tile.letter);
            bankDialog.dialog("close");
        }
    };

    this.showBankInfo = function () {
        if (bankDialog == null) {
            initBankDialog();
        }
        bankDialog.dialog('open');
    };
};

wm.scribble.History = function (board, language) {
    var insert = function (moves) {
        var rows = [];
        $.each(moves, function (i, move) {
            var link = '';
            if (move.type == 'MAKE') {
                var word = move.word;
                link = '<span class="moveMade">' + word.text + '</span>';
            } else if (move.type == 'EXCHANGE') {
                link = '<span class="moveExchange">' + language['exchange'] + '</span>';
            } else if (move.type == 'PASS') {
                link = '<span class="movePassed">' + language['passed'] + '</span>';
            }
            rows.push([1 + move.number, board.getPlayer(move.player).info.nickname, link, move.points]);
        });
        movesHistoryTable.fnAddData(rows, false);
        movesHistoryTable.fnDraw(true);

        var closest = board.getPlayboardElement('.movesHistory .dataTables_scrollBody');
        if (closest.length != 0) {
            closest.data('jsp').reinitialise();
        }
    };

    var movesHistoryTable = wm.ui.dataTable('.movesHistory table', {
        "bSort": true,
        "bSortClasses": false,
        "aaSorting": [
            [0, 'desc']
        ],
        "bAutoWidth": false,
        "bPaginate": false,
        "bInfo": false,
        "bFilter": false,
        "sScrollY": 235,
        "sScrollX": "100%",
        "bStateSave": true,
        "sDom": 't',
        "oLanguage": language
    });


    movesHistoryTable.click(function (e) {
        var pos = movesHistoryTable.fnGetPosition($(event.target).closest('tr')[0]);
        var index = movesHistoryTable.fnGetData(pos)[0] - 1;
        board.selectMove(index);
    });

    $(document).ready(function () {
        insert(board.getGameMoves());

        board.bind('gameMoves', function (event, newMoves) {
            insert(newMoves);
        });
    });
};

wm.scribble.Controls = function (board, language) {
    var widget = board.getPlayboardElement(".playboard");
    var toolbarElement = board.getPlayboardElement(".board-controls");

    var markTurnButton = board.getPlayboardElement(".makeTurnButton");
    var clearSelectionButton = board.getPlayboardElement(".clearSelectionButton");
    var selectTileButton = board.getPlayboardElement(".selectTileButton");
    var exchangeTilesButton = board.getPlayboardElement(".exchangeTilesButton");
    var passTurnButton = board.getPlayboardElement(".passTurnButton");
    var resignGameButton = board.getPlayboardElement(".resignGameButton");

    markTurnButton.button({disabled: true, icons: {primary: 'icon-controls-make'}});
    clearSelectionButton.button({disabled: true, icons: {primary: 'icon-controls-clear'}});
    selectTileButton.button({disabled: false, icons: {primary: 'icon-controls-highlight'}});
    exchangeTilesButton.button({disabled: true, icons: {primary: 'icon-controls-exchange'}});
    passTurnButton.button({disabled: true, icons: {primary: 'icon-controls-pass'}});
    resignGameButton.button({disabled: true, icons: {primary: 'icon-controls-resign'}});

    var onTileSelected = function () {
        if (wm.scribble.tile.isTileSelected(this)) {
            wm.scribble.tile.deselectTile(this);
        } else {
            wm.scribble.tile.selectTile(this);
        }
    };

    var showMoveResult = function (success, message, error) {
        if (success) {
            wm.ui.unlock(null, language['acceptedDescription']);
        } else {
            wm.ui.message(null, message + (error != null ? error : ''), true);
        }
    };

    var lockBoard = function () {
        wm.ui.lock(widget, language['updatingBoard']);
    };

    var unlockBoard = function () {
        updateControlsState();

        wm.ui.unlock(widget);
    };

    var updateSelectionState = function () {
        clearSelectionButton.removeClass("ui-state-hover").button(board.getSelectedTiles().length == 0 ? "disable" : "enable");
    };

    var updateControlsState = function () {
        toolbarElement.find('button').removeClass("ui-state-hover");

        updateSelectionState();

        markTurnButton.button(board.isBoardActive() && board.isPlayerActive() && board.getSelectedWord() != null ? "enable" : "disable");
        passTurnButton.button(board.isBoardActive() && board.isPlayerActive() ? "enable" : "disable");
        exchangeTilesButton.button(board.isBoardActive() && board.isPlayerActive() ? "enable" : "disable");
        resignGameButton.button(board.isBoardActive() ? "enable" : "disable");
    };

    var updateGameState = function (type) {
        var playerTurn = board.getPlayer(board.getPlayerTurn());
        if (type === 'playerTurn') {
            updateControlsState();

            if (board.isPlayerActive()) {
                wm.ui.notification(language['updatedLabel'], language['updatedYour'], 'your-turn');
            } else {
                wm.ui.notification(language['updatedLabel'], language['updatedOther'] + ' <b>' + playerTurn.info.nickname + '</b>.', 'opponent-turn');
            }
        } else if (type === 'resolution') {
            toolbarElement.hide();
            toolbarElement.find('button').button({disabled: true});
            var msg;
            var opts = {autoHide: false};
            if (board.getResolution() == 'RESIGNED') {
                msg = language['finishedInterrupted'] + " <b>" + playerTurn.info.nickname + "</b>.";
            } else {
                var winners = board.getWonPlayers();
                if (winners == undefined || winners.length == 0) {
                    msg = language['finishedDrew'];
                } else {
                    msg = language['finishedWon'];
                    $.each(winners, function (i, p) {
                        if (i != 0) {
                            msg += ", ";
                        }
                        msg += " <b>" + p.nickname + "</b>";
                    });
                }
            }
            wm.ui.notification(language['finishedLabel'], msg + "<div class='closeInfo'>" + language['clickToClose'] + "</div>", 'game-finished', opts);
        }
    };

    this.makeTurn = function () {
        board.makeTurn(showMoveResult);
    };

    this.passTurn = function () {
        wm.ui.confirm(language['pass'], language['passDescription'], function (approved) {
            if (approved) {
                board.passTurn(showMoveResult);
            }
        });
    };

    this.resignGame = function () {
        wm.ui.confirm(language['resignLabel'], language['resignDescription'], function (approved) {
            if (approved) {
                board.resign(showMoveResult);
            }
        });
    };

    this.exchangeTiles = function () {
        board.clearSelection();
        var tilesPanel = $($('.exchangeTilesPanel div').get(1)).empty();
        $.each(board.getHandTiles(), function (i, tile) {
            wm.scribble.tile.createTileWidget(tile).offset({top: 0, left: i * 22}).click(onTileSelected).appendTo(tilesPanel);
        });

        $('.exchangeTilesPanel').dialog({
                title: language['exchange'],
                draggable: false,
                modal: true,
                resizable: false,
                width: 400,
                buttons: [
                    {
                        text: language['exchange'],
                        click: function () {
                            $(this).dialog("close");
                            var tiles = [];
                            $.each(tilesPanel.children(), function (i, tw) {
                                if (wm.scribble.tile.isTileSelected(tw)) {
                                    tiles.push($(tw).data('tile'));
                                }
                            });
                            board.exchangeTiles(tiles, showMoveResult);
                        }
                    },
                    {
                        text: language['cancel'],
                        click: function () {
                            $(this).dialog("close");
                        }
                    }
                ]
            }
        )
    };

    board
        .bind("selectedTiles", function (event, tile, selected) {
            updateSelectionState();
        })
        .bind('selectedWord', function (event, word) {
            updateControlsState();
        })
        .bind('playerTurn', function (event, oldPlayerTurn, newPlayerTurn) {
            updateGameState('playerTurn');
        })
        .bind('resolution', function (event, oldResolution, newResolution) {
            updateGameState('resolution');
        })
        .bind('locked', function (event, locked) {
            if (locked) {
                lockBoard();
            } else {
                unlockBoard();
            }
        });

    updateControlsState();
};

wm.scribble.Players = function (board) {
    var playersInfo = board.getPlayboardElement('.playersInfo');

    var getPlayerInfoCells = function (pid, name) {
        return playersInfo.find(".player-info-" + pid + " " + name);
    };

    var showPlayerTimeout = function (pid, time) {
        updatePlayerInfo(pid, time);
    };

    var hideActiveMarker = function () {
        playersInfo.find(".player-info td").removeClass("ui-state-active");
    };

    var showActiveMarker = function (pid) {
        hideActiveMarker();
        playersInfo.find(".player-info .info").text("");
        getPlayerInfoCells(pid, "td").addClass("ui-state-active");
    };

    var showWinnerMarker = function (pid) {
        getPlayerInfoCells(pid, "td").addClass("ui-state-highlight");
    };

    var showPlayerRating = function (pid, oldRating, newRating) {
        var iconClass;
        var ratingDelta = newRating - oldRating;
        if (ratingDelta == 0) {
            ratingDelta = '+' + ratingDelta;
            iconClass = "same";
        } else if (ratingDelta > 0) {
            ratingDelta = '+' + ratingDelta;
            iconClass = "up";
        } else {
            ratingDelta = '' + ratingDelta;
            iconClass = "down";
        }
        updatePlayerInfo(pid, "<div class='rating " + iconClass + "'><div class='change'><sub>" + ratingDelta + "</sub></div><div class='value'>" + newRating + "</div></div>");
    };

    var showChallengeButton = function () {
        playersInfo.find('.createChallenge').show();
    };

    var updatePlayerPoints = function (pid, points) {
        getPlayerInfoCells(pid, ".points").text(points);
    };

    var updatePlayerInfo = function (pid, info) {
        getPlayerInfoCells(pid, ".info").html(info);
    };

    var updateBoardState = function () {
        $.each(board.getPlayers(), function (i, p) {
            updatePlayerPoints(p.id, p.score.points);
        });

        if (board.isBoardActive()) {
            showActiveMarker(board.getPlayerTurn());
            showPlayerTimeout(board.getPlayerTurn(), board.getRemainedTime().text);
        } else {
            hideActiveMarker();
            if (!board.isReadOnly()) {
                showChallengeButton();
            }
            $.each(board.getPlayers(), function (i, p) {
                if (p.score.winner) {
                    showWinnerMarker(p.id);
                }
                showPlayerRating(p.id, p.score.oldRating, p.score.newRating);
            });
        }
    };

    var initPlayersView = function () {
        var table = playersInfo.find("tbody");
        $.each(board.getPlayers(), function (i, p) {
            var row = '';
            row += '<tr class="player-info-' + p.id + ' player-info">';
            row += '<td width="24px" height="24px" class="winner-icon ui-corner-left ui-table-left"><div></div></td>';
            row += '<td class="nickname ui-table-middle">' + wm.ui.player(p.info) + '</td>';
            row += '<td width="20px" class="points ui-table-middle">' + p.score.points + '</td>';
            row += '<td width="60px" class="info ui-corner-right ui-table-right"></td>';
            row += '</tr>';
            table.append(row);
        });
    };

    $(".createChallenge button").click(function () {
        wm.util.url.redirect('/playground/scribble/create?t=board&p=' + board.getBoardId());
    });

    board.bind('validated', function () {
        updateBoardState();
    });

    initPlayersView();
    updateBoardState();
};

wm.scribble.Settings = function (board, language) {
    this.showSettings = function () {
        var dlg = $('<div><div class="loading-image" style="height: 200px"></div></div>');
        dlg.load('/playground/scribble/settings/load');
        dlg.dialog({
            title: language['title'],
            width: 550,
            minHeight: 'auto',
            modal: true,
            resizable: false,
            buttons: [
                {
                    text: language['apply'],
                    click: function () {
                        wm.ui.lock(null, language['waiting']);
                        $.post("/playground/scribble/settings/save.ajax", JSON.stringify($("#boardSettingsForm").serializeObject()),
                            function (response) {
                                if (response.success) {
                                    wm.util.url.reload();
                                } else {
                                    wm.ui.unlock(null, response.message, true);
                                }
                            }, 'json');

                    }
                },
                {
                    text: wm.i18n.value('button.cancel', 'Cancel'),
                    click: function () {
                        dlg.dialog("close");
                    }

                }
            ]
        });
    }
};

wm.scribble.ScoreEngine = function (gameBonuses, board) {
    var emptyHandBonus = 33;

    var bonuses = wm.util.createMatrix(15);
    $.each(gameBonuses, function (i, bonus) {
        bonuses[bonus.column][bonus.row] = bonus.type;
        bonuses[bonus.column][14 - bonus.row] = bonus.type;
        bonuses[14 - bonus.column][bonus.row] = bonus.type;
        bonuses[14 - bonus.column][14 - bonus.row] = bonus.type;
    });

    this.getCellBonus = function (row, col) {
        return bonuses[row][col];
    };

    this.getWordPoints = function (word) {
        var points = 0;
        var pointsRaw = 0;
        var pointsMult = 1;

        var formula = '';
        var formulaRaw = '';
        var formulaMult = '';

        $.each(word.tiles, function (i, tile) {
            var row = word.position.row + (word.direction == 'VERTICAL' ? i : 0 );
            var column = word.position.column + (word.direction == 'VERTICAL' ? 0 : i );
            var bonus = bonuses[column][row];
            if ((bonus == null || bonus == undefined) || board.isBoardTile(column, row)) {
                bonus = 1;
            } else {
                switch (bonus) {
                    case 'L2':
                        bonus = 2;
                        break;
                    case 'L3':
                        bonus = 3;
                        break;
                    case 'W2':
                        bonus = 1;
                        pointsMult *= 2;
                        formulaMult += '*2';
                        break;
                    case 'W3':
                        bonus = 1;
                        pointsMult *= 3;
                        formulaMult += '*3';
                        break;
                }
            }
            pointsRaw += tile.cost * bonus;
            if (formulaRaw.length != 0) {
                formulaRaw += '+';
            }
            formulaRaw += tile.cost;
            if (bonus != 1) {
                formulaRaw += '*' + bonus;
            }
        });

        if (formulaMult.length != 0) {
            formula = '(' + formulaRaw + ')' + formulaMult;
        } else {
            formula = formulaRaw;
        }
        points = pointsRaw * pointsMult;
        formula += '=' + points;

        return {points: points, formula: formula};
    };
};

wm.scribble.Progress = function (board, language) {
    var updateProgressBar = function () {
        var count = board.getBankCapacity();
        var bo = board.getBoardTilesCount(), ha = board.getHandTilesCount(), ba = board.getBankTilesCount();
        var p3 = Math.round(100 * ha / count), p2 = Math.round(100 * ba / count), p1 = 100 - p3 - p2;

        var boardWidget = board.getPlayboardElement(".game-progress .game-progress-board").css('width', p1 + '%');
        var bankWidget = board.getPlayboardElement(".game-progress .game-progress-bank").css('width', p2 + '%');
        var handWidget = board.getPlayboardElement(".game-progress .game-progress-hand").css('width', p3 + '%');
        board.getPlayboardElement(".game-progress .game-progress-caption").text(bo + ' / ' + ba + ' / ' + ha);

        if (p1 < 2) {
            boardWidget.hide();
            bankWidget.addClass("ui-corner-left");
        } else {
            boardWidget.show();
            bankWidget.removeClass("ui-corner-left");
        }
        if (p3 < 2) {
            handWidget.hide();
            bankWidget.addClass("ui-corner-right");
        }
    };

    var markAsFinished = function () {
        board.getPlayboardElement(".gameFinishedTime").html(board.getFinishedTime().text);

        var cap = board.getPlayboardElement(".gameResolution .game-progress-caption");
        var desc = board.getPlayboardElement(".gameResolution .game-resolution-player");
        var playerTurn = board.getPlayerTurn();

        var resolution = null;
        switch (board.getResolution()) {
            case 'INTERRUPTED':
                resolution = 'resolution.for';
                break;
            case 'STALEMATE':
                resolution = 'resolution.moves';
                break;
            case 'RESIGNED':
                resolution = 'resolution.by';
                break;
        }

        cap.text(language['resolution.' + board.getResolution().toLowerCase()]);
        if (playerTurn == null) {
            desc.text(language[resolution]);
        } else {
            desc.text(language[resolution] + ' ' + board.getPlayer(playerTurn).info.nickname);
        }
    };

    board.bind('validated',
        function (event, type) {
            board.getPlayboardElement(".spentTime").html(board.getSpentTime().text);
        }).bind('resolution',
        function (event, resolution) {
            markAsFinished();
            $(".state-change-marker").toggle();
        }).bind('gameMoves',
        function (event, moves) {
            updateProgressBar();
        });

    if (!board.isBoardActive()) {
        markAsFinished();
    } else {
        updateProgressBar();
    }
};

wm.scribble.Board = function (board, viewer, wildcardHandlerElement, controller, settings) {
    var playboard = this;

    var id = board.id;
    var language = board.settings.language;

    var bank = board.bank;
    var moves = board.moves;
    var players = board.players;

    var locked = false;
    var enabled = true;
    var handTiles = new Array(7);
    var boardTiles = wm.util.createMatrix(15);

    var draggingTile = null;

    var selectedWord = null;
    var selectedTileWidgets = [];

    var wildcardSelectionDialog = null;

    var scribbleEl = $("<div></div>").addClass('scribble');
    var backgroundEl = $("<div></div>").addClass('background').appendTo(scribbleEl);
    var fieldEl = $("<div></div>").addClass('field').appendTo(scribbleEl);
    var bonusesEl = $("<div></div>").addClass('bonuses').appendTo($(backgroundEl));
    var handEl = $("<div></div>").addClass('hand').appendTo($(fieldEl));
    var boardEl = $("<div></div>").addClass('board').appendTo($(fieldEl));
    var colorEl = $("<div></div>").addClass('color').appendTo(backgroundEl);
    var gridEl = $("<div></div>").addClass('grid').appendTo(backgroundEl);

    var highlighting = new function () {
        var element = $('<div></div>').addClass('highlighting tile').hide().appendTo(fieldEl);
        var previousCell = null;

        var updatePosition = function (cell) {
            var offset = cell.container.offset();
            element.offset({left: offset.left + cell.x * 22, top: offset.top + cell.y * 22});
        };

        this.start = function (tileWidget, cell) {
            element.css('backgroundPosition', -tileWidget.data('tile').cost * 22 + "px -88px");
            if (cell != null) {
                element.show();
                updatePosition(cell);
            }
            previousCell = cell;
        };

        this.stop = function () {
            element.hide();
            element.offset({top: 0, left: 0});
            previousCell = null;
        };

        this.highlight = function (cell) {
            if (cell != null) {
                if (previousCell == null) {
                    element.show();
                    updatePosition(cell);
                } else {
                    if (previousCell.x != cell.x || previousCell.y != cell.y) {
                        updatePosition(cell);
                    }
                }
            } else {
                if (previousCell != null) {
                    element.hide();
                    element.offset({top: 0, left: 0});
                }
            }
            previousCell = cell;
        };
    };

    var scoreEngine = new wm.scribble.ScoreEngine(board.bonuses, this);

    var initializeGame = function () {
        if (settings.showCaptions == undefined || settings.showCaptions) {
            var gameBorder = $("<div></div>").addClass('border ui-corner-all').appendTo(scribbleEl);
            for (var b = 0; b < 15; b++) {
                var z = wm.i18n.value('board.captions', 'ABCDEFGHIJKLMNO');
                var vv = z.charAt(b);
                var vh = '' + (b + 1);
                var pv = 18 + 22 * b;
                var ph = 12 + 22 * b;
                gameBorder.append("<span class='v' style='right: 1px; top: " + pv + "px;'>" + vv + "</span>");
                gameBorder.append("<span class='v' style='left: 0; top: " + pv + "px;'>" + vv + "</span>");
                gameBorder.append("<span class='h' style='top: -1px; left: " + ph + "px;'>" + vh + "</span>");
                gameBorder.append("<span class='h' style='bottom: 2px; left: " + ph + "px;'>" + vh + "</span>");
            }
        }

        for (var i = 0; i < 15; i++) {
            for (var j = 0; j < 15; j++) {
                var bonus = scoreEngine.getCellBonus(i, j);
                if (bonus != undefined) {
                    var text = wm.i18n.value(bonus, bonus.toUpperCase());
                    $("<div></div>").addClass('cell bonus-cell').addClass('bonus-cell-' + bonus.toLowerCase()).text(text).offset({left: j * 22, top: i * 22}).appendTo(bonusesEl);
                }
            }
        }
        if (scoreEngine.getCellBonus(7, 7) == undefined) {
            $("<div></div>").addClass('cell').addClass('bonus-cell-center').offset({left: 7 * 22, top: 7 * 22}).appendTo(bonusesEl);
        }

        $.each(moves, function (i, move) {
            registerBoardMove(move, true);
        });

        var playerInfo = playboard.getPlayer(viewer);
        if (playerInfo != null && board.handTiles != null) {
            $("<div></div>").addClass('hand').appendTo(backgroundEl);
            validateHandTile(board.handTiles);
        }

        $(document).bind("mouseup touchend touchcancel", onTileUp);
        $(document).bind("mousemove touchmove", onTileMove);
        $(scribbleEl).bind("mousedown", onBoardClick);
    };

    var onTileSelected = function () {
        if (!enabled) {
            return;
        }
        if (wm.scribble.tile.isTileSelected(this)) {
            changeTileSelection(this, false, true);
        } else {
            changeTileSelection(this, true, true);
        }
    };

    var onTileDown = function (ev) {
        if (!enabled) {
            return;
        }
        draggingTile = $(this);
        var offset = draggingTile.offset();
        var pointer = getPointerPosition(ev);
        var relatedCell = getRelatedCell(ev, {left: 0, top: 0});
        draggingTile.data('mouseOffset', {left: pointer.x - offset.left, top: pointer.y - offset.top});
        draggingTile.data('originalState', {offset: offset, cell: relatedCell, zIndex: draggingTile.css('zIndex')});
        draggingTile.css('zIndex', 333);
        highlighting.start(draggingTile, relatedCell);
        ev.preventDefault();
    };

    var onTileMove = function (ev) {
        if (!enabled) {
            return;
        }
        if (draggingTile != null && draggingTile != undefined) {
            var pointer = getPointerPosition(ev);
            var tileOffset = draggingTile.data('mouseOffset');
            var relatedCell = getRelatedCell(ev, {left: tileOffset.left - 5, top: tileOffset.top - 5});
            draggingTile.offset({left: pointer.x - tileOffset.left, top: pointer.y - tileOffset.top});
            highlighting.highlight(relatedCell);
            ev.preventDefault();
        }
    };

    var onTileUp = function (ev) {
        if (!enabled) {
            return;
        }
        if (draggingTile == null || draggingTile == undefined) {
            return;
        }

        var tileOffset = draggingTile.data('mouseOffset');
        var originalState = draggingTile.data('originalState');
        var relatedCell = getRelatedCell(ev, {left: tileOffset.left - 5, top: tileOffset.top - 5});
        if (relatedCell == null ||
            (relatedCell.container == boardEl && boardTiles[relatedCell.x][relatedCell.y] != undefined) ||
            (relatedCell.container == handEl && handTiles[relatedCell.x] != undefined)) {
            draggingTile.offset(originalState.offset);
        } else {
            // clear original position
            var originalCell = originalState.cell;
            if (originalCell.container == boardEl) {
                boardTiles[originalCell.x][originalCell.y] = null;
            } else if (originalCell.container == handEl) {
                handTiles[originalCell.x] = null;
            }

            // move to new position
            if (originalCell.container != relatedCell.container) {
                draggingTile.detach();
                draggingTile.css('top', relatedCell.y * 22).css('left', relatedCell.x * 22);
                draggingTile.appendTo(relatedCell.container);
            } else {
                draggingTile.css('top', relatedCell.y * 22).css('left', relatedCell.x * 22);
            }

            var tile = draggingTile.data('tile');
            if (relatedCell.container == boardEl) {
                if (tile.wildcard) {
                    var replacingTile = draggingTile;
                    wildcardHandler(tile, function (letter) {
                        wm.scribble.tile.setLetter(replacingTile, letter);
                        tile.row = relatedCell.y;
                        tile.column = relatedCell.x;
                        boardTiles[relatedCell.x][relatedCell.y] = replacingTile;
                        changeTileSelection(replacingTile.get(0), true, true);
                    });
                } else {
                    tile.row = relatedCell.y;
                    tile.column = relatedCell.x;
                    boardTiles[relatedCell.x][relatedCell.y] = draggingTile;
                    changeTileSelection(draggingTile.get(0), true, true);
                }
            } else if (relatedCell.container == handEl) {
                if (tile.wildcard) {
                    wm.scribble.tile.setLetter(draggingTile, '*');
                }
                handTiles[relatedCell.x] = draggingTile;
                changeTileSelection(draggingTile.get(0), false, true);
            }
        }

        highlighting.stop();

        draggingTile.css('zIndex', originalState.zIndex);
        draggingTile.removeData('mouseOffset');
        draggingTile.removeData('originalState');

        draggingTile = null;

        ev.preventDefault();
    };

    var onBoardClick = function (ev) {
        if (!enabled || !settings.clearByClick) {
            return;
        }
        var relatedCell = getRelatedCell(ev, {left: 0, top: 0});

        if (relatedCell == null) {
            return;
        }
        if (relatedCell.container != boardEl || relatedCell.x < 0 || relatedCell.y < 0 || relatedCell.x > 14 || relatedCell.y > 14) {
            return;
        }
        if (boardTiles[relatedCell.x][relatedCell.y] != undefined) {
            return;
        }
        clearSelectionImpl();
        ev.preventDefault();
    };

    var getRelatedCell = function (ev, tileOffset) {
        var bo = boardEl.offset();
        var pointer = getPointerPosition(ev);
        var x = (((pointer.x - bo.left - tileOffset.left) / 22) | 0);
        var y = (((pointer.y - bo.top - tileOffset.top) / 22) | 0);
        if (x >= 0 && x <= 14 && y >= 0 && y <= 14) {
            return {x: x, y: y, container: boardEl};
        }

        var ho = handEl.offset();
        x = (((pointer.x - ho.left - tileOffset.left) / 22) | 0);
        y = (((pointer.y - ho.top - tileOffset.top) / 22) | 0);
        if (x >= 0 && x <= 6 && y == 0) {
            return {x: x, y: y, container: handEl};
        }
        return null;
    };

    var getPointerPosition = function (e) {
        var out = {x: 0, y: 0};
        if (e.type == 'touchstart' || e.type == 'touchmove' || e.type == 'touchend' || e.type == 'touchcancel') {
            var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
            out.x = touch.pageX;
            out.y = touch.pageY;
        } else if (e.type == 'mousedown' || e.type == 'mouseup' || e.type == 'mousemove' || e.type == 'mouseover' || e.type == 'mouseout' || e.type == 'mouseenter' || e.type == 'mouseleave') {
            out.x = e.pageX;
            out.y = e.pageY;
        }
        return out;
    };

    var changeTileSelection = function (tileWidget, select, notifyWord) {
        var tile = $(tileWidget).data('tile');
        if (wm.scribble.tile.isTileSelected(tileWidget) != select) {
            if (select) {
                wm.scribble.tile.selectTile(tileWidget);
                selectedTileWidgets.push(tileWidget);
                scribbleEl.trigger('selectedTiles', [tile, true]);
            } else {
                wm.scribble.tile.deselectTile(tileWidget);
                for (var i = 0, len = selectedTileWidgets.length; i < len; i++) {
                    if (selectedTileWidgets[i] == tileWidget) {
                        selectedTileWidgets.splice(i, 1);
                        break;
                    }
                }
                scribbleEl.trigger('selectedTiles', [tile, false]);
            }
        }
        if (notifyWord) {
            changeSelectedWord(playboard.getSelectedWord());
        }
    };

    var addHandTile = function (tile) {
        $.each(handTiles, function (i, handTile) {
            if (handTile == null || handTile == undefined) {
                handTiles[i] = wm.scribble.tile.createTileWidget(tile).
                    offset({top: 0, left: i * 22}).bind("mousedown touchstart", onTileDown).appendTo(handEl);
                return false;
            }
        });
    };

    var getHandTileIndex = function (tile) {
        var res = null;
        $.each(handTiles, function (i, handTile) {
            if (handTile != null && handTile != undefined && handTile.data('tile').number == tile.number) {
                res = i;
                return false;
            }
        });
        return res;
    };

    var removeHandTile = function (tile) {
        $.each(handTiles, function (i, handTile) {
            if (handTile != null && handTile != undefined && handTile.data('tile').number == tile.number) {
                handTile.remove();
                handTiles[i] = null;
                return false;
            }
        });
    };

    var validateHandTile = function (tiles) {
        var handTiles = playboard.getHandTiles();
        $.each(handTiles, function (i, tile) {
            if (tile != null && tile != undefined) {
                if (!isTileInList(tile, tiles)) {
                    removeHandTile(tile);
                }
            }
        });

        $.each(tiles, function (i, tile) {
            if (!isTileInList(tile, handTiles)) {
                addHandTile(tile);
            }
        });
    };

    var registerBoardMove = function (move, init) {
        if (!init && move.number < moves.length) {
            return;
        }

        var movesCount = moves.length;
        if (move.type == 'MAKE') {
            $.each(move.word.tiles, function (i, tile) {
                tile.row = move.word.position.row + (move.word.direction == 'VERTICAL' ? i : 0 );
                tile.column = move.word.position.column + (move.word.direction == 'VERTICAL' ? 0 : i );

                removeHandTile(tile); // remove tile from the hand if it's hand tile

                if (boardTiles[tile.column][tile.row] == null || boardTiles[tile.column][tile.row] == undefined) {
                    var w = wm.scribble.tile.createTileWidget(tile).
                        offset({top: tile.row * 22, left: tile.column * 22}).click(onTileSelected);
                    wm.scribble.tile.pinTile(w.get(0));
                    boardTiles[tile.column][tile.row] = w.appendTo(boardEl);
                }
            });
        }

        if (!init) {
            moves.push(move);
            scribbleEl.trigger('gameMoves', [
                [move],
                moves
            ]);
        }
    };

    var isTileInList = function (tile, tiles) {
        for (var i = 0, count = tiles.length; i < count; i++) {
            var listTile = tiles[i];
            if (tile != null && tile != undefined && listTile != null && listTile != undefined && listTile.number == tile.number) {
                return true;
            }
        }
        return false;
    };

    var setBoardEnabled = function (enabled, locked) {
        if (playboard.enabled !== enabled) {
            playboard.enabled = enabled;
            scribbleEl.trigger('enabled', [enabled]);
        }

        if (playboard.locked !== locked) {
            playboard.locked = locked;
            scribbleEl.trigger('locked', [locked]);
        }
    };

    var isWordSelected = function (word) {
        if (word == selectedWord) {
            return true;
        }

        if (word != null && selectedWord != null) {
            if (word.text == selectedWord.text &&
                word.position.row == selectedWord.position.row &&
                word.position.column == selectedWord.position.column &&
                word.direction == selectedWord.direction) {
                return true;
            }
        }
        return false;
    };

    var changeSelectedWord = function (word) {
        if (!isWordSelected(word)) {
            if (word == null) {
                scribbleEl.trigger('selectedWord', null);
            } else {
                scribbleEl.trigger('selectedWord', word);
            }
        }
        selectedWord = word;
    };

    var clearSelectionImpl = function () {
        if (selectedTileWidgets.length == 0) {
            return;
        }
        while (selectedTileWidgets.length != 0) {
            var widget = selectedTileWidgets[0];
            if (!wm.scribble.tile.isTilePined(widget)) {
                $.each(handTiles, function (i, hv) {
                    if (hv == undefined || hv == null) {
                        handTiles[i] = $(widget).detach().css('top', 0).css('left', i * 22).appendTo(handEl);
                        return false;
                    }
                });
                var tile = $(widget).data('tile');
                if (tile.wildcard) {
                    wm.scribble.tile.setLetter(widget, '*');
                }
                changeTileSelection(widget, false, false);
                boardTiles[tile.column][tile.row] = null;
            } else {
                changeTileSelection(widget, false, false);
            }
        }
        changeSelectedWord(null);
    };

    var makeMove = function (type, data, handler) {
        setBoardEnabled(false, true);
        sendServerRequest(type, data, function (status, message, errorThrown) {
            setBoardEnabled(true, false);
            handler.call(this, status, message, errorThrown);
        });
    };

    var sendServerRequest = function (type, data, resultHandler) {
        controller.execute('board', type, 'b=' + id + '&m=' + moves.length, data, function (response) {
            processServerResponse(response);
            resultHandler.call(playboard, response.success, response.message);
        });
    };

    var updatePlayerScores = function (score) {
        if (score != null && score != undefined) {
            $.each(score, function (i, s) {
                players[i].score = s;
            });
        }
    };


    var updateHandTiles = function (tiles) {
        if (tiles != null && tiles != undefined) {
            var ownerInfo = playboard.getPlayer(viewer);
            if (ownerInfo != null && ownerInfo != undefined && tiles != null && tiles != undefined) {
                validateHandTile(tiles);
            }
        }
    };

    var updateGameState = function (state) {
        var oldResolution = board.resolution;
        var oldPlayerTurn = board.playerTurn;
        board = $.extend(false, {}, board, state);

        if (board.resolution != null && board.resolution != oldResolution) {
            setBoardEnabled(false, false);
            clearSelectionImpl();
            scribbleEl.trigger('resolution', [oldResolution, board.resolution]);
        }

        if (board.playerTurn != oldPlayerTurn) {
            scribbleEl.trigger('playerTurn', [oldPlayerTurn, board.playerTurn]);
        }
    };

    var updateMovesInfo = function (moves) {
        if (moves != undefined && moves.length > 0) {
            clearSelectionImpl();

            var lastMove = null;
            $.each(moves, function (i, move) {
                lastMove = move;
                registerBoardMove(move, false);
            });

            if (lastMove != null && lastMove.word != null && lastMove.word != undefined) {
                playboard.selectWord(lastMove.word);
            }
        }
    };

    var processServerResponse = function (response) {
        if (!response.success) {
            return;
        }

        if (response.data != null && response.data != undefined) {
            updatePlayerScores(response.data.score);
            updateHandTiles(response.data.handTiles);
            updateMovesInfo(response.data.moves);
            updateGameState(response.data.status);

            scribbleEl.trigger('validated');
        }
    };

    var wordIterator = function (word, handler) {
        var rowK = 0, columnK = 0;
        var row = word.position.row;
        var column = word.position.column;
        if (word.direction == 'VERTICAL') {
            rowK = 1;
        } else {
            columnK = 1;
        }
        for (var i = 0, count = word.tiles.length; i < count; i++) {
            var res = handler(word.tiles[i], row, column, i, word.text[i]);
            if (res === false) {
                return false;
            }
            row += rowK;
            column += columnK;
        }
        return true;
    };

    var wildcardHandler = function (tile, replacer) {
        if (wildcardSelectionDialog == null) {
            var tileLetter = tile.letter;
            wildcardSelectionDialog = $('#' + wildcardHandlerElement).dialog({
                autoOpen: false,
                draggable: false,
                modal: true,
                resizable: false,
                width: 400,
                close: function (event, ui) {
                    wildcardSelectionDialog.replacer(tileLetter);
                }
            });

            var panel = $($('#' + wildcardHandlerElement + ' div').get(1)).empty();
            $.each(playboard.getBankTilesInfo(), function (i, bti) {
                var row = Math.floor(i / 15);
                var col = (i - row * 15);
                var t = wm.scribble.tile.createTileWidget({number: 0, letter: bti.letter, cost: 0}).offset({top: row * 22, left: col * 22});
                t.hover(
                    function () {
                        wm.scribble.tile.selectTile(this);
                    },
                    function () {
                        wm.scribble.tile.deselectTile(this);
                    }).click(
                    function () {
                        tileLetter = $(this).data('tile').letter;
                        wildcardSelectionDialog.dialog("close");
                    }).appendTo(panel);
            });
        }
        wildcardSelectionDialog.replacer = replacer;
        wildcardSelectionDialog.dialog("open");
    };

    this.getBoardId = function () {
        return id;
    };

    this.getBoardElement = function () {
        return scribbleEl.get(0);
    };

    this.getStartedTime = function () {
        return board.startedTime;
    };

    this.getFinishedTime = function () {
        return board.finishedTime;
    };

    this.getSpentTime = function () {
        return board.spentTime;
    };

    this.getRemainedTime = function () {
        return board.remainedTime;
    };

    this.getResolution = function () {
        return board.resolution;
    };

    this.getPlayboardElement = function (selector) {
        return $("#board" + id + " " + selector);
    };

    this.bind = function (event, handler) {
        return scribbleEl.bind(event, handler);
    };

    this.unbind = function (event, handler) {
        return scribbleEl.unbind(event, handler);
    };

    this.isEnabled = function () {
        return enabled;
    };

    this.isLocked = function () {
        return locked;
    };

    this.isReadOnly = function () {
        return playboard.getPlayer(viewer) == null;
    };

    this.getPlayer = function (playerId) {
        var res = null;
        $.each(players, function (i, player) {
            if (player.id == playerId) {
                res = player;
                return false;
            }
            return true;
        });
        return res;
    };

    this.getPlayers = function () {
        return players;
    };

    this.getWonPlayers = function () {
        return $.grep(players, function (e, i) {
            return e.winner;
        });
    };

    this.getScoreEngine = function () {
        return scoreEngine;
    };

    this.isBoardTile = function (column, row) {
        var tile = boardTiles[column][row];
        return tile != null && wm.scribble.tile.isTilePined(tile);
    };

    this.getSelectedTiles = function () {
        var tiles = new Array(selectedTileWidgets.length);
        $.each(selectedTileWidgets, function (i, v) {
            tiles[i] = $(v).data('tile');
        });
        return tiles;
    };

    this.getSelectedWord = function () {
        if (selectedTileWidgets instanceof Array && selectedTileWidgets.length > 1) {
            var tiles = playboard.getSelectedTiles();

            var direction;
            if (tiles[0].row == tiles[1].row) {
                direction = 'HORIZONTAL';
            } else if (tiles[0].column == tiles[1].column) {
                direction = 'VERTICAL';
            } else {
                return null; // not a word
            }

            tiles.sort(function (a, b) {
                if (direction == 'VERTICAL') {
                    return a.row - b.row;
                } else {
                    return a.column - b.column;
                }
            });

            for (var i = 1, count = tiles.length; i < count; i++) {
                if (direction == 'HORIZONTAL' && (tiles[i].row != tiles[i - 1].row || tiles[i].column != tiles[i - 1].column + 1)) {
                    return null; // not a word
                } else if (direction == 'VERTICAL' && (tiles[i].column != tiles[i - 1].column || tiles[i].row != tiles[i - 1].row + 1)) {
                    return null; // not a word
                }
            }
            var word = "";
            $.each(tiles, function (i, v) {
                word += v.letter
            });
            return {
                tiles: tiles,
                direction: direction,
                position: { row: tiles[0].row, column: tiles[0].column},
                text: word
            }
        }
    };

    this.makeTurn = function (handler) {
        if (enabled) {
            makeMove('make', playboard.getSelectedWord(), handler);
        }
    };

    this.passTurn = function (handler) {
        if (enabled) {
            makeMove('pass', null, handler);
        }
    };

    this.exchangeTiles = function (tiles, handler) {
        if (enabled) {
            makeMove('exchange', tiles, handler);
        }
    };

    this.resign = function (handler) {
        if (enabled) {
            makeMove('resign', null, handler);
        }
    };

    this.checkWord = function (word) {
        return wordIterator(word, function (tile, row, column, i, letter) {
            var boardTile = boardTiles[column][row];
            if (boardTile != null && boardTile != undefined) {
                if (tile.number != boardTile.data('tile').number) {
                    return false;
                }
            } else {
                if (getHandTileIndex(tile) == null) {
                    return false;
                }
            }
        });
    };

    this.isWordPlaced = function (word) {
        return wordIterator(word, function (tile, row, column, i, letter) {
            var boardTile = boardTiles[column][row];
            if (boardTile == null || boardTile == undefined) {
                return false;
            }

            if (tile.number != boardTile.data('tile').number) {
                return false;
            }
        });
    };

    this.selectWord = function (word) {
        if (isWordSelected(word)) {
            return;
        }
        clearSelectionImpl();
        if (!playboard.checkWord(word)) {
            return false;
        }
        var res = wordIterator(word, function (tile, row, column, i, letter) {
            var boardTile = boardTiles[column][row];
            if (wm.scribble.tile.isTilePined(boardTile)) {
                if (tile.number != boardTile.data('tile').number) {
                    return false;
                }
                changeTileSelection(boardTile.get(0), true, false);
            } else {
                var tileIndex = getHandTileIndex(tile);
                if (tileIndex == null) {
                    return false;
                }
                var tileWidget = handTiles[tileIndex];
                tile = tileWidget.data('tile');

                if (tile.wildcard) {
                    wm.scribble.tile.setLetter(tileWidget, letter);
                }
                tile.row = row;
                tile.column = column;
                handTiles[tileIndex] = null;
                boardTiles[column][row] = tileWidget;
                tileWidget.detach().css('top', row * 22).css('left', column * 22).appendTo(boardEl);
                changeTileSelection(tileWidget.get(0), true, true);
            }
        });
        if (res) {
            changeSelectedWord(word);
        }
    };

    this.selectMove = function (moveNumber) {
        if (!enabled) {
            return;
        }
        if (moves[moveNumber].word != undefined) {
            playboard.selectHistoryWord(moves[moveNumber].word);
        } else {
            clearSelectionImpl();
        }
    };

    this.selectHistoryWord = function (word) {
        if (!enabled || isWordSelected(word)) {
            return;
        }
        clearSelectionImpl();

        var rowK, columnK;
        var row = word.position.row;
        var column = word.position.column;
        if (word.direction == 'VERTICAL') {
            rowK = 1;
            columnK = 0;
        } else {
            rowK = 0;
            columnK = 1;
        }
        for (var i = 0, count = word.tiles.length; i < count; i++) {
            changeTileSelection(boardTiles[column + i * columnK][row + i * rowK].get(0), true, false);
        }
        changeSelectedWord(playboard.getSelectedWord());
    };

    this.selectLetters = function (letter) {
        if (!enabled) {
            return;
        }

        clearSelectionImpl();
        for (var i = 0; i < 15; i++) {
            for (var j = 0; j < 15; j++) {
                var tile = boardTiles[i][j];
                if (tile != null && tile != undefined && wm.scribble.tile.isTilePined(tile)) {
                    var d = tile.data('tile');
                    if (d.letter == letter) {
                        changeTileSelection(tile.get(0), true, false);
                    }
                }
            }
        }
    };

    this.clearSelection = function () {
        if (!enabled) {
            return;
        }
        clearSelectionImpl();
    };

    this.getBankCapacity = function () {
        return bank.lettersCount;
    };

    this.getBankTilesInfo = function () {
        return bank.letterDescriptions;
    };

    this.getBoardTile = function (column, row) {
        var tile = boardTiles[column][row];
        if (tile == null || tile == undefined) {
            return null;
        }
        return wm.scribble.tile.isTilePined(tile) ? tile.data('tile') : null;
    };

    this.getBoardTilesCount = function () {
        var count = 0;
        for (var i = 0; i < 15; i++) {
            for (var j = 0; j < 15; j++) {
                if (boardTiles[i][j] != null && boardTiles[i][j] != undefined) {
                    count++;
                }
            }
        }
        return count;
    };

    this.getGameMoves = function () {
        return moves;
    };

    this.getHandTiles = function () {
        var res = new Array(handTiles.length);
        $.each(handTiles, function (i, tw) {
            if (tw != null && tw != undefined) {
                res[i] = tw.data('tile');
            } else {
                res[i] = null;
            }
        });
        return res;
    };

    this.getHandTilesCount = function () {
        var bankCapacity = playboard.getBankCapacity();
        var boardTiles = playboard.getBoardTilesCount();
        var handTiles = 7 * players.length;

        var cof = bankCapacity - boardTiles - handTiles;
        if (cof < 0) {
            handTiles += cof;
        }
        return handTiles;
    };

    this.getBankTilesCount = function () {
        var v = playboard.getBankCapacity() - playboard.getBoardTilesCount() - playboard.getHandTilesCount();
        return v < 0 ? 0 : v;
    };

    this.getPlayerTurn = function () {
        return board.playerTurn;
    };

    this.isPlayerActive = function () {
        return viewer === board.playerTurn;
    };

    this.isBoardActive = function () {
        return board.resolution == null;
    };

    this.isBoardEnabled = function () {
        return enabled;
    };

    this.getLanguage = function () {
        return language;
    };

    this.getMonitoringBean = function () {
        return new function () {
            this.getParameters = function () {
                return "l=" + board.lastChange;
            };

            this.getCallback = function () {
                return function (response) {
                    processServerResponse({success: response.success, data: response.data.boardChanges});
                }
            };
        };
    };

    initializeGame();
};

wm.scribble.Monitoring = function (board) {
    var items = {};
    var monitoringInstance = this;

    var sendServerRequest = function () {
        var params = '';
        $.each(items, function (i, v) {
            if (v != null) {
                var parameters = v.getParameters();
                if (parameters != null && parameters != undefined) {
                    params += '&' + parameters;
                }
            }
        });

        $.post('/playground/scribble/observe.ajax?b=' + board.getBoardId() + params, null).success(
            function (response) {
                $.each(items, function (i, v) {
                    if (v != null) {
                        v.getCallback()(response);
                    }
                });
            }
        );
    };

    this.addMonitoringBean = function (name, monitoringBean) {
        items[name] = monitoringBean;
    };

    this.removeMonitoring = function (name) {
        items[name] = null;
    };

    this.startMonitoring = function () {
        if (!board.isBoardActive()) {
            return false;
        }

        monitoringInstance.stopMonitoring();

        $(board).everyTime(60000, 'BoardMonitoring-' + board.getBoardId(), function () {
            sendServerRequest();
        });
        return true;
    };

    this.stopMonitoring = function () {
        $(board).stopTime('BoardMonitoring-' + board.getBoardId());
    };

    if (board.isBoardActive()) {
        board.bind('resolution', function (event, resolution) {
            monitoringInstance.stopMonitoring();
        });

        board.bind('playerTurn', function (event, oldPlayerTurn, newPlayerTurn) {
            var player = board.getPlayer(newPlayerTurn);
            if (player != null && player != undefined && player.info.type == "ROBOT") {
                setTimeout(sendServerRequest, 1000);
            }
        });
    }
};

$(document).ready(function () {
    jQuery.fn.extend({
        serializeObject: function () {
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

    $('[title]').cluetip({ showTitle: false, activation: 'hover', local: true});

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
    $(".quickInfo.ajax a").cluetip({
        width: 340,
        showTitle: false,
        ajaxCache: true,
        activation: 'click',
        closePosition: 'bottom',
        closeText: wm.i18n.value('button.close'),
        arrows: false,
        sticky: true,
        ajaxProcess: function (response) {
            if (response.success) {
                return response.data;
            }
            return null;
        },
        ajaxSettings: {
            type: 'post',
            dataType: 'json',
            contentType: 'application/json'
        },
        onActivate: function (e) {
            var element = $(this);
            if (activeQuickInfo != undefined) {
                activeQuickInfo.parent().attr('class', 'quickInfo ui-state-default');
            }
            activeQuickInfo = element;
            element.parent().attr('class', 'quickInfo ui-state-active');
            return true;
        },
        onHide: function (ct, ci) {
            $(this).parent().attr('class', 'quickInfo ui-state-default');
            activeQuickInfo = undefined;
        }
    });

    $(".quickInfo.local a").cluetip({
        width: 340,
        local: true,
        showTitle: false,
        ajaxCache: true,
//        activation: 'click',
        arrows: false,
        sticky: false,
        ajaxSettings: {
            dataType: 'html'
        },
        onActivate: function (e) {
            var element = $(this);
            if (activeQuickInfo != undefined) {
                activeQuickInfo.parent().attr('class', 'quickInfo ui-state-default');
            }
            activeQuickInfo = element;
            element.parent().attr('class', 'quickInfo ui-state-active');
            return true;
        },
        onHide: function (ct, ci) {
            $(this).parent().attr('class', 'quickInfo ui-state-default');
            activeQuickInfo = undefined;
        }
    });

    $(".wm-ui-button").button();
    $(".wm-ui-buttonset").buttonset();

    var globalSplitButtonMenu = null;
    $(".wm-ui-splitbutton").each(function (i, sb) {
        sb = $(sb);
        var ch = sb.children();
        var buttons = $("<div></div>").appendTo(sb.empty().append($(ch[1]))).append($(ch[0]));

        $("<button>&nbsp;</button>").appendTo(buttons).button({
            text: false,
            icons: {
                primary: "ui-icon-triangle-1-s"
            }
        }).click(function () {
                if (globalSplitButtonMenu != null) {
                    globalSplitButtonMenu.hide();
                    globalSplitButtonMenu = null;
                }

                globalSplitButtonMenu = $(ch[1]).menu().show().position({
                    my: "left top",
                    at: "left bottom",
                    of: this
                });

                $(document).one("click", function () {
                    if (globalSplitButtonMenu != null) {
                        globalSplitButtonMenu.hide();
                        globalSplitButtonMenu = null;
                    }
                });
                return false;
            });
        buttons.buttonset();
    });
});

$(document).ready(function () {
    var timeoutID;

    $('.dropdown')
        .mouseenter(function () {
            var submenu = $('.sublinks').stop(false, true).hide();
            window.clearTimeout(timeoutID);

            submenu.css({
                width: $(this).width() + 20 + 'px',
                top: $(this).offset().top + $(this).height() + 7 + 'px',
                left: $(this).offset().left + 'px'
            });

            submenu.stop().slideDown(300);

            submenu.mouseleave(function () {
                $(this).slideUp(300);
            });

            submenu.mouseenter(function () {
                window.clearTimeout(timeoutID);
            });

        })
        .mouseleave(function () {
            timeoutID = window.setTimeout(function () {
                $('.sublinks').stop(false, true).slideUp(300);
            }, 250);
        });
});
