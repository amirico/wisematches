if (wm == null) wm = {};
if (wm.game == null) wm.game = {};
if (wm.game.dict == null) wm.game.dict = {};
if (wm.game.tourney == null) wm.game.tourney = {};
if (wm.game.settings == null) wm.game.settings = {};

wm.game.help = new function () {
    this.showHelp = function (section, ctx) {
        $('<div><div class="loading-image" style="height: 300px"></div></div>').load(section + '?plain=true').dialog({
            title: ctx != undefined ? $(ctx).text() : '',
            width: 650,
            height: 450,
            modal: true,
            resizable: true,
            buttons: [
                {
                    text: wm.i18n.value('button.close', 'Close'),
                    click: function () {
                        $(this).dialog("close");
                    }
                }
            ]
        });
        return false;
    };
};

wm.game.Active = function (language) {
    var widget = $("#activeGamesWidget");
    wm.ui.dataTable('#dashboard', {
        "bStateSave": true,
        "bFilter": false,
        "bSortClasses": false,
        "aaSorting": [
            [3, 'asc']
        ],
        "aoColumns": [
            null,
            null,
            null,
            null,
            { "bSortable": false },
            { "bSortable": false }
        ],
        "oLanguage": language
    });

    this.cancelProposal = function (id) {
        wm.ui.lock(widget, language['cancelling']);
        $.ajax('decline.ajax?p=' + id, {
            success: function (result) {
                if (result.success) {
                    $("#proposal" + id).fadeOut();
                    wm.ui.unlock(widget, language['cancelled']);
                } else {
                    wm.ui.unlock(widget, result.summary, true);
                }
            }
        });
    }
};

wm.game.Join = function (language) {
    var widget = $("#waitingGamesWidget");
    wm.ui.dataTable('#gameboard', {
        "bStateSave": true,
        "bFilter": false,
        "bSort": false,
        "bSortClasses": false,
        "oLanguage": language
    });

    this.accept = function (id) {
        wm.ui.lock(widget, language['accepting']);
        $.post("/playground/scribble/accept.ajax?p=" + id, function (result) {
            if (result.success) {
                if (result.data.board == undefined) {
                    wm.util.url.redirect('/playground/scribble/join');
                } else {
                    wm.util.url.redirect('/playground/scribble/board?b=' + result.data.board);
                }
            } else {
                wm.ui.unlock(widget, result.summary, true);
            }
        });
    };

    this.decline = function (id) {
        wm.ui.lock(widget, language['declining']);
        $.post("/playground/scribble/decline.ajax?p=" + id, function (result) {
            if (result.success) {
                wm.util.url.redirect('/playground/scribble/join');
            } else {
                wm.ui.unlock(widget, result.summary, true);
            }
        });
    };
};

