<#include "/core.ftl">

<@wm.jstable/>

<div id="searchPlayerWidget" class="ui-helper-hidden">
    <div id="searchTypes">
        <input id="searchFriends" name="searchTypes" type="radio" value="friends" checked="checked"/>
        <label for="searchFriends">Friends</label>

        <input id="searchOtherFriends" name="searchTypes" type="radio" value="other"/>
        <label for="searchOtherFriends">Other Friends</label>
    </div>

    <div id="searchContent">
        <div id="searchLoading" class="ui-helper-hidden">
            <div class="loading-image" style="height: 100px"></div>
        </div>
        <div id="searchResult" class="ui-helper-hidden">
            <table style="width: 100%; padding-bottom: 5px;">
                <thead>
                <tr>
                    <th width="100%" nowrap="nowrap">Player</th>
                    <th nowrap="nowrap">Language</th>
                    <th nowrap="nowrap">Rating</th>
                    <th>Actions</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>

<script type="text/javascript">
    wm.search = new function() {
        var f_actions;
        var players;

        var languages = {
            ru:'<@message code="language.ru"/>',
            en:'<@message code="language.en"/>'
        };

        var resultTable = $('#searchResult table').dataTable({
            "bJQueryUI": true,
            "bSortClasses": false,
            "aoColumns": [
                { "bSortable": true },
                { "bSortable": true},
                { "bSortable": true},
                { "bSortable": false }
            ],
            "sDom": '<r<t>ip>',
            "sPaginationType": "full_numbers"
        });

        var loadContent = function(name) {
            $('#searchResult').hide();
            $('#searchLoading').show();

            players = new Array();
            resultTable.fnClearTable();

            $.post('/playground/search/' + name + '.ajax', function(result) {
                if (result.success) {
                    $.each(result.data.players, function(i, d) {
                        players[i] = d;
                        var a = '';
                        $.each(f_actions, function(i, d) {
                            a += '<a href="#" onclick="wm.search.callCallback(' + d.action + ',' + i + ');">' + d.text + '</a>';
                        });
                        resultTable.fnAddData([wm.ui.player(d), languages[d.language], d.rating, a]);
                    });
                }
                $('#searchLoading').hide();
                $('#searchResult').show();
            });
        };

        var reloadContent = function() {
            loadContent($("input[name='searchTypes']:checked").val());
        };

        this.callCallback = function(name, i) {
            name(players[i]);
        };

        this.openDialog = function(actions) {
            f_actions = actions;

            reloadContent();

            $("#searchPlayerWidget").dialog({
                title: 'Search Player',
                width: 600,
                buttons: {
                    'Close': function() {
                        $(this).dialog("close");
                    }
                }
            });
            return false;
        };

        this.closeDialog = function() {
            $("#searchPlayerWidget").dialog('close');
        };

        $("#searchTypes").buttonset().change(reloadContent);
    };
</script>
