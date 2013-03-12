<#-- @ftlvariable name="boardInfo" type="wisematches.server.web.servlet.sdo.scribble.BoardInfo" -->
<#-- @ftlvariable name="boardSettings" type="wisematches.playground.scribble.settings.BoardSettings" -->

<#include "/core.ftl">

<#macro row activeVisible=true passiveVisible=true>
    <#if !passiveVisible && !boardInfo.state.active> <#-- nothing to do if not active and -->
        <#return>
    </#if>

<tr class="<#if !activeVisible || !passiveVisible>state-change-marker</#if><#if !boardInfo.state.active && !activeVisible> ui-helper-hidden</#if>">
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

<@wm.ui.widget class="gameInfo" title="game.state.label" help="board.progress">

<table width="100%" border="0">
    <@element showSeparator=false>
        <td nowrap="nowrap"><strong><@message code="game.state.started"/>:</strong></td>
        <td width="100%">
            <div class="gameStartedTime">${boardInfo.startedTime.text}</div>
        </td>
    </@element>

    <@element activeVisible=false>
        <td nowrap="nowrap"><strong><@message code="game.state.finished"/>:</strong></td>
        <td>
            <div class="gameFinishedTime">
                <#if boardInfo.outcomes.finishedTime??>${boardInfo.outcomes.finishedTime.text}</#if>
            </div>
        </td>
    </@element>

    <@element passiveVisible=false>
        <td nowrap="nowrap" valign="top"><strong><@message code="game.state.progress"/>:</strong></td>
        <td style="padding-top: 2px;">
            <div class="game-progress ui-progressbar">
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
        <td nowrap="nowrap" valign="top"><strong><@message code="game.state.resolution"/>:</strong></td>
        <td>
            <div class="gameResolution">
                <div class="ui-progressbar game-progress">
                    <div class="ui-progressbar-value ui-corner-all game-progress-finished game-progress-caption sample">
                        <#if boardInfo.outcomes.resolution??><@message code="game.resolution.${boardInfo.outcomes.resolution.name()?lower_case}"/></#if>
                    </div>
                </div>
                <div class="sample game-resolution-player">
                <#--
                    <#if boardInfo.outcomes.resolution??>
                <#switch boardInfo.outcomes.resolution>
                        <#case 'FINISHED'><@message code="game.resolution.by"/> ${messageSource.getPersonalityNick(board.getPlayerTurn(), locale)}<#break>
                        <#case 'STALEMATE'><@message code="game.resolution.timeout"/><#break>
                        <#case 'TIMEOUT'><@message code="game.resolution.for"/> ${messageSource.getPersonalityNick(board.getPlayerTurn(), locale)}<#break>
                        <#case 'RESIGNED'><@message code="game.resolution.by"/> ${messageSource.getPersonalityNick(board.getPlayerTurn(), locale)}<#break>
                        <#default>
                    </#switch>
                </#if>
-->
                </div>
            </div>
        </td>
    </@element>

    <@element>
        <td nowrap="nowrap"><strong><@message code="game.state.language"/>:</strong></td>
        <td><@message code="language."+boardInfo.settings.language?lower_case/></td>
    </@element>

    <@element>
        <td nowrap="nowrap"><strong><@message code="game.state.spent"/>:</strong></td>
        <td>
            <div class="spentTime">${boardInfo.state.spentTime.text}</div>
        </td>
    </@element>

    <#if boardInfo.outcomes.relationship??>
        <@element>
            <td nowrap="nowrap"><strong><@message code="game.state.relationship"/>:</strong></td>
            <td><@wm.board.relationship boardInfo.relationship, true/></td>
        </@element>
    </#if>

    <@element passiveVisible=false>
        <td nowrap="nowrap"><strong><@message code="game.state.time"/>:</strong></td>
        <td>${boardInfo.settings.daysPerMove} ${messageSource.formatDays(boardInfo.settings.daysPerMove, locale)} <@message code="game.state.move"/></td>
    </@element>

    <#if boardInfo.settings.scratch>
        <@element>
            <td colspan="2">
                <div class="game-state-scratch">
                    <span><@message code="game.state.scratch.label"/></span>
                    <@wm.ui.info><@message code="game.state.scratch.description"/></@wm.ui.info>
                </div>
            </td>
        </@element>
    </#if>
</table>
</@wm.ui.widget>

<#if boardInfo.state.active>
<script type="text/javascript">
    wm.scribble.state = function (board) {
        var status = this;

        this.updateProgressBar = function () {
            var count = board.getBankCapacity();
            var bo = board.getBoardTilesCount(), ha = board.getHandTilesCount(), ba = board.getBankTilesCount();
            var p3 = Math.round(100 * ha / count), p2 = Math.round(100 * ba / count), p1 = 100 - p3 - p2;

            var boardWidget = board.getPlayboardElement(".game-progress .game-progress-board").css('width', p1 + '%');
            var bankWidget = board.getPlayboardElement(".game-progress .game-progress-bank").css('width', p2 + '%');
            var handWidget = board.getPlayboardElement(".game-progress .game-progress-hand").css('width', p3 + '%');
            board.getPlayboardElement(".game-progress .game-progress-caption").text(bo + ' / ' + ba + ' / ' + ha);

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

        this.markAsFinished = function (state) {
            board.getPlayboardElement(".gameFinishedTime").html(state.finishTimeMessage);
            var cap = board.getPlayboardElement(".gameResolution .game-progress-caption");
            var desc = board.getPlayboardElement(".gameResolution .game-resolution-player");
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

        board.bind('gameState',
                function (event, type, state) {
                    board.getPlayboardElement(".spentTime").html(state.spentTimeMessage);
                    if (type === 'finished') {
                        status.markAsFinished(state);
                    }
                }).bind('gameMoves',
                function (event, move) {
                    status.updateProgressBar();
                });

        status.updateProgressBar();
    }(board);
</script>
</#if>