wm.game.Create = function (maxOpponents, opponentsCount, playerSearch, language) {
    var attachPlayerSearchActions = function (a) {
        $(a).hover(
            function () {
                $(this).addClass("player-search-remove");
            },
            function () {
                $(this).removeClass("player-search-remove");
            }).click(function () {
                $(this).fadeOut('fast', function () {
                    $(this).remove();
                    if (opponentsCount == maxOpponents) {
                        $("#opponentsControl").fadeIn('slow');
                    }
                    opponentsCount--;
                });
            });
    };

    this.selectOpponent = function () {
        playerSearch.openDialog(insertPlayer);
        return false;
    };

    var insertPlayer = function (playerInfo) {
        var s = $('<div style="display: none;">' + wm.ui.player(playerInfo, true) + '<input type="hidden" name="opponents" value="' + playerInfo.id + '"/></div>');
        attachPlayerSearchActions(s);
        $("#opponentsList").append(s);
        $("#opponentsList .ui-state-error-text").remove();
        s.fadeIn('fast');
        opponentsCount++;
        if (opponentsCount == maxOpponents) {
            $("#opponentsControl").fadeOut('slow');
        }
    };

    $("#opponentsList div").each(function (i, a) {
        attachPlayerSearchActions(a);
    });

    $("#createTabRobot").change(function () {
        $(".create-form").slideUp();
        $("#robotForm").slideDown();
    });

    $("#createTabWait").change(function () {
        $(".create-form").slideUp();
        $("#waitingForm").slideDown();
    });

    $("#createTabChallenge").change(function () {
        $(".create-form").slideUp();
        $("#challengeForm").slideDown();
    });

    $(".player-search-action").hover(function () {
        $(this).addClass("ui-state-hover");
    }, function () {
        $(this).removeClass("ui-state-hover");
    });

    this.submitForm = function () {
        var $gameWidget = $("#createGame");

        wm.ui.lock($gameWidget, language['waiting']);
        var serializeObject = $("#form").serializeObject();
        if (serializeObject.opponents != undefined && !$.isArray(serializeObject.opponents)) {
            serializeObject.opponents = [serializeObject.opponents];
        }
        $.post("create.ajax", $.toJSON(serializeObject),
            function (response) {
                if (response.success) {
                    if (response.data == null || response.data.board == undefined) {
                        wm.util.url.redirect('/playground/scribble/active');
                    } else {
                        wm.util.url.redirect('/playground/scribble/board?b=' + response.data.board);
                    }
                } else {
                    wm.ui.unlock($gameWidget, response.summary, true);
                }
            }, 'json');
    };
};

