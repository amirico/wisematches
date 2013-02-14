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
        <tr>
            <td>${1+(move.moveNumber)}</td>
            <td>
            ${messageSource.getPersonalityNick(move.player, locale)}
            </td>
            <td>
                <#if move.class.simpleName == "MakeTurn">
                    <span class="moveMade">${move.word.text}</span>
                <#elseif move.class.simpleName == "ExchangeMove">
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
