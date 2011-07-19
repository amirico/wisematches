<#include "/core.ftl">

<@wm.widget id="boardLegend" title="game.legend.label" style="display: none; padding-top: 10px;">
<table>
    <tr>
        <td width="22px">
            <div class="cell bonus-cell-center" style="position: static;"></div>
        </td>
        <td> - <@message code='game.legend.center'/></td>
    </tr>
</table>
<div class="ui-widget-content ui-widget-separator"></div>
<table>
    <tr>
        <td width="22px">
            <div class="cell bonus-cell-2l" style="position: static;"></div>
        </td>
        <td> - <@message code='game.legend.double.letter'/></td>
        <td>
            <div class="cell bonus-cell-2w" style="position: static;"></div>
        </td>
        <td> - <@message code='game.legend.double.word'/></td>
    </tr>
    <tr>
        <td>
            <div class="cell bonus-cell-3l" style="position: static;"></div>
        </td>
        <td> - <@message code='game.legend.triple.letter'/></td>
        <td>
            <div class="cell bonus-cell-3l" style="position: static;"></div>
        </td>
        <td> - <@message code='game.legend.triple.word'/></td>
    </tr>
</table>
<div class="ui-widget-content ui-widget-separator"></div>
<table class="tilesInfoTable">
    <tbody>
    </tbody>
</table>

<div style="width: 100%; text-align: right;">
    <a href="javascript: boardLegend.hideLegend()">&laquo; <@message code="game.legend.hide.label"/></a>
</div>
</@wm.widget>

<script type="text/javascript">
    var boardLegend = new wm.scribble.Legend(board);

    $(document).ready(function() {
        var link = $("<a></a>").attr('id', 'showLegendButton').attr('href', 'javascript: boardLegend.showLegend()').html('<@message code="game.legend.show.label"/> &raquo;');
        $("<div></div>").css("width", "100%").css('text-align', 'right').append(link).appendTo('#gameInfo');
    });
</script>