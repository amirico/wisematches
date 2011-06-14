<#-- @ftlvariable name="timeZones" type="java.util.Collection<java.util.TimeZone>" -->
<#include "/core.ftl">

<table class="ui-widget-content" width="100%">
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
            <div class="sample">
                All text will be show in this language
            </div>
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
        <td><label for="email">EMail:</label></td>
        <td>
            <a href="javascript: $('#emailPane').show();">change email</a>
            <div id="emailPane" class="ui-helper-hidden">
                <@wm.field path="settings.email"/>
            </div>
        </td>
    </tr>

    <tr>
        <td><label for="password">Password:</label></td>
        <td><a href="#">change password</a></td>
    </tr>

    <tr>
        <td></td>
        <td align="left">
            <button id="save" name="save" type="submit" value="submit">Save Changes</button>
        </td>
    </tr>
</table>
