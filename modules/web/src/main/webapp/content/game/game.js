/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */
print = function(msg) {
    var c = document.getElementById('console');
    var m = document.createElement('div');
    m.innerHTML = msg;
    c.appendChild(m);
};

wm = {};
wm.scribble = {};

wm.g = new function() {
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

wm.Events = function() {
    this.addListener = function(type, listener) {
        if (typeof this._listeners[type] == "undefined") {
            this._listeners[type] = [];
        }

        this._listeners[type].push(listener);
    };

    this.removeListener = function(type, listener) {
        if (this._listeners[type] instanceof Array) {
            var listeners = this._listeners[type];
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

        if (this._listeners[event.type] instanceof Array) {
            var listeners = this._listeners[event.type];
            for (var i = 0, len = listeners.length; i < len; i++) {
                listeners[i].call(this, event);
            }
        }
    };
};

wm.scribble.tile = new function() {
    this.parseTileInfo = function(tile) {
        tile.cost = tile.className.match(/cost([0-9]*)/i)[1];
        tile.number = tile.id.match(/tile([0-9]*)/i)[1];
        return tile;
    };

    this.selectTile = function(tile) {
        var k = 22;
        if (tile.pinned) {
            k += 44;
        }
        wm.g.addClass(tile, "tile-selected");
        tile.style.backgroundPosition = tile.cost * -22 + "px" + " -" + k + "px";
        tile.selected = true;
    };

    this.deselectTile = function(tile) {
        var k = 0;
        if (tile.pinned) {
            k += 44;
        }
        wm.g.removeClass(tile, "tile-selected");
        tile.style.backgroundPosition = tile.cost * -22 + "px" + " -" + k + "px";
        tile.selected = null;
    };

    this.isTileSelected = function(tile) {
        return tile.selected;
    };
};

wm.scribble.ScoreEngine = function() {
    var emptyHandBonus = 33;
    var bonuses = new Array(15);

    for (var i = 0; i < 15; i++) {
        bonuses[i] = new Array(15);
    }

    this.BonusType = { DL: {name: '2l'}, TL: {name: '3l'}, DW: {name: '2w'}, TW: {name: '3w'} };

    bonuses[0][0] = this.BonusType.DL;
    bonuses[1][3] = this.BonusType.TL;
    bonuses[3][3] = this.BonusType.DW;
    bonuses[7][5] = this.BonusType.TW;

    this.init = function(bonuses, emptyHandBonus) {
        this.bonuses = bonuses;
        this.emptyHandBonus = emptyHandBonus;
    };


    this.getCellBonus = function(row, col) {
        return bonuses[row][col];
    }
};

wm.scribble.Highlighting = function() {
    var element = document.getElementById("highlighting");

    this.init = function(tile) {
        element.style.backgroundPosition = -tile.cost * 22 + "px -88px";
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
    var scoreEngine = new wm.scribble.ScoreEngine();
    var highlighting = new wm.scribble.Highlighting();

    var handTiles = new Array(7);
    var boardTiles = function() {
        var cells = new Array(15);
        for (var i = 0; i < 15; i++) {
            cells[i] = new Array(15);
        }
        return cells;
    }();

    var _listeners = {};
    wm.g.Events.call(this);

    var onTileSelected = function() {
        if (wm.scribble.tile.isTileSelected(this)) {
            wm.scribble.tile.deselectTile(this);
            events.dispatch('deselected', this);
        } else {
            events.dispatch('selected', this);
            wm.scribble.tile.selectTile(this);
        }
    };

    var onTileDown = function(ev) {
        draggingTile = this;
        draggingTile.mouseOffset = wm.g.getPosition(draggingTile, ev);
        draggingTile.initialState = {
            x:draggingTile.style.left,
            y:draggingTile.style.top,
            z:draggingTile.style.zIndex,
            cell:getRelatedCell(ev)};
        draggingTile.style.zIndex = 333;
        highlighting.init(draggingTile);
        return false;
    };

    var onTileUp = function(ev) {
        if (draggingTile == null) {
            return true;
        }
        var cell = getRelatedCell(ev);
        if (cell == null ||
                (cell.container == board && boardTiles[cell.x][cell.y] != undefined) ||
                (cell.container == hand && handTiles[cell.x] != undefined)) {
            print("Drop position incorrect. Revert.");

            draggingTile.style.top = draggingTile.initialState.y;
            draggingTile.style.left = draggingTile.initialState.x;
        } else {
            // clear original position
            if (draggingTile.initialState.cell.container == board) {
                boardTiles[draggingTile.initialState.cell.x][draggingTile.initialState.cell.y] = null;
            } else if (draggingTile.initialState.cell.container == hand) {
                handTiles[draggingTile.initialState.cell.x] = null;
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
                wm.scribble.tile.selectTile(draggingTile);
            } else if (cell.container == hand) {
                handTiles[cell.x] = draggingTile;
                wm.scribble.tile.deselectTile(draggingTile);
            }
        }

        highlighting.highlight(null);

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


    this.init = function() {
        print("start");
        document.onmouseup = onTileUp;
        document.onmousemove = onTileMove;

        var bonuses = document.getElementById('bonuses');
        for (var i = 0; i < 15; i++) {
            for (var j = 0; j < 15; j++) {
                var b = scoreEngine.getCellBonus(i, j);
                if (b != null) {
                    var e = document.createElement('div');
                    e.className = "cell bonus-cell-" + b.name;
                    e.style.top = i * 22 + "px";
                    e.style.left = j * 22 + "px";
                    bonuses.appendChild(e);
                }
            }
        }

        var tiles = hand.getElementsByTagName('div');
        for (var i = 0; i < tiles.length; i++) {
            var handTile = wm.scribble.tile.parseTileInfo(tiles[i]);
            print("Found hand tile: " + handTile.id + ", " + handTile.number + ", " + handTile.cost);

            handTiles[i] = handTile;
            handTile.onmousedown = onTileDown;
        }

        var tiles = board.getElementsByTagName('div');
        for (var i = 0; i < tiles.length; i++) {
            var boardTile = wm.scribble.tile.parseTileInfo(tiles[i]);
            print("Found board tile: " + boardTile.id + ", " + boardTile.number + ", " + boardTile.cost);


            boardTile.pinned = true;
            boardTile.onclick = onTileSelected;

            var y = boardTile.offsetTop / 22;
            var x = boardTile.offsetLeft / 22;

            print("Tile position: " + x + ", " + y);
            boardTiles[x][y] = boardTile;
        }
    };

    this.getScoreEngine = function() {
        return scoreEngine;
    };
};
wm.scribble.Board.prototype = new wm.g.Events();
