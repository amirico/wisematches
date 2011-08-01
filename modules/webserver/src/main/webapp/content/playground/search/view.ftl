<style type="text/css">
    #searchPlayerWidget {
        padding-left: 0;
        padding-right: 0;
        padding-top: 0;
        padding-bottom: 5px;
    }

    #searchTypes {
        padding: 0;
        padding-top: 5px;
        padding-bottom: 5px;
        margin: 0;
    }
</style>

<div id="searchPlayerWidget" class="ui-helper-hidden">
    <div id="searchTypes" class="ui-widget-content"
         style="background: none; border-width: 0;border-bottom-width: 1px;">
        <input id="searchFriends" name="searchFriends" type="radio" checked="checked"/>
        <label for="searchFriends">Friends</label>
    </div>

    <div id="searchContent"></div>
</div>

<script type="text/javascript">
    $("#searchTypes").buttonset();

    wm.search = new function() {
        this.openDialog = function(scriplets) {
            var a = $("#searchContent").html('<div class="loading-image" style="height: 250px"></div>').load('/playground/search/friends',
                    function() {
                        a.find(".scriplet").html('<a href="#" onclick="' + scriplets.action + '()">' + scriplets.text + '</a>')
                    });

            $("#searchPlayerWidget").dialog({
                title: 'Search Player',
                width: 600,
                height: 400,
                buttons: {
                    'Close': function() {
                        $(this).dialog("close");
                    }
                }
            });
            return false;
        };
    };
</script>
