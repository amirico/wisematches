<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
This is playboard

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
                <#if tile?has_content>
                    <div id="tile${tile.number}" class="tile cost${tile.cost}"
                         style="left: ${i*22}px; top: ${j*22}px; background-position: -${tile.cost*22}px -44px">
                        <span>${tile.letter?upper_case}</span>
                    </div>
                </#if>
            </#list>
        </#list>
        </div>

        <div id="hand">
        <#--<#list board.getPlayersHands() as hand>-->
                <#--${hand.}-->
            <#--</#list>-->
        <#--
            <div id="tile43" class="tile cost8" style="left: 0; top: 0; background-position: -176px 0">
                <span>T</span>
            </div>
            <div id="tile78" class="tile cost0" style="left: 22px; top: 0; background-position: 0 0">
                <span>M</span>
            </div>
-->
        </div>
    </div>
</div>

<div id="console" style="overflow: auto; border: 1px solid red; width: 100%; height: 300px;">
</div>

<script type="text/javascript">
    var board = new wm.scribble.Board();
    board.init();
</script>
