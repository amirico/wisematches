<#-- @ftlvariable name="viewMode" type="boolean" -->
<#-- @ftlvariable name="boardSettings" type="wisematches.playground.scribble.settings.BoardSettings" -->

<#include "/core.ftl">
<#if !viewMode??><#assign viewMode=true/></#if>

<div class="board-controls ui-widget-content ui-corner-all shadow" align="center">
    <div style="white-space: nowrap">
    <#if !viewMode>
        <button class="makeTurnButton" onclick="boardControls.makeTurn()">
            <@message code="game.play.make"/>
        </button>
    </#if>
        <button class="clearSelectionButton" onclick="board.clearSelection()">
        <@message code="game.play.clear"/>
        </button>
        <button class="selectTileButton" onclick="bankInfo.showBankInfo()">
        <@message code="game.play.highlight"/>
        </button>
    </div>
<#if !viewMode>
    <div style="padding-top: 3px;white-space: nowrap">
        <button class="exchangeTilesButton" onclick="boardControls.exchangeTiles()">
            <@message code="game.play.exchange"/>
        </button>
        <button class="passTurnButton" onclick="boardControls.passTurn()">
            <@message code="game.play.pass"/>
        </button>
        <button class="resignGameButton" onclick="boardControls.resignGame()">
            <@message code="game.play.resign"/>
        </button>
    </div>
</#if>
</div>

<#if !viewMode>
<div class="exchangeTilesPanel ui-helper-hidden ${boardSettings.tilesClass}">
    <div><@message code="game.play.exchange.description"/></div>
    <div style="height: 16px; position: relative;"></div>
</div>
</#if>

<div class="tiles-bank ui-widget-content ui-helper-hidden ${boardSettings.tilesClass}">
    <div style="display: inline-block; vertical-align: top;">
        <div class="tiles" style="position: relative;"></div>
    </div>

    <div class="info" style="display: inline-block; vertical-align: top; padding-left: 30px;">
        <table border="0" width="250px">
            <tr>
                <td nowrap="nowrap"><strong><@message code="game.state.bank.cost"/>:</strong></td>
                <td align="center" class="tileCost"></td>
            </tr>

            <tr>
                <td nowrap="nowrap"><strong><@message code="game.state.bank.view"/>:</strong></td>
                <td align="center">
                    <div class="tileView" style="position: relative; width: 22px; height: 22px"></div>
                </td>
            </tr>

            <tr>
                <td colspan="2" style="height: 25px;">&nbsp;</td>
            </tr>

            <tr>
                <td nowrap="nowrap"><strong><@message code="game.state.bank.total"/>:</strong>
                </td>
                <td align="center" class="totalCount"></td>
            </tr>

            <tr>
                <td nowrap="nowrap"><strong><@message code="game.state.bank.board"/>:</strong></td>
                <td align="center" class="boardCount"></td>
            </tr>
        </table>
    </div>

    <div style="padding-top: 5px">
        <div class="sample"><@message code="game.state.bank.info"/></div>
    </div>
</div>

<script type="text/javascript">
    var bankInfo = new wm.scribble.BankInfo(board, {'title': '<@message code="game.state.bankinfo.label"/>'});

    var boardControls = new wm.scribble.Controls(board, {
        "acceptedLabel": "<@message code="game.move.accepted.label"/>",
        "acceptedDescription": "<@message code="game.move.accepted.description"/>",
        "updatedLabel": "<@message code="game.move.updated.label"/>",
        "updatedYour": "<@message code="game.move.updated.you"/>",
        "updatedOther": "<@message code="game.move.updated.other"/>",
        "finishedLabel": "<@message code="game.move.finished.label"/>",
        "finishedInterrupted": "<@message code="game.move.finished.interrupted"/>",
        "finishedDrew": "<@message code="game.move.finished.drew"/>",
        "finishedWon": "<@message code="game.move.finished.won"/>",
        "clickToClose": "<@message code="game.move.clickToClose"/>",
        "passLabel": "<@message code="game.move.pass.label"/>",
        "passDescription": "<@message code="game.move.pass.description"/>",
        "resignLabel": "<@message code="game.move.resign.label"/>",
        "resignDescription": "<@message code="game.move.resign.description"/>",
        "exchange": "<@message code="game.play.exchange.label"/>",
        "cancel": "<@message code="button.cancel"/>",
        "updatingBoard": "<@message code="game.move.updating"/>"
    });
</script>