<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">

<@wm.widget id="gameInfo" title="game.state.label">
<table width="100%" border="0">
    <tr>
        <td valign="top"><b><@message code="game.state.progress.label"/>:</b></td>
        <td width="100%" style="padding-top: 2px;">
            <#if board.gameActive>
                <div id="gameProgressAction">
                    <div class="ui-progressbar game-progress">
                        <div class="ui-progressbar-value ui-corner-left game-progress-board" style="width:0"></div>
                        <div class="ui-progressbar-value game-progress-bank" style="width:0"></div>
                        <div class="ui-progressbar-value ui-corner-right game-progress-hand" style="width:0"></div>
                        <div class="game-progress-caption sample"></div>
                    </div>
                    <span class="sample"
                          style="font-size: smaller;"><@message code="game.state.progress.sample"/></span>
                </div>
            </#if>
            <div id="gameProgressFinished" <#if board.gameActive>style="display: none;"</#if>>
                <div class="ui-progressbar game-progress">
                    <div class="ui-progressbar-value ui-corner-all game-progress-finished game-progress-caption sample"
                         style="width: 100%;"><@message code="game.state.finished.label"/></div>
                </div>
            </div>
        </td>
    </tr>
    <tr>

        <td colspan="2">
            <div class="ui-widget-content ui-widget-separator"></div>
        </td>
    </tr>
    <tr>
        <td><b><@message code="game.state.started.label"/>:</b></td>
        <td width="100%">${gameMessageSource.formatDate(board.startedTime, locale)}</td>
    </tr>
    <#if board.gameActive>
        <tr>
            <td><b><@message code="game.state.moved.label"/>:</b></td>
            <td width="100%">${gameMessageSource.formatDate(board.lastMoveTime, locale)}</td>
        </tr>
        <#else>
            <tr>
                <td><b><@message code="game.state.finished.label"/>:</b></td>
                <td width="100%">${gameMessageSource.formatDate(board.finishedTime, locale)}</td>
            </tr>
    </#if>
</table>
</@wm.widget>

<#if board.gameActive>
<script type="text/javascript">
    wm.scribble.state = new function() {
        this.updateProgressBar = function() {
            var count = board.getBankCapacity();
            var bo = board.getBoardTilesCount(), ha = board.getHandTilesCount(), ba = board.getBankTilesCount();
            var p3 = Math.round(100 * ha / count), p2 = Math.round(100 * ba / count), p1 = 100 - p3 - p2;
            var divs = $("#gameProgressAction");

            $("#gameProgressAction .game-progress-board").css('width', p1 + '%');
            $("#gameProgressAction .game-progress-bank").css('width', p2 + '%');
            $("#gameProgressAction .game-progress-hand").css('width', p3 + '%');
            $("#gameProgressAction .game-progress-caption").text(bo + ' / ' + ba + ' / ' + ha);
        };

        this.markAsFinished = function() {
            $("#gameProgressAction").toggle();
            $("#gameProgressFinished").toggle();
        };
    };

    wm.scribble.state.updateProgressBar();

    board.bind('gameState',
            function(event, type, state) {
                if (type === 'finished') {
                    wm.scribble.state.markAsFinished();
                }
            }).bind('gameMoves',
            function(event, move) {
                $($("#gameInfo table td").get(6)).text(move.timeMessage);
                wm.scribble.state.updateProgressBar();
            });
</script>
</#if>