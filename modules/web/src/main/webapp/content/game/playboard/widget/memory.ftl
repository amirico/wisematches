<#-- @ftlvariable name="memory" type="java.util.Collection<wisematches.server.gameplaying.scribble.Word>" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="memoryWords" title="Memory Words" style="padding-top: 10px;">
<table width="100%">
    <thead>
    <tr>
        <th><@message code="game.memory.word"/></th>
        <th width="60px"><@message code="game.memory.points"/></th>
        <th width="32px"></th>
    </tr>
    </thead>
    <tbody>
    <#--<#list memory as m>-->
        <#--<tr>-->
            <#--<td>${m.text}</td>-->
            <#--<td width="60" align="center">123</td>-->
            <#--<td width="24">-->
                <#--<a href="javascript: selectWord()" style="border: 0">-->
                    <#--<div class="icon-memory-select" style="width: 16px; height: 16px; display: inline-block;"></div>-->
                <#--</a>-->
                <#--<a href="javascript: removeWord()" style="border: 0">-->
                    <#--<div class="icon-memory-remove" style="width: 16px; height: 16px; display: inline-block;"></div>-->
                <#--</a>-->
            <#--</td>-->
        <#--</tr>-->
        <#--</#list>-->
    </tbody>
</table>

<div id="memoryWordsToolbar" style="padding-top: 5px;">
    <div style="margin: 0;">
        <button id="memoryAddButton"><@message code="game.memory.remember"/></button>
        <button id="memoryClearButton"><@message code="game.memory.clear"/></button>
    </div>
</div>
</@wm.widget>

<script type="text/javascript">
    $("#memoryWordsToolbar div").buttonset();

    $("#memoryAddButton").button({disabled: true, icons: {primary: 'icon-memory-add'}}).click(function() {
        $.post('/game/memory/add.ajax?b=' + board.getBoardId(), $.toJSON(board.getSelectedWord()), function(data) {
            alert(data.success);
        }, 'json');
    });

    $("#memoryClearButton").button({disabled: true, icons: {primary: 'icon-memory-clear'}});

    var memoryTable = $("#memoryWords table").dataTable({
                "bJQueryUI": true,
                "bFilter": false,
                "bSort": true,
                "bSortClasses": true,
                "sDom": 't',
                "aaSorting": [
                    [1,'desc']
                ],
                "aoColumns": [
                    null,
                    null,
                    { "bSortable": false }
                ]
            });

    var memoryWords;

    $.get('/game/memory/load.ajax?b=' + board.getBoardId(), function(result) {
        if (result.success) {
            var scoreEngine = board.getScoreEngine();
            memoryWords = result.data.words;
            $.each(memoryWords, function(i, word) {
                var e = '<a href="javascript: board.selectMemoryWord(memoryWords[' + i + '])" style="border: 0">' +
                        '<div class="icon-memory-select" style="width: 16px; height: 16px; display: inline-block;"></div>' +
                        '</a>' +
                        '<a href="javascript: removeWord()" style="border: 0">' +
                        '<div class="icon-memory-remove" style="width: 16px; height: 16px; display: inline-block;"></div>' +
                        '</a>';
                memoryTable.fnAddData([word.text, scoreEngine.getWordBonus(word).points, e]);
            });
        }
    }, 'json');

    board.bind('wordSelection', function(event, word) {
        $("#memoryAddButton").button(word == null ? "disable" : "enable");
    });
</script>