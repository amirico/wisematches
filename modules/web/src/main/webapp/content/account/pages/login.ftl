<#include "/core.ftl">

<table>
    <tr>
        <td style="width: 100%; vertical-align: top; text-align: left;">
        <#include "/content/info/modelConverter.ftl">
        </td>

        <td style="vertical-align: top;">
            <div id="navigation" style="width: 270px;">
            <@ext.roundPanel><@ext.roundPanel>
                <div id="login-panel">
                    <form id="login-form" method="post" action="/account/authMember.html">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td colspan="2">

                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label for="j_username"><@message code="account.login.email.label"/>:</label>
                                </td>
                                <td>
                                    <input id="j_username" name="j_username" type="text"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label for="j_password"><@message code="account.login.password.label"/>:</label>
                                </td>
                                <td>
                                    <input id="j_password" name="j_password" type="password"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">
                                    <input id="_remember_me" name="_remember_me" type="checkbox"
                                           checked="checked"
                                           value="true">
                                </td>
                                <td align="left">
                                    <label for="_remember_me"><@message code="account.login.remember.label"/></label>
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td align="left">
                                    <button type="submit"><@message code="account.login.signin.label"/></button>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" height="33.0" align="center" valign="bottom">
                                    <a href=""><@message code="account.login.restore.label"/></a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </@ext.roundPanel></@ext.roundPanel>

            <@ext.roundPanel id="register-panel">
                <input type="button" id="register-link" onclick="wm.account.showRegistrationWindow();"
                       value="<@message code="login.info.register.label"/>">
            <#--<div id="register-link"></div>-->
                <div class="navigation-separator"><@message code="separator.or"/></div>
                <div id="login-guest-link">
                    <a href="/accounts/loginGuest.html"><@message code="login.info.guest.label"/></a>
                </div>
                <div class="navigation-separator"><@message code="separator.or"/></div>
                <div id="info-links">
                <@message code="login.info.readmore.label"/>
                    <a href="/info/about.html">
                    <@message code="login.info.about.label"/> <@message code="wisematches.label"/>
                    </a>
                <@message code="separator.and"/><br>
                    <a href="/info/features.html">
                    <@message code="login.info.features.label"/>
                    </a>
                </div>
            </@ext.roundPanel>
            </div>
        </td>
    </tr>
</table>
