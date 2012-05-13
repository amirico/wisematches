<#include "/core.ftl">

<div id="recovery">
    <div id="info-recovery-header" class="info-header">
        <div class="info-label"><@message code="account.recovery.check.label"/></div>

        <div class="info-description">
        <@message code="account.recovery.check.description"/>
        </div>
    </div>

    <div id="info-recovery-form">
        <form id="recoveryForm" action="confirmation" method="post">
            <table class="x-panel" style="padding-left: 10px; padding-right: 10px; width: 400px;">
                <tr>
                    <td colspan="2">
                        <div class="section label"><@message code="account.recovery.check.email.label"/></div>
                    </td>
                </tr>
                <tr>
                    <td>
                    <#--@declare id="email"-->
                        <label for="email"><@message code="account.register.email.label"/>:</label>
                    </td>
                    <td>
                    <@wm.field path="recovery.token">
                        <input type="hidden" id="token" name="token" value="${spring.stringStatusValue}">
                    </@wm.field>

                    <@wm.fieldInput path="recovery.email"/>
                        <span class="sample"><@message code="account.recovery.email.description"/></span>
                    </td>
                </tr>

                <tr>
                    <td colspan="2">
                        <hr>
                    </td>
                </tr>

                <tr>
                    <td colspan="2">
                        <div class="section label"><@message code="account.recovery.check.pwd.label"/></div>
                    </td>
                </tr>
                <tr>
                    <td>
                    <#--@declare id="password"-->
                        <label for="password"><@message code="account.register.pwd.label"/>:</label>
                    </td>
                    <td>
                    <@wm.fieldInput path="recovery.password" fieldType="password"/>
                    </td>
                </tr>
                <tr>
                    <td>
                    <#--@declare id="confirm"-->
                        <label for="confirm"><@message code="account.register.pwd-cfr.label"/>:</label>
                    </td>
                    <td>
                    <@wm.fieldInput path="recovery.confirm" fieldType="password"/>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                    <@wm.field path="recovery.rememberMe">
                        <input type="checkbox" id="rememberMe" name="rememberMe"
                               <#if spring.stringStatusValue=="true">checked="checked"</#if>/>
                        <label for="rememberMe"><@message code="account.login.remember.label"/></label>
                    </@wm.field>
                    </td>
                </tr>


                <tr>
                    <td colspan="2">
                        <hr>
                    </td>
                </tr>

                <tr>
                    <td colspan="2">
                        <div class="section label"><@message code="captcha.label"/></div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div style="float: right;">
                        <@wm.captcha path="recovery.captcha"/>
                            <span class="sample"><@message code="captcha.description"/></span>
                        </div>
                    </td>
                </tr>

                <tr>
                    <td></td>
                    <td>
                        <button id="recoveryAccount"
                                name="recoveryAccount"
                                type="submit"
                                value="submit"><@message code='account.recovery.submit.label'/></button>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