wm.game.History = function (pid, columns, language) {
    $.each(columns, function (i, a) {
        if (a.sName == 'title') {
            a.fnRender = function (oObj) {
                var id = oObj.aData['boardId'];
                var title = oObj.aData['title'];
                if (id != 0) {
                    return "<a href='/playground/scribble/board?b=" + id + "'>" + title + "</a>";
                } else {
                    return title;
                }
            };
        } else if (a.sName == 'players') {
            a.fnRender = function (oObj) {
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
            a.fnRender = function (oObj) {
                var rc = oObj.aData['ratingChange'];
                var res = '';
                res += '<div class="rating ' + (rc < 0 ? 'down' : rc == 0 ? 'same' : 'up') + '">';
                res += '<div class="change"><sub>' + (rc < 0 ? '' : '+') + rc + '</sub></div>';
                res += '</div>';
                return res;
            }
            /*
             } else if (a.sName == 'resolution') {
             a.fnRender = function (oObj) {
             var id = oObj.aData['boardId'];
             var state = oObj.aData['resolution'];
             if (id != 0) {
             return "<a href='/playground/scribble/board?b=" + id + "'>" + state + "</a>";
             } else {
             return state;
             }
             }
             */
        }
    });

    wm.ui.dataTable('#history', {
        "bStateSave": false,
        "bFilter": false,
        "bSortClasses": false,
        "aaSorting": [
            [0, 'desc']
        ],
        "iDisplayStart": 0,
        "aoColumns": columns,
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": "/playground/scribble/history/load.ajax?p=" + pid,
        "fnServerData": function (sSource, aoData, fnCallback) {
            var data = {};
            for (var i in aoData) {
                data[aoData[i]['name']] = aoData[i]['value'];
            }
            $.post(sSource, $.toJSON(data), fnCallback);
        },
        "oLanguage": language
    });
};

wm.game.Search = function (columns, scriplet, language) {
    var players;
    var callback;

    var search = this;

    $.each(columns, function (i, a) {
        if (a.sName == 'nickname') {
            a.fnRender = function (oObj) {
                return wm.ui.player(oObj.aData.nickname, scriplet);
            };
        }
    });

    var resultTable = wm.ui.dataTable('#searchResult', {
        "bSortClasses": false,
        "aoColumns": columns,
        "bProcessing": true,
        "bServerSide": true,
        "aaSorting": [
            [ 1, "desc" ],
            [ 2, "desc" ]
        ],
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
        }
    });

    var reloadContent = function () {
        resultTable.fnDraw();
    };

    resultTable.find("tbody").click(function (event) {
        var p = $(event.target).closest('tr');
        search.closeDialog();
        var pos = resultTable.fnGetPosition(p.get(0));
        callback(players[pos]['nickname']);
    });

    this.closeDialog = function () {
        $("#searchPlayerWidget").dialog('close');
    };

    this.openDialog = function (c) {
        callback = c;
        reloadContent();
        $("#searchPlayerWidget").dialog({
            title: language['title'],
            modal: true,
            width: 800,
            buttons: [
                {
                    text: wm.i18n.value('button.close', 'Close'),
                    click: function () {
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

wm.game.dict.Suggestion = function (lang, i18n) {
    var wordEntryEditor = $("#wordEntryEditor");
    var wordEntryAction = wordEntryEditor.find("#action");

    var sendRequest = function () {
        var v = wordEntryEditor.parent();
        wm.ui.lock(v);
        var serializeObject = wordEntryEditor.find("form").serializeObject();
        $.post("/playground/dictionary/editWordEntry.ajax", $.toJSON(serializeObject), function (response) {
            if (response.success) {
                wm.ui.unlock(v, i18n['waiting'], false);
                wordEntryEditor.dialog("close");
            } else {
                wm.ui.unlock(v, response.summary, true);
            }
        });
    };

    var startEditing = function () {
        wordEntryAction.val("UPDATE");
        wordEntryEditor.find(".view").hide();
        wordEntryEditor.find(".edit").show();
        $("#wordEditorRemoveBtn").hide();
        $("#wordEditorChangeBtn").find("span").text(i18n['save']);
    };

    var resetEntryEditor = function () {
        if (wordEntryAction.val() == "ADD") {
            $("#wordEditorRemoveBtn").show();
            wordEntryEditor.find(".create").toggle();
        }
        wordEntryAction.val("VIEW");
        wordEntryEditor.find(".view").show();
        wordEntryEditor.find(".edit").hide();

        wordEntryEditor.find(".word-view").text("");
        wordEntryEditor.find(".word-input").val("");
        wordEntryEditor.find(".definition-view").text("");
        wordEntryEditor.find(".definition-input").val("");
        $("input[name=attributes]").prop('checked', false);
        wordEntryEditor.find(".attributes-view").text("");
    };

    var showWordEntry = function (wordEntry) {
        var dialog = wordEntryEditor.dialog({
            title: i18n['title'],
            dialogClass: 'word-editor-dlg',
            draggable: false,
            modal: true,
            autoOpen: false,
            resizable: false,
            width: 700,
            buttons: [
                {
                    id: 'wordEditorChangeBtn',
                    text: i18n['edit'],
                    click: function () {
                        var val = wordEntryAction.val();
                        if (val == "ADD") {
                            sendRequest();
                        } else if (val == "VIEW") {
                            startEditing();
                        } else {
                            sendRequest();
                        }
                    }
                },
                {
                    id: 'wordEditorRemoveBtn',
                    text: i18n['remove'],
                    click: function () {
                        wm.ui.confirm(i18n['remove.title'], i18n['remove.confirm'], function (approve) {
                            if (approve) {
                                wordEntryAction.val("REMOVE");
                                sendRequest();
                            }
                        });
                    }
                },
                {
                    id: 'wordEditorCancelBtn',
                    text: wm.i18n.value('button.cancel', 'Cancel'),
                    click: function () {
                        $(this).dialog("close");
                    }
                }
            ]
        });

        resetEntryEditor();

        if (wordEntry != null) {
            wordEntryEditor.find(".word-view").text(wordEntry.word);
            wordEntryEditor.find(".word-input").val(wordEntry.word);
            wordEntryEditor.find(".definition-view").text(wordEntry.definitions[0].text);
            wordEntryEditor.find(".definition-input").val(wordEntry.definitions[0].text);

            var attrs = "";
            $.each(wordEntry.definitions[0].attributes, function (i, v) {
                attrs += " " + i18n[v];
                $("#" + v).prop('checked', true);
            });
            wordEntryEditor.find(".attributes-view").text(attrs);
        } else {
            startEditing();
            wordEntryAction.val("ADD");
            wordEntryEditor.find(".create").toggle();
        }
        dialog.dialog("open");
    };

    this.addWordEntry = function () {
        showWordEntry(null);
    };

    this.viewWordEntry = function (word) {
        $.post("/playground/dictionary/loadWordEntry.ajax?l=" + lang + "&w=" + word, function (response) {
            if (response.success) {
                showWordEntry(response.data.wordEntry);
                wm.ui.unlock(null);
            } else {
                wm.ui.unlock(null, response.summary, true);
            }
        });
    };
};

wm.game.dict.Dictionary = function (lang, i18n) {
    var instance = this;
    var dictionary = $("#dictionary");

    var activeTopLetter;
    var activeSubLetter;

    var topAlphabet = dictionary.find("#topAlphabet");
    var topAlphabetLinks = topAlphabet.find("a");

    var subAlphabet = dictionary.find("#subAlphabet");
    var subAlphabetLinks = subAlphabet.find("a");

    var searchInput = dictionary.find("#dictionarySearch");

    var wordsViewPanel = dictionary.find(".scroll-pane");
    var wordsViewTable = wordsViewPanel.find("table");
    var wordsViewStatus = dictionary.find("#wordsCount span");
    var wordsViewScrollPane = wordsViewPanel.jScrollPane({showArrows: true, horizontalGutter: 10, hideFocus: true}).data('jsp');

    var createViewItem = function (entry) {
        var def = "";
        $.each(entry.definitions, function (j, d) {
            if (def != "") {
                def += "<br>";
            }
            var attrs = "";
            $.each(d.attributes, function (k, a) {
                attrs += " " + i18n[a];
            });
            def += "<span class='sample'>" + attrs + "</span> " + d.text;
        });
        return $("" +
            "<tr>" +
            "<td><a href='#' onclick='dictionarySuggestion.viewWordEntry(\"" + entry.word + "\"); return false;'>" + entry.word + "<a/></td>" +
            "<td>" + def + "</td>" +
            "</tr>");
    };

    var loadWordEntries = function (prefix) {
        wm.ui.lock(wordsViewPanel, i18n['status.words.loading']);
        wordsViewStatus.text(i18n['status.words.loading']);

        if (prefix.length == 0) {
            wm.ui.unlock(wordsViewPanel);
        } else {
            $.post("/playground/dictionary/loadWordEntries.ajax?l=ru&p=" + prefix, null, function (response) {
                if (response.success) {
                    var words = response.data.wordEntries;
                    wordsViewStatus.text(words.length);

                    $.each(words, function (i, v) {
                        wordsViewTable.append(createViewItem(v));
                    });
                    wordsViewScrollPane.reinitialise();
                    wm.ui.unlock(wordsViewPanel);
                } else {
                    wm.ui.unlock(wordsViewPanel, response.summary, true);
                }
            });
        }
    };

    var selectTopLetter = function (el) {
        var letter = el != null ? el.attr('href').substring(1) : '';
        if (letter != activeTopLetter) {
            activeTopLetter = letter;
            topAlphabet.find("a").removeClass("active-letter");
            subAlphabet.find("div").hide();
            dictionary.find("#subAlphabet" + letter).show();
            if (el != null) {
                el.addClass("active-letter");
            }
        }
        return letter;
    };

    var selectSubLetter = function (el) {
        var letter = el != null ? el.attr('href').substring(1) : '';
        if (letter != activeSubLetter) {
            activeSubLetter = letter;
            subAlphabet.find("a").removeClass("active-letter");
            if (el != null) {
                el.addClass("active-letter");
            }
        }
        return letter;
    };

    var clearTable = function () {
        wordsViewTable.empty();
        wordsViewStatus.text(0);
    };

    var initAlphabets = function (value) {
        if (value.length > 0) {
            var letter = value.charAt(0).toUpperCase();
            selectTopLetter(topAlphabet.find('a[href$="#' + letter + '"]'));
        } else {
            selectSubLetter(null);
            selectTopLetter(null);
        }

        if (value.length > 1) {
            var subLetter = letter.toLowerCase() + value.charAt(1).toLowerCase();
            selectSubLetter(subAlphabet.find('a[href$="#' + subLetter + '"]'));
        } else {
            selectSubLetter(null);
        }
    };

    topAlphabetLinks.click(function (e) {
        clearTable();

        var a = selectTopLetter($(this)).toLowerCase();
        searchInput.val(a);
        window.location.hash = "#" + a;
        e.preventDefault();
    });

    subAlphabetLinks.click(function (e) {
        clearTable();
        var a = selectSubLetter($(this));
        searchInput.val(a);
        window.location.hash = "#" + a;
        loadWordEntries(a);
        e.preventDefault();
    });

    searchInput.bind("input propertychange", function (evt) {
        if (window.event && event.type == "propertychange" && event.propertyName != "value")
            return;
        clearTable();

        var input = $(this);
        var value = input.val();

        initAlphabets(value);
        window.location.hash = "#" + value;

        if (value.length >= 2) {
            window.clearTimeout(input.data("timeout"));
            input.data("timeout", setTimeout(function () {
                loadWordEntries(value);
            }, 800));
        }
    });

    var initByHash = function () {
        var hash = window.location.hash.substring(1);
        initAlphabets(hash);
        searchInput.val(hash);
        if (hash.length >= 2) {
            loadWordEntries(hash);
        }
    };
    initByHash();
};

wm.game.tourney.Subscription = function (announce, subscribed, subscriptions, language) {
    var subscriptionView = $("#subscriptionView");
    var subscriptionForm = $("#subscriptionForm");
    var subscriptionDialog = $("#subscriptionDialog");
    var subscriptionDetails = $("#subscriptionDetails");

    var subscribe = function (comp, lang, section, callback) {
        var data = $.toJSON({language: lang, section: section});
        wm.ui.lock(comp, language["register.subscribing"]);
        $.post("/playground/tourney/changeSubscription.ajax?t=" + announce, data,
            function (response) {
                if (response.success) {
                    subscriptions = response.data.subscriptions;
                    wm.ui.unlock(comp, language["register.subscribed"]);
                } else {
                    wm.ui.unlock(comp, response.summary, true);
                }
                updateAnnounceView(true);
                callback(response.success);
            }, 'json');
    };

    var unsubscribe = function (comp, callback) {
        wm.ui.lock(comp, language["register.unsubscribing"]);
        $.post("/playground/tourney/changeSubscription.ajax?t=" + announce, $.toJSON({}),
            function (response) {
                if (response.success) {
                    subscriptions = response.data.subscriptions;
                    wm.ui.unlock(comp, language["register.unsubscribed"]);
                } else {
                    wm.ui.unlock(comp, response.summary, true);
                }
                updateAnnounceView(false);
                callback(response.success);
            }, 'json');
    };

    var updateAnnounceView = function (sub) {
        subscribed = sub;

        var announceAction = subscriptionView.find('button .ui-button-text');
        var announceSection = subscriptionView.find("#announceSection");
        var announceLanguage = subscriptionView.find("#announceLanguage");

        if (subscribed) {
            subscriptionView.find(".tourney-state").removeClass("ui-state-disabled");

            announceAction.text(language["register.refuse"]);
            announceSection.text(getSelectedValue('section', false));
            announceLanguage.text(getSelectedValue('language', false));
        } else {
            subscriptionView.find(".tourney-state").addClass("ui-state-disabled");
            announceAction.text(language["register.accept"]);
            announceSection.text(language["register.unspecified"]);
            announceLanguage.text(language["register.unspecified"]);
        }

        var tsum = 0;
        $.each(subscriptions, function (l, ss) {
            var sum = 0;
            $.each(ss, function (s, v) {
                sum += v;
                subscriptionView.find(".subscriptionDetails" + l + s).text(v);
            });
            subscriptionView.find(".subscriptionDetails" + l).text(sum);
            tsum += sum;
        });
    };

    var getSelectedValue = function (input, value) {
        var el;
        if (input == 'language') {
            el = subscriptionForm.find("select[name=language] option:selected");
        } else if (input == 'section') {
            el = subscriptionForm.find("input[name=section]:checked");
        }

        if (value) {
            return el.val();
        } else {
            if (input == 'section') {
                el = $(subscriptionForm.find("#subscriptionSectionLabel" + el.val() + " span").get(0));
            }
            return el.text();
        }
    };

    var showSubscriptionDialog = function () {
        subscriptionDialog.dialog({
            id: "jQueryDialog",
            title: language["register.title"],
            width: 550,
            minHeight: 350,
            modal: true,
            resizable: false,
            buttons: [
                {
                    class: "tourney-unsubscribed",
                    text: language["register.button"],
                    click: function () {
                        subscribe(subscriptionDialog.closest(".ui-dialog"),
                            getSelectedValue('language', true),
                            getSelectedValue('section', true),
                            function () {
                                subscriptionDialog.dialog("close");
                            });
                    }
                },
                {
                    text: wm.i18n.value('button.cancel', 'Cancel'),
                    click: function () {
                        subscriptionDialog.dialog("close");
                    }

                }
            ]
        });
    };

    subscriptionForm.find("#subscriptionLanguage").change(function () {
        var language = getSelectedValue('language', true);
        var find = subscriptionForm.find("#subscriptionSection .players");
        $.each(find, function (i, v) {
            v = $(v);
            v.text(subscriptions[language][v.attr('id').substring(26)]);
        });
    });

    subscriptionView.find('button').button().click(function () {
        if (subscribed) {
            unsubscribe(subscriptionView, function () {
            });
        } else {
            showSubscriptionDialog();
        }
    });
};

wm.game.settings.Board = function () {
    var prevSet = $(".tiles-set-prev");
    var nextSet = $(".tiles-set-next");
    var tileSetView = $(".tiles-set-view");

    var selected = 0;
    var tilesSet = ['tiles-set-classic', 'tiles-set-classic2'];

    $.each(tilesSet, function (i, v) {
        if (tileSetView.hasClass(v)) {
            selected = i;
            return false;
        }
    });

    var checkButtons = function () {
        if (selected == 0) {
            prevSet.attr('disabled', 'disabled');
        } else {
            prevSet.removeAttr('disabled');
        }

        if (selected == tilesSet.length - 1) {
            nextSet.attr('disabled', 'disabled');
        } else {
            nextSet.removeAttr('disabled');
        }
    };

    var changeTilesView = function (value) {
        $("#tilesClass").val(tilesSet[selected + value]);

        tileSetView.removeClass(tilesSet[selected]);
        tileSetView.addClass(tilesSet[selected + value]);

        selected = selected + value;
    };

    $(".tiles-set-nav").hover(
        function () {
            if ($(this).attr('disabled') == undefined) {
                $(this).removeClass('ui-state-default').addClass('ui-state-hover');
            }
        },
        function () {
            if ($(this).attr('disabled') == undefined) {
                $(this).removeClass('ui-state-hover').addClass('ui-state-default');
            }
        });

    prevSet.click(function () {
        if (selected > 0) {
            changeTilesView(-1);
            checkButtons();
            prevSet.removeClass('ui-state-hover').addClass('ui-state-default');
            return false;
        }
    });

    nextSet.click(function () {
        if (selected < tilesSet.length - 1) {
            changeTilesView(1);
            checkButtons();
            nextSet.removeClass('ui-state-hover').addClass('ui-state-default');
            return false;
        }
    });
    checkButtons();
};