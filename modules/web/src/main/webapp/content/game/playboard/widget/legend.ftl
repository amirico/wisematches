<#include "/core.ftl">

<script type="text/javascript">
    wm.scribble.legend = new function() {
        this.showLegend = function() {
            $('#boardLegend').parent().slideToggle('slow');
            $('#showLegendButton').slideToggle('slow');
        };

        this.hideLegend = function() {
            $('#boardLegend').parent().slideToggle('slow');
            $('#showLegendButton').slideToggle('slow');
        };
    };

    $(document).ready(function() {
        var link = $("<a></a>").attr('id', 'showLegendButton').attr('href', 'javascript: wm.scribble.legend.showLegend()').html('<@message code="game.legend.show.label"/> &raquo;');
        $("<div></div>").css("width", "100%").css('text-align', 'right').append(link).appendTo('#gameInfo');
    });
</script>

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
    <a href="javascript: wm.scribble.legend.hideLegend()">&laquo; <@message code="game.legend.hide.label"/></a>
</div>
</@wm.widget>

<script type="text/javascript">
    var infoTable = $("table .tilesInfoTable");
    for (var i = 0; i < 12; i++) {
        var count = 0;
        var e = $("<tr></tr>");
        $('<td></td>').append($('<div style="position: relative; height: 22px; width:22px"></div>').append(wm.scribble.tile.createTileWidget({letter: '' + i, cost: i}))).appendTo(e);
        $('<td>&nbsp;-&nbsp;</td>').appendTo(e);
        var d = $('<div style="position: relative; height: 22px;"></div>');
        $('<td></td>').append(d).appendTo(e);
        $.each(scribbleBoard.bankTilesInfo, function(j, bti) {
            if (bti.cost == i) {
                d.append(wm.scribble.tile.createTileWidget({letter: bti.letter, cost: i}).offset({left: count * 22, top: 0}));
                count++;
            }
        });
        d.width(count * 22);
        if (count > 0) {
            e.appendTo(infoTable);
        }
    }
</script>