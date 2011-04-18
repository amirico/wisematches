<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">

<#macro row activeVisible=true passiveVisible=true>
    <#if !passiveVisible && !board.gameActive> <#-- nothing to do if not active and -->
        <#return>
    </#if>

<tr class="<#if !activeVisible || !passiveVisible>state-change-marker</#if><#if board.gameActive && !activeVisible> ui-helper-hidden</#if>">
    <#nested>
</tr>
</#macro>

<#macro separator activeVisible=true passiveVisible=true>
<@row activeVisible=activeVisible passiveVisible=passiveVisible>
<td colspan="2">
    <div class="ui-widget-content ui-widget-separator"></div>
</td>
</@row>
</#macro>

<#macro element activeVisible=true passiveVisible=true showSeparator=true>
    <#if showSeparator><@separator activeVisible=activeVisible passiveVisible=passiveVisible/></#if>
<@row activeVisible=activeVisible passiveVisible=passiveVisible><#nested></@row>
</#macro>

<@wm.widget id="gameInfo" title="game.state.label">
<table width="100%" border="0">
<@element showSeparator=false>
    <td><strong><@message code="game.state.started"/>:</strong></td>
    <td width="100%">
        <div id="gameStartedTime">
        ${gameMessageSource.formatDate(board.startedTime, locale)}
        </div>
    </td>
</@element>

<@element activeVisible=false>
    <td><strong><@message code="game.state.finished"/>:</strong></td>
    <td>
        <div id="gameFinishedTime">
            <#if board.finishedTime??>${gameMessageSource.formatDate(board.finishedTime, locale)}</#if>
        </div>
    </td>
</@element>

<@element passiveVisible=false>
    <td valign="top"><strong><@message code="game.state.progress"/>:</strong></td>
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
</@element>

<@element activeVisible=false>
    <td valign="top"><strong><@message code="game.state.resolution"/>:</strong></td>
    <td>
        <div id="gameResolution">
            <div class="ui-progressbar game-progress">
                <div class="ui-progressbar-value ui-corner-all game-progress-finished game-progress-caption sample">
                    <#if board.gameResolution??><@message code="game.resolution.${board.gameResolution.name()?lower_case}"/></#if>
                </div>
            </div>
            <div class="sample game-resolution-player">
                <#if board.gameResolution??>
                <#switch board.gameResolution>
                    <#case 'FINISHED'><@message code="game.resolution.by"/> ${playerManager.getPlayer(board.getPlayerTurn().getPlayerId()).nickname}<#break>
                    <#case 'STALEMATE'><@message code="game.resolution.timeout"/><#break>
                    <#case 'TIMEOUT'><@message code="game.resolution.for"/> ${playerManager.getPlayer(board.getPlayerTurn().getPlayerId()).nickname}<#break>
                    <#case 'RESIGNED'><@message code="game.resolution.by"/> ${playerManager.getPlayer(board.getPlayerTurn().getPlayerId()).nickname}<#break>
                    <#default>
                </#switch>
                </#if>
            </div>
        </div>
    </td>
</@element>

<@element>
    <td><strong><@message code="game.state.language"/>:</strong></td>
    <td><@message code="language."+board.gameSettings.language/></td>
</@element>

<@element>
    <td><strong><@message code="game.state.spent"/>:</strong></td>
    <td>
        <div id="spentTime">${gameMessageSource.formatSpentTime(board, locale)}</div>
    </td>
</@element>

<@element passiveVisible=false>
    <td><strong><@message code="game.state.time"/>:</strong></td>
    <td>${board.gameSettings.daysPerMove?string.computer} <@message code="game.state.per"/></td>
</@element>

    <#if board.gameSettings.scratch>
    <@element>
        <td colspan="2">
            <div class="game-state-scratch">
                <span><@message code="game.state.scratch.label"/></span>
            <@wm.info><@message code="game.state.scratch.description"/></@wm.info>
            </div>
        </td>
    </@element>
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

            var boardWidget = $("#gameProgress .game-progress-board").css('width', p1 + '%');
            var bankWidget = $("#gameProgress .game-progress-bank").css('width', p2 + '%');
            var handWidget = $("#gameProgress .game-progress-hand").css('width', p3 + '%');
            $("#gameProgress .game-progress-caption").text(bo + ' / ' + ba + ' / ' + ha);

            if (p1 < 2) {
                boardWidget.hide();
                bankWidget.addClass("ui-corner-left");
            } else {
                boardWidget.show();
                bankWidget.removeClass("ui-corner-left");
            }
            if (p3 < 2) {
                handWidget.hide();
                bankWidget.addClass("ui-corner-right");
            }
        };

        this.markAsFinished = function(state) {
            $("#gameFinishedTime").html(state.finishTimeMessage);
            var cap = $("#gameResolution .game-progress-caption");
            var desc = $("#gameResolution .game-resolution-player");
            switch (state.resolution) {
                case 'FINISHED':
                    cap.text("<@message code="game.resolution.finished"/>");
                    desc.text("<@message code="game.resolution.by"/> " + board.getPlayerInfo(state.playerTurn).nickname);
                    break;
                case 'STALEMATE':
                    cap.text("<@message code="game.resolution.stalemate"/>");
                    desc.text("<@message code="game.resolution.moves"/>");
                    break;
                case 'TIMEOUT':
                    cap.text("<@message code="game.resolution.timeout"/>");
                    desc.text("<@message code="game.resolution.for"/> " + board.getPlayerInfo(state.playerTurn).nickname);
                    break;
                case 'RESIGNED':
                    cap.text("<@message code="game.resolution.resigned"/>");
                    desc.text("<@message code="game.resolution.by"/> " + board.getPlayerInfo(state.playerTurn).nickname);
                    break;
            }

            $(".state-change-marker").toggle();
        };
    };

    wm.scribble.state.updateProgressBar();

    board.bind('gameState',
            function(event, type, state) {
                $("#spentTime").html(state.spentTimeMessage);
                if (type === 'finished') {
                    wm.scribble.state.markAsFinished(state);
                }
            }).bind('gameMoves',
            function(event, move) {
                wm.scribble.state.updateProgressBar();
            });
</script>
</#if>