<#include "/core.ftl">

<#if principal?? && !principal.type.member>
<div class="shadow ui-state-highlight" style="text-align: left; padding: 5px 5px 5px 30px;">
    <table>
        <tr>
            <td align="left" valign="top" nowrap="nowrap">
                <div class="restriction-icon wm-icon-warning" style="float: left; padding-right: 5px;"></div>
            </td>
            <td align="left" valign="top" width="100%">
                <div><strong><@message code="game.guest.warn.label"/></strong></div>
                <div>
                    <@message code="game.guest.warn.description"/>
                    <a href="#"
                       onclick="$(this).hide().parent().find('ul').slideDown(); return false;"><@message code="info.readmore.label"/></a>
                    <ul class="ui-helper-hidden">
                        <li><@message code="game.guest.warn.1"/></li>
                        <li><@message code="game.guest.warn.2"/></li>
                        <li><@message code="game.guest.warn.3"/></li>
                        <li><@message code="game.guest.warn.4"/></li>
                        <li><@message code="game.guest.warn.5"/></li>
                        <li><@message code="game.guest.warn.6"/></li>
                        <li><@message code="game.guest.warn.7"/></li>
                        <li><@message code="game.guest.warn.8"/></li>
                    </ul>
                </div>
            </td>
            <td valign="bottom" align="right" nowrap="nowrap">
                <a href="/account/create"><@message code="game.guest.warn.register"/></a>
            </td>
        </tr>
    </table>
</div>
</#if>
