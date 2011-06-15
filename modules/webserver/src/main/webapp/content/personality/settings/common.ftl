<#-- @ftlvariable name="timeZones" type="java.util.Collection<java.util.TimeZone>" -->
<#-- @ftlvariable name="settings" type="wisematches.server.web.controllers.personality.settings.form.SettingsForm" -->
<#include "/core.ftl">

<table class="common-settings ui-widget-content ui-state-default shadow ui-corner-all" style="background-image: none;"
       width="100%">
    <tr>
        <td>
            <label for="language"><@message code="account.register.language.label"/>:</label>
        </td>
        <td>
        <@wm.field path="settings.language">
            <select id="language" name="language" style="width: 170px;">
                <#list ["en", "ru"] as l>
                    <option value="${l}" <#if (locale==l)>selected="selected"</#if>>
                    <@message code="language.${l}"/>
                    </option>
                </#list>
            </select>
        </@wm.field>
            <span class="sample"><@message code="account.register.language.description"/></span>
        </td>
    </tr>

    <tr>
        <td>
            <label for="timezone">Time zone:</label>
        </td>
        <td>
        <@wm.field path="settings.timezone">
            <select id="timezone" name="timezone" style="width: 170px;">
                <#list timeZones as tz>
                    <option value="${tz.ID}" <#if (wm.statusValue==tz.ID)>selected="selected"</#if>>
                    ${tz.ID} (GMT:<#if (tz.rawOffset>=0)>+<#else>-</#if>${tz.rawOffset/3600000}:${tz.rawOffset%3600000})
                    </option>
                </#list>
            </select>
        </@wm.field>
            <div class="sample">
                All displayed dates and times will be converted to that time zone. By default GMT timezone is used.
            </div>
        </td>
    </tr>

    <tr>
        <td>
            <label for="email">EMail address:</label>
        </td>
        <td>
        <#if !settings.changeEmail><a href="#" onclick="wm.setting.changeEmail(this);">change email</a></#if>

            <div id="emailPane" <#if !settings.changeEmail>class="ui-helper-hidden"</#if>>
            <@wm.fieldInput path="settings.email"/>
                <span class="sample"><@message code="account.register.email.description"/></span>
                <input id="changeEmail" name="changeEmail" type="hidden" value="${settings.changeEmail?string}">
            </div>
        </td>
    </tr>

    <tr>
        <td>
            <label for="password">Password:</label>
        </td>
        <td>
        <#if !settings.changePassword>
            <a href="#" onclick="wm.setting.changePassword(this);">change password</a>
        </#if>

            <div id="passwordPane" <#if !settings.changePassword>class="ui-helper-hidden"</#if>>
                <span><@message code="account.register.pwd.label"/>:</span>
            <@wm.fieldInput path="settings.password" fieldType="password"/>
                <span><@message code="account.register.pwd-cfr.label"/>:<span>
                <@wm.fieldInput path="settings.confirm" fieldType="password"/>
                    <input id="changePassword" name="changePassword" type="hidden"
                           value="${settings.changePassword?string}">
            </div>
        </td>
    </tr>

    <tr>
        <td></td>
        <td align="left">
            <button id="save" name="save" type="submit" value="submit">Save Changes</button>
        </td>
    </tr>
</table>

<script type="text/javascript">
    wm.setting = new function() {
        this.changeEmail = function(link) {
            $(link).slideUp();
            $('#emailPane').slideDown();
            $('#changeEmail').val('true');
            return false;
        };

        this.changePassword = function(link) {
            $(link).slideUp();
            $('#passwordPane').slideDown();
            $('#changePassword').val('true');
            return false;
        }
    }
</script>
