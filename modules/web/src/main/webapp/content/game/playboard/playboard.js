/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */
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

    this.parseTileInfo = function(tile) {
        tile.cost = tile.className.match(/cost([0-9]*)/i)[1];
        tile.number = tile.id.match(/tile([0-9]*)/i)[1];
        tile.selected = false;
        tile.pinned = false;
        tile.letter = tile.innerText;
        updateTileImage(tile);
        return tile;
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

wm.scribble.Highlighting = function() {
    var element = $('<div></div>').attr('id', 'highlighting');
    var playboard = $('#container');

    this.start = function(tile) {
        element.css('backgroundPosition', -tile.cost * 22 + "px -88px");
        element.css('top', tile.parentNode.offsetTop + tile.offsetTop + 'px');
        element.css('left', tile.parentNode.offsetLeft + tile.offsetLeft + 'px');
        element.appendTo(playboard);
    };

    this.stop = function(tile) {
        element.detach();
    };

    this.highlight = function(cell) {
        if (cell != null) {
            element.css('top', cell.container.offsetTop + cell.y * 22 + 'px');
            element.css('left', cell.container.offsetLeft + cell.x * 22 + 'px');
            element.show();
        } else {
            element.hide();
            element.offset({top:0, left:0});
        }
    };
};

wm.scribble.Board = function() {
    var scribble;
    var hand;
    var board;

    var scoreEngine;
    var highlighting;

    var handTiles = new Array(7);
    var boardTiles = wm.util.createMatrix(15);

    var selectedTiles = [];
    var draggingTile = null;

    var playboard = this;

    this.init = function() {
        scribble = $('#scribble');
        hand = document.getElementById('hand');
        board = document.getElementById('board');

        scoreEngine = new wm.scribble.ScoreEngine();
        highlighting = new wm.scribble.Highlighting();

        document.onmouseup = onTileUp;
        document.onmousemove = onTileMove;

        scoreEngine.init();

        var tiles = hand.getElementsByTagName('div');
        for (var i = 0; i < tiles.length; i++) {
            var handTile = wm.scribble.tile.parseTileInfo(tiles[i]);
            handTile.onmousedown = onTileDown;

            handTile.cell = {x:i, y:0, container: hand};
            handTiles[i] = handTile;
        }

        tiles = board.getElementsByTagName('div');
        for (i = 0; i < tiles.length; i++) {
            var boardTile = wm.scribble.tile.parseTileInfo(tiles[i]);
            wm.scribble.tile.pinTile(boardTile);
            boardTile.onclick = onTileSelected;

            boardTile.cell = {x:boardTile.offsetLeft / 22, y:boardTile.offsetTop / 22, container: board};
            boardTiles[boardTile.cell.x][boardTile.cell.y] = boardTile;
        }
    };

    var onTileSelected = function() {
        if (wm.scribble.tile.isTileSelected(this)) {
            changeTileSelection(this, false);
        } else {
            changeTileSelection(this, true);
        }
    };

    var onTileDown = function(ev) {
        draggingTile = this;
        draggingTile.mouseOffset = wm.ui.getPosition(draggingTile, ev);
        draggingTile.initialState = {
            x:draggingTile.style.left,
            y:draggingTile.style.top,
            z:draggingTile.style.zIndex};
        draggingTile.style.zIndex = 333;
        highlighting.start(draggingTile);
        return false;
    };

    var onTileUp = function(ev) {
        if (draggingTile == null || draggingTile == undefined) {
            return true;
        }

        var cell = getRelatedCell(ev);
        if (cell == null ||
                (cell.container == board && boardTiles[cell.x][cell.y] != undefined) ||
                (cell.container == hand && handTiles[cell.x] != undefined)) {
            draggingTile.style.top = draggingTile.initialState.y;
            draggingTile.style.left = draggingTile.initialState.x;
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

            // update cell
            draggingTile.cell = cell;

            // mark new position
            if (cell.container == board) {
                boardTiles[cell.x][cell.y] = draggingTile;
                changeTileSelection(draggingTile, true);
            } else if (cell.container == hand) {
                handTiles[cell.x] = draggingTile;
                changeTileSelection(draggingTile, false);
            }
        }

        highlighting.stop(draggingTile);

        draggingTile.style.zIndex = draggingTile.initialState.z;
        draggingTile.mouseOffset = null;
        draggingTile.initialState = null;

        draggingTile = null;
    };

    var onTileMove = function(ev) {
        if (draggingTile != null) {
            var pos = wm.ui.getPosition(draggingTile.parentNode, ev);

            draggingTile.style.top = pos.y - draggingTile.mouseOffset.y;
            draggingTile.style.left = pos.x - draggingTile.mouseOffset.x;

            highlighting.highlight(getRelatedCell(ev));
        }
        return false;
    };

    var getRelatedCell = function(ev) {
        var boardPos = wm.ui.getPosition(board, ev);
        if (boardPos.x >= 0 && boardPos.x < 330 && boardPos.y >= 0 && boardPos.y < 330) {
            x = (((boardPos.x) / 22) | 0);
            y = (((boardPos.y) / 22) | 0);
            return {x:x, y:y, container: board};
        } else {
            var handPos = wm.ui.getPosition(hand, ev);
            if (handPos.x >= 0 && handPos.x < 154 && handPos.y >= 0 && handPos.y < 22) {
                x = (((handPos.x) / 22) | 0);
                y = (((handPos.y) / 22) | 0);
                return {x:x, y:y, container: hand};
            }
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

    this.getScoreEngine = function() {
        return scoreEngine;
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

    this.pinSelectedWord = function(newHandTiles) {
        for (var i = selectedTiles.length - 1; i >= 0; i--) {
            var tile = selectedTiles[i];
            wm.scribble.tile.pinTile(tile);
            tile.onmousedown = null; // no dragging
            tile.onclick = onTileSelected; // selection
            changeTileSelection(tile, false);
        }

        for (i = newHandTiles.length - 1; i >= 0; i--) {
            var tileInfo = newHandTiles[i];

            var s = document.createElement('span');
            s.innerText = tileInfo.letter;

            tile = document.createElement('div');
            tile.appendChild(s);

            tile.id = 'tile' + tileInfo.number;
            tile.className = 'tile cost' + tileInfo.cost;
            tile.onmousedown = onTileDown;

            wm.scribble.tile.parseTileInfo(tile);
            registerHandTile(tile);
        }
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
};
