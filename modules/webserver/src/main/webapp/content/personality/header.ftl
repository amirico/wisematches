<#-- @ftlvariable name="headerTitle" type="java.lang.String" -->
<#-- @ftlvariable name="requestQueryString" type="java.lang.String" -->
<#include "/core.ftl">

<#macro languageUrl code>
    <#if requestQueryString?? && requestQueryString?has_content>
        <#assign requestQueryString=requestQueryString?replace("language=\\w+\\&?", "", "ri")/>
    </#if>

    <#if requestQueryString?? && requestQueryString?has_content>
    ?${requestQueryString}<#if !requestQueryString?ends_with("&")>&</#if>language=${code}
    <#else>
    ?language=${code}
    </#if>
</#macro>

<table id="header" width="100%" cellpadding="0" cellspacing="0" class="ui-widget-content shadow"
       style="background: none; border: 0;">
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
                        <div id="language-combobox" class="ui-widget" style="display:  inline-block;">
                            <div class="dropdown ui-state-default ui-corner-all">
                                <span style="padding-right: 20px;">
                                    <#switch locale>
                                        <#case "ru">Русский
                                            <#break>
                                        <#case "en">English
                                            <#break>
                                    </#switch>
                                </span>
                                <span style="position: absolute; right: 0;"
                                      class="ui-button-icon-primary ui-icon ui-icon-triangle-1-s"></span>
                            </div>
                            <ul class="sublinks ui-widget ui-widget-content ui-corner-all">
                                <li class="ui-state-default ui-corner-all">
                                    <a rel="alternate" href="<@languageUrl code="ru"/>">Русский</a>
                                </li>
                                <li class="ui-state-default ui-corner-all">
                                    <a rel="alternate" href="<@languageUrl code="en"/>">English</a>
                                </li>
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
        </@wm.topRoundPanel>
        </td>
    </tr>
</table>
