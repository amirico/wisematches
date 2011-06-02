<#-- @ftlvariable name="headerTitle" type="java.lang.String" -->
<#include "/core.ftl">

<table id="header" width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td width="178px">
            <img id="header-image" src="/resources/images/logo.png" width="178" height="72"/>
        </td>
        <td style="width: 100%">
        <@wm.topRoundPanel id="header-panel">
            <table>
                <tr>
                    <td id="header-welcome-label" style="width: 100%; text-align: left;">
                    <@message code=headerTitle!"title.default"/>
                    </td>
                    <td style="text-align: right;">
                        <select id="language-combobox" size="1"
                                onchange="location.href = wm.util.url.extend(null, 'language', this.options[this.selectedIndex].value, true)">
                            <option value="ru" <#if locale="ru">selected="selected"</#if>>Русский</option>
                            <option value="en" <#if locale="en">selected="selected"</#if>>English</option>
                        </select>
                    </td>
                </tr>
            </table>
        </@wm.topRoundPanel>
        </td>
    </tr>
</table>
