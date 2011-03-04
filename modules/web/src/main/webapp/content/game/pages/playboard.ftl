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

<#macro tileObject tile i j><@tilePlain number=tile.number cost=tile.cost letter=tile.letter i=i j=j/></#macro>

<script type="text/javascript">
    $(document).ready(function() {
        $("#boardsNavigationToolbar").buttonset();
        $("#boardActionsToolbar div").buttonset();

        $("#movesHistory table").dataTable({
            "bJQueryUI": true,
            "bFilter": false,
            "bSort": false,
            "bSortClasses": false,
            "sDom": 't'
        });
    });
</script>

<div id="boardsNavigationToolbar" style="text-align: right;">
    <div>
        <button>Previous</button>
        <button>#1234</button>
        <button>Next</button>
    </div>
</div>

<table cellpadding="5" align="center">
    <tr>
        <td style="vertical-align: top;">
            <div class="ui-widget">
                <div class="ui-widget-header ui-corner-top">
                    Tiles Info
                </div>
                <div id="tilesInfo" class="ui-widget-content">
                    <table>
                        <tbody>
                        <#list tilesBankInfo as i>
                            <#if i??>
                            <tr>
                                <td>
                                    <div style="position: relative; height: 22px; width:22px">
                                    <@tilePlain number="" cost=i_index letter=i_index?string i=0 j=0 />
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
                </div>
            </div>
        </td>

        <td style="vertical-align: top;">
            <div class="ui-widget">
                <div class="ui-widget-header ui-corner-top">
                    Play Board
                </div>
                <div class="ui-widget-content">
                    <div id="scribble">
                        <div id="playboard">
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

                    <div id="boardActionsToolbar" style="position: relative;">
                        <div style="display: inline-block;">
                            <button style="background-image: url('/resources/images/scribble/playboard/makeTurn.png'); background-repeat: no-repeat; background-position: left center">
                                Make Turn
                            </button>
                            <button style="background-image: url('/resources/images/scribble/playboard/exchangeTiles.png'); background-repeat: no-repeat; background-position: left center">
                                Exchange Tiles
                            </button>
                            <button style="background-image: url('/resources/images/scribble/playboard/passTurn.png'); background-repeat: no-repeat; background-position: left center">
                                Pass Turn
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </td>

        <td style="vertical-align: top; width: auto;">
            <div class="ui-widget">
                <div class="ui-widget-header ui-corner-top">
                    Players Info
                </div>
                <div id="playerInfo" class="ui-widget-content">
                <#list board.playersHands as hand>
                    <div class="playerInfo" <#if board.getPlayerTurn() == hand>style="border: 1px solid red"</#if>>
                        <img align="top" src="/resources/images/noPlayerIcon.png" alt=""/>

                        <div style="display: inline-block;">
                            <#assign p = playerManager.getPlayer(hand.getPlayerId())/>
                            <div>
                                <b>Name:</b> <@game.player player=p showRating=true/>
                            </div>
                            <div>
                                <b>Score:</b> ${hand.getPoints()}
                            </div>
                            <div>
                                <b>Time left:</b> ${hand.getPoints()}
                            </div>
                        </div>
                    </div>
                </#list>
                </div>
            </div>

            <div class="ui-widget" style="padding-top: 10px; width: 250px;">
                <div class="ui-widget-header ui-corner-top">
                    Move Info
                </div>
                <div id="moveInfo" class="ui-widget-content">
                    <b>Selected Tiles:</b>

                    <div id="selectedTilesInfo" style="position: relative; height: 22px; padding-left: 5px;">
                        no tiles selected
                    </div>

                    <b>Selected Word:</b>

                    <div id="selectedWordInfo" style="position: relative; height: 22px; padding-left: 5px;">
                        no word selected
                    </div>

                    <b>Selected Word Cost:</b>

                    <div id="selectedWordCost" style="position: relative; height: 22px; padding-left: 5px;">
                        (1*<b>2</b> + 2 + 7 + 9)*<b>2</b>=12
                    </div>
                </div>
            </div>

            <div class="ui-widget" style="padding-top: 10px; width: 250px;">
                <div class="ui-widget-header ui-corner-top">
                    Move Info
                </div>
                <div id="movesHistory" class="ui-widget-content">
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
                </div>
            </div>
        </td>
    </tr>
</table>

<div id="console" style="overflow: auto; border: 1px solid red; width: 100%; height: 300px;">
</div>

<script type="text/javascript">
    var board = new wm.scribble.Board();
    board.init();
    board.addListener("selected", function(p) {
        var tile = p.tile;
        var place = $("#selectedTilesInfo");
        if (place.children("#selectedTile" + tile.number).length != 0) {
            return;
        }
        var count = place.children("div.tile").length;
        if (count == 0) {
            place.text("");
        }
        var txt = $("<span></span>").text(tile.letter);
        var newTile = $("<div></div>").attr('id', 'selectedTile' + tile.number).addClass("tile cost" + tile.cost).css('background-position', '-' + tile.cost * 22 + 'px 0');
        newTile.offset({left: (count * 22), top: 0});
        newTile.append(txt).appendTo("#selectedTilesInfo");
    });

    board.addListener("deselected", function(p) {
        $("#selectedTilesInfo").children("#selectedTile" + p.tile.number).detach();
    });
</script>
