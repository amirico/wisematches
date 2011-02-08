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
wm.g = {};
wm.scribble = {};

wm.g.mouse = new function() {
    this.getAbsolutePosition = function(event) {
        event = event || window.event;
        if (event.pageX || event.pageY) {
            return {x:event.pageX, y:event.pageY};
        }
        return {
            x:event.clientX + document.body.scrollLeft - document.body.clientLeft,
            y:event.clientY + document.body.scrollTop - document.body.clientTop
        };
    };

    this.getRelativityPosition = function (element, event) {
        var docPos = wm.g.element.getLocation(element);
        var mousePos = this.getAbsolutePosition(event);
        return {x:mousePos.x - docPos.x, y:mousePos.y - docPos.y};
    };

    this.containsRelativity = function(event, rect) {

    };
};

wm.g.element = new function() {
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
};

wm.scribble.Tile = function(letter, cost) {
    var element;
    var pinned = false;
    var selected = false;
    var tile = this;

    var backgroundPosition = 0;

    var updateTileImage = function() {
        element.style.backgroundPosition = "-" + (cost * 22) + "px " + backgroundPosition + "px";
    };

    this.select = function() {
        this.selected = true;
        backgroundPosition -= 22;
        updateTileImage();
    };

    this.deselect = function() {
        this.selected = false;
        backgroundPosition += 22;
        updateTileImage();
    };

    this.pin = function() {
        this.pinned = true;
        backgroundPosition -= 44;
        updateTileImage();
    };

    this.getElement = function() {
        return element;
    };

    element = function() {
        var element = document.createElement('div');
        element.onclick = function() {
            if (tile.selected) {
                tile.deselect();
            } else {
                tile.select();
            }
        };
        element.id = "tile12";
        element.className = "tile cost" + cost;
        if (letter != null) {
            element.innerHTML = '<span style="position: relative; top: 3px;">' + letter + '</span>';
        }
        return element;
    }();
    updateTileImage();
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

wm.scribble.Board = function() {
    var draggingTile = null;

    var board = document.getElementById('tiles');

    var scoreEngine = new wm.scribble.ScoreEngine();

    var boardTiles = function() {
        var bonuses = new Array(15);

        for (var i = 0; i < 15; i++) {
            bonuses[i] = new Array(15);
        }
        return bonuses;
    }();

    var isBoardPosition = function(pos) {
        var p = wm.scribble.utils.getPosition(board);
        return
    };

    var mouseDown = function(ev) {
        draggingTile = this;
        draggingTile.mouseOffset = wm.scribble.utils.getMouseOffset(this, ev);
        draggingTile.initialPosition = {x:draggingTile.style.left, y:draggingTile.style.top, z:draggingTile.style.zIndex};
        draggingTile.style.zIndex = 333;
        return false;
    };

    var mouseUp = function(ev) {
        if (draggingTile != null) {
            var cell = getBoardCell(ev);
            if (cell != null && boardTiles[cell.x][cell.y] == undefined) {
                print("Drop position: " + cell.x + "," + cell.y);

                draggingTile.style.top = cell.y * 22;
                draggingTile.style.left = cell.x * 22;

                boardTiles[cell.x][cell.y] = draggingTile;
            } else {
                print("Drop position incorrect. Revert.");

                draggingTile.style.top = draggingTile.initialPosition.y;
                draggingTile.style.left = draggingTile.initialPosition.x;
            }

            draggingTile.style.zIndex = draggingTile.initialPosition.z;

            draggingTile.mouseOffset = null;
            draggingTile.initialPosition = null;

            draggingTile = null;

            var b = document.getElementById('cellH');
            b.style.visibility = 'hidden';
        }
    };

    var mouseMove = function(ev) {
        if (draggingTile != null) {
            var pos = wm.scribble.utils.getMouseOffset(board, ev);

            draggingTile.style.position = 'absolute';
            draggingTile.style.top = pos.y - draggingTile.mouseOffset.y;
            draggingTile.style.left = pos.x - draggingTile.mouseOffset.x;

            var cell = getBoardCell(ev);
            if (cell != null) {
                var b = document.getElementById('cellH');
                b.style.top = cell.y * 22;
                b.style.left = cell.x * 22;
                b.style.visibility = 'visible';
            }
        }
        return false;
    };

    var getBoardCell = function(ev) {
        var pos = wm.scribble.utils.getMouseOffset(board, ev);
        print("P: " + pos.x + "," + pos.y);

        if (pos.x >= 0 && pos.x <= 330 && pos.y >= 0 && pos.y <= 330) {
            var x = (((pos.x) / 22) | 0);
            var y = (((pos.y) / 22) | 0);
            return {x:x, y:y, type:'board'};
        } else if (pos.x > 88 && pos.x < 242 && pos.y >= 336 && pos.y <= 352) {
            var x = (((pos.x) / 22) | 0);
            var y = (((pos.y) / 22) | 0);
            return {x:x, y:y, type:'hand'};
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
                    e.style.position = "absolute";
                    e.style.top = i * 22 + "px";
                    e.style.left = j * 22 + "px";
                    bonuses.appendChild(e);
                }
            }
        }

        var hand = document.getElementById('tiles');
        for (var k = 0; k < 7; k++) {
            var t2 = new wm.scribble.Tile('F', k);
            t2.getElement().style.position = 'absolute';
            t2.getElement().style.top = 22 * 15 + 4 + "px";
            t2.getElement().style.left = 22 * 4 + 22 * k + "px";

            t2.getElement().onmousedown = mouseDown;
            hand.appendChild(t2.getElement());
        }
    };

    this.getScoreEngine = function() {
        return scoreEngine;
    };

};

