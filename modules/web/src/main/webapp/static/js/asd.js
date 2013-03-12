wm.scribble.Board = function (gameInfo, boardViewer, wildcardHandlerElement, controller, settings) {
    var playboard = this;

    var id = gameInfo.id;
    var language = gameInfo.settings.language;

    var bank = gameInfo.bank;
    var moves = gameInfo.moves;
    var players = gameInfo.players;

    var state = gameInfo.state;
    var outcomes = gameInfo.outcomes;

    var enabled = true;
    var handTiles = new Array(7);
    var boardTiles = wm.util.createMatrix(15);

    var draggingTile = null;

    var selectedWord = null;
    var selectedTileWidgets = [];

    var wildcardSelectionDialog = null;

    var scribble = $("<div></div>").addClass('scribble');

    var background = $("<div></div>").addClass('background').appendTo(scribble);
    $("<div></div>").addClass('color').appendTo(background);
    $("<div></div>").addClass('grid').appendTo(background);
    var bonuses = $("<div></div>").addClass('bonuses').appendTo($(background));

    var field = $("<div></div>").addClass('field').appendTo(scribble);

    var hand = $("<div></div>").addClass('hand').appendTo($(field));
    var board = $("<div></div>").addClass('board').appendTo($(field));

    var highlighting = new function () {
        var element = $('<div></div>').addClass('highlighting').hide().appendTo(field);
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

    var scoreEngine = new wm.scribble.ScoreEngine(gameInfo.bonuses, this);

    var initializeGame = function () {
        if (settings.showCaptions == undefined || settings.showCaptions) {
            var gameBorder = $("<div></div>").addClass('border ui-corner-all').appendTo(scribble);
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
                    $("<div></div>").addClass('cell bonus-cell').addClass('bonus-cell-' + bonus.toLowerCase()).text(text).offset({left: j * 22, top: i * 22}).appendTo(bonuses);
                }
            }
        }
        if (scoreEngine.getCellBonus(7, 7) == undefined) {
            $("<div></div>").addClass('cell').addClass('bonus-cell-center').offset({left: 7 * 22, top: 7 * 22}).appendTo(bonuses);
        }

        $.each(moves, function (i, move) {
            registerBoardMove(move, true);
        });

        var playerInfo = playboard.getPlayerInfo(boardViewer);
        if (playerInfo != null && gameInfo.handTiles != null) {
            $("<div></div>").addClass('hand').appendTo(background);
            validateHandTile(gameInfo.handTiles);
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
        var relatedCell = getRelatedCell(ev, {left: 0, top: 0});
        draggingTile.data('mouseOffset', {left: ev.pageX - offset.left, top: ev.pageY - offset.top});
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
            var tileOffset = draggingTile.data('mouseOffset');
            var relatedCell = getRelatedCell(ev, {left: tileOffset.left - 5, top: tileOffset.top - 5});
            draggingTile.offset({left: ev.pageX - tileOffset.left, top: ev.pageY - tileOffset.top});
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
        var relatedCell = getRelatedCell(ev, {left: tileOffset.left - 5, top: tileOffset.top - 5});
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
        var relatedCell = getRelatedCell(ev, {left: 0, top: 0});
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
            return {x: x, y: y, container: board};
        }

        var ho = hand.offset();
        x = (((ev.pageX - ho.left - tileOffset.left) / 22) | 0);
        y = (((ev.pageY - ho.top - tileOffset.top) / 22) | 0);
        if (x >= 0 && x <= 6 && y == 0) {
            return {x: x, y: y, container: hand};
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
                        offset({top: 0, left: i * 22}).mousedown(onTileDown).appendTo(hand);
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
        if (move.type == 'MAKE') {
            $.each(move.word.tiles, function (i, tile) {
                tile.row = move.word.position.row + (move.word.direction == 'VERTICAL' ? i : 0 );
                tile.column = move.word.position.column + (move.word.direction == 'VERTICAL' ? 0 : i );

                removeHandTile(tile); // remove tile from the hand if it's hand tile

                if (boardTiles[tile.column][tile.row] == null || boardTiles[tile.column][tile.row] == undefined) {
                    var w = wm.scribble.tile.createTileWidget(tile).
                            offset({top: tile.row * 22, left: tile.column * 22}).click(onTileSelected);
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
            resultHandler.call(playboard, response.success, response.message);
        });
    };

    var processServerResponse = function (response) {
        if (!response.success) {
            return;
        }

        if (response.data != null && response.data != undefined) {
            if (response.data.hands != null && response.data.hands != undefined) {
                updatePlayersInfo(response.data.hands);
            }

            if (response.data.moves != null && response.data.moves != undefined) {
                updateMovesInfo(response.data.moves);
            }

            if (response.data.state != null && response.data.state != undefined) {
                updateGameState(response.data.state);
                scribble.trigger('gameState', ['info', state]);
            }
        }
    };


    var updateGameState = function (newState) {
        var oldState = state;
        state = newState;
        if (outcomes.resolution != null) {
            enabled = false;
            clearSelectionImpl();
            scribble.trigger('gameState', ['finished', state]);
        } else if (state.playerTurn != oldState.playerTurn) {
            scribble.trigger('gameState', ['turn', state]);
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

    var updatePlayersInfo = function (playersState) {
        if (playersState != null && playersState != undefined) {
            players = $.extend(true, {}, players, playersState);
        }

        var ownerInfo = playboard.getPlayerInfo(boardViewer);
        if (ownerInfo != null && ownerInfo != undefined) {
            validateHandTile(ownerInfo.tiles);
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

    this.getPlayers = function () {
        return players;
    };

    this.getPlayerInfo = function (playerId) {
        var res = null;
        $.each(players, function (i, player) {
            if (player.id == playerId) {
                res = player;
                return false;
            }
        });
        return res;
    };

    this.getRemainedTime = function () {
        return state.remainedTimeMessage;
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
        return bank.letterDistributions;
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
        return outcomes.resolution == null;
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
                return function (response) {
                    processServerResponse({success: response.success, data: response.data.board});
                }
            };
        };
    };

    initializeGame();
};
