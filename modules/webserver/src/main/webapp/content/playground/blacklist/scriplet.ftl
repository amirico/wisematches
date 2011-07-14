<script type="text/javascript">
    wm.ignores = new function() {
        this.ignore = function(pid, callback) {
            wm.ui.showStatus("Ignoring the player. Please wait...");
            $.post('/playground/blacklist/add.ajax', $.toJSON({person: pid, comment: 'Mock comment'}), function(result) {
                if (result.success) {
                    wm.ui.showStatus("Abuse report has been sent");
                    if (callback != undefined) {
                        callback(pid);
                    }
                } else {
                    wm.ui.showStatus(result.summary, true);
                }
            });
        }
    };
</script>

<#macro ignore_link pid callback=""><a title="Add the player to ignore list" href="#"
                                       onclick="wm.ignores.ignore(${pid}, <#if callback?has_content>${callback}</#if>)"><#nested/></a></#macro>