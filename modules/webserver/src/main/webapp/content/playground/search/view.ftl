<#include "/core.ftl">

<@wm.jstable/>

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
    <div id="searchTypes">
        <input id="searchFriends" name="searchFriends" type="radio" checked="checked"/>
        <label for="searchFriends">Friends</label>
    </div>

    <div id="searchContent">
        <table id="friends" width="100%" class="display">
            <thead>
            <tr>
                <th nowrap="nowrap">Player</th>
                <th nowrap="nowrap">Language</th>
                <th nowrap="nowrap">Rating</th>
                <th width="100%">Actions</th>
            </tr>
            </thead>
        </table>
    </div>
</div>

<script type="text/javascript">
    $("#searchTypes").buttonset();

    $('#friends').dataTable({
        "bJQueryUI": true,
        "bSortClasses": false,
        "aoColumns": [
            { "bSortable": true },
            { "bSortable": true},
            { "bSortable": true},
            { "bSortable": false }
        ],
//        "bProcessing": true,
//        "sAjaxSource": "/playground/search/a",
//        "sAjaxDataProp": "data.aaData",
        "sDom": '<r<t>ip>',
        "sPaginationType": "full_numbers"
    });

    wm.search = new function() {
        this.openDialog = function(scriplets) {
            $.post('/playground/search/friends.ajax', function(result) {
                if (result.success) {
                    $.each(result.data.players, function(i, d) {
                        var table = $('#friends').dataTable();
                        table.fnClearTable();
                        table.fnAddData([wm.ui.player(d), d.language, d.rating, "asd"]);
                    });
                }
            });
//            var a = $("#searchContent").html('<div class="loading-image" style="height: 250px"></div>').load('/playground/search/friends',
//                    function() {
//                        a.find(".scriplet").html('<a href="#" onclick="' + scriplets.action + '()">' + scriplets.text + '</a>')
//                    });

            $("#searchPlayerWidget").dialog({
                title: 'Search Player',
                width: 600,
//                height: 400,
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
