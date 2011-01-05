<#include "/core.ftl">

<table id="header">
    <tr>
        <td>
            <img id="header-image" src="/resources/images/logo.png"/>
        </td>
        <td style="width: 100%">
        <@ext.topRoundPanel id="header-panel">
            <table>
                <tr>
                    <td id="header-welcome-label" style="width: 100%; text-align: left;">
                    <@spring.message code="login.header"/>
                    </td>
                    <td style="text-align: right;">
                        <input id="language-combobox" type="text" size="20"/>
                    </td>
                </tr>
            </table>
        </@ext.topRoundPanel>
        </td>
    </tr>
</table>
