<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="memoryWords" title="Memory Words">
<table width="100%">
    <thead>
    <tr>
        <th>Word</th>
        <th>Points</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>GFER</td>
        <td width="60" align="center">234</td>
        <td width="24">
            <a href="javascript: selectWord()" style="border: 0">
                <div class="icon-memory-select" style="width: 16px; height: 16px; display: inline-block;"></div>
            </a>
            <a href="javascript: removeWord()" style="border: 0">
                <div class="icon-memory-remove" style="width: 16px; height: 16px; display: inline-block;"></div>
            </a>
        </td>
    </tr>
    </tbody>
</table>

<div id="memoryWordsToolbar" style="padding-top: 5px;">
    <div style="margin: 0;">
        <button id="memoryAddButton" class="icon-memory-add">Remember Word</button>
        <button id="memoryClearButton" class="icon-memory-clear">Clear Memory
        </button>
    </div>
</div>
</@wm.widget>

<script type="text/javascript">
    $("#memoryWordsToolbar div").buttonset();
    $("#memoryWordsToolbar button").button("disable");

    $("#memoryWords table").dataTable({
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

    board.bind('wordChanged', function(event, word) {
        $("#memoryAddButton").button(word == null ? "disable" : "enable");
    });
</script>