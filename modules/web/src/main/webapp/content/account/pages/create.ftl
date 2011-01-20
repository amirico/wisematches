<#include "/core.ftl">

<div id="registration">
    <div class="label"><@message code="account.register.label"/></div>

    <div class="description"><@message code="account.register.description"/></div>

    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tbody>
        <tr>
            <td width="500px">
                <form id="form" class="form" action="/account/create.html" method="post">
                    <table class="x-panel" width="100%">
                        <tr>
                            <td colspan="2">
                                <div style="color: #36C; padding-top: 15px; font-size: medium; font-weight: bold;">
                                    Required information for WiseMatches account
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="email"><@message code="account.register.email.label"/>:</label>
                            </td>
                            <td>
                                <div class="field">
                                <@spring.bind "registration.email"/>
                                    <input type="text" id="email" name="email" size="30"
                                           value="${spring.stringStatusValue}"/>
                                </div>
                                <span class="sample"><@message code="account.register.email.description"/></span>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <label for="nickname"><@message code="account.register.nickname.label"/>:</label>
                            </td>
                            <td>
                                <div class="field">
                                <@spring.bind "registration.nickname"/>
                                    <input type="text" id="nickname" name="nickname" size="30"
                                           value="${spring.stringStatusValue}"/>
                                </div>
                                <span class="sample"><@message code="account.register.nickname.description"/></span>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <label for="password"><@message code="account.register.pwd.label"/>:</label>
                            </td>
                            <td>
                                <div class="field">
                                <@spring.bind "registration.password"/>
                                    <input type="password" id="password" name="password" size="30"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="confirm"><@message code="account.register.pwd-cfr.label"/>:</label>
                            </td>
                            <td>
                                <div class="field">
                                <@spring.bind "registration.confirm"/>
                                    <input type="password" id="confirm" name="confirm" size="30"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <div class="field">
                                <@spring.bind "registration.rememberMe"/>
                                    <input type="checkbox" id="rememberMe" name="rememberMe"
                                           <#if spring.stringStatusValue=="true">checked="checked"</#if>/>
                                    <label for="rememberMe"><@message code="account.login.remember.label"/></label>
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="2">
                                <hr>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="2">
                                <div style="color: #36C; padding-top: 15px; font-size: medium; font-weight: bold;">
                                    Get started with WiseMatches
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="language"><@message code="account.register.language.label"/>:</label>
                            </td>
                            <td>
                                <div class="field">
                                <@spring.bind "registration.language"/>
                                    <select id="language" name="language" style="width: 170px;">
                                        <option value="en" <#if (locale=="en")>selected="selected"</#if>>English
                                        </option>
                                        <option value="ru" <#if (locale=="ru")>selected="selected"</#if>>Русский
                                        </option>
                                    </select>
                                </div>
                                <input type="hidden" id="timezone" name="timezone" value="">
                                <span class="sample"><@message code="account.register.language.description"/></span>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <label><@message code='account.register.terms.label'/>:</label>
                            </td>
                            <td align="center">
                                <span><@message code="account.register.terms.description"/></span>

                                <div class="field">
                                    <button id="createAccount"
                                            name="createAccount"
                                            type="submit"
                                            value="submit"><@message code='account.register.submit.label'/></button>
                                </div>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
            <td>
                <div id="terms_tabs" class="x-panel">
                    <a id="terms-page-link" class="x-panel tab active"
                       href="javascript: wm.account.loadTermsPage('terms');">
                    <@message code="info.policies.terms_of_use.label"/>
                    </a>
                    <a id="policy-page-link" class="x-panel tab" href="javascript: wm.account.loadTermsPage('policy');">
                    <@message code="info.policies.privacy_policy.label"/>
                    </a>
                    <a id="naming-page-link" class="x-panel tab" href="javascript: wm.account.loadTermsPage('naming');">
                    <@message code="info.policies.naming.label"/>
                    </a>

                    <div style="clear: left;"></div>
                </div>
                <div id="terms_panel" class="x-panel"></div>
            </td>
        </tr>
        </tbody>
    </table>
</div>