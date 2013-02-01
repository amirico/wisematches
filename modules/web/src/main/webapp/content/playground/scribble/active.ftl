<#-- @ftlvariable name="player" type="wisematches.core.Personality" -->
<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="activeBoards" type="java.util.Collection<wisematches.playground.scribble.ScribbleBoard>" -->
<#-- @ftlvariable name="activeProposals" type="java.util.Collection<wisematches.server.playground.propose.GameProposal<wisematches.server.playground.scribble.ScribbleSettings>" -->
<#include "/core.ftl">

<#macro gameStatus board>
    <#if board.isGameActive()>
        <#if board.getPlayerTurn().getPlayerId() == principal.getId()>
            <@wm.board.href board.boardId><strong><@message code="game.status.move_you"/></strong></@wm.board.href>
        <#else>
            <@message code="game.status.move_opp" args=["${playerManager.getPlayer(board.getPlayerTurn().getPlayerId()).nickname!}"]/>
        </#if>
    </#if>
</#macro>

<@wm.ui.table.dtinit/>

<@wm.ui.playground id="activeGamesWidget">
    <@wm.ui.table.header>
        <#if player != principal>
            <@message code="game.player"/> <@wm.player.name player/>
        <#else><@message code="game.menu.games.label"/>
        </#if>
    > <@message code="game.dashboard.label"/>
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar>
    <table width="100%">
        <tr>
            <td align="left">
                <div class="wm-ui-buttonset">
                    <a href="/playground/scribble/join"><@message code="game.join.label"/></a>
                    <a href="/playground/scribble/create"><@message code="game.create.label"/></a>
                </div>
            </td>
            <td align="right">
                <div class="wm-ui-buttonset">
                    <a href="/playground/scribble/history<#if player != principal>?p=${player.id}</#if>"><@message code="game.past.history.label"/></a>
                </div>
            </td>
        </tr>
    </table>
    </@wm.ui.table.toolbar>

    <@wm.ui.table.content>
    <table id="dashboard" width="100%" class="display">
        <thead>
        <tr>
            <th width="100%"><@message code="game.title.label"/></th>
            <th><@message code="game.language.label"/></th>
            <th><@message code="game.status.label"/></th>
            <th><@message code="game.remained.label"/></th>
            <th><@message code="game.opponents.label"/></th>
            <th><@message code="game.scores.label"/></th>
        </tr>
        </thead>
        <tbody>
            <#list activeBoards as board>
            <tr id="board${board.boardId}">
                <#assign settings=board.settings/>
                <td><@wm.board.name board, true, false/></td>
                <td><@message code="language.${settings.language?lower_case}"/></td>
                <td><@gameStatus board=board/></td>
                <td class="center">
                ${gameMessageSource.formatRemainedTime(board, locale)}
                </td>
                <td>
                    <#list board.players as hand>
                        <div><@wm.player.name playerManager.getPlayer(hand.getPlayerId())/></div>
                    </#list>
                </td>
                <td class="center">
                    <#list board.players as hand>
                        <div>${hand.points}</div>
                    </#list>
                </td>
            </tr>
            </#list>

            <#list activeProposals as proposal>
            <tr id="proposal${proposal.id}">
                <td>${proposal.settings.title}</td>
                <td><@message code="language.${proposal.settings.language?lower_case}"/></td>
                <td>
                    <span class="player waiting"><span
                            class="nickname"><@message code="game.status.waiting"/></span></span>

                    <span style="float: right;">
                        <a href="decline?h=${proposal.id}"
                           onclick="activeGames.cancelProposal(${proposal.id}); return false;">
                            <@message code="game.proposal.cancel"/>
                        </a>
                    </span>
                </td>
                <td class="center">
                ${gameMessageSource.formatTimeMinutes(proposal.settings.daysPerMove *24 * 60, locale)}
                </td>
                <td>
                    <#list proposal.players as p>
                    <div>
                        <#if p??>
                            <@wm.player.name player=playerManager.getPlayer(p) waiting=!proposal.isPlayerJoined(p)/>
                        <#else>
                            <span class="player waiting"><span
                                    class="nickname"><@message code="game.status.waiting"/></span></span>
                        </div>
                        </#if>
                    </#list>
                </td>
                <td class="center">
                    <#list proposal.players as p>
                        <div><#if p??>0<#else>-</#if></div>
                    </#list>
                    <div>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
    </@wm.ui.table.content>

    <@wm.ui.table.footer/>
</@wm.ui.playground>

<script type="text/javascript">
    var activeGames = new wm.game.Active({
        cancelled: "<@message code="game.proposal.cancelled"/>",
        cancelling: "<@message code="game.proposal.cancelling"/>"
    <#if player == principal>
        , "sEmptyTable": "<@message code="game.dashboard.empty" args=['/playground/scribble/create', '/playground/scribble/join']/>"
    </#if>
    });
</script>