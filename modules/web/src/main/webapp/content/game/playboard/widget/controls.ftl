<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">

<div id="boardActionsToolbar" class="ui-widget-content ui-corner-all" style="border-top: 0" align="center">
    <div>
        <button id="makeTurnButton" onclick="wm.scribble.controls.makeTurn()">
        <@message code="game.play.make"/>
        </button>
        <button id="clearSelectionButton" onclick="board.clearSelection()">
        <@message code="game.play.clear"/>
        </button>
        <button id="exchangeTilesButton" onclick="wm.scribble.controls.exchangeTiles()">
        <@message code="game.play.exchange"/>
        </button>
    </div>
    <div>
        <button id="passTurnButton" onclick="wm.scribble.controls.passTurn()">
        <@message code="game.play.pass"/>
        </button>
        <button id="resignGameButton" onclick="wm.scribble.controls.resignGame()">
        <@message code="game.play.resign"/>
        </button>
    </div>
</div>

<div id="exchangeTilesPanel" style="display: none;">
    <div><@message code="game.play.exchange.description"/></div>
    <div style="height: 16px; position: relative;"></div>
</div>

<script type="text/javascript">
    wm.scribble.controls = new function() {
        $("#makeTurnButton").button({disabled: true, icons: {primary: 'icon-controls-make'}});
        $("#clearSelectionButton").button({disabled: true, icons: {primary: 'icon-controls-clear'}});
        $("#exchangeTilesButton").button({disabled: true, icons: {primary: 'icon-controls-exchange'}});
        $("#passTurnButton").button({disabled: true, icons: {primary: 'icon-controls-pass'}});
        $("#resignGameButton").button({disabled: true, icons: {primary: 'icon-controls-resign'}});

        var overlayCSS = {
            '-moz-border-radius': '5px',
            '-webkit-border-radius': '5px',
            'border-radius': '5px',
            backgroundColor:'#DFEFFC'
        };

        var onTileSelected = function() {
            if (wm.scribble.tile.isTileSelected(this)) {
                wm.scribble.tile.deselectTile(this);
            } else {
                wm.scribble.tile.selectTile(this);
            }
        };

        var showMoveResult = function(success, message, error) {
            if (success) {
                wm.ui.showGrowl("<@message code="game.move.accepted.label"/>", "<@message code="game.move.accepted.description"/>", 'move-accepted');
            } else {
                wm.ui.showMessage({message: message + (error != null ? error : ''), error:true});
            }
        };

        this.blockBoard = function() {
            $(board.getBoardElement()).parent().block({ message: null, overlayCSS: overlayCSS });
            $("#boardActionsToolbar").block({ message: null, overlayCSS: overlayCSS });
        };

        this.unblockBoard = function() {
            wm.scribble.controls.updateControlsState();

            $("#boardActionsToolbar").unblock();
            $(board.getBoardElement()).parent().unblock();
        };

        this.updateSelectionState = function() {
            $("#clearSelectionButton").removeClass("ui-state-hover").button(board.getSelectedTiles().length == 0 ? "disable" : "enable");
        };

        this.updateControlsState = function() {
            $("#boardActionsToolbar button").removeClass("ui-state-hover");

            this.updateSelectionState();

            $("#makeTurnButton").button(board.isBoardActive() && board.isPlayerActive() && board.getSelectedWord() != null ? "enable" : "disable");
            $("#passTurnButton").button(board.isBoardActive() && board.isPlayerActive() ? "enable" : "disable");
            $("#exchangeTilesButton").button(board.isBoardActive() && board.isPlayerActive() ? "enable" : "disable");
            $("#resignGameButton").button(board.isBoardActive() ? "enable" : "disable");
        };

        this.updateGameState = function(type, state) {
            if (type === 'turn') {
                wm.scribble.controls.updateControlsState();

                if (board.isPlayerActive()) {
                    wm.ui.showGrowl("<@message code="game.move.updated.label"/>", "<@message code="game.move.updated.you"/>", 'your-turn');
                } else {
                    wm.ui.showGrowl("<@message code="game.move.updated.label"/>", "<@message code="game.move.updated.other"/> " + "<b>" + board.getPlayerInfo(state.playerTurn).nickname + "</b>.", 'opponent-turn');
                }
            } else if (type === 'finished') {
                $("#boardActionsToolbar").hide();
                $("#boardActionsToolbar button").button({disabled: true});
                var msg;
                var opts = {autoHide: false};
                if (state.resolution == 'RESIGNED') {
                    msg = "<@message code="game.move.finished.interrupted"/> <b>" + board.getPlayerInfo(state.playerTurn).nickname + "</b>.";
                } else {
                    if (state.winners == undefined || state.winners.length == 0) {
                        msg = "<@message code="game.move.finished.drew"/>";
                    } else {
                        msg = "<@message code="game.move.finished.won"/> ";
                        $.each(state.winners, function(i, pid) {
                            if (i != 0) {
                                msg += ", ";
                            }
                            msg += "<b>" + board.getPlayerInfo(pid).nickname + "</b>";
                        });
                    }
                }
                wm.ui.showGrowl("<@message code="game.move.finished.label"/>", msg + "<div class='closeInfo'><@message code="game.move.clickToClose"/></div>", 'game-finished', opts);
            }
        };

        this.makeTurn = function() {
            board.makeTurn(showMoveResult);
        };

        this.passTurn = function() {
            wm.ui.showConfirm("<@message code="game.move.pass.label"/>", "<@message code="game.move.pass.description"/>", function(approved) {
                if (approved) {
                    board.passTurn(showMoveResult);
                }
            });
        };

        this.resignGame = function() {
            wm.ui.showConfirm("<@message code="game.move.resign.label"/>", "<@message code="game.move.resign.description"/>", function(approved) {
                if (approved) {
                    board.resign(showMoveResult);
                }
            });
        };

        this.exchangeTiles = function() {
            var tilesPanel = $($('#exchangeTilesPanel div').get(1));
            tilesPanel.empty();
            $.each(board.getHandTiles(), function(i, tile) {
                wm.scribble.tile.createTileWidget(tile).offset({top: 0, left: i * 22}).click(onTileSelected).appendTo(tilesPanel);
            });

            $('#exchangeTilesPanel').dialog({
                        title: "<@message code="game.play.exchange.label"/>",
                        draggable: false,
                        modal: true,
                        resizable: false,
                        width: 400,
                        buttons: {
                            "<@message code="game.play.exchange.label"/>": function() {
                                $(this).dialog("close");
                                var tiles = new Array();
                                $.each(tilesPanel.children(), function(i, tw) {
                                    if (wm.scribble.tile.isTileSelected(tw)) {
                                        tiles.push($(tw).data('tile'));
                                    }
                                });
                                board.exchangeTiles(tiles, showMoveResult);
                            },
                            "<@message code="cancel.label"/>": function() {
                                $(this).dialog("close");
                            } }
                    });
        };
        this.updateControlsState();
    };

    board.bind("tileSelection",
            function(event, selected, tile) {
                wm.scribble.controls.updateSelectionState();
            })
            .bind('wordSelection',
            function(event, word) {
                wm.scribble.controls.updateControlsState();
            }).bind('gameState',
            function(event, type, state) {
                wm.scribble.controls.updateGameState(type, state);
            }).bind('boardState',
            function(event, enabled) {
                if (!enabled) {
                    wm.scribble.controls.blockBoard();
                } else {
                    wm.scribble.controls.unblockBoard();
                }
            });
</script>
