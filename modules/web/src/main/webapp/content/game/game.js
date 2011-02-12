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
    this.Events = function() {
        var events = [];

        this.addEventListener = function(event, callback) {
            this.events[event] = this.events[event] || [];
            if (this.events[event]) {
                this.events[event].push(callback);
            }
        };

        this.removeEventListener = function(event, callback) {
            if (this.events[event]) {
                var listeners = this.events[event];
                for (var i = listeners.length - 1; i >= 0; --i) {
                    if (listeners[i] === callback) {
                        listeners.splice(i, 1);
                        return true;
                    }
                }
            }
            return false;
        };

        this.dispatch = function(event) {
            if (this.events[event]) {
                var listeners = this.events[event], len = listeners.length;
                while (len--) {
                    listeners[len](this);	//callback with self
                }
            }
        };
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

wm.scribble.Tile = function(number, letter, cost) {
    var element;

    var tile = this;
    var fastened = false;
    var selected = false;

    var backgroundPosition = 0;

    wm.g.Events.call(this);

    var registerSelectionListener = function() {
        element.onclick = function() {
            if (tile.selected) {
                tile.deselect();
            } else {
                tile.select();
            }
        };
    };

    var updateTileImage = function() {
        element.style.backgroundPosition = "-" + (cost * 22) + "px " + backgroundPosition + "px";
    };

    this.select = function() {
        this.selected = true;
        backgroundPosition -= 22;
        updateTileImage();
        this.dispatch('selected');
    };

    this.deselect = function() {
        this.selected = false;
        backgroundPosition += 22;
        updateTileImage();
        this.dispatch('deselected');
    };

    this.fasten = function() {
        this.fastened = true;
        backgroundPosition -= 44;
        registerSelectionListener();
        updateTileImage();
        this.dispatch('fastened');
    };

    this.isSelected = function() {
        return this.selected;
    };

    this.isFasten = function() {
        return this.fastened;
    };

    this.getElement = function() {
        return element;
    };

    element = function() {
        var element = document.createElement('div');
        element.id = "tile" + number;
        element.className = "tile cost" + cost;
        if (letter != null) {
            element.innerHTML = '<span style="position: relative; top: 3px;">' + letter + '</span>';
        }
        return element;
    }();
    updateTileImage();
};
wm.scribble.Tile.prototype = new wm.g.Events();

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
    var previousCell = null;

    this.highlight = function(cell) {
        if (cell != null) {
            element.style.top = cell.container.offsetTop + cell.y * 22;
            element.style.left = cell.container.offsetLeft + cell.x * 22;
            element.style.visibility = 'visible';
        } else {
            element.style.visibility = 'hidden';
            element.style.top = 0;
            element.style.left = 0;
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

    var mouseDown = function(ev) {
        draggingTile = this;
        draggingTile.mouseOffset = wm.g.getPosition(draggingTile, ev);
        draggingTile.initialState = {
            x:draggingTile.style.left,
            y:draggingTile.style.top,
            z:draggingTile.style.zIndex,
            cell:getRelatedCell(ev)};
        draggingTile.style.zIndex = 333;
        return false;
    };

    var mouseUp = function(ev) {
        if (draggingTile != null) {
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
                    draggingTile.style.backgroundPosition = "0 -22px";
                } else if (cell.container == hand) {
                    handTiles[cell.x] = draggingTile;
                    draggingTile.style.backgroundPosition = "0 0px";
                }
            }

            highlighting.highlight(null);

            draggingTile.style.zIndex = draggingTile.initialState.z;
            draggingTile.mouseOffset = null;
            draggingTile.initialState = null;

            draggingTile = null;
        }
    };

    var mouseMove = function(ev) {
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
        document.onmouseup = mouseUp;
        document.onmousemove = mouseMove;

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
            var handTile = tiles[i];
            print("Found hand tile: " + handTile.id);

            handTiles[i] = handTile;
            handTile.onmousedown = mouseDown;
        }

        var tiles = board.getElementsByTagName('div');
        for (var i = 0; i < tiles.length; i++) {
            var boardTile = tiles[i];
            print("Found board tile: " + boardTile.id);

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

