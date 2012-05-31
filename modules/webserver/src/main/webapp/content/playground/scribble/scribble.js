/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */
if (wm == null) wm = {};
if (wm.scribble == null) wm.scribble = {};

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
            return $.toJSON(data);
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
    var comments = new Array();

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
                comments = result.data.comments;

                var count = 0;
                var historyCount = 0;
                var unreadLoaded = true;
                $.each(result.data.comments, function (i, a) {
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
                wm.ui.unlock(widget, result.summary, true);
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

        var read = new Array();
        var visible = new Array();
        for (var i = loadedComments; i < loadedComments + count; i++) {
            visible.push(comments[i].id);
            read['c' + comments[i].id] = comments[i].read;
        }

        wm.ui.lock(widget, language['loading']);
        controller.execute('comments', 'get', 'b=' + board.getBoardId(), visible, function (result) {
            if (result.success) {
                $.each(result.data.comments, function (i, a) {
                    showComment(a, read['c' + a.id], false);
                });
                loadedComments += count;
                updateStatus();
                wm.ui.unlock(widget);
            } else {
                wm.ui.unlock(widget, result.summary, true);
            }
        });
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
            interval:300,
            over:function () {
                $(this).find(".info").slideDown('fast');
                $(this).toggleClass("collapsed");
            },
            out:function () {
                $(this).find(".info").slideUp('fast');
                $(this).toggleClass("collapsed");
            }
        });
    };

    var showComment = function (comment, collapsed, top) {
        var item = $('<div class="item' + (collapsed ? ' collapsed' : '') + '"></div>');
        var info = $('<div class="info"></div>').appendTo(item);
        var time = $('<div class="time"></div>').appendTo(info).html('(' + comment.elapsed + ' ' + language['ago'] + ')');
        var player = $('<div class="sender"></div>').appendTo(info).html(wm.ui.player(board.getPlayerInfo(comment.person)));

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
        $.post('/playground/scribble/comment/add.ajax?b=' + board.getBoardId(), $.toJSON({text:val}), function (result) {
            if (result.success) {
                loadedComments += 1;
                comments.unshift({id:result.data.id, read:true});
                showComment(result.data, true, true);
                clearEditor();
                updateStatus();
                widget.find('.content').show();
                wm.ui.unlock(widget, language['saved']);
            } else {
                showEditorError();
                wm.ui.unlock(widget, result.summary);
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
            var d = response.data.comments;
            if (d != undefined && d.length > 0) {
                $.each(d.reverse(), function (i, a) {
                    comments.unshift({id:a.id, read:false});
                    showComment(a, false, true);
                });
                loadedComments += d.length;
                if (loadedComments == d.length) {
                    widget.find('.content').show();
                    updateStatus();
                }
                $.post('/playground/scribble/comment/mark.ajax?b=' + board.getBoardId());
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

    board.bind('gameState',
            function (event, type, state) {
                if (type === 'finished') {
                    widget.find('.create-comment').remove();
                }
            });

    initWidget();
};

wm.scribble.Memory = function (board, controller, clearMemory, language) {
    var nextWordId = 0;
    var memoryWords = new Array();
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
                wm.ui.unlock(memoryWordWidget, result.summary, true);
            }
        });
    };

    var reloadMemoryWords = function () {
        memoryTable.fnClearTable();
        executeRequest('load', null, function (data) {
            $.each(data.words, function (i, word) {
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
        "bFilter":false,
        "bSort":true,
        "bSortClasses":true,
        "sDom":'t',
        "aaSorting":[
            [1, 'desc']
        ],
        "aoColumns":[
            null,
            { "sClass":"center"},
            { "bSortable":false }
        ],
        "oLanguage":language
    });

    addWordButton.button({disabled:true, icons:{primary:'icon-memory-add'}}).click(this.remember);
    clearWordButton.button({disabled:true, icons:{primary:'icon-memory-clear'}}).click(this.clear);

    board.bind('wordSelection',
            function (event, word) {
                addWordButton.button(word == null ? "disable" : "enable");
            }).bind('gameState',
            function (event, type, state) {
                if (type === 'finished') {
                    memoryWordWidget.remove();
                }
            }).bind('gameMoves',
            function (event, move) {
                validateWords();
            });

    reloadMemoryWords();
};

wm.scribble.Selection = function (board) {
    var wordInfoElement = board.getPlayboardElement('.selectedWordInfo');
    var wordCostElement = board.getPlayboardElement('.selectedWordCost');
    var selectedTilesElement = board.getPlayboardElement('.selectedTilesInfo');

    var selectedWordInfo = wordInfoElement.text();
    var selectedWordCost = wordCostElement.text();
    var selectedTilesInfo = selectedTilesElement.text();

    board.bind("tileSelection",
            function (event, selected, tile) {
                var tiles = selectedTilesElement.find('div');
                var length = board.getSelectedTiles().length;
                if (selected && length == 1) {
                    selectedTilesElement.empty();
                }
                if (selected) {
                    wm.scribble.tile.createTileWidget(tile).offset({left:((length - 1) * 22), top:0}).appendTo(selectedTilesElement);
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
            .bind('wordSelection',
            function (event, word) {
                if (word != null) {
                    wordCostElement.empty().text(board.getScoreEngine().getWordPoints(word).formula);
                    wordInfoElement.empty();
                    $.each(word.tiles, function (i, t) {
                        wm.scribble.tile.createTileWidget(t).offset({left:(i * 22), top:0}).appendTo(wordInfoElement);
                    });
                } else {
                    wordInfoElement.text(selectedWordInfo);
                    wordCostElement.text(selectedWordCost);
                }
            });
};

wm.scribble.Thesaurus = function (board, checkWords) {
    var thesaurus = this;

    var checkTimer = undefined;

    var input = board.getPlayboardElement('.word-value');
    var status = board.getPlayboardElement('.word-status');
    var checkButton = board.getPlayboardElement('.word-check');
    var lookupButton = board.getPlayboardElement('.word-lookup');

    const CHECK_WORD_TIMEOUT = 1000;

    this.checkWord = function () {
        validateWord(input.val());
        return false;
    };

    this.lookupWord = function () {
        window.open('http://slovari.yandex.ru/' + input.val() + '/значение');
        return false;
    };

    var startAutoChecker = function () {
        stopAutoChecker();
        var val = input.val();
        checkTimer = $(thesaurus).oneTime(CHECK_WORD_TIMEOUT, 'checkWord', function () {
            if (isAutoChecker()) {
                validateWord(val);
            }
        });
    };

    var stopAutoChecker = function () {
        if (isAutoChecker()) {
            $(thesaurus).stopTime('checkWord');
            checkTimer = undefined;
        }
    };

    var isAutoChecker = function () {
        return (checkTimer != undefined);
    };

    var validateWord = function (text) {
        stopAutoChecker();
        if (text.length == 0) {
            return;
        }
        status.removeClass('icon-empty').addClass('icon-wait');
        $.post('/playground/scribble/board/check.ajax', $.toJSON({word:text, lang:board.getLanguage()}),
                function (response) {
                    if (isAutoChecker()) {
                        return;
                    }
                    status.removeClass('icon-wait');
                    checkButton.button('disable').removeClass("ui-state-hover");
                    if (response.success) {
                        status.addClass('icon-word-valid');
                    } else {
                        input.addClass('ui-state-error');
                        status.addClass('icon-word-invalid');
                    }
                }, 'json');
    };

    var validateInputValue = function () {
        input.removeClass('ui-state-error');
        status.attr('class', 'word-status ui-icon icon-empty');
        if (input.val().length != 0) {
            checkButton.button('enable');
            lookupButton.button('enable');

            if (checkWords) {
                startAutoChecker();
            }
        } else {
            checkButton.button('disable').removeClass("ui-state-hover");
            lookupButton.button('disable').removeClass("ui-state-hover");

            if (checkWords) {
                stopAutoChecker();
            }
        }
    };

    board.bind('wordSelection',
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
            thesaurus.checkWord();
            e.preventDefault();
        } else {
            validateInputValue();
        }
    });

    lookupButton.button({disabled:true});
    checkButton.button({disabled:true, icons:{primary:'icon-word-check'}});
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
            var t = wm.scribble.tile.createTileWidget({number:bti.count, letter:bti.letter, cost:bti.cost}).offset({top:row * 22, left:col * 22});
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
            title:language['title'],
            width:500,
            height:'auto',
            resizable:false,
            autoOpen:false,
            buttons:[
                {
                    text:wm.i18n.value('button.cancel', 'Cancel'),
                    click:function () {
                        $(this).dialog("close");
                    }
                }
            ]
        });
    };

    var showTileInfo = function (tile) {
        var activeBoardTiles = getBoardTiles(tile);

        $(".tiles-bank .tileView").empty().append(wm.scribble.tile.createTileWidget({number:0, letter:tile.letter, cost:tile.cost}).offset({left:0, top:0}));
        $(".tiles-bank .tileCost").text(tile.cost);
        $(".tiles-bank .totalCount").text(tile.count);
        $(".tiles-bank .boardCount").text(activeBoardTiles.length);
    };

    var getBoardTiles = function (tile) {
        var res = new Array();
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
    var addMoveToHistory = function (move) {
        var link = '';
        if (move.type == 'make') {
            var word = move.word;
            link = '<span class="moveMade">' + word.text + '</span>';
        } else if (move.type == 'exchange') {
            link = '<span class="moveExchange">' + language['exchange'] + '</span>';
        } else if (move.type == 'pass') {
            link = '<span class="movePassed">' + language['passed'] + '</span>';
        }
        movesHistoryTable.fnAddData([1 + move.number, board.getPlayerInfo(move.player).nickname, link, move.points]);
    };

    var movesHistoryTable = wm.ui.dataTable('.movesHistory table', {
        "bSort":true,
        "bSortClasses":false,
        "aaSorting":[
            [0, 'desc']
        ],
        "bAutoWidth":false,
        "bPaginate":false,
        "bInfo":false,
        "bFilter":false,
        "sScrollY":235,
        "sScrollX":"100%",
        "bStateSave":true,
        "sDom":'t',
        "oLanguage":language
    });


    movesHistoryTable.click(function (e) {
        var pos = movesHistoryTable.fnGetPosition($(event.target).closest('tr')[0]);
        var index = movesHistoryTable.fnGetData(pos)[0] - 1;
        board.selectMove(index);
    });

    $(document).ready(function () {
        board.bind('gameMoves', function (event, move) {
            addMoveToHistory(move);
        });
    });
};

wm.scribble.Controls = function (board, language) {
    var widget = board.getPlayboardElement(".playboard");
    var toolbarElement = board.getPlayboardElement(".boardActionsToolbar");

    var markTurnButton = board.getPlayboardElement(".makeTurnButton");
    var clearSelectionButton = board.getPlayboardElement(".clearSelectionButton");
    var selectTileButton = board.getPlayboardElement(".selectTileButton");
    var exchangeTilesButton = board.getPlayboardElement(".exchangeTilesButton");
    var passTurnButton = board.getPlayboardElement(".passTurnButton");
    var resignGameButton = board.getPlayboardElement(".resignGameButton");

    markTurnButton.button({disabled:true, icons:{primary:'icon-controls-make'}});
    clearSelectionButton.button({disabled:true, icons:{primary:'icon-controls-clear'}});
    selectTileButton.button({disabled:false, icons:{primary:'icon-controls-highlight'}});
    exchangeTilesButton.button({disabled:true, icons:{primary:'icon-controls-exchange'}});
    passTurnButton.button({disabled:true, icons:{primary:'icon-controls-pass'}});
    resignGameButton.button({disabled:true, icons:{primary:'icon-controls-resign'}});

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

    var blockBoard = function () {
        wm.ui.lock(widget, language['updatingBoard']);
    };

    var unblockBoard = function () {
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

    var updateGameState = function (type, state) {
        if (type === 'turn') {
            updateControlsState();

            if (board.isPlayerActive()) {
                wm.ui.notification(language['updatedLabel'], language['updatedYour'], 'your-turn');
            } else {
                wm.ui.notification(language['updatedLabel'], language['updatedOther'] + ' <b>' + board.getPlayerInfo(state.playerTurn).nickname + '</b>.', 'opponent-turn');
            }
        } else if (type === 'finished') {
            toolbarElement.hide();
            toolbarElement.find('button').button({disabled:true});
            var msg;
            var opts = {autoHide:false};
            if (state.resolution == 'RESIGNED') {
                msg = language['finishedInterrupted'] + " <b>" + board.getPlayerInfo(state.playerTurn).nickname + "</b>.";
            } else {
                if (state.winners == undefined || state.winners.length == 0) {
                    msg = language['finishedDrew'];
                } else {
                    msg = language['finishedWon'];
                    $.each(state.winners, function (i, pid) {
                        if (i != 0) {
                            msg += ", ";
                        }
                        msg += "<b>" + board.getPlayerInfo(pid).nickname + "</b>";
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
        var tilesPanel = $($('.exchangeTilesPanel div').get(1));
        $.each(board.getHandTiles(), function (i, tile) {
            wm.scribble.tile.createTileWidget(tile).offset({top:0, left:i * 22}).click(onTileSelected).appendTo(tilesPanel);
        });

        $('.exchangeTilesPanel').dialog({
                    title:language['exchange'],
                    draggable:false,
                    modal:true,
                    resizable:false,
                    width:400,
                    buttons:[
                        {
                            text:language['exchange'],
                            click:function () {
                                $(this).dialog("close");
                                var tiles = new Array();
                                $.each(tilesPanel.children(), function (i, tw) {
                                    if (wm.scribble.tile.isTileSelected(tw)) {
                                        tiles.push($(tw).data('tile'));
                                    }
                                });
                                board.exchangeTiles(tiles, showMoveResult);
                            }
                        },
                        {
                            text:language['cancel'],
                            click:function () {
                                $(this).dialog("close");
                            }
                        }
                    ]
                }
        )
    };

    board.bind("tileSelection",
            function (event, selected, tile) {
                updateSelectionState();
            })
            .bind('wordSelection',
            function (event, word) {
                updateControlsState();
            }).bind('gameState',
            function (event, type, state) {
                updateGameState(type, state);
            }).bind('boardState',
            function (event, enabled) {
                if (!enabled) {
                    blockBoard();
                } else {
                    unblockBoard();
                }
            });

    updateControlsState();
};

wm.scribble.Players = function (board) {
    var playersInfo = board.getPlayboardElement('.playersInfo');

    var getPlayerInfoCells = function (pid, name) {
        return playersInfo.find(".player-info-" + pid + " " + name);
    };

    var selectActivePlayer = function (pid) {
        playersInfo.find(".player-info td").removeClass("ui-state-active");
        playersInfo.find(".player-info .info").text("");
        getPlayerInfoCells(pid, "td").addClass("ui-state-active");
    };

    var selectWonPlayers = function (pids) {
        playersInfo.find(".player-info td").removeClass("ui-state-active");
        $.each(pids, function (i, pid) {
            getPlayerInfoCells(pid, "td").addClass("ui-state-highlight");
            getPlayerInfoCells(pid, "td.winner-icon").html("<img src='/resources/images/scribble/winner.png'>");
        });
    };

    var showPlayerTimeout = function (pid, time) {
        updatePlayerInfo(pid, time);
    };

    var showPlayerRating = function (pid, ratingDelta, ratingFinal) {
        var iconClass;
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
        updatePlayerInfo(pid, "<div class='rating " + iconClass + "'><div class='change'><sub>" + ratingDelta + "</sub></div><div class='value'>" + ratingFinal + "</div></div>");
    };

    var updatePlayerPoints = function (pid, points) {
        getPlayerInfoCells(pid, ".points").text(points);
    };

    var updatePlayerInfo = function (pid, info) {
        getPlayerInfoCells(pid, ".info").html(info);
    };

    var updateBoardState = function () {
        if (board.isBoardActive()) {
            selectActivePlayer(board.getPlayerTurn());
            showPlayerTimeout(board.getPlayerTurn(), board.getRemainedTime());
        } else {
            $.each(board.getPlayerRatings(), function (i, rating) {
                showPlayerRating(rating.playerId, rating.ratingDelta, rating.newRating);
                updatePlayerPoints(rating.playerId, rating.points);
            });
            selectWonPlayers(board.getWonPlayers());
        }
    };

    updateBoardState();

    board.bind('gameMoves',
            function (event, move) {
                var playerInfo = board.getPlayerInfo(move.player);
                if (move.type == 'make') {
                    updatePlayerPoints(playerInfo.playerId, playerInfo.points);
                }
            })
            .bind('gameState',
            function (event, type, state) {
                updateBoardState();
            });
};

wm.scribble.Settings = function (board, language) {
    this.showSettings = function () {
        var dlg = $('<div><div class="loading-image" style="height: 200px"></div></div>');
        dlg.load('/playground/scribble/settings/load');
        dlg.dialog({
            title:language['title'],
            width:550,
            minHeight:'auto',
            modal:true,
            resizable:false,
            buttons:[
                {
                    text:language['apply'],
                    click:function () {
                        $("#boardSettingsForm").ajaxSubmit({
                            dataType:'json',
                            contentType:'application/x-www-form-urlencoded',
                            success:function (data) {
                                window.location.reload();
                            }
                        });
                    }
                },
                {
                    text:wm.i18n.value('button.cancel', 'Cancel'),
                    click:function () {
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
                    case '2l':
                        bonus = 2;
                        break;
                    case '3l':
                        bonus = 3;
                        break;
                    case '2w':
                        bonus = 1;
                        pointsMult *= 2;
                        formulaMult += '*2';
                        break;
                    case '3w':
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

        return {points:points, formula:formula};
    };
};

wm.scribble.Board = function (gameInfo, boardViewer, wildcardHandlerElement, controller, settings) {
    var playboard = this;

    var scribble = $("<div></div>").addClass('scribble');
    var gameBackground = $("<div></div>").addClass('background').appendTo(scribble);
    $("<div></div>").addClass('color').appendTo(gameBackground);
    $("<div></div>").addClass('grid').appendTo(gameBackground);
    var gameField = $("<div></div>").addClass('field').appendTo(scribble);

    var hand = $("<div></div>").addClass('hand').appendTo($(gameField));
    var board = $("<div></div>").addClass('board').appendTo($(gameField));
    var bonuses = $("<div></div>").addClass('bonuses').appendTo($(gameField));

    var id = gameInfo.id;
    var state = gameInfo.state;
    var bank = gameInfo.bank;
    var players = gameInfo.players;
    var ratings = gameInfo.ratings;
    var moves = gameInfo.board.moves;
    var language = gameInfo.language;

    var enabled = true;

    var handTiles = new Array(7);
    var boardTiles = wm.util.createMatrix(15);

    var draggingTile = null;

    var selectedWord = null;
    var selectedTileWidgets = [];

    var wildcardSelectionDialog = null;

    var highlighting = new function () {
        var element = $('<div></div>').addClass('highlighting').hide().appendTo(gameField);
        var previousCell = null;

        var updatePosition = function (cell) {
            var offset = cell.container.offset();
            element.offset({left:offset.left + cell.x * 22, top:offset.top + cell.y * 22});
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
            element.offset({top:0, left:0});
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
                    element.offset({top:0, left:0});
                }
            }
            previousCell = cell;
        };
    };

    var scoreEngine = new wm.scribble.ScoreEngine(gameInfo.board.bonuses, this);

    var initializeGame = function () {
        for (var i = 0; i < 15; i++) {
            for (var j = 0; j < 15; j++) {
                var bonus = scoreEngine.getCellBonus(i, j);
                if (bonus != undefined) {
                    var text = wm.i18n.value(bonus, bonus.toUpperCase());
                    $("<div></div>").addClass('cell bonus-cell').addClass('bonus-cell-' + bonus).text(text).offset({left:j * 22, top:i * 22}).appendTo(bonuses);
                }
            }
        }
        if (scoreEngine.getCellBonus(7, 7) == undefined) {
            $("<div></div>").addClass('cell').addClass('bonus-cell-center').offset({left:7 * 22, top:7 * 22}).appendTo(bonuses);
        }

        $.each(moves, function (i, move) {
            registerBoardMove(move, true);
        });

        if (gameInfo.privacy != null && gameInfo.privacy != undefined) {
            validateHandTile(gameInfo.privacy.handTiles);
        }

        $(document).mouseup(onTileUp);
        $(document).mousemove(onTileMove);
        $(scribble).mousedown(onBoardClick);
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
        var relatedCell = getRelatedCell(ev, {left:0, top:0});
        draggingTile.data('mouseOffset', {left:ev.pageX - offset.left, top:ev.pageY - offset.top});
        draggingTile.data('originalState', {offset:offset, cell:relatedCell, zIndex:draggingTile.css('zIndex')});
        draggingTile.css('zIndex', 333);
        highlighting.start(draggingTile, relatedCell);
        ev.preventDefault();
    };

    var onTileMove = function (ev) {
        if (!enabled) {
            return;
        }
        if (draggingTile != null && draggingTile != undefined) {
            var tileOffset = draggingTile.data('mouseOffset');
            var relatedCell = getRelatedCell(ev, {left:tileOffset.left - 5, top:tileOffset.top - 5});
            draggingTile.offset({left:ev.pageX - tileOffset.left, top:ev.pageY - tileOffset.top});
            highlighting.highlight(relatedCell);
        }
        ev.preventDefault();
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
        var relatedCell = getRelatedCell(ev, {left:tileOffset.left - 5, top:tileOffset.top - 5});
        if (relatedCell == null ||
                (relatedCell.container == board && boardTiles[relatedCell.x][relatedCell.y] != undefined) ||
                (relatedCell.container == hand && handTiles[relatedCell.x] != undefined)) {
            draggingTile.offset(originalState.offset);
        } else {
            // clear original position
            var originalCell = originalState.cell;
            if (originalCell.container == board) {
                boardTiles[originalCell.x][originalCell.y] = null;
            } else if (originalCell.container == hand) {
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
            if (relatedCell.container == board) {
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
            } else if (relatedCell.container == hand) {
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
        var relatedCell = getRelatedCell(ev, {left:0, top:0});
        if (relatedCell == null) {
            return;
        }
        if (relatedCell.container != board || relatedCell.x < 0 || relatedCell.y < 0 || relatedCell.x > 14 || relatedCell.y > 14) {
            return;
        }
        if (boardTiles[relatedCell.x][relatedCell.y] != undefined) {
            return;
        }
        clearSelectionImpl();
        ev.preventDefault();
    };

    var getRelatedCell = function (ev, tileOffset) {
        var bo = board.offset();
        var x = (((ev.pageX - bo.left - tileOffset.left) / 22) | 0);
        var y = (((ev.pageY - bo.top - tileOffset.top) / 22) | 0);
        if (x >= 0 && x <= 14 && y >= 0 && y <= 14) {
            return {x:x, y:y, container:board};
        }

        var ho = hand.offset();
        x = (((ev.pageX - ho.left - tileOffset.left) / 22) | 0);
        y = (((ev.pageY - ho.top - tileOffset.top) / 22) | 0);
        if (x >= 0 && x <= 6 && y == 0) {
            return {x:x, y:y, container:hand};
        }
        return null;
    };

    var changeTileSelection = function (tileWidget, select, notifyWord) {
        var tile = $(tileWidget).data('tile');
        if (wm.scribble.tile.isTileSelected(tileWidget) != select) {
            if (select) {
                wm.scribble.tile.selectTile(tileWidget);
                selectedTileWidgets.push(tileWidget);
                scribble.trigger('tileSelection', [true, tile]);
            } else {
                wm.scribble.tile.deselectTile(tileWidget);
                for (var i = 0, len = selectedTileWidgets.length; i < len; i++) {
                    if (selectedTileWidgets[i] == tileWidget) {
                        selectedTileWidgets.splice(i, 1);
                        break;
                    }
                }
                scribble.trigger('tileSelection', [false, tile]);
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
                        offset({top:0, left:i * 22}).mousedown(onTileDown).appendTo(hand);
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
        if (move.type == 'make') {
            $.each(move.word.tiles, function (i, tile) {
                tile.row = move.word.position.row + (move.word.direction == 'VERTICAL' ? i : 0 );
                tile.column = move.word.position.column + (move.word.direction == 'VERTICAL' ? 0 : i );

                removeHandTile(tile); // remove tile from the hand if it's hand tile

                if (boardTiles[tile.column][tile.row] == null || boardTiles[tile.column][tile.row] == undefined) {
                    var w = wm.scribble.tile.createTileWidget(tile).
                            offset({top:tile.row * 22, left:tile.column * 22}).click(onTileSelected);
                    wm.scribble.tile.pinTile(w.get(0));
                    boardTiles[tile.column][tile.row] = w.appendTo(board);
                }
            });
        }
        if (!init) {
            moves.push(move);
            scribble.trigger('gameMoves', [move]);
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

    var updateBoardState = function (e) {
        if (enabled !== e) {
            scribble.trigger('boardState', [enabled = e]);
        }
    };

    var updateGameState = function (newState) {
        var oldState = state;
        state = newState;
        if (!state.active) {
            enabled = false;
            clearSelectionImpl();
            ratings = state.ratings;
            scribble.trigger('gameState', ['finished', state]);
        } else if (state.playerTurn != oldState.playerTurn) {
            scribble.trigger('gameState', ['turn', state]);
        }
        scribble.trigger('gameState', ['info', state]);
    };

    var updatePlayersInfo = function (playersState) {
        if (playersState != null && playersState != undefined) {
            players = $.extend(true, players, playersState);
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
                scribble.trigger('wordSelection', null);
            } else {
                scribble.trigger('wordSelection', [word]);
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
                        handTiles[i] = $(widget).detach().css('top', 0).css('left', i * 22).appendTo(hand);
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
        updateBoardState(false);
        sendServerRequest(type, data, function (status, message, errorThrown) {
            updateBoardState(true);
            handler.call(this, status, message, errorThrown);
        });
    };

    var sendServerRequest = function (type, data, resultHandler) {
        controller.execute('board', type, 'b=' + id + '&m=' + moves.length, data, function (response) {
            processServerResponse(response);
            resultHandler.call(playboard, response.success, response.summary);
        });
    };

    var processServerResponse = function (response) {
        if (!response.success) {
            return;
        }
        var moves = response.data.moves;
        if (moves != undefined && moves.length > 0) {
            clearSelectionImpl();

            var lastMove = null;
            $.each(moves, function (i, move) {
                lastMove = move;
                var playerInfo = playboard.getPlayerInfo(move.player);
                playerInfo.points = playerInfo.points + move.points;
                registerBoardMove(move, false);
            });

            var hand = response.data.hand;
            if (hand != null && hand != undefined) {
                validateHandTile(hand);
            }

            if (lastMove != null && lastMove.word != null && lastMove.word != undefined) {
                playboard.selectWord(lastMove.word);
            }
        }

        updatePlayersInfo(response.data.players);
        updateGameState(response.data.state);
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
                autoOpen:false,
                draggable:false,
                modal:true,
                resizable:false,
                width:400,
                close:function (event, ui) {
                    wildcardSelectionDialog.replacer(tileLetter);
                }
            });

            var panel = $($('#' + wildcardHandlerElement + ' div').get(1)).empty();
            $.each(bank.tilesInfo, function (i, bti) {
                var row = Math.floor(i / 15);
                var col = (i - row * 15);
                var t = wm.scribble.tile.createTileWidget({number:0, letter:bti.letter, cost:0}).offset({top:row * 22, left:col * 22});
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
        return scribble.get(0);
    };

    this.getPlayboardElement = function (selector) {
        return $("#board" + id + " " + selector);
    };

    this.bind = function (event, handler) {
        return scribble.bind(event, handler);
    };

    this.unbind = function (event, handler) {
        return scribble.unbind(event, handler);
    };

    this.getPlayerInfo = function (playerId) {
        var res;
        $.each(players, function (i, player) {
            if (player.playerId == playerId) {
                res = player;
                return false;
            }
        });
        return res;
    };

    this.getPlayerRatings = function () {
        return ratings;
    };

    this.getPlayerRating = function (playerId) {
        var res;
        if (ratings != null && ratings != undefined) {
            $.each(ratings, function (i, rating) {
                if (rating.playerId == playerId) {
                    res = rating;
                    return false;
                }
            });
        }
        return res;
    };

    this.getPlayerHands = function () {
        return players;
    };

    this.getRemainedTime = function () {
        return state.remainedTimeMessage;
    };

    this.getWonPlayers = function () {
        return state.winners;
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
            var tiles = this.getSelectedTiles();

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
                tiles:tiles,
                direction:direction,
                position:{ row:tiles[0].row, column:tiles[0].column},
                text:word
            }
        }
    };

    this.makeTurn = function (handler) {
        if (enabled) {
            makeMove('make', this.getSelectedWord(), handler);
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
                tileWidget.detach().css('top', row * 22).css('left', column * 22).appendTo(board);
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
            this.selectHistoryWord(moves[moveNumber].word);
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
        return bank.capacity;
    };

    this.getBankTilesInfo = function () {
        return bank.tilesInfo;
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
        return state.playerTurn;
    };

    this.isPlayerActive = function () {
        return boardViewer === state.playerTurn;
    };

    this.isBoardActive = function () {
        return state.active;
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
                return "m=" + moves.length;
            };

            this.getCallback = function () {
                return processServerResponse;
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

        $.post('/playground/scribble/changes.ajax?b=' + board.getBoardId() + params, null).success(
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

        $(board).everyTime(60000, 'board' + board.getBoardId() + 'Monitoring', function () {
            sendServerRequest();
        });
    };

    this.stopMonitoring = function () {
        $(board).stopTime('board' + board.getBoardId() + 'Monitoring');
    };

    board.bind('gameState', function (event, type, state) {
        if (type === 'finished') {
            monitoringInstance.stopMonitoring();
        }
    });
};