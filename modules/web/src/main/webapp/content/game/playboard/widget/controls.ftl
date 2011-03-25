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

        var showMoveResult = function(success, message, error) {
            if (success) {
                wm.ui.showGrowl("Move Accepted", "Your move has been accepted and turn has been transferred to next player", 'move-accepted');
            } else {
                wm.ui.showMessage({message: message, error:true});
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

        this.makeTurn = function() {
            board.makeTurn(showMoveResult);
        };

        this.passTurn = function() {
            wm.ui.showConfirm("Pass Turn", "<b>Are you sure to pass the turn?</b> After passing your scores won't be changed and the move will be transferred to next player.", function() {
                board.passTurn(showMoveResult);
            });
        };

        this.exchangeTiles = function() {
            alert("Not Implemented YET");
//            board.exchangeTiles(showMoveResult);
        };

        this.resignGame = function() {
            wm.ui.showConfirm("Resign Game", "<b>Are you sure to resign the game?</b> The game will be finished, your score will be cleared and your rating will be decreased.", function() {
                board.resign(showMoveResult);
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
            }).bind('gameTurn',
            function(event, state) {
                wm.scribble.controls.updateControlsState();

                if (board.isPlayerActive()) {
                    wm.ui.showGrowl("Board State Updated", "<span>It's you turn again!</span> Please select your word and press 'Make Turn' button.", 'your-turn');
                } else {
                    wm.ui.showGrowl("Board State Updated", "Move has been transferred to the player <b>" + board.getPlayerInfo(state.playerTurn).nickname + "</b>.", 'opponent-turn');
                }
            }).bind('gameFinalization',
            function(event, state) {
                $("#boardActionsToolbar").hide();
                $("#boardActionsToolbar button").button({disabled: true});
                var msg;
                var opts = {autoHide: false};
                if (state.state == 'INTERRUPTED') {
                    msg = "Game has been interrupted by <b>" + board.getPlayerInfo(state.playerTurn).nickname + "</b>.";
                } else {
                    msg = "Game has been finished.";
                }
                wm.ui.showGrowl("Game Finished", msg + "<div class='closeInfo'>click to close</div>", 'game-finished', opts);
            }).bind('boardState',
            function(event, enabled) {
                if (!enabled) {
                    wm.scribble.controls.blockBoard();
                } else {
                    wm.scribble.controls.unblockBoard();
                }
            });
</script>
