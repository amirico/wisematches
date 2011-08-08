wm.Search = function(language) {
    var players;
    var callback;

    var languages = {
        ru: language['ru'],
        en: language['en']
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
            title: language['title'],
            modal: true,
            width: 600,
            buttons: [
                {
                    text: language['close'],
                    click: function() {
                        $(this).dialog("close");
                    }
                }
            ]
        });
        return false;
    };

    $("#searchTypes").buttonset().change(reloadContent);
};

wm.Create = function(maxOpponents, opponentsCount, playerSearch) {
    var attachPlayerSearchActions = function(a) {
        $(a).hover(
                function() {
                    $(this).addClass("player-search-remove");
                },
                function() {
                    $(this).removeClass("player-search-remove");
                }).click(function() {
                    $(this).fadeOut('fast', function() {
                        $(this).remove();
                        if (opponentsCount == maxOpponents) {
                            $("#opponentsControl").fadeIn('slow');
                        }
                        opponentsCount--;
                    });
                });
    };

    this.selectOpponent = function() {
        playerSearch.openDialog(insertPlayer);
        return false;
    };

    var insertPlayer = function(playerInfo) {
        var s = $('<div style="display: none;">' + wm.ui.player(playerInfo, true) + '<input type="hidden" name="opponents" value="' + playerInfo.playerId + '"/></div>');
        attachPlayerSearchActions(s);
        $("#opponentsList").append(s);
        $("#opponentsList .ui-state-error-text").remove();
        s.fadeIn('fast');
        opponentsCount++;
        if (opponentsCount == maxOpponents) {
            $("#opponentsControl").fadeOut('slow');
        }
    };

    $("#opponentsList div").each(function(i, a) {
        attachPlayerSearchActions(a);
    });

    $("#createGame #radio").buttonset();
    $("#createGame button").button();

    $("#opponentTypeRobot").change(function() {
        $(".create-form").slideUp();
        $("#robotForm").slideDown();
    });

    $("#opponentTypeWait").change(function() {
        $(".create-form").slideUp();
        $("#waitingForm").slideDown();
    });

    $("#opponentTypeChallenge").change(function() {
        $(".create-form").slideUp();
        $("#challengeForm").slideDown();
    });

    $(".player-search-action").hover(function() {
                $(this).addClass("ui-state-hover");
            }, function() {
                $(this).removeClass("ui-state-hover");
            });
};
