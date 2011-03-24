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
<table>
    <tbody>
        <#list tilesBankInfo as i>
            <#if i??>
            <tr>
                <td>
                    <div style="position: relative; height: 22px; width:22px">
                    <@tilePlain number="" cost=i_index letter="<b>" + i_index?string + "</b>" i=0 j=0 />
                    </div>
                </td>
                <td>&nbsp;-&nbsp;</td>
                <td>
                    <div style="position: relative; height: 22px; width: ${(i?size)*22}px">
                        <#list i as c><@tilePlain number="" cost=i_index letter=c?upper_case i=c_index j=0 /></#list>
                    </div>
                </td>
            </tr>
            </#if>
        </#list>
    </tbody>
</table>

<div style="width: 100%; text-align: right;">
    <a href="javascript: wm.scribble.legend.hideLegend()">&laquo; <@message code="game.legend.hide.label"/></a>
</div>
</@wm.widget>
