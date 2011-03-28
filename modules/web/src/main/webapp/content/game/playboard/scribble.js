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
        return $("<div></div>").addClass("tile cost" + tile.cost).css('background-position', '-' + tile.cost * 22 + 'px 0').append($("<span></span>").text(tile.letter.toUpperCase())).data('tile', tile);
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
        return $(tileWidget).get(0).selected == true;
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
        return $(tileWidget).get(0).pinned == true;
    };
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

    this.getWordBonus = function(word) {
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

        return {points: points, formula: formula};
    };
};

wm.scribble.Board = function(gameInfo, wildcardHandler) {
    var playboard = this;

    var scribble = $("<div></div>").addClass('scribble');
    var gameField = $("<div></div>").addClass('field').appendTo(scribble);

    var hand = $("<div></div>").addClass('hand').appendTo($(gameField));
    var board = $("<div></div>").addClass('board').appendTo($(gameField));
    var bonuses = $("<div></div>").addClass('bonuses').appendTo($(gameField));

    var enabled = true;
    var boardId = gameInfo.boardId;
    var gameState = gameInfo.gameState;
    var movesCount = gameInfo.moves.length;
    var playerTurn = gameInfo.playerTurn;
    var boardViewer = gameInfo.boardViewer;
    var bankCapacity = gameInfo.bankCapacity;

    var players = new Array();
    var handTiles = new Array(7);
    var boardTiles = wm.util.createMatrix(15);

    var draggingTile = null;
    var selectedTileWidgets = [];

    var highlighting = new function() {
        var element = $('<div></div>').addClass('highlighting').hide().appendTo(gameField);
        var previousCell = null;

        var updatePosition = function(cell) {
            var offset = cell.container.offset();
            element.offset({left: offset.left + cell.x * 22, top: offset.top + cell.y * 22});
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
    var scoreEngine = new wm.scribble.ScoreEngine(gameInfo.bonuses, this);

    var initializeGame = function(gameInfo) {
        for (var i = 0; i < 15; i++) {
            for (var j = 0; j < 15; j++) {
                var bonus = scoreEngine.getCellBonus(i, j);
                $("<div></div>").addClass('cell').addClass('bonus-cell-' + bonus).offset({left: j * 22, top: i * 22}).appendTo(bonuses);
            }
        }

        $.each(gameInfo.bonuses, function(i, bonus) {
            var b = $("<div></div>").addClass('cell').addClass('bonus-cell-' + bonus.type);
            if (bonus.column != 7 && bonus.row != 7) {
                b.clone().offset({left: bonus.column * 22, top: bonus.row * 22}).appendTo(bonuses);
                b.clone().offset({left: bonus.column * 22, top: 22 + (13 - bonus.row) * 22}).appendTo(bonuses);
                b.clone().offset({left: 22 + (13 - bonus.column) * 22, top: bonus.row * 22}).appendTo(bonuses);
                b.clone().offset({left: 22 + (13 - bonus.column) * 22, top: 22 + (13 - bonus.row) * 22}).appendTo(bonuses);
            } else {
                b.offset({left: bonus.column * 22, top: bonus.row * 22}).appendTo(bonuses);
            }
        });

        $.each(gameInfo.handTiles, function(i, tile) {
            addHandTile(tile);
        });

        $.each(gameInfo.moves, function(i, move) {
            addBoardMove(move);
        });

        players = new Array(gameInfo.players.length);
        $.each(gameInfo.players, function(i, player) {
            players['player' + player.id] = player;
        });

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
        var relatedCell = getRelatedCell(ev, {left:0, top: 0});
        draggingTile.data('mouseOffset', {left: ev.pageX - offset.left, top: ev.pageY - offset.top});
        draggingTile.data('originalState', {offset: offset, cell: relatedCell, zIndex: draggingTile.css('zIndex')});
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
            var relatedCell = getRelatedCell(ev, {left:tileOffset.left - 5, top: tileOffset.top - 5});
            draggingTile.offset({left: ev.pageX - tileOffset.left, top: ev.pageY - tileOffset.top});
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
        var relatedCell = getRelatedCell(ev, {left:tileOffset.left - 5, top: tileOffset.top - 5});
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
            return {x:x, y:y, container: board};
        }

        var ho = hand.offset();
        x = (((ev.pageX - ho.left - tileOffset.left) / 22) | 0);
        y = (((ev.pageY - ho.top - tileOffset.top) / 22) | 0);
        if (x >= 0 && x <= 6 && y == 0) {
            return {x:x, y:y, container: hand};
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
                        offset({top: 0, left: i * 22}).mousedown(onTileDown).appendTo(hand);
                return false;
            }
        });
    };

    var removeHandTile = function(tile) {
        $.each(handTiles, function(i, handTile) {
            if (handTile != null && handTile != undefined && handTile.data('tile').number == tile.number) {
                handTiles[i] = null;
                return false;
            }
        });
    };

    var addBoardMove = function(move) {
        if (move.type == 'make') {
            $.each(move.word.tiles, function(i, tile) {
                tile.row = move.word.position.row + (move.word.direction == 'VERTICAL' ? i : 0 );
                tile.column = move.word.position.column + (move.word.direction == 'VERTICAL' ? 0 : i );

                if (boardTiles[tile.column][tile.row] == null || boardTiles[tile.column][tile.row] == undefined) {
                    var w = wm.scribble.tile.createTileWidget(tile).
                            offset({top: tile.row * 22, left: tile.column * 22}).click(onTileSelected);
                    wm.scribble.tile.pinTile(w.get(0));
                    boardTiles[tile.column][tile.row] = w.appendTo(board);
                }
            });
        }
    };

    var isHandTile = function(tileNumber) {
        for (var i = 0, count = handTiles.length; i < count; i++) {
            var tile = handTiles[i];
            if (tile != null && tile != undefined && $(tile).data('tile').number == tileNumber) {
                return true;
            }
        }
        return false;
    };

    var updateBoardState = function(e) {
        enabled = e;
        scribble.trigger('boardState', [enabled]);
    };

    var updateGameState = function(stateEvent) {
        if (stateEvent.state != 'ACTIVE') {
            playerTurn = null;
            gameState = stateEvent.state;
            playboard.stopBoardMonitoring();

            scribble.trigger('gameFinalization', [stateEvent]);
        } else if (playerTurn != stateEvent.playerTurn) {
            playerTurn = stateEvent.playerTurn;
            scribble.trigger('gameTurn', [stateEvent]);
        }
        scribble.trigger('gameInfo', [stateEvent]);
    };

    var makeMove = function(type, move, handler) {
        var data = null;
        if (move != null && move != undefined) {
            data = $.toJSON(move);
        }
        updateBoardState(false);
        $.post('/game/playboard/' + type + '.ajax?b=' + boardId, data)
                .success(
                function(response) {
                    updateBoardState(true);
                    if (response.success) {
                        if (response.data.move != undefined) {
                            movesCount++;
                            playerTurn = response.data.move.playerTurn;
                            $.each(selectedTileWidgets, function(i, tileWidget) {
                                if (!wm.scribble.tile.isTilePined(tileWidget)) {
                                    $(tileWidget).unbind('mousedown', onTileDown).click(onTileSelected);
                                    wm.scribble.tile.pinTile(tileWidget);
                                }
                            });
                            $.each(response.data.hand, function(i, tile) {
                                if (!isHandTile(tile.number)) {
                                    addHandTile(tile);
                                }
                            });
                            scribble.trigger('gameMoves', [response.data.move]);
                        }
                        updateGameState(response.data.state);
                        handler.call(playboard, true, response.summary);
                    } else {
                        handler.call(playboard, false, response.summary);
                    }
                })
                .error(
                function(jqXHR, textStatus, errorThrown) {
                    updateBoardState(true);
                    handler.call(playboard, false, textStatus, errorThrown);
                });
    };

    this.startBoardMonitoring = function(handler) {
        if (!playboard.isBoardActive()) {
            return false;
        }

        this.stopBoardMonitoring();

        $(scribble).everyTime(10000, 'board' + boardId + 'Monitoring', function() {
            var cfg = {
                type: 'post',
                url: '/game/playboard/changes.ajax?b=' + boardId + '&m=' + movesCount,
                dataType: 'json',
                contentType: 'application/json',
                success: function(response) {
                    if (response.success) {
                        var moves = response.data.moves;
                        if (moves != undefined && moves.length > 0) {
                            playboard.clearSelection();
                            movesCount += moves.length;
                            $.each(moves, function(i, move) {
                                addBoardMove(move);
                                scribble.trigger('gameMoves', [move]);
                            });
                        }
                        updateGameState(response.data.state);
                    } else {
                        handler.call(playboard, false, response.summary);
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    handler.call(playboard, false, textStatus, errorThrown);
                }
            };
            $.ajax(cfg);
        });
    };

    this.stopBoardMonitoring = function() {
        $(scribble).stopTime('board' + boardId + 'Monitoring');
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
        return players['player' + playerId];
    };

    this.getScoreEngine = function() {
        return scoreEngine;
    };

    this.isBoardTile = function(row, column) {
        var tile = boardTiles[row][column];
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
                tiles: tiles,
                direction: direction,
                position: { row: tiles[0].row, column: tiles[0].column},
                text: word
            }
        }
    };

    this.makeTurn = function(handler) {
        if (!enabled) {
            return;
        }
        makeMove('make', this.getSelectedWord(), handler);
    };

    this.passTurn = function(handler) {
        if (!enabled) {
            return;
        }
        makeMove('pass', null, handler);
    };

    this.exchangeTiles = function(tiles, handler) {
        if (!enabled) {
            return;
        }
        $.each(tiles, function(i, tile) {
            removeHandTile(tile);
        });
        makeMove('exchange', tiles, function(success, message, error) {
            if (!success) {
                $.each(tiles, function(i, tile) {
                    addHandTile(tile);
                });
            }
            handler.call(this, success, message, error);
        });
    };

    this.resign = function(handler) {
        if (!enabled) {
            return;
        }
        makeMove('resign', null, handler);
    };

    this.selectMemoryWord = function(word) {
        if (!enabled) {
            return;
        }
        alert("Not implemented");
    };

    this.selectHistoryWord = function(word) {
        if (!enabled) {
            return;
        }
        playboard.clearSelection();

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
        while (selectedTileWidgets.length != 0) {
            var widget = selectedTileWidgets[0];
            if (!wm.scribble.tile.isTilePined(widget)) {
                $.each(handTiles, function(i, hv) {
                    if (hv == undefined || hv == null) {
                        $(widget).detach().css('top', 0).css('left', i * 22).appendTo(hand);
                        handTiles[i] = widget;
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

    this.getBankCapacity = function() {
        return bankCapacity;
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

    this.getHandTiles = function() {
        var res = new Array(handTiles.length);
        $.each(handTiles, function(i, tw) {
            res[i] = tw.data('tile');
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

    this.isPlayerActive = function() {
        return boardViewer == playerTurn;
    };

    this.isBoardActive = function() {
        return gameState = 'ACTIVE';
    };

    this.isBoardEnabled = function() {
        return enabled;
    };

    initializeGame(gameInfo);
};
