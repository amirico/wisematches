<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">

<@wm.widget id="scribbleBoard" style="width: 100%" title="<center>${board.gameSettings.title} #${board.boardId}</center>"/>
<#if !viewMode>
<div id="boardActionsToolbar" class="ui-widget-content ui-corner-all" style="border-top: 0" align="center">
    <div>
        <button id="makeTurnButton" class="icon-make-turn"
                onclick="board.makeTurn()">
        <@message code="game.play.make"/>
        </button>
        <button id="clearSelectionButton" class="icon-clear-word"
                onclick="board.clearSelection()">
        <@message code="game.play.clear"/>
        </button>
        <button id="exchangeTilesButton" class="icon-exchange-tiles"
                onclick="board.exchangeTiles()">
        <@message code="game.play.exchange"/>
        </button>
    </div>
    <div>
        <button id="passTurnButton" class="icon-pass-turn"
                onclick="board.passTurn()">
        <@message code="game.play.pass"/>
        </button>
        <button id="resignGameButton" class="icon-resign-game" onclick="board.resign()">
        <@message code="game.play.resign"/>
        </button>
    </div>
</div>
</#if>

<script type="text/javascript">
    $("#scribbleBoard").prepend(board.getBoardElement());

    <#if !viewMode>
    $("#boardActionsToolbar button").button({disabled: true});
    $("#resignGameButton").button("enable"); // resign available in any time

    if (board.isPlayerActive()) {
        $("#passTurnButton").button("enable");
        $("#exchangeTilesButton").button("enable");
    }
    board.bind('wordChanged', function(event, word) {
        $("#makeTurnButton").button(word == null || !board.isPlayerActive() ? "disable" : "enable").removeClass("ui-state-hover");
    });
    board.bind('playerMoved', function(event, gameMove) {
        board.clearSelection();
        $("#passTurnButton").button(board.isPlayerActive() ? "enable" : "disable").removeClass("ui-state-hover");
        $("#exchangeTilesButton").button(board.isPlayerActive() ? "enable" : "disable").removeClass("ui-state-hover");
    });

    board.bind("tileSelected", function(event, tile) {
        if (board.getSelectedTiles().length != 0) {
            $("#clearSelectionButton").button('enable');
        }
    });
    board.bind("tileDeselected", function(event, tile) {
        if (board.getSelectedTiles().length == 0) {
            $("#clearSelectionButton").button('disable').removeClass("ui-state-hover");
        }
    });
    </#if>
</script>
