<#include "/core.ftl">

<div id="registration"">
<div class="info-header">
    <div class="info-label"><@message code="account.register.label"/></div>

    <div class="info-description"><@message code="account.register.description"/></div>
</div>

<table cellpadding="0" cellspacing="0" border="0" width="100%">
    <tbody>
    <tr>
        <td width="500px">
            <form id="form" class="form" action="/account/create.html" method="post">
                <table class="x-panel" width="100%">
                    <tr>
                        <td colspan="2">
                            <div class="group label"><@message code="account.register.group.required.label"/></div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <#--@declare id="email"-->
                            <label for="email"><@message code="account.register.email.label"/>:</label>
                        </td>
                        <td>
                        <@wisematches.fieldInput path="registration.email"/>
                            <span class="sample"><@message code="account.register.email.description"/></span>
                        </td>
                    </tr>

                    <tr>
                        <td>
                        <#--@declare id="nickname"-->
                            <label for="nickname"><@message code="account.register.nickname.label"/>:</label>
                        </td>
                        <td>
                        <@wisematches.fieldInput path="registration.nickname"/>
                            <span class="sample"><@message code="account.register.nickname.description"/></span>
                        </td>
                    </tr>
                <#--
                        <tr>
                            <td></td>
                            <td>
                                <button id="checkAvailability" type="button"
                                        onclick="wm.account.checkAvailability(this)">
                                <@message code="account.register.availability.check.label"/>
                                </button>
                            </td>
                        </tr>
-->

                    <tr>
                        <td>
                        <#--@declare id="password"-->
                            <label for="password"><@message code="account.register.pwd.label"/>:</label>
                        </td>
                        <td>
                        <@wisematches.fieldInput path="registration.password" fieldType="password"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <#--@declare id="confirm"-->
                            <label for="confirm"><@message code="account.register.pwd-cfr.label"/>:</label>
                        </td>
                        <td>
                        <@wisematches.fieldInput path="registration.confirm" fieldType="password"/>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                        <@wisematches.field path="registration.rememberMe">
                            <input type="checkbox" id="rememberMe" name="rememberMe"
                                   <#if spring.stringStatusValue=="true">checked="checked"</#if>/>
                            <label for="rememberMe"><@message code="account.login.remember.label"/></label>
                        </@wisematches.field>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2">
                            <hr>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2">
                            <div class="group label"><@message code="account.register.group.getstarted.label"/></div>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <label for="language"><@message code="account.register.language.label"/>:</label>
                        </td>
                        <td>
                        <@wisematches.field path="registration.language">
                            <select id="language" name="language" style="width: 170px;">
                                <option value="en" <#if (locale=="en")>selected="selected"</#if>>English
                                </option>
                                <option value="ru" <#if (locale=="ru")>selected="selected"</#if>>Русский
                                </option>
                            </select>
                        </@wisematches.field>
                            <input type="hidden" id="timezone" name="timezone" value="0">
                            <script type="text/javascript">
                                document.getElementById('timezone').value = new Date().getTimezoneOffset();
                            </script>
                            <span class="sample"><@message code="account.register.language.description"/></span>
                        </td>
                    </tr>

                    <tr>
                        <td>
                        <#--@declare id="captcha"-->
                            <label for="captcha"><@message code="captcha.label"/>:</label>
                        </td>
                        <td>
                        <@wisematches.captcha path="registration.captcha"/>
                            <span class="sample"><@message code="captcha.description"/></span>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <label><@message code='account.register.terms.label'/>:</label>
                        </td>
                        <td align="center">
                            <div style="padding-bottom: 10px;"><@message code="account.register.terms.description"/></div>

                            <button id="createAccount"
                                    name="createAccount"
                                    type="submit"
                                    value="submit"><@message code='account.register.submit.label'/></button>
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

<script type="text/javascript">
    Ext.onReady(function() {
        wm.account.loadTermsPage('terms');
    });
</script>