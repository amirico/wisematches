<#-- @ftlvariable name="loginErrorType" type="java.lang.String" -->
<#include "/core.ftl">

<table>
    <tr>
        <td style="width: 100%; vertical-align: top; text-align: left;">
        <#include "/content/info/modelConverter.ftl">
        </td>

        <td style="vertical-align: top;">
            <div id="login-navigation" style="width: 270px;">
            <@ext.roundPanel>
            <@ext.roundPanel>
                <div id="login-panel">
                    <div id="login-title"><@message code="account.login.title"/></div>
                    <div id="login-type"><@message code="account.header"/></div>

                    <form id="login-form" method="post" action="/account/loginProcessing.html">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                <#--@declare id="j_username"-->
                                    <label style="white-space: nowrap;"
                                           for="j_username"><@message code="account.login.email.label"/>:</label>
                                </td>
                                <td>
                                    <#if "insufficient"=loginErrorType!"">
                                    <@spring.bind "login.j_username"/>
                                        <span><b>${spring.stringStatusValue}</b></span>
                                        <#else>
                                        <@wisematches.fieldInput path="login.j_username" size="0"/>
                                    </#if>
                                    <input type="hidden" id="j_username" name="j_username"
                                           value="${spring.stringStatusValue}"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                <#--@declare id="j_password"-->
                                    <label style="white-space: nowrap;"
                                           for="j_password"><@message code="account.login.password.label"/>:</label>
                                </td>
                                <td>
                                <@wisematches.fieldInput path="login.j_password" fieldType="password" size="0"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="right" valign="middle" style="text-align: right; vertical-align: middle;">
                                <@wisematches.field path="login.rememberMe">
                                    <input type="checkbox" id="rememberMe" name="rememberMe" value="true"
                                           <#if spring.stringStatusValue=="true">checked="checked"</#if>/>
                                </@wisematches.field>
                                </td>
                                <td align="left" valign="middle" style="text-align: left; vertical-align: middle;">
                                    <label for="rememberMe"><@message code="account.login.remember.label"/></label>
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td align="left">
                                    <button type="submit"><@message code="account.login.signin.label"/></button>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" height="33.0" align="center" valign="bottom"
                                    style="text-align: center; vertical-align: bottom;">
                                    <span>
                                        <a href="/account/recovery.html"><@message code="account.login.recovery.label"/></a>
                                    </span>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </@ext.roundPanel>
            </@ext.roundPanel>
                <div style="height:10px;"></div>
            <@ext.roundPanel>
                <div id="register-panel">
                    <div id="register-link">
                        <button id="createAnAccount" class="account-button"
                                onclick="wm.util.url.redirect('/account/create.html')">
                        <@message code="account.register.label"/>
                        </button>
                    </div>
                    <div class="separator"><@message code="separator.or"/></div>
                    <div id="login-guest-link">
                        <a href="/account/loginGuest.html"><@message code="account.guest.label"/></a>
                    </div>
                    <div class="separator"><@message code="separator.or"/></div>
                    <div id="info-links">
                    <@message code="info.readmore.label"/>
                        <a href="/info/about.html"><@message code="info.principles.label"/></a>
                    <@message code="separator.and"/><br>
                        <a href="/info/features.html"><@message code="info.features.label"/></a>
                    </div>
                </div>
            </@ext.roundPanel>
            </div>
        </td>
    </tr>
</table>
