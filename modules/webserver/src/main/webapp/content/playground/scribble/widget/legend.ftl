<#include "/core.ftl">

<@wm.widget id="boardLegend" title="game.legend.label" style="padding-top: 10px;">
<div>
    <div class="legendContent">
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

    <div class="legendTabs" style="line-height: normal; padding-top: 5px;">
        <a href="#">Cell Info</a>

        <a href="#">Tiles cost</a>

        <a href="#">Tiles count</a>
    <#--
            <a class="ui-widget-content ui-state-active ui-corner-left"
               style="background-image: none; margin: 0; padding: 5px;" href="#">Cell Info</a>

            <a class="ui-widget-content ui-state-default"
               style="background-image: none; margin: -5px; padding: 5px;" href="#">Tiles
                cost</a>

            <a class="ui-widget-content ui-state-default ui-corner-right"
               style="background-image: none; padding: 5px;" href="#">Tiles
                count</a>
    -->
    </div>
</div>
</@wm.widget>

<script type="text/javascript">
    var boardLegend = new wm.scribble.Legend(board);

    $(".legendTabs a").each(function(index) {
        $(this).click(function() {
            $($(".legendContent").children().slideUp('fast').get(index)).slideDown('fast');
            $($(".legendTabs a").removeClass('ui-state-active').addClass('ui-state-default').get(index)).removeClass('ui-state-default').addClass('ui-state-active');
            return false;
        });
    });
</script>