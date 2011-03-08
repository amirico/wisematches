<#-- @ftlvariable name="tilesBankInfo" type="char[][]" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#include "/core.ftl">
<#import "../macros.ftl" as game>
<#import "/content/utils.ftl" as utils>

<#macro tilePlain number cost letter i j>
<div <#if number??>id="tile${number}"</#if> class="tile cost${cost}"
     style="left: ${i*22}px; top: ${j*22}px; background-position: -${cost*22}px -${j*22}px">
    <span>${letter?upper_case}</span>
</div>
</#macro>

<#macro bankInfo>
<@ext.widget id="bankInfo" title="Bank's Info">
<table width="100%" border="0">
    <tr>
        <td>Total tiles:</td>
        <td width="100%">21</td>
    </tr>
    <tr>
        <td colspan="2">
            <div class="ui-widget-content ui-widget-separator"></div>
        </td>
    </tr>
    <tr>
        <td>Tiles in bank:</td>
        <td>21</td>
    </tr>
    <tr>
        <td>Tiles on board:</td>
        <td>34</td>
    </tr>
    <tr>
        <td>Tiles in hands:</td>
        <td>34</td>
    </tr>
</table>
</@ext.widget>
</#macro>

<#macro boardLegend>
<@ext.widget id="boardLegend" title="Board's Legend">
<table>
    <tr>
        <td width="22px">
            <div class="cell bonus-cell-center" style="position: static;"></div>
        </td>
        <td> - center position</td>
    </tr>
</table>
<div class="ui-widget-content ui-widget-separator"></div>
<table>
    <tr>
        <td width="22px">
            <div class="cell bonus-cell-2l" style="position: static;"></div>
        </td>
        <td> - double letter</td>
        <td>
            <div class="cell bonus-cell-2w" style="position: static;"></div>
        </td>
        <td> - double word</td>
    </tr>
    <tr>
        <td>
            <div class="cell bonus-cell-3l" style="position: static;"></div>
        </td>
        <td> - tripple letter</td>
        <td>
            <div class="cell bonus-cell-3l" style="position: static;"></div>
        </td>
        <td> - tripple word</td>
    </tr>
</table>
<div class="ui-widget-content ui-widget-separator"></div>
<table>
    <tbody>
        <#list tilesBankInfo as i>
            <#if i??>
            <tr>
                <td>
                    <div style="position: relative; height: 22px; width:22px">
                    <@tilePlain number="" cost=i_index letter="<b>" + i_index?string + "</b>" i=0 j=0 />
                    </div>
                </td>
                <td>&nbsp;-&nbsp;</td>
                <td>
                    <div style="position: relative; height: 22px; width: ${(i?size)*22}px">
                        <#list i as c><@tilePlain number="" cost=i_index letter=c?upper_case i=c_index j=0 /></#list>
                    </div>
                </td>
            </tr>
            </#if>
        </#list>
    </tbody>
</table>
</@ext.widget>
</#macro>

<#macro movesHistory>
<@ext.widget id="movesHistory" title="History Moves">
<table width="100%">
    <thead>
    <tr>
        <th>#</th>
        <th>Player</th>
        <th>Word</th>
        <th>Cost</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>1</td>
        <td>smklimenko</td>
        <td>TEST</td>
        <td>123</td>
    </tr>
    <tr>
        <td>1</td>
        <td>smklimenko</td>
        <td>TEST</td>
        <td>123</td>
    </tr>
    <tr>
        <td>1</td>
        <td>smklimenko</td>
        <td>TEST</td>
        <td>123</td>
    </tr>
    </tbody>
</table>
</@ext.widget>
</#macro>

<#macro playersInfo>
<@ext.widget id="playersInfo" title="Players Info">
<table cellpadding="5">
    <#list board.playersHands as hand>
        <#assign p = playerManager.getPlayer(hand.getPlayerId())/>
        <#assign active=(board.getPlayerTurn() == hand)/>
        <#assign playerStyle="ui-state-active"/>
        <tr id="playerInfo${hand.getPlayerId()}" class="playerInfo <#if active>active<#else>passive</#if>">
            <td class="ui-corner-left ${playerStyle} ui-table-left">
                <img align="top" src="/resources/images/noPlayerIcon.png" width="31" height="28" alt=""/>
            </td>
            <td class="${playerStyle} ui-table-middle"><@game.player player=p showRating=false/></td>
            <td class="${playerStyle} ui-table-middle" align="center" width="40">${hand.getPoints()}</td>
            <td class="ui-corner-right ${playerStyle} ui-table-right" align="left" width="60">3d 21m</td>
        </tr>
    </#list>
