<#-- @ftlvariable name="title" type="java.lang.String" -->
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
       style="background: none; border: 0; padding: 0">
    <tr>
        <td width="165px">
            <img id="header-image" src="<@wm.ui.static "images/logo.png"/>" width="165px" height="75px"/>
        </td>
        <td>
            <table style="width: 100%; height: 75px">
                <tr>
                    <td id="header-welcome-label" style="width: 100%; text-align: left; vertical-align: bottom">
                        <span class="info-header">
                        <span class="info-label">
                        <@message code=title!"title.default"/>
                        </span>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right; vertical-align: bottom">
                        <a rel="alternate" href="<@languageUrl code="ru"/>">Русский</a>
                        <a rel="alternate" href="<@languageUrl code="en"/>">English</a>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
