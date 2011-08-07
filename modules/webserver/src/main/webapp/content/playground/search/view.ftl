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
                    <th nowrap="nowrap">Rating</th>
                    <th nowrap="nowrap">Language</th>
                    <th nowrap="nowrap">Active Games</th>
                    <th nowrap="nowrap">Finished Games</th>
                    <th nowrap="nowrap">Average Move Time</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>

<script type="text/javascript">
    wm.search = new function() {
        var players;
        var callback;

        var languages = {
            ru:'<@message code="language.ru"/>',
            en:'<@message code="language.en"/>'
        };

        var resultTable = $('#searchResult table').dataTable({
            "bJQueryUI": true,
            "bSortClasses": false,
            "aoColumns": [
                { "bSortable": true },
                { "bSortable": true },
                { "bSortable": true },
                { "bSortable": true },
                { "bSortable": true },
                { "bSortable": true }
            ],
            "sDom": '<r<t>ip>',
            "sPaginationType": "full_numbers"
        });

        resultTable.click(function(event) {
            var p = $(event.target).closest('tr').find("div[row]").attr('row');
            closeDialog();
            callback(players[p]);
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
                        resultTable.fnAddData(['<div row="' + i + '">' + wm.ui.player(d, true) + '</div>', d.rating, languages[d.language], d.activeGames, d.finishedGames, d.averageMoveTime]);
                    });
                }
                $('#searchLoading').hide();
                $('#searchResult').show();
            });
        };

        var reloadContent = function() {
            loadContent($("input[name='searchTypes']:checked").val());
        };

        var closeDialog = function() {
            $("#searchPlayerWidget").dialog('close');
        };

        this.openDialog = function(c) {
            callback = c;

            reloadContent();

            $("#searchPlayerWidget").dialog({
                title: 'Search Player',
                modal: true,
                width: 600,
                buttons: {
                    'Close': function() {
                        $(this).dialog("close");
                    }
                }
            });
            return false;
        };

        $("#searchTypes").buttonset().change(reloadContent);
    };
</script>
