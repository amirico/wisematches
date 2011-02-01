<#-- @ftlvariable name="headerTitle" type="java.lang.String" -->
<#include "/core.ftl">

<table id="header">
    <tr>
        <td>
            <img id="header-image" src="/resources/images/logo.png" width="178" height="72"/>
        </td>
        <td style="width: 100%">
        <@ext.topRoundPanel id="header-panel">
            <table>
                <tr>
                    <td id="header-welcome-label" style="width: 100%; text-align: left;">
                    <@message code="${headerTitle!'wisematches.title'}"/>
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
        </@ext.topRoundPanel>
        </td>
    </tr>
</table>
