<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">

<#macro separator>
<tr>
    <td colspan="2">
        <div class="ui-widget-content ui-widget-separator"></div>
    </td>
</tr>
</#macro>

<@wm.widget id="gameInfo" title="game.state.label">
<table width="100%" border="0">
    <tr>
        <td><strong>Started:</strong></td>
    <#--<td><b><@message code="game.state.started.label"/>:</b></td>-->
        <td width="100%">${gameMessageSource.formatDate(board.startedTime, locale)}</td>
    </tr>
<@separator/>
    <tr>
        <td><strong>Language:</strong></td>
        <td width="100%"><@message code="language."+board.gameSettings.language/></td>
    </tr>
<@separator/>
    <tr>
        <td valign="top"><strong>Progress:</strong></td>
    <#--<td valign="top"><b><@message code="game.state.progress.label"/>:</b></td>-->
        <td width="100%" style="padding-top: 2px;">
        <#--<#if board.gameActive>-->
            <div id="gameProgressAction">
                <div class="ui-progressbar game-progress">
                    <div class="ui-progressbar-value ui-corner-left game-progress-board" style="width:0"></div>
                    <div class="ui-progressbar-value game-progress-bank" style="width:0"></div>
                    <div class="ui-progressbar-value ui-corner-right game-progress-hand" style="width:0"></div>
                    <div class="game-progress-caption sample"></div>
                </div>
                <div style="text-align: center;">
                    <span class="sample"
                          style="font-size: smaller;"><@message code="game.state.progress.sample"/></span>
                </div>
            </div>
        <#--</#if>-->
            <div id="gameProgressFinished">
            <#--<#if board.gameActive>style="display: none;"</#if>>-->
                <div class="ui-progressbar game-progress">
                    <div class="ui-progressbar-value ui-corner-all game-progress-finished game-progress-caption sample"
                         style="width: 100%;"><@message code="game.state.finished.label"/></div>
                </div>
                <div style="text-align: right;">
                    <div>
                        <span class="sample" style="font-size: smaller;">by <i>smklimenko</i></span>
                    </div>
                    <div>
                        <span class="sample" style="font-size: smaller;">time is over for <i>smklimenko</i></span>
                    </div>
                    <div>
                        <span class="sample" style="font-size: smaller;">resigned by <i>smklimenko</i></span>
                    </div>
                    <div>
                        <span class="sample" style="font-size: smaller;">stalemate</span>
                    </div>
                </div>
            </div>
        </td>
    </tr>
<@separator/>
    <tr>
        <td><strong>Time Control:</strong></td>
        <td width="100%">${board.gameSettings.daysPerMove} days per move</td>
    </tr>
    <#if !board.gameSettings.scratch>
        <tr>
            <td colspan="2">
                <div class="ui-widget-content ui-widget-separator"></div>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <div style="text-align: center;">
                    <span style="font-style: italic; font-weight: bold;">Game not persistent</span>
                <@wm.info>The game is not persistent and not stored </@wm.info>
                </div>
            </td>
        </tr>
    </#if>
<#--
    <tr>
        <td colspan="2">
            <div class="ui-widget-content ui-widget-separator"></div>
        </td>
    </tr>
&lt;#&ndash;<#if board.gameActive>&ndash;&gt;
    <tr>
        <td><b>Finished:</b></td>
    &lt;#&ndash;<td><b><@message code="game.state.finished.label"/>:</b></td>&ndash;&gt;
        <td width="100%">${gameMessageSource.formatDate(board.lastMoveTime, locale)}</td>
    </tr>
    <tr>
        <td><b>Last Move:</b></td>
    &lt;#&ndash;<td><b><@message code="game.state.moved.label"/>:</b></td>&ndash;&gt;
        <td width="100%">${gameMessageSource.formatDate(board.lastMoveTime, locale)}</td>
    </tr>
-->
<#--<#else>-->
<#--</#if>-->
</table>
</@wm.widget>

<#if board.gameActive>
<script type="text/javascript">
    wm.scribble.state = new function() {
        this.updateProgressBar = function() {
            var count = board.getBankCapacity();
            var bo = board.getBoardTilesCount(), ha = board.getHandTilesCount(), ba = board.getBankTilesCount();
            var p3 = Math.round(100 * ha / count), p2 = Math.round(100 * ba / count), p1 = 100 - p3 - p2;

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