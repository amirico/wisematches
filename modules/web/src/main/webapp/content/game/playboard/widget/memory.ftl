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
    wm.scribble.memory = new function() {
        var memoryWords = new Array();
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

        var addWordToTable = function(word) {
            var scoreEngine = board.getScoreEngine();

            var e = '';
            e += '<div class="scribble-memory">';
            var text = word.text;
            var points = scoreEngine.getWordBonus(word).points;
            if (!board.checkWord(word)) {
                text = "<del>" + text + "<del>";
                e += '<span style="width: 16px; height: 16px; display: inline-block;"></span>';
            } else {
                e += '<a class="icon-memory-select" href="javascript: wm.scribble.memory.select()"></a>';
            }
            e += '<a class="icon-memory-remove" href="javascript: wm.scribble.memory.remove()"></a>';
            e += '</div>';

            memoryTable.fnAddData([text, points, e]);
        };

        var removeWordFromTable = function(index) {
        };

        var loadMemoryWords = function() {
            $.get('/game/memory/load.ajax?b=' + board.getBoardId(), function(result) {
                if (result.success) {
                    $.each(result.data.words, function(i, word) {
                        addWordToTable(word);
                    });
                }
            }, 'json');
        };

        this.select = function() {
            alert("Select: " + memoryTable.fnGetPosition(this));
        };

        this.remove = function() {
            alert("Remove: " + memoryTable.fnGetPosition(this));
        };

        this.clear = function() {
            memoryTable.fnClearTable();
        };

        this.remember = function() {
            var word = board.getSelectedWord();
            $.post('/game/memory/add.ajax?b=' + board.getBoardId(), $.toJSON(word), function(data) {
                if (data.success) {
                    addWordToTable(word);
                } else {

                }
            }, 'json');
        };

        loadMemoryWords();
    };

    $("#memoryWordsToolbar div").buttonset();
    $("#memoryAddButton").button({disabled: true, icons: {primary: 'icon-memory-add'}}).click(wm.scribble.memory.remember);
    $("#memoryClearButton").button({disabled: true, icons: {primary: 'icon-memory-clear'}}).click(wm.scribble.memory.clear);

    board.bind('wordSelection', function(event, word) {
        $("#memoryAddButton").button(word == null ? "disable" : "enable");
    });
</script>