<#include "/core.ftl">

<@wm.widget id="movesHistory" title="game.history.label" style="padding-top: 10px">
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
    </tbody>
</table>
</@wm.widget>

<script type="text/javascript">
    var movesHistory = new wm.scribble.History(board, {
        "passed": "<@message code="game.history.passed.label"/>",
        "exchange": "<@message code="game.history.exchange.label"/>",
        "sEmptyTable": "<@message code='game.history.empty'/>"
    });
</script>
