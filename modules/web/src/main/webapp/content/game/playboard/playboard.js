/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */
function print(msg) {
    $("#console").prepend($("<div></div>").text(msg));
}

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
        return $("<div></div>").addClass("tile cost" + tile.cost).css('background-position', '-' + tile.cost * 22 + 'px 0').append($("<span></span>").text(tile.letter)).data('tile', tile);
    };

    this.selectTile = function(tileWidget) {
        $(tileWidget).addClass("tile-selected");
        tileWidget.selected = true;
        updateTileImage(tileWidget);
    };

    this.deselectTile = function(tileWidget) {
        $(tileWidget).removeClass("tile-selected");
        tileWidget.selected = false;
        updateTileImage(tileWidget);
    };

    this.pinTile = function(tileWidget) {
        tileWidget.pinned = true;
        updateTileImage(tileWidget);
    };

    this.isTileSelected = function(tileWidget) {
        return tileWidget.selected == true;
    };

    this.getLetter = function(tileWidget) {
        return tileWidget.innerText;
    };

    this.isTilePined = function(tileWidget) {
        return tileWidget.pinned == true;
    };
};

wm.scribble.ScoreEngine = function() {
    var emptyHandBonus = 33;

    var bonuses = wm.util.createMatrix(15);

    this.BonusType = { DL: {name: '2l'}, TL: {name: '3l'}, DW: {name: '2w'}, TW: {name: '3w'} };

    this.init = function() {
        var bonuses = document.getElementById('container').getElementsByTagName('bonuses');
        for (var i = 0; i < bonuses.length; i++) {
            var bonus = bonuses[i];
            var x = bonus.offsetLeft / 22;
            var y = bonus.offsetTop / 22;

            bonuses[x][y] = this.BonusType[bonus.className.match(/bonus-cell-([0-9][a-z])/i)[1]];
        }
    };

    this.getCellBonus = function(row, col) {
        return bonuses[row][col];
    }
};

