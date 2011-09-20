if (wm == null) wm = {};
if (wm.game == null) wm.game = {};

wm.game.Search = function(columns, scriplet, language) {
    var players;
    var callback;

    var search = this;

    $.each(columns, function(i, a) {
        if (a.sName == 'nickname') {
            a.fnRender = function (oObj) {
                return wm.ui.player(oObj.aData.nickname);
            };
        }
    });

    var resultTable = $('#searchResult').dataTable({
        "bJQueryUI": true,
        "bSortClasses": false,
        "aoColumns": columns,
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": "/playground/players/load.ajax",
        "fnServerData": function (sSource, aoData, fnCallback) {
            var data = {};
            for (var i in aoData) {
                data[aoData[i]['name']] = aoData[i]['value'];
            }
            $.post(sSource + "?area=" + $("input[name='searchTypes']:checked").val(), $.toJSON(data), function (json) {
                players = json.aaData;
                fnCallback(json)
            });
        },
        "sDom": '<"H"lCr>t<"F"ip>',
        "sPaginationType": "full_numbers"
    });

    var reloadContent = function() {
        resultTable.fnDraw();
    };

    resultTable.find("tbody").click(function(event) {
        var p = $(event.target).closest('tr');
        search.closeDialog();
        var pos = resultTable.fnGetPosition(p.get(0));
        callback(players[pos][0]);
    });

    this.closeDialog = function() {
        $("#searchPlayerWidget").dialog('close');
    };

    this.openDialog = function(c) {
        callback = c;
        reloadContent();
        $("#searchPlayerWidget").dialog({
            title: language['title'],
            modal: true,
            width: 800,
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

    if (!scriplet) {
        reloadContent();
    }
};

wm.game.Create = function(maxOpponents, opponentsCount, playerSearch) {
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
