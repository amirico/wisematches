<#include "/core.ftl">

<script type="text/javascript">
    wm.friends = new function() {
        var execute = function(pid, comment, callback) {
            wm.ui.showStatus("<@message code="friends.status.adding"/>");
            $.post('/playground/friends/add.ajax', $.toJSON({person: pid, comment: comment}), function(result) {
                if (result.success) {
                    wm.ui.showStatus("<@message code="friends.status.added"/>");
                    if (callback != undefined) {
                        callback(pid);
                    }
                } else {
                    wm.ui.showStatus(result.summary, true);
                }
            });
        };

        this.add = function(pid, callback) {
            $("#friendInfoDialog").dialog({
                title: "<@message code="friends.confirm.label"/>",
                width: 400,
                modal: true,
                buttons: {
                    "<@message code="friends.confirm.execute"/>": function() {
                        $(this).dialog("close");
                        execute(pid, $("#friendInfoDialog textarea").val(), callback);
                    },
                    "<@message code="button.cancel"/>": function() {
                        $(this).dialog("close");
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