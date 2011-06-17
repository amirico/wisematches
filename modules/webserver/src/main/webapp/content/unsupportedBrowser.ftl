<#-- @ftlvariable name="supportedBrowser" type="java.lang.Boolean" -->
<#if !(supportedBrowser!false)>
<div class="unsupported-browser ui-state-error shadow">
    <div>Unfortunately your browser is not well supported ans the site can look incorrect or don't work at all.
        Please use the following browsers and versions or latest one:
    </div>
    <div align="center">
        <table>
            <tr>
                <td><img src="/resources/images/browser/chrome64.png" alt="Google Chrome"></td>
                <td><img src="/resources/images/browser/opera64.png" alt="Opera"></td>
                <td><img src="/resources/images/browser/firefox64.png" alt="Firefox"></td>
                <td><img src="/resources/images/browser/safari64.png" alt="Safari"></td>
            </tr>
            <tr>
                <td><a href="http://www.google.com/chrome">Google Chrome 12</a></td>
                <td><a href="http://opera.com/download/">Opera 11</a></td>
                <td><a href="http://www.mozilla.com/firefox/">Firefox 4</a></td>
                <td><a href="http://www.apple.com/safari/download">Safary 5</a></td>
            </tr>
        </table>
    </div>
</div>
</#if>