</table>
</@ext.widget>
</#macro>

<#macro moveInfo>
<@ext.widget id="moveInfo" title="Move Info">
<table>
    <tr>
        <td height="22" valign="bottom"><b>Tiles:</b></td>
        <td height="22" valign="bottom" style="padding-left: 5px">
            <div id="selectedTilesInfo" style="position: relative; height: 22px;">
                <span class="sample">no tiles selected</span>
            </div>
        </td>
    </tr>
    <tr>
        <td height="22" valign="bottom"><b>Word:</b></td>
        <td height="22" valign="bottom" style="padding-left: 5px">
            <div id="selectedWordInfo" style="position: relative; height: 22px;">
                <span class="sample">no tiles selected</span>
            </div>
        </td>
    </tr>
    <tr>
        <td height="22" valign="bottom"><b>Points:</b></td>
        <td height="22" valign="bottom">
            <div id="selectedWordCost" style="position: relative; padding-left: 5px">
                (1x<b>2</b> + 2 + 7 + 9)x<b>2</b>=12
            </div>
        </td>
    </tr>
</table>

<div id="qqqq2" style="padding-top: 5px;">
    <div style="margin: 0">
        <button style="padding-left: 15px; background-image: url('/resources/images/scribble/playboard/clearMemory.png'); background-repeat: no-repeat; background-position: 5px center;">
            Check Word
        </button>
        <button style="padding-left: 15px; background-image: url('/resources/images/scribble/playboard/rememberWord.png'); background-repeat: no-repeat; background-position: 5px center;  float: right">
            Remember Word
        </button>
    </div>
</div>
</@ext.widget>
</#macro>

<#macro scribbleBoard>
<@ext.widget id="scribbleBoard" title="Play Board">
<div id="scribble">
    <div id="container">
        <div id="bonuses">
            <#list board.getScoreEngine().getScoreBonuses() as bonus>
                <div class="cell bonus-cell-${bonus.getType().displayName}"
                     style="left: ${bonus.column*22}px; top: ${bonus.row*22}px"></div>
                <div class="cell bonus-cell-${bonus.getType().displayName}"
                     style="left: ${bonus.column*22}px; top: ${22+(13-bonus.row)*22}px"></div>
                <div class="cell bonus-cell-${bonus.getType().displayName}"
                     style="left: ${22 + (13-bonus.column)*22}px; top: ${bonus.row*22}px"></div>
                <div class="cell bonus-cell-${bonus.getType().displayName}"
                     style="left: ${22+(13-bonus.column)*22}px; top: ${22+(13-bonus.row)*22}px"></div>
            </#list>
        </div>

        <div id="board">
            <#list 0..14 as i>
    <#list 0..14 as j>
                <#assign tile=board.getBoardTile(i, j)!"">
                <#if tile?has_content><@tileObject tile=tile i=i j=j/></#if>
            </#list>
</#list>
        </div>

        <div id="hand">
            <#assign playerHand=board.getPlayerHand(player.getId())!""/>
<#if playerHand??>
            <#list playerHand.tiles as tile>
                <#if tile?has_content><@tileObject tile=tile i=tile_index j=0/></#if>
            </#list>
        </#if>
        </div>
    </div>
</div>

<div id="boardActionsToolbar" style="float: right; padding-top: 3px">
    <div style="display: inline-block; margin: 0;">
        <button style="padding-left: 15px; background-image: url('/resources/images/scribble/playboard/makeTurn.png'); background-repeat: no-repeat; background-position: 5px center">
            Ходить
        </button>
        <button id="clearSelectionButton" onclick="board.clearSelection()"
                style="padding-left: 15px; background-image: url('/resources/images/scribble/playboard/selectWord.png'); background-repeat: no-repeat; background-position: 5px center">
            Сбросить
        </button>
        <button style="padding-left: 15px; background-image: url('/resources/images/scribble/playboard/exchangeTiles.png'); background-repeat: no-repeat; background-position: 5px center">
            Обменять
        </button>
        <button style="padding-left: 15px;  background-image: url('/resources/images/scribble/playboard/passTurn.png'); background-repeat: no-repeat; background-position: 5px center">
            Пропустить
        </button>
    </div>
</div>
</@ext.widget>
</#macro>

