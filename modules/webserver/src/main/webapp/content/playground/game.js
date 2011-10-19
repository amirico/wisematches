if (wm == null) wm = {};
if (wm.game == null) wm.game = {};

wm.game.help = new function() {
    this.showHelp = function(section, ctx) {
        $('<div><div class="loading-image" style="height: 300px"></div></div>').load(section + '?plain=true').dialog({
            title:ctx != undefined ? $(ctx).text() : '',
            width:650,
            height:450,
            modal:true,
            resizable:true,
            buttons:[
                {
                    text:wm.i18n.value('button.close', 'Close'),
                    click:function() {
                        $(this).dialog("close");
                    }
                }
            ]
        });
        return false;
    };
};

wm.game.History = function(pid, columns, language) {
    $.each(columns, function(i, a) {
        if (a.sName == 'players') {
            a.fnRender = function(oObj) {
                var res = "";
                var opponents = oObj.aData['players'];
                for (var i in opponents) {
                    res += wm.ui.player(opponents[i]);
                    if (i != opponents.length - 1) {
                        res += ', ';
                    }
                }
                return res;
            };
        } else if (a.sName == 'ratingChange') {
            a.fnRender = function(oObj) {
                var rc = oObj.aData['ratingChange'];
                var res = '';
                res += '<div class="rating ' + (rc < 0 ? 'down' : rc == 0 ? 'same' : 'up') + '">';
                res += '<div class="change"><sub>' + (rc < 0 ? '' : '+') + rc + '</sub></div>';
                res += '</div>';
                return res;
            }
        } else if (a.sName == 'resolution') {
            a.fnRender = function(oObj) {
                var id = oObj.aData['boardId'];
                var state = oObj.aData['resolution'];
                if (id != 0) {
                    return "<a href='/playground/scribble/board?b=" + id + "'>" + state + "</a>";
                } else {
                    return state;
                }
            }
        }
    });

    $('#history').dataTable({
        "bJQueryUI":true,
        "bStateSave":false,
        "bFilter":false,
        "bSortClasses":false,
        "aaSorting":[
            [0, 'asc']
        ],
        "iDisplayStart":0,
        "aoColumns":columns,
        "bProcessing":true,
        "bServerSide":true,
        "sAjaxSource":"/playground/scribble/history/load.ajax?p=" + pid,
        "sDom":'<"H"lCr>t<"F"ip>',
        "sPaginationType":"full_numbers",
        "fnServerData":function(sSource, aoData, fnCallback) {
            var data = {};
            for (var i in aoData) {
                data[aoData[i]['name']] = aoData[i]['value'];
            }
            $.post(sSource, $.toJSON(data), fnCallback);
        },
        "oLanguage":language
    });
};

wm.game.Search = function(columns, scriplet, language) {
    var players;
    var callback;

    var search = this;

    $.each(columns, function(i, a) {
        if (a.sName == 'nickname') {
            a.fnRender = function(oObj) {
                return wm.ui.player(oObj.aData.nickname, scriplet);
            };
        }
    });

    var resultTable = $('#searchResult').dataTable({
        "bJQueryUI":true,
        "bSortClasses":false,
        "aoColumns":columns,
        "bProcessing":true,
        "bServerSide":true,
        "aaSorting":[
            [ 2, "desc" ]
        ],
        "sAjaxSource":"/playground/players/load.ajax",
        "fnServerData":function(sSource, aoData, fnCallback) {
            var data = {};
            for (var i in aoData) {
                data[aoData[i]['name']] = aoData[i]['value'];
            }
            $.post(sSource + "?area=" + $("input[name='searchTypes']:checked").val(), $.toJSON(data), function(json) {
                players = json.aaData;
                fnCallback(json)
            });
        },
        "sDom":'<"H"lCr>t<"F"ip>',
        "sPaginationType":"full_numbers"
    });

    var reloadContent = function() {
        resultTable.fnDraw();
    };

    resultTable.find("tbody").click(function(event) {
        var p = $(event.target).closest('tr');
        search.closeDialog();
        var pos = resultTable.fnGetPosition(p.get(0));
        callback(players[pos]['nickname']);
    });

    this.closeDialog = function() {
        $("#searchPlayerWidget").dialog('close');
    };

    this.openDialog = function(c) {
        callback = c;
        reloadContent();
        $("#searchPlayerWidget").dialog({
            title:language['title'],
            modal:true,
            width:800,
            buttons:[
                {
                    text:wm.i18n.value('button.close', 'Close'),
                    click:function() {
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
