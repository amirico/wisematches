/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */
print = function(msg) {
    var c = document.getElementById('console');
    var m = document.createElement('div');
    m.innerHTML = msg;
    c.appendChild(m);
};

if (wm == null) wm = {};
if (wm.scribble == null) wm.scribble = {};

if (wm.g == null) wm.g = new function() {
    this.createMatrix = function() {
        var m = new Array(15);
        for (var i = 0; i < 15; i++) {
            m[i] = new Array(15);
        }
        return m;
    };

    this.addClass = function (ele, cls) {
        if (!this.hasClass(ele, cls)) ele.className += " " + cls;
    };

    this.removeClass = function (ele, cls) {
        ele.className = ele.className.replace(new RegExp('(\\s|^)' + cls + '(\\s|$)'), ' ');
    };

    this.hasClass = function (ele, cls) {
        return ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
    };

    this.getPosition = function (element, event) {
        var loc = this.getLocation(element);
        var pos = this.getPositionOnScreen(event);
        return {x:pos.x - loc.x, y:pos.y - loc.y};
    };

    this.getPositionOnScreen = function(event) {
        event = event || window.event;
        if (event.pageX || event.pageY) {
            return {x:event.pageX, y:event.pageY};
        }
        return {
            x:event.clientX + document.body.scrollLeft - document.body.clientLeft,
            y:event.clientY + document.body.scrollTop - document.body.clientTop
        };
    };

    this.getLocation = function(element) {
        var top = 0;
        var left = 0;

        do {
            top += element.offsetTop;
            left += element.offsetLeft;
            element = element.offsetParent;
        } while (element.offsetParent);
        return {x:left, y:top};
    };

    this.getBounds = function(element) {
        var loc = this.getLocation(element);
        loc.width = element.offsetWidth;
        loc.height = element.offsetHeight;
        return loc;
    };

    this.containsPoint = function(rect, point) {
        return (rect.x >= point.x) && (rect.x + rect.width <= point.x) &&
                (rect.y >= point.y) && (rect.y + rect.height <= point.y);
    }
};

wm.EventSupport = function() {
    var _listeners = {};

    this.addListener = function(type, listener) {
        if (typeof _listeners[type] == "undefined") {
            _listeners[type] = [];
        }
        _listeners[type].push(listener);
    };

    this.removeListener = function(type, listener) {
        if (_listeners[type] instanceof Array) {
            var listeners = _listeners[type];
            for (var i = 0, len = listeners.length; i < len; i++) {
                if (listeners[i] === listener) {
                    listeners.splice(i, 1);
                    break;
                }
            }
        }
    };

    this.fireEvent = function(event) {
        if (typeof event == "string") {
            event = { type: event };
        }
        if (!event.target) {
            event.target = this;
        }

        if (!event.type) {  //falsy
            throw new Error("Event object missing 'type' property.");
        }

        if (_listeners[event.type] instanceof Array) {
            var listeners = _listeners[event.type];
            for (var i = 0, len = listeners.length; i < len; i++) {
                listeners[i].call(this, event);
            }
        }
    };
};

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
        wm.g.addClass(tile, "tile-selected");
        tile.selected = true;
        updateTileImage(tile);
    };

    this.deselectTile = function(tile) {
        wm.g.removeClass(tile, "tile-selected");
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

    var bonuses = wm.g.createMatrix();

    this.BonusType = { DL: {name: '2l'}, TL: {name: '3l'}, DW: {name: '2w'}, TW: {name: '3w'} };

    this.init = function() {
        var bonuses = document.getElementById('playboard').getElementsByTagName('bonuses');
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
    var element = document.createElement('div');
    element.id = 'highlighting';

    var playboard = document.getElementById("playboard");

    this.start = function(tile) {
        element.style.backgroundPosition = -tile.cost * 22 + "px -88px";
        element.style.top = tile.parentNode.offsetTop + tile.offsetTop;
        element.style.left = tile.parentNode.offsetLeft + tile.offsetLeft;
        playboard.appendChild(element);
    };

    this.stop = function(tile) {
        playboard.removeChild(element);
    };

    this.highlight = function(cell) {
        if (cell != null) {
            element.style.top = cell.container.offsetTop + cell.y * 22;
            element.style.left = cell.container.offsetLeft + cell.x * 22;
            element.style.visibility = 'visible';
        } else {
            element.style.top = 0;
            element.style.left = 0;
            element.style.visibility = 'hidden';
        }
    };
};

wm.scribble.Board = function() {
    var draggingTile = null;

    var hand = document.getElementById('hand');
    var board = document.getElementById('board');

    var eventSupport = new wm.EventSupport();
    var scoreEngine = new wm.scribble.ScoreEngine();
    var highlighting = new wm.scribble.Highlighting();

    var handTiles = new Array(7);
    var boardTiles = wm.g.createMatrix();
    var selectedTiles = [];

    var onTileSelected = function() {
        if (wm.scribble.tile.isTileSelected(this)) {
            changeTileSelection(this, false);
        } else {
            changeTileSelection(this, true);
        }
    };

    var onTileDown = function(ev) {
        draggingTile = this;
        draggingTile.mouseOffset = wm.g.getPosition(draggingTile, ev);
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
            var pos = wm.g.getPosition(draggingTile.parentNode, ev);

            draggingTile.style.top = pos.y - draggingTile.mouseOffset.y;
            draggingTile.style.left = pos.x - draggingTile.mouseOffset.x;

            highlighting.highlight(getRelatedCell(ev));
        }
        return false;
    };

    var getRelatedCell = function(ev) {
        var boardPos = wm.g.getPosition(board, ev);
        if (boardPos.x >= 0 && boardPos.x < 330 && boardPos.y >= 0 && boardPos.y < 330) {
            x = (((boardPos.x) / 22) | 0);
            y = (((boardPos.y) / 22) | 0);
            return {x:x, y:y, container: board};
        } else {
            var handPos = wm.g.getPosition(hand, ev);
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
            eventSupport.fireEvent({type:"selected", tile: tile});
        } else {
            removeTileFromSelected(tile);
            wm.scribble.tile.deselectTile(tile);
            eventSupport.fireEvent({type:"deselected", tile: tile});
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

    this.init = function() {
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

    this.addListener = function(type, listener) {
        return eventSupport.addListener(type, listener);
    };

    this.removeListener = function(type, listener) {
        return event.removeListener(type, listener);
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
                numbers: numbers
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
