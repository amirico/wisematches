<#include "/core.ftl">

<script type="text/javascript">
    wm.friends = new function () {
        this.add = function (pid, callback) {
            var dlg = $("#friendInfoDialog");
            dlg.dialog({
                title: "<@message code="friends.confirm.label"/>",
                width: 400,
                modal: true,
                buttons: {
                    "<@message code="friends.confirm.execute"/>": function () {
                        var widget = dlg.closest(".ui-dialog");
                        var comment = $("#friendInfoDialog textarea").val()
                        wm.ui.lock(widget, "<@message code="friends.status.adding"/>");
                        $.post('/playground/friends/add.ajax', JSON.stringify({person: pid, comment: comment}), function (result) {
                            if (result.success) {
                                wm.ui.unlock(widget, "<@message code="friends.status.added"/>");
                                if (callback != undefined) {
                                    callback(pid);
                                }
                                dlg.dialog("close");
                            } else {
                                wm.ui.unlock(widget, result.message, true);
                            }
                        });
                    },
                    "<@message code="button.cancel"/>": function () {
                        dlg.dialog("close");
                    }
                }});
            return false;
        };
    };
</script>

<div id="friendInfoDialog" class='friend-info ui-helper-hidden'>
    <div style="text-align: justify;"><@message code="friends.confirm.description"/></div>
    <div>&nbsp;</div>
    <div><@message code="friends.confirm.comment"/>:</div>
    <div>
        <textarea style="width: 100%" rows="7"></textarea>
    </div>
</div>

<#macro friends pid callback=""><a title="<@message code="friends.confirm.label"/>" href="#"
                                   onclick="wm.friends.add(${pid}<#if callback?has_content>, ${callback}</#if>)"><#nested/></a></#macro>