<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#include "/core.ftl">

<@wm.widget id="boardLegend" title="Board's Legend">
<table>
    <tr>
        <td width="22px">
            <div class="cell bonus-cell-center" style="position: static;"></div>
        </td>
        <td> - center position</td>
    </tr>
</table>
<div class="ui-widget-content ui-widget-separator"></div>
<table>
    <tr>
        <td width="22px">
            <div class="cell bonus-cell-2l" style="position: static;"></div>
        </td>
        <td> - double letter</td>
        <td>
            <div class="cell bonus-cell-2w" style="position: static;"></div>
        </td>
        <td> - double word</td>
    </tr>
    <tr>
        <td>
            <div class="cell bonus-cell-3l" style="position: static;"></div>
        </td>
        <td> - tripple letter</td>
        <td>
            <div class="cell bonus-cell-3l" style="position: static;"></div>
        </td>
        <td> - tripple word</td>
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
</@wm.widget>