<#macro memoryWords>
<@ext.widget id="memoryWords" title="Memory Words">
<table width="100%">
    <thead>
    <tr>
        <th>Word</th>
        <th>Points</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>ASDE</td>
        <td width="60" align="center">123</td>
        <td width="24">
            <a href="javascript: selectWord()" style="border: 0">
                <img src="/resources/images/scribble/playboard/selectWord.png" width="16" height="16" alt="">
            </a>
            <a href="javascript: removeWord()" style="border: 0">
                <img src="/resources/images/scribble/playboard/removeWord.png" width="16" height="16" alt="">
            </a>
        </td>
    </tr>
    <tr>
        <td>GFER</td>
        <td width="60" align="center">234</td>
        <td width="24">
            <a href="javascript: selectWord()" style="border: 0">
                <img src="/resources/images/scribble/playboard/selectWord.png" width="16" height="16" alt="">
            </a>
            <a href="javascript: removeWord()" style="border: 0">
                <img src="/resources/images/scribble/playboard/removeWord.png" width="16" height="16" alt="">
            </a>
        </td>
    </tr>
    </tbody>
</table>

<div id="qqqq" style="padding-top: 5px;">
    <div style="margin: 0">
        <button style="padding-left: 15px; background-image: url('/resources/images/scribble/playboard/rememberWord.png'); background-repeat: no-repeat; background-position: 5px center">
            Remember Word
        </button>
        <button style="padding-left: 15px; background-image: url('/resources/images/scribble/playboard/clearMemory.png'); background-repeat: no-repeat; background-position: 5px center; float: right;">
            Clear Memory
        </button>
    </div>
</div>
</@ext.widget>
</#macro>

<#macro tileObject tile i j><@tilePlain number=tile.number cost=tile.cost letter=tile.letter i=i j=j/></#macro>

<table id="playboard" cellpadding="5" align="center">
    <tr>
        <td style="vertical-align: top;">
        <@bankInfo/>
            <div style="height: 10px"></div>
        <@boardLegend/>
            <div style="height: 10px"></div>
        <@movesHistory/>
        </td>

        <td style="vertical-align: top;">
        <@scribbleBoard/>
        </td>

        <td style="vertical-align: top;">
        <@playersInfo/>
            <div style="height: 10px"></div>
        <@moveInfo/>
            <div style="height: 10px"></div>
        <@memoryWords/>
        </td>
    </tr>
</table>

<script type="text/javascript">
    $("#qqqq div").buttonset();
    $("#qqqq2 div").buttonset();
    $("#boardsNavigationToolbar").buttonset();
    $("#boardActionsToolbar div").buttonset();

    $("#movesHistory table").dataTable({
        "bJQueryUI": true,
        "bFilter": false,
        "bSort": false,
        "bSortClasses": false,
        "sDom": 't'
    });

    $("#memoryWords table").dataTable({
        "bJQueryUI": true,
        "bFilter": false,
        "bSort": true,
        "bSortClasses": true,
        "sDom": 't',
        "aaSorting": [
            [1,'desc']
        ],
        "aoColumns": [
            null,
            null,
            { "bSortable": false }
        ]
    });
    $("#clearSelectionButton").attr("disabled", true);

    var board = new wm.scribble.Board();
    board.init();

    function createTileWidget(tile) {
        return $("<div></div>").addClass("tile cost" + tile.cost).css('background-position', '-' + tile.cost * 22 + 'px 0').append($("<span></span>").text(tile.letter));
    }

    function updateSelectedWord() {
        var swi = $("#selectedWordInfo");
        var word = board.getSelectedWord();
        if (word != null) {
            board.getScoreEngine().
                    swi.empty();
            for (var i = 0, len = word.tiles.length; i < len; i++) {
                var newTile = createTileWidget(word.tiles[i]);
                newTile.offset({left: (i * 22), top: 0});
                swi.append(newTile);
            }
        } else {
            swi.text("no tiles selected");
        }
    }

    board.addListener("selected", function(p) {
        $("#clearSelectionButton").attr("disabled", false);
        updateSelectedWord();
        var tile = p.tile;
        var place = $("#selectedTilesInfo");
        if (place.children("#selectedTile" + tile.number).length != 0) {
            return;
        }
        var count = place.children("div.tile").length;
        if (count == 0) {
            place.text("");
        }

        var newTile = createTileWidget(tile);
        newTile.attr('id', 'selectedTile' + tile.number);
        newTile.offset({left: (count * 22), top: 0});
        newTile.appendTo("#selectedTilesInfo");
    });

    board.addListener("deselected", function(p) {
        updateSelectedWord();
        var place = $("#selectedTilesInfo");
        place.children("#selectedTile" + p.tile.number).detach();
        if (place.children("#selectedTile" + p.tile.number).length == 0) {
            place.text('no tiles selected');
        }
        if (board.getSelectedTiles().length == 0) {
            $("#clearSelectionButton").attr("disabled", true);
        }
    });
</script>
