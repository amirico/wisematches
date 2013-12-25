<#-- @ftlvariable name="templateName" type="java.lang.String" -->
<#include "/core.ftl">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/content/meta.ftl"/>
</head>
<body>
<#include 'browser.ftl'/>

<div id="wisematchesHeader">
    <table id="header" width="100%" cellpadding="0" cellspacing="0" class="ui-widget-content shadow"
           style="background: none; border: 0; padding: 0">
        <tr>
            <td width="170px" valign="top">
                <img src="<@wm.ui.static "images/logo/logo170x70x2.png"/>" width="170px" height="70px"/>
            </td>
            <td style="padding-right: 5px">
            <#if principal??><#include "playground/header.ftl"/><#else><#include "account/header.ftl"/></#if>
            </td>
        </tr>
    </table>
<#include "visitor.ftl"/>
</div>

<div id="notification-block"></div>

<#if principal??>
<div id="header-separator" style="height: 20px;"></div>
</#if>

<div id="wisematchesContent" style="padding: 5px; display: none;">
<#include "${templateName}"/>
</div>

<div id="wisematchesFooter" style="display: none;">
<#if principal?? && principal?has_content><#include "playground/footer.ftl"/><#else><#include "account/footer.ftl"/></#if>
</div>

<script type="text/javascript">
    $("#wisematchesHeader").find("#gameToolbar").show();
    $("#wisematchesFooter").show();
    $("#wisematchesContent").show();
</script>
</body>
</html>