<#include "/core.ftl">

<script type="text/javascript">
    wm.blacklist = new function () {
        this.add = function (pid, callback) {
            var dlg = $("#blacklistConfirmDialog");
            dlg.dialog({
                title: "<@message code="blacklist.confirm.label"/>",
                width: 400,
                modal: true,
                buttons: {
                    "<@message code="blacklist.confirm.execute"/>": function () {
                        var widget = dlg.closest(".ui-dialog");
                        var comment = $("#blacklistConfirmDialog textarea").val()
                        wm.ui.lock(widget, "<@message code="blacklist.status.adding"/>");
                        $.post('/playground/blacklist/add.ajax', $.toJSON({person: pid, comment: comment}), function (result) {
                            if (result.success) {
                                wm.ui.unlock(widget, "<@message code="blacklist.status.added"/>");
                                if (callback != undefined) {
                                    callback(pid);
                                }
                                dlg.dialog("close");
                            } else {
                                wm.ui.unlock(widget, result.summary, true);
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

<div id="blacklistConfirmDialog" class='blacklist-confirm ui-helper-hidden'>
    <div style="text-align: justify;"><@message code="blacklist.confirm.description"/></div>
    <div>&nbsp;</div>
    <div><@message code="blacklist.confirm.comment"/>:</div>
    <div>
        <textarea style="width: 100%" rows="7"></textarea>
    </div>
</div>

<#macro blacklist pid callback=""><a title="<@message code="blacklist.confirm.label"/>" href="#"
                                     onclick="wm.blacklist.add(${pid}<#if callback?has_content>, ${callback}</#if>)"><#nested/></a></#macro>