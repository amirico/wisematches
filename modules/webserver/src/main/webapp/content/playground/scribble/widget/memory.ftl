<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget class="memoryWordsWidget" title="game.memory.label" style="padding-top: 10px;" help="board.memory">
<table class="memoryWords display" width="100%">
    <thead>
    <tr>
        <th><@message code="game.memory.word"/></th>
        <th width="60px"><@message code="game.memory.points"/></th>
        <th width="32px"></th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>

<table width="100%">
    <tr>
        <td align="left">
            <button class="memoryAddButton"><@message code="game.memory.remember"/></button>
        </td>
        <td align="right">
            <button class="memoryClearButton"><@message code="game.memory.clear"/></button>
        </td>
    </tr>
</table>
</@wm.widget>

<script type="text/javascript">
    var memoryWords = new wm.scribble.Memory(board, scribbleController, {
        "sEmptyTable":"<@message code='game.memory.empty'/>",
        "changeMemory":"<@message code='game.memory.changing'/>"
    });
</script>
