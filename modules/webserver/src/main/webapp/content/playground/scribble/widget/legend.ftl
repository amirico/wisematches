<#include "/core.ftl">

<@wm.widget id="boardLegend" title="game.legend.label" style="display: none; padding-top: 10px;">
<div style="padding-top: 3px">
    <div class="legendTabs" style="line-height: normal; padding-top: 5px; padding-bottom: 5px">
        <a class="ui-widget-content ui-state-active ui-corner-tl"
           style="border-bottom: none; background-image: none; margin: 0; padding: 5px;" href="#">Cell Info</a>

        <a class="ui-widget-content ui-state-default"
           style="border-bottom: none; background-image: none; margin: -5px; padding: 5px;" href="#">Tiles
            cost</a>

        <a class="ui-widget-content ui-state-default ui-corner-tr"
           style="border-bottom: none; background-image: none; padding: 5px;" href="#">Tiles
            count</a>
    </div>

    <div class="legendContent ui-widget-content">
        <div class="tilesCellInfo">
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
                <tbody>
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
                        <div class="cell bonus-cell-3w" style="position: static;"></div>
                    </td>
                    <td> - <@message code='game.legend.triple.word'/></td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="ui-helper-hidden">
            <table class="tilesCostInfo">
                <tbody>
                </tbody>
            </table>
        </div>

        <div class="ui-helper-hidden">
            <table class="tilesCountTable">
                <tbody>
                </tbody>
            </table>
        </div>
    </div>
</div>

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

    $(".legendTabs a").each(function(index) {
        $(this).click(function() {
            $($(".legendContent").children().slideUp('fast').get(index)).slideDown('fast');
            $($(".legendTabs a").removeClass('ui-state-active').addClass('ui-state-default').get(index)).removeClass('ui-state-default').addClass('ui-state-active');
            return false;
        });
    });
</script>