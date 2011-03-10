/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */
function print(msg) {
    $("<div></div>").text(msg).appendTo("#console");
}

if (wm == null) wm = {};
if (wm.scribble == null) wm.scribble = {};

wm.scribble.tile = new function() {
    var updateTileImage = function(tile) {
        var k = 0;
        if (tile.selected) {
            k += 22;
        }
        if (tile.pinned) {
            k += 44;
        }
        tile.style.backgroundPosition = tile.cost * -22 + "px" + " -" + k + "px";
    };

    this.createTileWidget = function(tile) {
        return $("<div></div>").addClass("tile cost" + tile.cost).css('background-position', '-' + tile.cost * 22 + 'px 0').append($("<span></span>").text(tile.letter)).data('tile', tile);
    };

    this.selectTile = function(tile) {
        $(tile).addClass("tile-selected");
        tile.selected = true;
        updateTileImage(tile);
    };

    this.deselectTile = function(tile) {
        $(tile).removeClass("tile-selected");
        tile.selected = false;
        updateTileImage(tile);
    };

    this.pinTile = function(tile) {
        tile.pinned = true;
        updateTileImage(tile);
    };

    this.isTileSelected = function(tile) {
        return tile.selected;
    };

    this.getLetter = function(tile) {
        return tile.innerText;
    };

    this.isTilePined = function(tile) {
        return tile.pinned;
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
    var element = $('<div></div>').addClass('highlighting').appendTo(gameField);

    this.start = function(tileWidget) {
        element.css('backgroundPosition', -tileWidget.data('tile').cost * 22 + "px -88px");
        element.offset(tileWidget.offset());
    };

    this.stop = function(tile) {
        element.hide();
        element.offset({top:0, left:0});
    };

    this.highlight = function(cell) {
        if (cell != null) {
            var offset = cell.container.offset();
            element.offset({left: offset.left + cell.x * 22, top: offset.top + cell.y * 22});
            element.show();
        } else {
            element.hide();
            element.offset({top:0, left:0});
        }
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

    var selectedTiles = [];
    var draggingTile = null;

    var highlighting = new wm.scribble.Highlighting(gameField);

    var onTileSelected = function() {
        if (wm.scribble.tile.isTileSelected(this)) {
            changeTileSelection(this, false);
        } else {
            changeTileSelection(this, true);
        }
    };

    var onTileDown = function(ev) {
        print("Start");
        draggingTile = $(this);
        var offset = draggingTile.offset();
        draggingTile.data('mouseOffset', {left: ev.pageX - offset.left, top: ev.pageY - offset.top});
        draggingTile.data('initialState', { offset: offset, zIndex: draggingTile.css('zIndex')});
        draggingTile.css('zIndex', 333);
        highlighting.start(draggingTile);
    };

    var onTileMove = function(ev) {
        if (draggingTile != null) {
            var tileOffset = draggingTile.data('mouseOffset');
            $(draggingTile).offset({left: ev.pageX - tileOffset.left, top: ev.pageY - tileOffset.top});
            highlighting.highlight(getRelatedCell(ev));
        }
    };

    var onTileUp = function(ev) {
        if (draggingTile == null || draggingTile == undefined) {
            return true;
        }

        var cell = getRelatedCell(ev);
        if (cell == null ||
                (cell.container == board && boardTiles[cell.x][cell.y] != undefined) ||
                (cell.container == hand && handTiles[cell.x] != undefined)) {
            draggingTile.offset(draggingTile.data('initialState').offset);
        } else {
            // clear original position
            if (draggingTile.cell.container == board) {
                boardTiles[draggingTile.cell.x][draggingTile.cell.y] = null;
            } else if (draggingTile.cell.container == hand) {
                handTiles[draggingTile.cell.x] = null;
            }

            // move to new position
            if (draggingTile.parentNode != cell.container) {
                draggingTile.parentNode.removeChild(draggingTile);
                draggingTile.style.top = cell.y * 22;
                draggingTile.style.left = cell.x * 22;
                cell.container.appendChild(draggingTile);
            } else {
                draggingTile.style.top = cell.y * 22;
                draggingTile.style.left = cell.x * 22;
            }

            // mark new position
            if (cell.container == board) {
                boardTiles[cell.x][cell.y] = draggingTile;
//                changeTileSelection(draggingTile, true);
            } else if (cell.container == hand) {
                handTiles[cell.x] = draggingTile;
//                changeTileSelection(draggingTile, false);
            }
        }

        highlighting.stop(draggingTile);

        draggingTile.css('zIndex', draggingTile.data('initialState').zIndex);
        draggingTile.removeData('mouseOffset');
        draggingTile.removeData('initialState');

        draggingTile = null;
    };

    var getRelatedCell = function(ev) {
        var bo = board.offset();
        var x = (((ev.pageX - bo.left) / 22) | 0);
        var y = (((ev.pageY - bo.top) / 22) | 0);
        if (x >= 0 && x <= 14 && y >= 0 && y <= 14) {
            return {x:x, y:y, container: board};
        }

        var ho = hand.offset();
        x = (((ev.pageX - ho.left) / 22) | 0);
        y = (((ev.pageY - ho.top) / 22) | 0);
        if (x >= 0 && x <= 6 && y == 0) {
            return {x:x, y:y, container: hand};
        }
        return null;
    };

    var addTileToSelected = function(tile) {
        removeTileFromSelected(tile);
        selectedTiles.push(tile);
    };

    var removeTileFromSelected = function(tile) {
        for (var i = 0, len = selectedTiles.length; i < len; i++) {
            if (selectedTiles[i] == tile) {
                break;
            }
        }
        if (i != selectedTiles.length) {
            selectedTiles.splice(i, 1);
            return true;
        }
        return false;
    };

    var changeTileSelection = function(tile, select) {
        if (select) {
            addTileToSelected(tile);
            wm.scribble.tile.selectTile(tile);
            scribble.trigger('selected', [tile, playboard.getSelectedTiles(), playboard.getSelectedWord()]);
        } else {
            removeTileFromSelected(tile);
            wm.scribble.tile.deselectTile(tile);
            scribble.trigger('deselected', [tile, playboard.getSelectedTiles(), playboard.getSelectedWord()]);
        }
    };

    var registerHandTile = function(tile) {
        for (var j = 0, len2 = handTiles.length; j < len2; j++) {
            if (handTiles[j] == undefined) {
                break;
            }
        }
        tile.cell = {x:j, y:0, container: hand};
        tile.style.top = 0;
        tile.style.left = j * 22;
        tile.cell.container.appendChild(tile);
        handTiles[j] = tile;
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
            var handTile = wm.scribble.tile.createTileWidget(tile).offset({top: 0, left: i * 22}).mousedown(onTileDown).appendTo(hand);
            handTiles[i] = handTile;
        }

        return this.getBoardElement();
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
        return selectedTiles;
    };

    this.getSelectedWord = function() {
        if (selectedTiles instanceof Array && selectedTiles.length > 1) {
            var v = true;
            var h = true;
            for (i = 1,len = selectedTiles.length; i < len; i++) {
                if (selectedTiles[0].cell.x != selectedTiles[i].cell.x) {
                    v = false;
                }
                if (selectedTiles[0].cell.y != selectedTiles[i].cell.y) {
                    h = false;
                }
            }

            var direction, stopper;
            if (!v && !h) {
                return null;
            } else if (!v) {
                direction = 'x';
                stopper = 'y';
            } else {
                direction = 'y';
                stopper = 'x';
            }

            var sortedTiles = selectedTiles.sort(function(a, b) {
                return a.cell[direction] - b.cell[direction];
            });
            var tile = sortedTiles[0];
            var numbers = [tile.number];
            var word = wm.scribble.tile.getLetter(tile);
            for (var i = 1, len = sortedTiles.length; i < len; i++) {
                if (tile.cell[stopper] != sortedTiles[i].cell[stopper]) {
                    return null;
                }
                if (Math.abs(tile.cell[direction] - sortedTiles[i].cell[direction]) != 1) {
                    return null;
                }
                tile = sortedTiles[i];
                numbers[i] = tile.number;
                word += wm.scribble.tile.getLetter(tile);
            }
            return {
                start: { row: sortedTiles[0].cell.y, col: sortedTiles[0].cell.x},
                end: {row: sortedTiles[sortedTiles.length - 1].cell.y, col: sortedTiles[sortedTiles.length - 1].cell.x},
                text: word,
                tiles: sortedTiles
            };
        }
        return null;
    };

    this.makeTurn = function() {
        alert("Not implemented");
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
        for (var i = selectedTiles.length - 1; i >= 0; i--) {
            var tile = selectedTiles[i];
            if (!wm.scribble.tile.isTilePined(tile)) {
                tile.parentNode.removeChild(tile);
                boardTiles[tile.cell.x][tile.cell.y] = null;
                changeTileSelection(tile, false);
                registerHandTile(tile);
            } else {
                changeTileSelection(tile, false);
            }
        }
    };

    $(document).mouseup(onTileUp);
    $(document).mousemove(onTileMove);
};
