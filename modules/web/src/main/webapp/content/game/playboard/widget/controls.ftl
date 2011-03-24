<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">

<div id="boardActionsToolbar" class="ui-widget-content ui-corner-all" style="border-top: 0" align="center">
    <div>
        <button id="makeTurnButton" class="icon-make-turn" onclick="wm.scribble.controls.makeTurn()">
        <@message code="game.play.make"/>
        </button>
        <button id="clearSelectionButton" class="icon-clear-word"
                onclick="board.clearSelection()">
        <@message code="game.play.clear"/>
        </button>
        <button id="exchangeTilesButton" class="icon-exchange-tiles" onclick="wm.scribble.controls.exchangeTiles()">
        <@message code="game.play.exchange"/>
        </button>
    </div>
    <div>
        <button id="passTurnButton" class="icon-pass-turn" onclick="wm.scribble.controls.passTurn()">
        <@message code="game.play.pass"/>
        </button>
        <button id="resignGameButton" class="icon-resign-game" onclick="wm.scribble.controls.resignGame()">
        <@message code="game.play.resign"/>
        </button>
    </div>
</div>

<script type="text/javascript">
    wm.scribble.controls = new function() {
        $("#boardActionsToolbar button").button({disabled: true});

        var overlayCSS = {
            '-moz-border-radius': '5px',
            '-webkit-border-radius': '5px',
            'border-radius': '5px',
            backgroundColor:'#DFEFFC'
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
            $("#clearSelectionButton").removeClass("ui-state-hover").button(board.getSelectedTiles().length == 0 ? "disable" : "enabled");
        };

        this.updateControlsState = function() {
            $("#boardActionsToolbar button").removeClass("ui-state-hover");

            this.updateSelectionState();

            $("#makeTurnButton").button(board.isBoardActive() && board.isPlayerActive() && board.getSelectedWord() != null ? "enable" : "disable");
            $("#passTurnButton").button(board.isBoardActive() && board.isPlayerActive() ? "enable" : "disable");
            $("#exchangeTilesButton").button(board.isBoardActive() && board.isPlayerActive() ? "enable" : "disable");
            $("#resignGameButton").button(board.isBoardActive() ? "enable" : "disable");
        };

        this.makeTurn = function() {
            board.makeTurn(function(success, message, error) {
                if (!success) {
                    wm.ui.showMessage({message: message, error:true});
                } else {
                    wm.ui.showGrowl('asd: ' + state, message);
                }
            });
        };

        this.passTurn = function() {
            board.passTurn(function(success, message, error) {
            });
        };

        this.exchangeTiles = function() {
            board.exchangeTiles(function(success, message, error) {
            });
        };

        this.resignGame = function() {
            board.resign(function(success, message, error) {
            });
        };

        // init state
        this.updateControlsState();
    };

    board.bind("tileSelection",
            function(event, selected, tile) {
                wm.scribble.controls.updateSelectionState();
            })
            .bind('wordSelection',
            function(event, selected, word) {
                wm.scribble.controls.updateControlsState();
            }).bind('gameMoves',
            function(event, move) {
                wm.scribble.controls.updateControlsState();

                if (move.player != ${player.getId()}) {
                    if (move.type == 'pass') {
                        wm.ui.showGrowl("Board State Updated", "Player '" + board.getPlayerInfo(move.player).nickname + "' has passed a move");
                    } else {
                        wm.ui.showGrowl("Board State Updated", "Player '" + board.getPlayerInfo(move.player).nickname + "' has made a move: " + move.word.text);
                    }
                }
            }).bind('gameState',
            function(event, state) {
                wm.scribble.controls.updateControlsState();

                if (board.isPlayerActive()) {
                    wm.ui.showGrowl("Board State Updated", "<b>It's you turn again!</b>");
                } else {
                    wm.ui.showGrowl("Board State Updated", "Move has been transferred to player '" + board.getPlayerInfo(state.playerTurn).nickname + "'.");
                }
            }).bind('gameFinalization',
            function(event, state) {
                $("#boardActionsToolbar").hide();
                $("#boardActionsToolbar button").button({disabled: true});
                if (state.state == 'INTERRUPTED') {
                    wm.ui.showMessage({title: "Board State Updated", message: "Game has been interrupted by '" + board.getPlayerInfo(state.playerTurn).nickname + "'.", error: false});
                } else {
                    wm.ui.showGrowl("Board State Updated", "Game has been finished.");
                }
            }).bind('boardState',
            function(event, enabled) {
                if (!enabled) {
                    wm.scribble.controls.blockBoard();
                } else {
                    wm.scribble.controls.unblockBoard();
                }
            });
</script>


