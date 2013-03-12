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
    <tbody></tbody>
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
