<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">

<#macro separator class="state-static">
<tr class="${class}">
    <td colspan="2">
        <div class="ui-widget-content ui-widget-separator"></div>
    </td>
</tr>
</#macro>

<style type="text/css">
    .state-passive {
        display: none;
    }
</style>

<@wm.widget id="gameInfo" title="game.state.label">
<table width="100%" border="0">
    <tr class="state-static">
        <td><strong>Started:</strong></td>
        <td>
            <div id="gameStartedTime">
            ${gameMessageSource.formatDate(board.startedTime, locale)}
            </div>
        </td>
    </tr>

<@separator class="state-passive"/>
    <tr class="state-passive">
        <td><strong>Finished:</strong></td>
        <td>
            <div id="gameFinishedTime">
            <#--${gameMessageSource.formatDate(board.startedTime, locale)} <span class="sample">(123d 12h 34m)</span>-->
            </div>
        </td>
    </tr>

<@separator class="state-active"/>
    <tr class="state-active">
        <td valign="top"><strong>Progress:</strong></td>
        <td style="padding-top: 2px;">
            <div id="gameProgress" class="ui-progressbar game-progress">
                <div class="ui-progressbar-value ui-corner-left game-progress-board" style="width:0"></div>
                <div class="ui-progressbar-value game-progress-bank" style="width:0"></div>
                <div class="ui-progressbar-value ui-corner-right game-progress-hand" style="width:0"></div>
                <div class="game-progress-caption sample"></div>
            </div>
            <div class="sample" style="text-align: center; font-size: 10px">
            <@message code="game.state.progress.sample"/>
            </div>
        </td>
    </tr>
<@separator class="state-passive"/>
    <tr class="state-passive">
        <td valign="top"><strong>Resolution:</strong></td>
        <td>
            <div id="gameResolution">
                <div class="ui-progressbar game-progress">
                    <div class="ui-progressbar-value ui-corner-all game-progress-finished game-progress-caption sample">
                    <#--Finished-->
                    </div>
                </div>
                <div class="sample" style="text-align: right; font-size: 10px">
                    by <span class="sample game-resolution-player" style="font-style: italic; font-size: 10px"></span>
                <#--by <i>smklimenko</i>-->
                </div>
            </div>
        </td>
    </tr>
<@separator/>
    <tr class="state-static">
        <td><strong>Language:</strong></td>
        <td><@message code="language."+board.gameSettings.language/></td>
    </tr>
<@separator/>
    <tr class="state-static">
        <td><strong>Time Control:</strong></td>
        <td>${board.gameSettings.daysPerMove} days per move</td>
    </tr>

    <#if !board.gameSettings.scratch>
    <@separator/>
        <tr class="state-static">
            <td colspan="2">
                <div style="text-align: center;">
                    <span style="font-style: italic; font-weight: bold;">Game not persistent</span>
                <@wm.info>The game is not persistent and it's state can be lost at any time.</@wm.info>
                </div>
            </td>
        </tr>
    </#if>
</table>
</@wm.widget>

<a id="testLink" href="#">Mark as finished</a>

<#if board.gameActive>
<script type="text/javascript">
    wm.scribble.state = new function() {
        this.updateProgressBar = function() {
            var count = board.getBankCapacity();
            var bo = board.getBoardTilesCount(), ha = board.getHandTilesCount(), ba = board.getBankTilesCount();
            var p3 = Math.round(100 * ha / count), p2 = Math.round(100 * ba / count), p1 = 100 - p3 - p2;

            $("#gameProgress .game-progress-board").css('width', p1 + '%');
            $("#gameProgress .game-progress-bank").css('width', p2 + '%');
            $("#gameProgress .game-progress-hand").css('width', p3 + '%');
            $("#gameProgress .game-progress-caption").text(bo + ' / ' + ba + ' / ' + ha);
        };

        this.markAsFinished = function() {
            $("#gameFinishedTime").html("April 14, 2011 <span class='sample'>(123d 35m)</span>");
            $("#gameResolution .game-progress-caption").text("Time is over");
            $("#gameResolution .game-resolution-player").text("smklimenko");

            $(".state-active").toggle();
            $(".state-passive").toggle();
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

    $("#testLink").click(function() {
        wm.scribble.state.markAsFinished();
    });
</script>
</#if>