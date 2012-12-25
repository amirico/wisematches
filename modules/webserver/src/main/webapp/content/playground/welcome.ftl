<#include "/core.ftl"/>

<script src="http://vk.com/js/api/openapi.js" type="text/javascript"></script>

<div id="fb-root"></div>

<table width="100%">
    <tr>
        <td valign="top">
        <#include "/content/playground/profile/edit.ftl"/>
        </td>
        <td valign="top">
            <div id="vk_groups"></div>
        </td>
    </tr>
</table>

<script type="text/javascript">
    VK.Widgets.Group("vk_groups", {mode: 0, width: "200px"}, 46620223);
</script>
