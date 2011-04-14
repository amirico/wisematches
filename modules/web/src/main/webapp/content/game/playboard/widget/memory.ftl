<#-- @ftlvariable name="memory" type="java.util.Collection<wisematches.server.gameplaying.scribble.Word>" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget title="game.memory.label" style="padding-top: 10px;">
<table id="memoryWords" width="100%">
    <thead>
    <tr>
        <th width="0"></th>
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
            <button id="memoryAddButton"><@message code="game.memory.remember"/></button>
        </td>
        <td align="right">
            <button id="memoryClearButton"><@message code="game.memory.clear"/></button>
        </td>
    </tr>
</table>
</@wm.widget>

<script type="text/javascript">
    wm.scribble.memory = new function() {
        var nextWordId = 0;
        var memoryWords = new Array();
        var memoryWordsCount = 0;

        var overlayCSS = {
            '-moz-border-radius': '5px',
            '-webkit-border-radius': '5px',
            'border-radius': '5px',
            backgroundColor:'#DFEFFC'
        };

        var controlsRenderer = function(obj) {
            var id = obj.aData[0];
            var e = '<div id="memoryWordControls' + id + '" class="memory-controls">';
            if (!obj.aData[3]) {
                e += '<span></span>';
            } else {
                e += '<a class="icon-memory-select" href="javascript: wm.scribble.memory.select(' + id + ')"></a>';
            }
            e += '<a class="icon-memory-remove" href="javascript: wm.scribble.memory.remove(' + id + ')"></a>';
            e += '</div>';
            return e;
        };

        var invalidRowRenderer = function(obj) {
            var sReturn = obj.aData[ obj.iDataColumn ];
            if (!obj.aData[3]) {
                return "<del>" + sReturn + "</del>";
            }
            return sReturn;
        };

        var addWord = function(word) {
            var scoreEngine = board.getScoreEngine();

            memoryWordsCount++;
            var id = nextWordId++;
            memoryWords[id] = word;
            memoryTable.fnAddData([id, word.text, scoreEngine.getWordPoints(word).points, board.checkWord(word)]);
            $("#memoryClearButton").button(memoryWordsCount == 0 ? "disable" : "enable");
        };

        var getWord = function(id) {
            return memoryWords[id];
        };

        var removeWord = function(id) {
            var word = memoryWords[id];
            if (word != null && word != undefined) {
                memoryWordsCount--;
                memoryWords[id] = null;
                var row = $('#memoryWordControls' + id).closest('tr').get(0);
                memoryTable.fnDeleteRow(memoryTable.fnGetPosition(row));

                $("#memoryClearButton").button(memoryWordsCount == 0 ? "disable" : "enable");
            }
        };

        var executeRequest = function(type, data, successHandler) {
            $("#memoryWords").block({ message: null, overlayCSS: overlayCSS });

            if (data != null) {
                data = $.toJSON(data);
            }

            $.post('/game/memory/' + type + '.ajax?b=' + board.getBoardId(), data, function(result) {
                if (result.success) {
                    successHandler(result.data);
                } else {
                    wm.ui.showMessage({message: result.summary, error:true});
                }
                $("#memoryWords").unblock();
            });
        };

        this.select = function(id) {
            var word = getWord(id);
            if (word != null && word != undefined) {
                board.selectWord(word);
            }
        };

        this.reloadMemoryWords = function() {
            memoryTable.fnClearTable();
            executeRequest('load', null, function(data) {
                $.each(data.words, function(i, word) {
                    addWord(word);
                });
            });
        };

        this.remove = function(id) {
            var word = getWord(id);
            if (word != null && word != undefined) {
                executeRequest('remove', word, function(data) {
                    removeWord(id);
                });
            }
        };

        this.clear = function() {
            executeRequest('clear', null, function(data) {
                memoryTable.fnClearTable();
            });
        };

        this.remember = function() {
            var word = board.getSelectedWord();
            if (word != null || word != undefined) {
                executeRequest('add', word, function(data) {
                    addWord(word);
                });
            }
        };

        var memoryTable = $("#memoryWords").dataTable({
                    "bJQueryUI": true,
                    "bFilter": false,
                    "bSort": true,
                    "bSortClasses": true,
                    "sDom": 't',
                    "aaSorting": [
                        [2,'desc']
                    ],
                    "aoColumns": [
                        { "bVisible": false },
                        { "fnRender": invalidRowRenderer },
                        { "fnRender": invalidRowRenderer, "sClass": "center"},
                        { "fnRender": controlsRenderer, "bSortable": false }
                    ],
                    "oLanguage": {
                        "sEmptyTable": "<@message code='game.memory.empty'/>"
                    }
                });

    };

    $("#memoryAddButton").button({disabled: true, icons: {primary: 'icon-memory-add'}}).click(wm.scribble.memory.remember);
    $("#memoryClearButton").button({disabled: true, icons: {primary: 'icon-memory-clear'}}).click(wm.scribble.memory.clear);

    board.bind('wordSelection', function(event, word) {
        $("#memoryAddButton").button(word == null ? "disable" : "enable");
    });

    wm.scribble.memory.reloadMemoryWords();
</script>