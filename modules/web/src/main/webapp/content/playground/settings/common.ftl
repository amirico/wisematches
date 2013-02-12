<#-- @ftlvariable name="timeZones" type="java.util.Collection<wisematches.server.web.servlet.mvc.playground.player.settings.form.TimeZoneInfo>" -->
<#-- @ftlvariable name="settings" type="wisematches.server.web.servlet.mvc.playground.player.settings.form.SettingsForm" -->
<#include "/core.ftl">

<table class="common-settings ui-widget-content ui-state-default shadow ui-corner-all" style="background-image: none;"
       width="100%">
    <tr>
        <td>
            <label for="language"><@message code="account.register.language.label"/>:</label>
        </td>
        <td>
        <@wm.ui.field path="settings.language">
            <select id="language" name="language" style="width: 170px;">
                <#list ["en", "ru"] as l>
                    <option value="${l}" <#if (locale==l)>selected="selected"</#if>>
                        <@message code="language.${l?lower_case}"/>
                    </option>
                </#list>
            </select>
        </@wm.ui.field>
            <span class="sample"><@message code="account.register.language.description"/></span>
        </td>
    </tr>

    <tr>
        <td>
            <label for="timezone"><@message code="account.register.timezone.label"/>:</label>
        </td>
        <td>
        <@wm.ui.field path="settings.timezone">
            <select id="timezone" name="timezone" style="width: 400px;">
                <#list timeZones as tz>
                    <option value="${tz.id}" <#if (wm.ui.statusValue==tz.id)>selected="selected"</#if>>
                        (<@message code="account.register.timezone.gmt"/> ${tz.offsetName}) ${tz.id}
                    </option>
                </#list>
            </select>
        </@wm.ui.field>
            <div class="sample"><@message code="account.register.timezone.description"/></div>
        </td>
    </tr>

    <tr>
        <td>
        <#--@declare id="email"--><label for="email"><@message code="account.modify.email.label"/>:</label>
        </td>
        <td>
        <#if !settings.changeEmail><a href="#"
                                      onclick="wm.setting.changeEmail(this);"><@message code="account.modify.change.email"/></a></#if>

            <div id="emailPane" <#if !settings.changeEmail>class="ui-helper-hidden"</#if>>
            <@wm.ui.input path="settings.email"/>
                <span class="sample"><@message code="account.register.email.description"/></span>
                <input id="changeEmail" name="changeEmail" type="hidden" value="${settings.changeEmail?string}">
            </div>
        </td>
    </tr>

    <tr>
        <td>
        <#--@declare id="password"--><label for="password"><@message code="account.modify.password.label"/>:</label>
        </td>
        <td>
        <#if !settings.changePassword>
            <a href="#" onclick="wm.setting.changePassword(this);"><@message code="account.modify.change.password"/></a>
        </#if>

            <div id="passwordPane" <#if !settings.changePassword>class="ui-helper-hidden"</#if>>
                <span><@message code="account.register.pwd.label"/>:</span>
            <@wm.ui.input path="settings.password" fieldType="password"/>
                <span><@message code="account.register.pwd-cfr.label"/>:<span>
                <@wm.ui.input path="settings.confirm" fieldType="password"/>
                    <input id="changePassword" name="changePassword" type="hidden"
                           value="${settings.changePassword?string}">
            </div>
        </td>
    </tr>
</table>

<script type="text/javascript">
    wm.setting = new function () {
        this.changeEmail = function (link) {
            $(link).slideUp();
            $('#emailPane').slideDown();
            $('#changeEmail').val('true');
            return false;
        };

        this.changePassword = function (link) {
            $(link).slideUp();
            $('#passwordPane').slideDown();
            $('#changePassword').val('true');
            return false;
        };
    };
</script>