wm.scribble.Highlighting = function(gameField) {
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

wm.scribble.Board = function() {
    var playboard = this;

    var scribble = $("<div></div>").addClass('scribble');
    var gameField = $("<div></div>").addClass('field').appendTo(scribble);
    var hand = $("<div></div>").addClass('hand').appendTo($(gameField));
    var board = $("<div></div>").addClass('board').appendTo($(gameField));
    var bonuses = $("<div></div>").addClass('bonuses').appendTo($(gameField));

    var handTiles = new Array(7);
    var boardTiles = wm.util.createMatrix(15);

    var draggingTile = null;
    var selectedTileWidgets = [];

    var highlighting = new wm.scribble.Highlighting(gameField);

    var onTileSelected = function() {
        if (wm.scribble.tile.isTileSelected(this)) {
            tileStateChanged(this, false, true);
        } else {
            tileStateChanged(this, true, true);
        }
    };

    var onTileDown = function(ev) {
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
        if (draggingTile != null && draggingTile != undefined) {
            var tileOffset = draggingTile.data('mouseOffset');
            var relatedCell = getRelatedCell(ev, {left:tileOffset.left - 5, top: tileOffset.top - 5});
            draggingTile.offset({left: ev.pageX - tileOffset.left, top: ev.pageY - tileOffset.top});
            highlighting.highlight(relatedCell);
        }
        ev.preventDefault();
    };

    var onTileUp = function(ev) {
        if (draggingTile == null || draggingTile == undefined) {
            return true;
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

            if (relatedCell.container == board) {
                var tile = draggingTile.data('tile');
                tile.row = relatedCell.y;
                tile.column = relatedCell.x;
                boardTiles[relatedCell.x][relatedCell.y] = draggingTile;
                tileStateChanged(draggingTile.get(0), true, true);
            } else if (relatedCell.container == hand) {
                handTiles[relatedCell.x] = draggingTile;
                tileStateChanged(draggingTile.get(0), false, true);
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

    var tileStateChanged = function(tileWidget, select, notifyWord) {
        var tile = $(tileWidget).data('tile');
        if (wm.scribble.tile.isTileSelected(tileWidget) != select) {
            if (select) {
                wm.scribble.tile.selectTile(tileWidget);
                selectedTileWidgets.push(tileWidget);
                scribble.trigger('tileSelected', [tile]);
            } else {
                wm.scribble.tile.deselectTile(tileWidget);
                for (var i = 0, len = selectedTileWidgets.length; i < len; i++) {
                    if (selectedTileWidgets[i] == tileWidget) {
                        selectedTileWidgets.splice(i, 1);
                        break;
                    }
                }
                scribble.trigger('tileDeselected', [tile]);
            }
        } else {
            scribble.trigger('tileMoved', [tile]);
        }
        if (notifyWord) {
            scribble.trigger('wordChanged', [playboard.getSelectedWord()]);
        }
    };

    var addTileToBoard = function(tile) {
        alert("Not implemented");
    };

    var addTileToHand = function(tile) {
        alert("Not implemented");
        /*
         $.each(handTiles, function(i, v) {
         if (handTiles[i] == undefined) {
         */
        /*
         tile.cell = {x:j, y:0, container: hand};
         tile.style.top = 0;
         tile.style.left = j * 22;
         tile.cell.container.appendChild(tile);
         */
        /*

         handTiles[i] = v;
         return false;
         }
         });
         */
    };

    this.initializeGame = function (gameInfo) {
        $(bonuses).empty();
        for (var i = 0, count = gameInfo.bonuses.length; i < count; i++) {
            var bonus = gameInfo.bonuses[i];
            var b = $("<div></div>").addClass('cell').addClass('bonus-cell-' + bonus.type);
            if (bonus.column != 7 && bonus.row != 7) {
                b.clone().offset({left: bonus.column * 22, top: bonus.row * 22}).appendTo(bonuses);
                b.clone().offset({left: bonus.column * 22, top: 22 + (13 - bonus.row) * 22}).appendTo(bonuses);
                b.clone().offset({left: 22 + (13 - bonus.column) * 22, top: bonus.row * 22}).appendTo(bonuses);
                b.clone().offset({left: 22 + (13 - bonus.column) * 22, top: 22 + (13 - bonus.row) * 22}).appendTo(bonuses);
            } else {
                b.offset({left: bonus.column * 22, top: bonus.row * 22}).appendTo(bonuses);
            }
        }

        $(hand).empty();
        for (i = 0,count = gameInfo.handTiles.length; i < count; i++) {
            var tile = gameInfo.handTiles[i];
            handTiles[i] = wm.scribble.tile.createTileWidget(tile).offset({top: 0, left: i * 22}).mousedown(onTileDown).appendTo(hand);
        }

        return playboard.getBoardElement();
    };

    this.getBoardElement = function() {
        return scribble.get(0);
    };

    this.bind = function(event, handler) {
        scribble.bind(event, handler);
    };

    this.unbind = function(event, handler) {
        scribble.unbind(event, handler);
    };

    this.getScoreEngine = function() {
        return null;
    };

    this.getSelectedTiles = function() {
        var tiles = new Array(selectedTileWidgets.length);
        // get tiles
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
                direction = 'h';
            } else if (tiles[0].column == tiles[1].column) {
                direction = 'v';
            } else {
                return null; // not a word
            }

            tiles.sort(function(a, b) {
                if (direction == 'v') {
                    return a.row - b.row;
                } else {
                    return a.column - b.column;
                }
            });

            for (var i = 1, count = tiles.length; i < count; i++) {
                if (direction == 'h' && (tiles[i].row != tiles[i - 1].row || tiles[i].column != tiles[i - 1].column + 1)) {
                    return null; // not a word
                } else if (direction == 'v' && (tiles[i].column != tiles[i - 1].column || tiles[i].row != tiles[i - 1].row + 1)) {
                    return null; // not a word
                }
            }
            var word = "";
            $.each(tiles, function(i, v) {
                word += v.letter
            });
            return {
                start: { row: tiles[0].row, column: tiles[0].column},
                length: tiles.length,
                direction: direction,
                text: word,
                tiles: tiles
            }
        }
    };

    this.makeTurn = function() {
        var word = this.getSelectedWord();
        if (word == null) {
            return false;
        }

        alert($.toJSON(word));

        $.ajax({
            type: 'post',
            url: '/game/playboard/move.ajax',
            dataType: 'json',
            data: $.toJSON(word),
            contentType: 'application/json',
            success: function(data) {
            }
        });
    };

    this.passTurn = function() {
        alert("Not implemented");
    };

    this.exchangeTiles = function(tiles) {
        alert("Not implemented");
    };

//    this.pinSelectedWord = function(newHandTiles) {
//        for (var i = selectedTiles.length - 1; i >= 0; i--) {
//            var tile = selectedTiles[i];
//            wm.scribble.tile.pinTile(tile);
//            tile.onmousedown = null; // no dragging
//            tile.onclick = onTileSelected; // selection
//            changeTileSelection(tile, false);
//        }
//
//        for (i = newHandTiles.length - 1; i >= 0; i--) {
//            var tileInfo = newHandTiles[i];
//
//            var s = document.createElement('span');
//            s.innerText = tileInfo.letter;
//
//            tile = document.createElement('div');
//            tile.appendChild(s);
//
//            tile.id = 'tile' + tileInfo.number;
//            tile.className = 'tile cost' + tileInfo.cost;
//            tile.onmousedown = onTileDown;
//
//            wm.scribble.tile.parseTileInfo(tile);
//            registerHandTile(tile);
//        }
//    };
    this.selectWord = function(word) {
        alert("Not implemented");
    };

    this.selectHistoryWord = function(word) {
        alert("Not implemented");
    };

    this.clearSelection = function() {
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
                tileStateChanged(widget, false, false);
                boardTiles[tile.column][tile.row] = null;
            } else {
                tileStateChanged(widget, false, false);
            }
        }
        scribble.trigger('wordChanged', null);
    };

    $(document).mouseup(onTileUp);
    $(document).mousemove(onTileMove);
};
