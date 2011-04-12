<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">

<@wm.widget id="moveInfo" title="game.selection.label" style="padding-top: 10px;">
<table width="100%">
    <tr>
        <td height="22" valign="bottom"><b><@message code="game.selection.tiles"/>:</b></td>
        <td height="22" width="100%" valign="bottom" style="padding-left: 5px">
            <div id="selectedTilesInfo" style="position: relative; height: 22px;">
            <@message code="game.selection.notiles"/>
            </div>
        </td>
    </tr>
    <tr>
        <td height="22" valign="bottom"><b><@message code="game.selection.word"/>:</b></td>
        <td height="22" valign="bottom" style="padding-left: 5px">
            <div id="selectedWordInfo" style="position: relative; height: 22px;">
            <@message code="game.selection.noword"/>
            </div>
        </td>
    </tr>
    <tr>
        <td height="22" valign="bottom"><b><@message code="game.selection.points"/>:</b></td>
        <td height="22" valign="bottom">
            <div id="selectedWordCost" style="position: relative; padding-left: 5px">
            <@message code="game.selection.noword"/>
            </div>
        </td>
    </tr>
</table>
<table width="100%">
    <tr>
        <td valign="middle" nowrap="nowrap">
            <div id="wordStatus">
                <span id="wordStatusIcon"></span>
                <span id="wordStatusMessage"></span>
            </div>
        </td>
        <td valign="middle" nowrap="nowrap" align="right">
            <button id="checkWordButton"
                    onclick="wm.scribble.selection.checkSelectedWord()">
            <@message code="game.selection.check"/>
            </button>
        </td>
    </tr>
</table>
</@wm.widget>

<script type="text/javascript">
    $("#checkWordButton").button({disabled: true, icons: {primary:'icon-word-check'}});

    wm.scribble.selection = new function() {
        this.checkSelectedWord = function() {
            var word = board.getSelectedWord();
            if (word == null || word == undefined) {
                return;
            }
            $("#checkWordButton").button('disable').removeClass("ui-state-hover");
            $("#wordStatusMessage").text('<@message code="game.selection.checking"/>');
            $("#wordStatusIcon").addClass('icon-wait').removeClass('icon-word-valid icon-word-invalid wordValid wordInvalid');
            $.post('/game/playboard/check.ajax', $.toJSON({word:word.text, lang:'${board.dictionary.locale}'}),
                    function(response) {
                        if (response.success) {
                            $("#wordStatusMessage").text('<@message code="game.selection.valid"/>');
                            $("#wordStatusIcon").removeClass('icon-wait').addClass("icon-word-valid");
                        } else {
                            $("#wordStatusMessage").text('<@message code="game.selection.invalid"/>');
                            $("#wordStatusIcon").removeClass('icon-wait').addClass("icon-word-invalid");
                        }
                    }, 'json');
        };

        board.bind("tileSelection",
                function(event, selected, tile) {
                    var tiles = $("#selectedTilesInfo div");
                    var length = board.getSelectedTiles().length;
                    if (selected && length == 1) {
                        $("#selectedTilesInfo").empty();
                    }
                    if (selected) {
                        wm.scribble.tile.createTileWidget(tile).offset({left: ((length - 1) * 22), top: 0}).appendTo('#selectedTilesInfo');
                    } else {
                        var updateOffset = false;
                        $.each(tiles, function(i, tileWidget) {
                            var v = $(tileWidget);
                            if (v.data('tile').number == tile.number) {
                                updateOffset = true;
                                v.remove();
                            } else if (updateOffset) {
                                v.css('left', (i - 1) * 22);
                            }
                        });
                    }
                    if (length == 0) {
                        $("#selectedTilesInfo").text('<@message code="game.selection.notiles"/>');
                    }
                })
                .bind('wordSelection',
                function(event, word) {
                    var swi = $("#selectedWordInfo");
                    var swc = $("#selectedWordCost");
                    if (word != null) {
                        swc.empty().text(board.getScoreEngine().getWordBonus(word).formula);
                        swi.empty();
                        $.each(word.tiles, function(i, t) {
                            wm.scribble.tile.createTileWidget(t).offset({left: (i * 22), top: 0}).appendTo(swi);
                        });
                        $("#checkWordButton").button('enable');
                    } else {
                        swi.text('<@message code="game.selection.noword"/>');
                        swc.text('<@message code="game.selection.noword"/>');
                        $("#checkWordButton").button('disable').removeClass("ui-state-hover");
                    }
                    $("#wordStatusIcon").attr('class', '');
                    $("#wordStatusMessage").text("");
                });
    };
</script>
