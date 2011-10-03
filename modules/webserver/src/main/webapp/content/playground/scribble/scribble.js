/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */
if (wm == null) wm = {};
if (wm.scribble == null) wm.scribble = {};

wm.scribble.tile = new function() {
    var updateTileImage = function(tileWidget) {
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

    this.createTileWidget = function(tile) {
        return $("<div></div>").addClass("tile cost" + tile.cost).css('background-position', '-' + tile.cost * 22 + 'px 0').append($("<span></span>").html(tile.letter.toUpperCase())).data('tile', tile);
    };

    this.selectTile = function(tileWidget) {
        $(tileWidget).addClass("tile-selected").get(0).selected = true;
        updateTileImage(tileWidget);
    };

    this.deselectTile = function(tileWidget) {
        $(tileWidget).removeClass("tile-selected").get(0).selected = false;
        updateTileImage(tileWidget);
    };

    this.pinTile = function(tileWidget) {
        $(tileWidget).get(0).pinned = true;
        updateTileImage(tileWidget);
    };

    this.isTileSelected = function(tileWidget) {
        return $(tileWidget).get(0).selected === true;
    };

    this.getLetter = function(tileWidget) {
        return $(tileWidget).data('tile').letter;
    };

    this.setLetter = function(tileWidget, letter) {
        var v = $(tileWidget);
        v.data('tile').letter = letter;
        return v.children().get(0).innerText = letter.toUpperCase();
    };

    this.isTilePined = function(tileWidget) {
        return tileWidget != null && tileWidget != undefined && $(tileWidget).get(0).pinned === true;
    };
};

wm.scribble.Comments = function(board, language) {
    var loadedComments = 0;
    var comments = new Array();

    var widget = $("#board" + board.getBoardId() + ' .annotation');
    var view = widget.find('.items');

    var editor = widget.find('.editor');
    var editorError = editor.find('.ui-state-error-text');

    var status = widget.find('.status');

    var initWidget = function() {
        block();
        editor.find("button").button();
        editor.find("textarea").change(function() {
            showEditorError(null);
        });
        loadStatuses();
    };

    var loadStatuses = function() {
        $.post('/playground/scribble/comment/load.ajax?b=' + board.getBoardId(), function(result) {
            if (result.success) {
                comments = result.data.comments;

                var count = 0;
                var historyCount = 0;
                var unreadLoaded = true;
                $.each(result.data.comments, function(i, a) {
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
                    unblock();
                }
                $.post('/playground/scribble/comment/mark.ajax?b=' + board.getBoardId());
            } else {
                wm.ui.showStatus(result.summary, true);
                unblock();
            }
        });
    };

    var loadComments = function(count) {
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

        block();
        $.post('/playground/scribble/comment/get.ajax?b=' + board.getBoardId(), $.toJSON(visible), function(result) {
            if (result.success) {
                $.each(result.data.comments, function(i, a) {
                    showComment(a, read['c' + a.id], false);
                });
                loadedComments += count;
                updateStatus();
            } else {
                wm.ui.showStatus(result.summary, true);
            }
            unblock();
        });
    };

    var updateStatus = function() {
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

    var getNextLoadCount = function() {
        var c = comments.length - loadedComments;
        if (c > 5) {
            return 5;
        }
        return c;
    };

    var block = function() {
        widget.block({ message:null});
    };

    var unblock = function() {
        widget.unblock();
    };

    var clearEditor = function() {
        editor.slideUp('fast');
        showEditorError(null);
        editor.find("textarea").val('');
    };

    var showEditorError = function(msg) {
        if (msg == undefined || msg == null) {
            editorError.html('');
        } else {
            editorError.html(msg);
        }
    };

    var registerItemControls = function(item) {
        item.hoverIntent(function() {
            $(this).find(".info").slideDown('fast');
            $(this).toggleClass("collapsed");
        }, function() {
            $(this).find(".info").slideUp('fast');
            $(this).toggleClass("collapsed");
        });
    };

    var showComment = function(comment, collapsed, top) {
        var item = $('<div class="item' + (collapsed ? ' collapsed' : '') + '"></div>');
        var info = $('<div class="info"></div>').appendTo(item);
        var time = $('<div class="time"></div>').appendTo(info).html('(' + comment.elapsed + ' ' + language['ago'] + ')');
        var player = $('<div class="sender"></div>').appendTo(info).html(wm.ui.player(board.getPlayerInfo(comment.person)));

        var msg = $('<div class="message"></div>').appendTo(item).html(comment.text + "<span></span>");

        if (collapsed) {
            registerItemControls(item);
        } else {
            item.click(function() {
                registerItemControls($(this).click(null));
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

    this.create = function() {
        editor.slideDown('fast');
    };

    this.save = function() {
        var val = editor.find('textarea').val();
        if (val.trim().length == 0) {
            showEditorError("Message is empty. Please enter a message.");
            return false;
        }
        block();
        $.post('/playground/scribble/comment/add.ajax?b=' + board.getBoardId(), $.toJSON({text:val}), function(result) {
            if (result.success) {
                loadedComments += 1;
                comments.unshift({id:result.data.id, read:true});
                showComment(result.data, true, true);
                clearEditor();
                updateStatus();
                widget.find('.content').show();
            } else {
                showEditorError(result.summary);
            }
            unblock();
        });
    };

    this.cancel = function() {
        clearEditor();
    };

    this.load = function() {
        loadComments(getNextLoadCount());
    };

    this.getMonitoringBean = function() {
        var processLoadedComments = function(response) {
            var d = response.data.comments;
            if (d != undefined && d.length > 0) {
                $.each(d.reverse(), function(i, a) {
                    comments.unshift({id:a.id, read:false});
                    showComment(a, false, true);
                });
                loadedComments += d.length;
                updateStatus();
                $.post('/playground/scribble/comment/mark.ajax?b=' + board.getBoardId());
            }
        };

        return new function() {
            this.getParameters = function() {
                return "c=" + comments.length;
            };

            this.getCallback = function() {
                return processLoadedComments;
            };
        };
    };

    board.bind('gameState',
            function(event, type, state) {
                if (type === 'finished') {
                    widget.find('.create-comment').remove();
                }
            });

    initWidget();
};

wm.scribble.Memory = function(board, language) {
    var nextWordId = 0;
    var memoryWords = new Array();
    var memoryWordsCount = 0;

    var addWord = function(word, checkPlacement) {
        memoryWordsCount++;

        var id = nextWordId++;
        memoryWords[id] = word;
        memoryTable.fnAddData(createNewRecord(id, word, checkPlacement));
        $("#memoryClearButton").button(memoryWordsCount == 0 ? "disable" : "enable");
        board.clearSelection();
    };

    var getWord = function(id) {
        return memoryWords[id];
    };

    var removeWord = function(id) {
        var word = memoryWords[id];
        if (word != null && word != undefined) {
            memoryWordsCount--;
            memoryWords[id] = null;

            var row = $('#memoryWordControls' + id).closest('tr').get(0);
            memoryTable.fnDeleteRow(memoryTable.fnGetPosition(row));

            $("#memoryClearButton").button(memoryWordsCount == 0 ? "disable" : "enable");
        }
    };

    var createNewRecord = function(id, word, checkPlacement) {
        var scoreEngine = board.getScoreEngine();

        var text = word.text;
        var valid = board.checkWord(word);
        if (valid && checkPlacement) {
            valid = !board.isWordPlaced(word);
        }
        var points = scoreEngine.getWordPoints(word).points.toString();

        var e = '<div id="memoryWordControls' + id + '" class="memory-controls">';
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

    var executeRequest = function(type, data, successHandler) {
        wm.ui.showStatus(language['changeMemory'], false, true);
        $("#memoryWords").block({ message:null});

        if (data != null) {
            data = $.toJSON(data);
        }

        $.post('/playground/scribble/memory/' + type + '.ajax?b=' + board.getBoardId(), data, function(result) {
            if (result.success) {
                successHandler(result.data);
            } else {
                wm.ui.showMessage({message:result.summary, error:true});
            }
            $("#memoryWords").unblock();
            wm.ui.clearStatus();
        });
    };

    var reloadMemoryWords = function() {
        memoryTable.fnClearTable();
        executeRequest('load', null, function(data) {
            $.each(data.words, function(i, word) {
                addWord(word, true);
            });
        });
    };

    var validateWords = function() {
        $.each(memoryWords, function(id, word) {
            if (word != null && word != undefined) {
                var row = $('#memoryWordControls' + id).closest('tr').get(0);
                memoryTable.fnUpdate(createNewRecord(id, word, true), memoryTable.fnGetPosition(row), 0);
            }
        });
    };

    this.select = function(id) {
        var word = getWord(id);
        if (word != null && word != undefined) {
            board.selectWord(word);
        }
    };

    this.remove = function(id) {
        var word = getWord(id);
        if (word != null && word != undefined) {
            executeRequest('remove', word, function(data) {
                removeWord(id);
            });
        }
    };

    this.clear = function() {
        executeRequest('clear', null, function(data) {
            memoryTable.fnClearTable();
        });
    };

    this.remember = function() {
        var word = board.getSelectedWord();
        if (word != null || word != undefined) {
            executeRequest('add', word, function(data) {
                addWord(word, false);
            });
        }
    };

    var memoryTable = $("#memoryWords").dataTable({
        "bJQueryUI":true,
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

    $("#memoryAddButton").button({disabled:true, icons:{primary:'icon-memory-add'}}).click(this.remember);
    $("#memoryClearButton").button({disabled:true, icons:{primary:'icon-memory-clear'}}).click(this.clear);

    board.bind('wordSelection',
            function(event, word) {
                $("#memoryAddButton").button(word == null ? "disable" : "enable");
            }).bind('gameState',
            function(event, type, state) {
                if (type === 'finished') {
                    $("#memoryWordsWidget").parent().remove();
                }
            }).bind('gameMoves',
            function(event, move) {
                validateWords();
            });

    reloadMemoryWords();
};

wm.scribble.Selection = function(board, language) {
    var selectedWordInfo = $("#selectedWordInfo").text();
    var selectedWordCost = $("#selectedWordCost").text();
    var selectedTilesInfo = $("#selectedTilesInfo").text();

    this.checkSelectedWord = function() {
        var word = board.getSelectedWord();
        if (word == null || word == undefined) {
            return;
        }
        $("#checkWordButton").button('disable').removeClass("ui-state-hover");
        $("#wordStatusMessage").text(language['checking']);
        $("#wordStatusIcon").addClass('icon-wait').removeClass('icon-word-valid icon-word-invalid wordValid wordInvalid');
        $.post('/playground/scribble/board/check.ajax', $.toJSON({word:word.text, lang:board.getLanguage()}),
                function(response) {
                    if (response.success) {
                        $("#wordStatusMessage").text(language['valid']);
                        $("#wordStatusIcon").removeClass('icon-wait').addClass("icon-word-valid");
                    } else {
                        $("#wordStatusMessage").text(language['invalid']);
                        $("#wordStatusIcon").removeClass('icon-wait').addClass("icon-word-invalid");
                    }
                }, 'json');
    };

    $("#checkWordButton").button({disabled:true, icons:{primary:'icon-word-check'}});

    board.bind("tileSelection",
            function(event, selected, tile) {
                var tiles = $("#selectedTilesInfo div");
                var length = board.getSelectedTiles().length;
                if (selected && length == 1) {
                    $("#selectedTilesInfo").empty();
                }
                if (selected) {
                    wm.scribble.tile.createTileWidget(tile).offset({left:((length - 1) * 22), top:0}).appendTo('#selectedTilesInfo');
                } else {
                    var updateOffset = false;
                    $.each(tiles, function(i, tileWidget) {
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
                    $("#selectedTilesInfo").text(selectedTilesInfo);
                }
            })
            .bind('wordSelection',
            function(event, word) {
                var swi = $("#selectedWordInfo");
                var swc = $("#selectedWordCost");
                if (word != null) {
                    swc.empty().text(board.getScoreEngine().getWordPoints(word).formula);
                    swi.empty();
                    $.each(word.tiles, function(i, t) {
                        wm.scribble.tile.createTileWidget(t).offset({left:(i * 22), top:0}).appendTo(swi);
                    });
                    $("#checkWordButton").button('enable');
                } else {
                    swi.text(selectedWordInfo);
                    swc.text(selectedWordCost);
                    $("#checkWordButton").button('disable').removeClass("ui-state-hover");
                }
                $("#wordStatusIcon").attr('class', '');
                $("#wordStatusMessage").text("");
            })
            .bind('gameState',
            function(event, type, state) {
                if (type === 'finished') {
                    $("#moveInfo").parent().remove();
                }
            });
};

wm.scribble.Legend = function(board) {
    var costTable = $("table .tilesCostInfo");
    for (var i = 0; i < 12; i++) {
        var count = 0;
        var e = $("<tr></tr>");
        $('<td nowrap="nowrap"></td>').html(i + ' points').appendTo(e);
        $('<td>&nbsp;-&nbsp;</td>').appendTo(e);
        var d = $('<div style="position: relative; height: 22px;"></div>');
        $('<td></td>').append(d).appendTo(e);
        $.each(board.getBankTilesInfo(), function(j, bti) {
            if (bti.cost == i) {
                d.append(wm.scribble.tile.createTileWidget({letter:bti.letter, cost:bti.cost}).offset({left:count * 22, top:0}));
                count++;
            }
        });
        d.width(count * 22);
        if (count > 0) {
            e.appendTo(costTable);
        }
    }

    var countTable = $("table .tilesCountTable");
    for (var i = 0; i < 30; i++) {
        var count = 0;
        var e = $("<tr></tr>");
        $('<td></td>').html(i + 'шт').appendTo(e);
        $('<td>&nbsp;-&nbsp;</td>').appendTo(e);
        var d = $('<div style="position: relative; height: 22px;"></div>');
        $('<td></td>').append(d).appendTo(e);
        $.each(board.getBankTilesInfo(), function(j, bti) {
            if (bti.count == i) {
                d.append(wm.scribble.tile.createTileWidget({letter:bti.letter, cost:bti.cost}).offset({left:count * 22, top:0}));
                count++;
            }
        });
        d.width(count * 22);
        if (count > 0) {
            e.appendTo(countTable);
        }
    }
};

wm.scribble.History = function(board, language) {
    var addMoveToHistory = function(move) {
        var link = '';
        if (move.type == 'make') {
            var word = move.word;
            link = '<span class="moveMade"><a href="javascript: board.selectHistoryWord(' +
                    '{row: ' + word.position.row + ', column: ' + word.position.column +
                    ', direction: \'' + word.direction + '\', length: ' + word.tiles.length + '})">' +
                    word.text + '</a></span>';
        } else if (move.type == 'exchange') {
            link = '<span class="moveExchange">' + language['exchange'] + '</span>';
        } else if (move.type == 'pass') {
            link = '<span class="movePassed">' + language['passed'] + '</span>';
        }
        movesHistoryTable.fnAddData([1 + move.number, board.getPlayerInfo(move.player).nickname, link, move.points]);
    };

    var movesHistoryTable = $("#movesHistory table").dataTable({
        "bJQueryUI":true,
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

    $.each(board.getGameMoves(), function(i, move) {
        addMoveToHistory(move)
    });

    board.bind('gameMoves', function(event, move) {
        addMoveToHistory(move)
    });
};

wm.scribble.Controls = function(board, language) {
    $("#makeTurnButton").button({disabled:true, icons:{primary:'icon-controls-make'}});
    $("#clearSelectionButton").button({disabled:true, icons:{primary:'icon-controls-clear'}});
    $("#exchangeTilesButton").button({disabled:true, icons:{primary:'icon-controls-exchange'}});
    $("#passTurnButton").button({disabled:true, icons:{primary:'icon-controls-pass'}});
    $("#resignGameButton").button({disabled:true, icons:{primary:'icon-controls-resign'}});

    var onTileSelected = function() {
        if (wm.scribble.tile.isTileSelected(this)) {
            wm.scribble.tile.deselectTile(this);
        } else {
            wm.scribble.tile.selectTile(this);
        }
    };

    var showMoveResult = function(success, message, error) {
        if (success) {
            wm.ui.showAlert(language['acceptedLabel'], language['acceptedDescription'], 'move-accepted');
        } else {
            wm.ui.showMessage({message:message + (error != null ? error : ''), error:true});
        }
    };

    var blockBoard = function() {
        $(board.getBoardElement()).parent().block({ message:null});
        $("#boardActionsToolbar").block({ message:null});
        wm.ui.showStatus(language['updatingBoard'], false, true);
    };

    var unblockBoard = function() {
        updateControlsState();

        $("#boardActionsToolbar").unblock();
        $(board.getBoardElement()).parent().unblock();
        wm.ui.clearStatus();
    };

    var updateSelectionState = function() {
        $("#clearSelectionButton").removeClass("ui-state-hover").button(board.getSelectedTiles().length == 0 ? "disable" : "enable");
    };

    var updateControlsState = function() {
        $("#boardActionsToolbar button").removeClass("ui-state-hover");

        updateSelectionState();

        $("#makeTurnButton").button(board.isBoardActive() && board.isPlayerActive() && board.getSelectedWord() != null ? "enable" : "disable");
        $("#passTurnButton").button(board.isBoardActive() && board.isPlayerActive() ? "enable" : "disable");
        $("#exchangeTilesButton").button(board.isBoardActive() && board.isPlayerActive() ? "enable" : "disable");
        $("#resignGameButton").button(board.isBoardActive() ? "enable" : "disable");
    };

    var updateGameState = function(type, state) {
        if (type === 'turn') {
            updateControlsState();

            if (board.isPlayerActive()) {
                wm.ui.showAlert(language['updatedLabel'], language['updatedYour'], 'your-turn');
            } else {
                wm.ui.showAlert(language['updatedLabel'], language['updatedOther'] + ' <b>' + board.getPlayerInfo(state.playerTurn).nickname + '</b>.', 'opponent-turn');
            }
        } else if (type === 'finished') {
            $("#boardActionsToolbar").hide();
            $("#boardActionsToolbar button").button({disabled:true});
            var msg;
            var opts = {autoHide:false};
            if (state.resolution == 'RESIGNED') {
                msg = language['finishedInterrupted'] + " <b>" + board.getPlayerInfo(state.playerTurn).nickname + "</b>.";
            } else {
                if (state.winners == undefined || state.winners.length == 0) {
                    msg = language['finishedDrew'];
                } else {
                    msg = language['finishedWon'];
                    $.each(state.winners, function(i, pid) {
                        if (i != 0) {
                            msg += ", ";
                        }
                        msg += "<b>" + board.getPlayerInfo(pid).nickname + "</b>";
                    });
                }
            }
            wm.ui.showAlert(language['finishedLabel'], msg + "<div class='closeInfo'>" + language['clickToClose'] + "</div>", 'game-finished', opts);
        }
    };

    this.makeTurn = function() {
        board.makeTurn(showMoveResult);
    };

    this.passTurn = function() {
        wm.ui.showConfirm(language['pass'], language['passDescription'], function(approved) {
            if (approved) {
                board.passTurn(showMoveResult);
            }
        });
    };

    this.resignGame = function() {
        wm.ui.showConfirm(language['resignLabel'], language['resignDescription'], function(approved) {
            if (approved) {
                board.resign(showMoveResult);
            }
        });
    };

    this.exchangeTiles = function() {
        var tilesPanel = $($('#exchangeTilesPanel div').get(1));
        tilesPanel.empty();
        $.each(board.getHandTiles(), function(i, tile) {
            wm.scribble.tile.createTileWidget(tile).offset({top:0, left:i * 22}).click(onTileSelected).appendTo(tilesPanel);
        });

        $('#exchangeTilesPanel').dialog({
                    title:language['exchange'],
                    draggable:false,
                    modal:true,
                    resizable:false,
                    width:400,
                    buttons:[
                        {
                            text:language['exchange'],
                            click:function() {
                                $(this).dialog("close");
                                var tiles = new Array();
                                $.each(tilesPanel.children(), function(i, tw) {
                                    if (wm.scribble.tile.isTileSelected(tw)) {
                                        tiles.push($(tw).data('tile'));
                                    }
                                });
                                board.exchangeTiles(tiles, showMoveResult);
                            }
                        },
                        {
                            text:language['cancel'],
                            click:function() {
                                $(this).dialog("close");
                            }
                        }
                    ]
                }
        )
    };

    board.bind("tileSelection",
            function(event, selected, tile) {
                updateSelectionState();
            })
            .bind('wordSelection',
            function(event, word) {
                updateControlsState();
            }).bind('gameState',
            function(event, type, state) {
                updateGameState(type, state);
            }).bind('boardState',
            function(event, enabled) {
                if (!enabled) {
                    blockBoard();
                } else {
                    unblockBoard();
                }
            });

    updateControlsState();
};

wm.scribble.Players = function(board) {
    var getPlayerInfoCells = function(pid, name) {
        return $("#playersInfo .player-info-" + pid + " " + name);
    };

    var selectActivePlayer = function(pid) {
        $("#playersInfo .player-info td").removeClass("ui-state-active");
        $("#playersInfo .player-info .info").text("");
        getPlayerInfoCells(pid, "td").addClass("ui-state-active");
    };

    var selectWonPlayers = function(pids) {
        $("#playersInfo .player-info td").removeClass("ui-state-active");
        $.each(pids, function(i, pid) {
            getPlayerInfoCells(pid, "td").addClass("ui-state-highlight");
            getPlayerInfoCells(pid, "td.winner-icon").html("<img src='/resources/images/scribble/winner.png'>");
        });
    };

    var showPlayerTimeout = function(pid, time) {
        updatePlayerInfo(pid, time);
    };

    var showPlayerRating = function(pid, ratingDelta, ratingFinal) {
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

    var updatePlayerPoints = function(pid, points) {
        getPlayerInfoCells(pid, ".points").text(points);
    };

    var updatePlayerInfo = function(pid, info) {
        getPlayerInfoCells(pid, ".info").html(info);
    };

    var updateBoardState = function() {
        if (board.isBoardActive()) {
            selectActivePlayer(board.getPlayerTurn());
            showPlayerTimeout(board.getPlayerTurn(), board.getRemainedTime());
        } else {
            $.each(board.getPlayerRatings(), function(i, rating) {
                showPlayerRating(rating.playerId, rating.ratingDelta, rating.newRating);
                updatePlayerPoints(rating.playerId, rating.points);
            });
            selectWonPlayers(board.getWonPlayers());
        }
    };

    updateBoardState();

    board.bind('gameMoves',
            function(event, move) {
                var playerInfo = board.getPlayerInfo(move.player);
                if (move.type = 'make') {
                    updatePlayerPoints(playerInfo.playerId, playerInfo.points);
                }
            })
            .bind('gameState',
            function(event, type, state) {
                updateBoardState();
            });
};

wm.scribble.ScoreEngine = function(gameBonuses, board) {
    var emptyHandBonus = 33;

    var bonuses = wm.util.createMatrix(15);
    $.each(gameBonuses, function(i, bonus) {
        bonuses[bonus.column][bonus.row] = bonus.type;
        bonuses[bonus.column][14 - bonus.row] = bonus.type;
        bonuses[14 - bonus.column][bonus.row] = bonus.type;
        bonuses[14 - bonus.column][14 - bonus.row] = bonus.type;
    });

    this.getCellBonus = function(row, col) {
        return bonuses[row][col];
    };

    this.getWordPoints = function(word) {
        var points = 0;
        var pointsRaw = 0;
        var pointsMult = 1;

        var formula = '';
        var formulaRaw = '';
        var formulaMult = '';

        $.each(word.tiles, function(i, tile) {
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

wm.scribble.Board = function(gameInfo, boardViewer, wildcardHandlerElement) {
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
    var selectedTileWidgets = [];

    var wildcardSelectionDialog = null;

    var highlighting = new function() {
        var element = $('<div></div>').addClass('highlighting').hide().appendTo(gameField);
        var previousCell = null;

        var updatePosition = function(cell) {
            var offset = cell.container.offset();
            element.offset({left:offset.left + cell.x * 22, top:offset.top + cell.y * 22});
        };

        this.start = function(tileWidget, cell) {
            element.css('backgroundPosition', -tileWidget.data('tile').cost * 22 + "px -88px");
            if (cell != null) {
                element.show();
                updatePosition(cell);
            }
            previousCell = cell;
        };

        this.stop = function() {
            element.hide();
            element.offset({top:0, left:0});
            previousCell = null;
        };

        this.highlight = function(cell) {
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

    var initializeGame = function() {
        for (var i = 0; i < 15; i++) {
            for (var j = 0; j < 15; j++) {
                var bonus = scoreEngine.getCellBonus(i, j);
                if (bonus != undefined) {
                    $("<div></div>").addClass('cell').addClass('bonus-cell-' + bonus).offset({left:j * 22, top:i * 22}).appendTo(bonuses);
                }
            }
        }
        if (scoreEngine.getCellBonus(7, 7) == undefined) {
            $("<div></div>").addClass('cell').addClass('bonus-cell-center').offset({left:7 * 22, top:7 * 22}).appendTo(bonuses);
        }

        $.each(moves, function(i, move) {
            registerBoardMove(move, true);
        });

        if (gameInfo.privacy != null && gameInfo.privacy != undefined) {
            validateHandTile(gameInfo.privacy.handTiles);
        }

        $(document).mouseup(onTileUp);
        $(document).mousemove(onTileMove);
    };

    var onTileSelected = function() {
        if (!enabled) {
            return;
        }
        if (wm.scribble.tile.isTileSelected(this)) {
            changeTileSelection(this, false, true);
        } else {
            changeTileSelection(this, true, true);
        }
    };

    var onTileDown = function(ev) {
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

    var onTileMove = function(ev) {
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

    var onTileUp = function(ev) {
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
                    wildcardHandler(tile, function(letter) {
                        wm.scribble.tile.setLetter(replacingTile, letter);
                    });
                }
                tile.row = relatedCell.y;
                tile.column = relatedCell.x;
                boardTiles[relatedCell.x][relatedCell.y] = draggingTile;
                changeTileSelection(draggingTile.get(0), true, true);
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

    var getRelatedCell = function(ev, tileOffset) {
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

    var changeTileSelection = function(tileWidget, select, notifyWord) {
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
            scribble.trigger('wordSelection', [playboard.getSelectedWord()]);
        }
    };

    var addHandTile = function(tile) {
        $.each(handTiles, function(i, handTile) {
            if (handTile == null || handTile == undefined) {
                handTiles[i] = wm.scribble.tile.createTileWidget(tile).
                        offset({top:0, left:i * 22}).mousedown(onTileDown).appendTo(hand);
                return false;
            }
        });
    };

    var getHandTileIndex = function(tile) {
        var res = null;
        $.each(handTiles, function(i, handTile) {
            if (handTile != null && handTile != undefined && handTile.data('tile').number == tile.number) {
                res = i;
                return false;
            }
        });
        return res;
    };

    var removeHandTile = function(tile) {
        $.each(handTiles, function(i, handTile) {
            if (handTile != null && handTile != undefined && handTile.data('tile').number == tile.number) {
                handTile.remove();
                handTiles[i] = null;
                return false;
            }
        });
    };

    var validateHandTile = function(tiles) {
        var handTiles = playboard.getHandTiles();
        $.each(handTiles, function(i, tile) {
            if (tile != null && tile != undefined) {
                if (!isTileInList(tile, tiles)) {
                    removeHandTile(tile);
                }
            }
        });

        $.each(tiles, function(i, tile) {
            if (!isTileInList(tile, handTiles)) {
                addHandTile(tile);
            }
        });
    };

    var registerBoardMove = function(move, init) {
        if (move.type == 'make') {
            $.each(move.word.tiles, function(i, tile) {
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

    var isTileInList = function(tile, tiles) {
        for (var i = 0, count = tiles.length; i < count; i++) {
            var listTile = tiles[i];
            if (tile != null && tile != undefined && listTile != null && listTile != undefined && listTile.number == tile.number) {
                return true;
            }
        }
        return false;
    };

    var updateBoardState = function(e) {
        if (enabled !== e) {
            scribble.trigger('boardState', [enabled = e]);
        }
    };

    var updateGameState = function(newState) {
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

    var updatePlayersInfo = function(playersState) {
        if (playersState != null && playersState != undefined) {
            players = $.extend(true, players, playersState);
        }
    };

    var clearSelectionImpl = function() {
        if (selectedTileWidgets.length == 0) {
            return;
        }
        while (selectedTileWidgets.length != 0) {
            var widget = selectedTileWidgets[0];
            if (!wm.scribble.tile.isTilePined(widget)) {
                $.each(handTiles, function(i, hv) {
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
        scribble.trigger('wordSelection', null);
    };

    var makeMove = function(type, data, handler) {
        updateBoardState(false);
        sendServerRequest(type, data, function(status, message, errorThrown) {
            updateBoardState(true);
            handler.call(this, status, message, errorThrown);
        });
    };

    var sendServerRequest = function(type, data, resultHandler) {
        if (data != null && data != undefined) {
            data = $.toJSON(data);
        }
        $.post('/playground/scribble/board/' + type + '.ajax?b=' + id + '&m=' + moves.length, data)
                .success(function(response) {
                    processServerResponse(response);
                    resultHandler.call(playboard, response.success, response.summary);
                })
                .error(function(jqXHR, textStatus, errorThrown) {
                    resultHandler.call(playboard, false, textStatus, errorThrown);
                });
    };

    var processServerResponse = function(response) {
        if (!response.success) {
            return;
        }
        var moves = response.data.moves;
        if (moves != undefined && moves.length > 0) {
            clearSelectionImpl();

            var lastMove = null;
            $.each(moves, function(i, move) {
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
                $(scribble).bind('mousedown', function() {
                    $(scribble).unbind('mousedown');
                    playboard.clearSelection();
                });
            }
        }

        updatePlayersInfo(response.data.players);
        updateGameState(response.data.state);
    };

    var wordIterator = function(word, handler) {
        var rowK = 0, columnK = 0;
        var row = word.position.row;
        var column = word.position.column;
        if (word.direction == 'VERTICAL') {
            rowK = 1;
        } else {
            columnK = 1;
        }
        for (var i = 0, count = word.tiles.length; i < count; i++) {
            var res = handler(i, word.tiles[i], row, column);
            if (res === false) {
                return false;
            }
            row += rowK;
            column += columnK;
        }
        return true;
    };

    var wildcardHandler = function(tile, replacer) {
        if (wildcardSelectionDialog == null) {
            wildcardSelectionDialog = $('#' + wildcardHandlerElement).dialog({
                autoOpen:false,
                draggable:false,
                modal:true,
                resizable:false,
                width:400
            });

            var panel = $($('#' + wildcardHandlerElement + ' div').get(1)).empty();
            $.each(bank.tilesInfo, function(i, bti) {
                var row = Math.floor(i / 15);
                var col = (i - row * 15);
                var t = wm.scribble.tile.createTileWidget({number:0, letter:bti.letter, cost:0}).offset({top:row * 22, left:col * 22});
                t.hover(
                        function() {
                            wm.scribble.tile.selectTile(this);
                        },
                        function() {
                            wm.scribble.tile.deselectTile(this);
                        }).click(
                        function() {
                            wildcardSelectionDialog.replacer($(this).data('tile').letter);
                            wildcardSelectionDialog.dialog("close");
                        }).appendTo(panel);
            });
        }
        wildcardSelectionDialog.replacer = replacer;
        wildcardSelectionDialog.dialog("open");
    };

    this.getBoardId = function() {
        return id;
    };

    this.getBoardElement = function() {
        return scribble.get(0);
    };

    this.bind = function(event, handler) {
        return scribble.bind(event, handler);
    };

    this.unbind = function(event, handler) {
        return scribble.unbind(event, handler);
    };

    this.getPlayerInfo = function(playerId) {
        var res;
        $.each(players, function(i, player) {
            if (player.playerId == playerId) {
                res = player;
                return false;
            }
        });
        return res;
    };

    this.getPlayerRatings = function() {
        return ratings;
    };

    this.getPlayerRating = function(playerId) {
        var res;
        if (ratings != null && ratings != undefined) {
            $.each(ratings, function(i, rating) {
                if (rating.playerId == playerId) {
                    res = rating;
                    return false;
                }
            });
        }
        return res;
    };

    this.getPlayerHands = function() {
        return players;
    };

    this.getRemainedTime = function() {
        return state.remainedTimeMessage;
    };

    this.getWonPlayers = function() {
        return state.winners;
    };

    this.getScoreEngine = function() {
        return scoreEngine;
    };

    this.isBoardTile = function(column, row) {
        var tile = boardTiles[column][row];
        return tile != null && wm.scribble.tile.isTilePined(tile);
    };

    this.getSelectedTiles = function() {
        var tiles = new Array(selectedTileWidgets.length);
        $.each(selectedTileWidgets, function(i, v) {
            tiles[i] = $(v).data('tile');
        });
        return tiles;
    };

    this.getSelectedWord = function() {
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

            tiles.sort(function(a, b) {
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
            $.each(tiles, function(i, v) {
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

    this.makeTurn = function(handler) {
        if (enabled) {
            makeMove('make', this.getSelectedWord(), handler);
        }
    };

    this.passTurn = function(handler) {
        if (enabled) {
            makeMove('pass', null, handler);
        }
    };

    this.exchangeTiles = function(tiles, handler) {
        if (enabled) {
            makeMove('exchange', tiles, handler);
        }
    };

    this.resign = function(handler) {
        if (enabled) {
            makeMove('resign', null, handler);
        }
    };

    this.checkWord = function(word) {
        return wordIterator(word, function(i, tile, row, column) {
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

    this.isWordPlaced = function(word) {
        return wordIterator(word, function(i, tile, row, column) {
            var boardTile = boardTiles[column][row];
            if (boardTile == null || boardTile == undefined) {
                return false;
            }

            if (tile.number != boardTile.data('tile').number) {
                return false;
            }
        });
    };

    this.selectWord = function(word) {
        clearSelectionImpl();
        if (!playboard.checkWord(word)) {
            return false;
        }
        var res = wordIterator(word, function(i, tile, row, column) {
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
                    wm.scribble.tile.setLetter(tileWidget, tile.letter);
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
            scribble.trigger('wordSelection', [word]);
        }
    };

    this.selectHistoryWord = function(word) {
        if (!enabled) {
            return;
        }
        clearSelectionImpl();

        var rowK, columnK;
        var row = word.row;
        var column = word.column;
        if (word.direction == 'VERTICAL') {
            rowK = 1;
            columnK = 0;
        } else {
            rowK = 0;
            columnK = 1;
        }
        for (var i = 0, count = word.length; i < count; i++) {
            changeTileSelection(boardTiles[column + i * columnK][row + i * rowK].get(0), true, false);
        }
        scribble.trigger('wordSelection', [playboard.getSelectedWord()]);
    };

    this.clearSelection = function() {
        if (!enabled) {
            return;
        }
        clearSelectionImpl();
    };

    this.getBankCapacity = function() {
        return bank.capacity;
    };

    this.getBoardTilesCount = function() {
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

    this.getGameMoves = function() {
        return moves;
    };

    this.getBankTilesInfo = function() {
        return bank.tilesInfo;
    };

    this.getHandTiles = function() {
        var res = new Array(handTiles.length);
        $.each(handTiles, function(i, tw) {
            if (tw != null && tw != undefined) {
                res[i] = tw.data('tile');
            } else {
                res[i] = null;
            }
        });
        return res;
    };

    this.getHandTilesCount = function() {
        var bankCapacity = playboard.getBankCapacity();
        var boardTiles = playboard.getBoardTilesCount();
        var handTiles = 7 * players.length;

        var cof = bankCapacity - boardTiles - handTiles;
        if (cof < 0) {
            handTiles += cof;
        }
        return handTiles;
    };

    this.getBankTilesCount = function() {
        var v = playboard.getBankCapacity() - playboard.getBoardTilesCount() - playboard.getHandTilesCount();
        return v < 0 ? 0 : v;
    };

    this.getPlayerTurn = function() {
        return state.playerTurn;
    };

    this.isPlayerActive = function() {
        return boardViewer === state.playerTurn;
    };

    this.isBoardActive = function() {
        return state.active;
    };

    this.isBoardEnabled = function() {
        return enabled;
    };

    this.getLanguage = function() {
        return language;
    };

    this.getMonitoringBean = function() {
        return new function() {
            this.getParameters = function() {
                return "m=" + moves.length;
            };

            this.getCallback = function() {
                return processServerResponse;
            };
        };
    };

    initializeGame();
};

wm.scribble.Monitoring = function(board) {
    var items = {};

    var sendServerRequest = function() {
        var params = '';
        $.each(items, function(i, v) {
            if (v != null) {
                var parameters = v.getParameters();
                if (parameters != null && parameters != undefined) {
                    params += '&' + parameters;
                }
            }
        });

        $.post('/playground/scribble/changes.ajax?b=' + board.getBoardId() + params, null).success(
                function(response) {
                    $.each(items, function(i, v) {
                        if (v != null) {
                            v.getCallback()(response);
                        }
                    });
                }
        );
    };

    this.addMonitoringBean = function(name, monitoringBean) {
        items[name] = monitoringBean;
    };

    this.removeMonitoring = function(name) {
        items[name] = null;
    };

    this.startMonitoring = function() {
        if (!board.isBoardActive()) {
            return false;
        }

        this.stopMonitoring();

        $(board).everyTime(60000, 'board' + board.getBoardId() + 'Monitoring', function() {
            sendServerRequest();
        });
    };

    this.stopMonitoring = function() {
        $(board).stopTime('board' + board.getBoardId() + 'Monitoring');
    };

    board.bind('gameState', function(event, type, state) {
        if (type === 'finished') {
            this.stopMonitoring();
        }
    });
};