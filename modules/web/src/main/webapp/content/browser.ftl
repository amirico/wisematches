<#-- @ftlvariable name="supportedBrowser" type="java.lang.Boolean" -->
<#include "/core.ftl">

<#if !(supportedBrowser!true)>
<div class="unsupported-browser ui-state-error shadow">
    <div>
        <@message code="browser.unsupported"/>
    </div>
    <div align="center">
        <table>
            <tr>
                <td><img src="<@wm.ui.static "images/browser/chrome64.png"/>" alt="Google Chrome"></td>
                <td><img src="<@wm.ui.static "images/browser/opera64.png"/>" alt="Opera"></td>
                <td><img src="<@wm.ui.static "images/browser/firefox64.png"/>" alt="Firefox"></td>
                <td><img src="<@wm.ui.static "images/browser/safari64.png"/>" alt="Safari"></td>
                <td><img src="<@wm.ui.static "images/browser/yandex64.png"/>" alt="Yandex"></td>
            </tr>
            <tr>
                <td><a href="http://www.google.com/chrome">Google Chrome 12</a></td>
                <td><a href="http://opera.com/download/">Opera 11</a></td>
                <td><a href="http://www.mozilla.com/firefox/">Firefox 4</a></td>
                <td><a href="http://www.apple.com/safari/download">Safary 5</a></td>
                <td><a href="http://browser.yandex.ru/">Яндекс 1.5</a></td>
            </tr>
        </table>
    </div>
</div>
</#if>
