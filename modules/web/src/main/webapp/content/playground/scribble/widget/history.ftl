<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.ui.widget class="movesHistory" title="game.history.label" help="board.history">
<table width="100%" class="display">
    <thead>
    <tr>
        <th>#</th>
        <th><@message code="game.history.player"/></th>
        <th><@message code="game.history.word"/></th>
        <th width="30px"><@message code="game.history.points"/></th>
    </tr>
    </thead>
    <tbody>
        <#list board.gameMoves as move>
            <#assign playerMove = move.playerMove/>
        <tr>
            <td>${1+(move.moveNumber)}</td>
            <td>
            ${gameMessageSource.getPlayerNick(personalityManager.getPlayer(playerMove.playerId), locale)}
            </td>
            <td>
                <#if playerMove.class.simpleName == "MakeWordMove">
                    <span class="moveMade">${playerMove.word.text}</span>
                <#elseif playerMove.class.simpleName == "ExchangeTilesMove">
                    <span class="moveExchange"><@message code="game.history.exchange.label"/></span>
                <#else>
                    <span class="movePassed"><@message code="game.history.passed.label"/></span>
                </#if>
            </td>
            <td>${move.points}</td>
        </tr>
        </#list>
    </tbody>
</table>
</@wm.ui.widget>

<script type="text/javascript">
    $(function () {
        new wm.scribble.History(board, {
            "passed": "<@message code="game.history.passed.label"/>",
            "exchange": "<@message code="game.history.exchange.label"/>",
            "sEmptyTable": "<@message code='game.history.empty'/>"
        });
    });
</script>
