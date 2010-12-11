<#include "/core.ftl">

<table id="body">
    <tr>
        <td style="width: 100%; vertical-align: top; text-align: left;">
            <div id="content">
            <#include "${pageName}">
            </div>
        </td>
        <td style="width: 250px; vertical-align: top; text-align: left;">
            <div id="navigation" class='x-hide-display'>
                <div id="login-panel"></div>

            <@ext.roundPanel id="register-panel">
                <div id="register-link"></div>
                <div class="navigation-separator"><@spring.message code="separator.or"/></div>
                <div id="login-guest-link">
                    <a href="/accounts/loginGuest.html"><@spring.message code="login.info.guest.label"/></a>
                </div>
                <div class="navigation-separator"><@spring.message code="separator.or"/></div>
                <div id="info-links">
                <@spring.message code="login.info.readmore.label"/>
                    <a href="/info/about.html">
                    <@spring.message code="login.info.about.label"/> <@spring.message code="wisematches.label"/>
                    </a>
                <@spring.message code="separator.and"/><br>
                    <a href="/info/features.html">
                    <@spring.message code="login.info.features.label"/>
                    </a>
                </div>
            </@ext.roundPanel>
            </div>
        </td>
    </tr>
</table>