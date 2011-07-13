<script type="text/javascript">
    wm.ignores = new function() {
        this.ignore = function(player) {
            alert("Ignore user: " + player);
        }
    };
</script>

<#macro ignore_link pid><a title="Add the player to ignore list" href="#"
                           onclick="wm.ignores.ignore(${pid})"><#nested/></a></#macro>