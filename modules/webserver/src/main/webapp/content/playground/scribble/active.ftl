<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="activeBoards" type="java.util.Collection<wisematches.playground.scribble.ScribbleBoard>" -->
<#-- @ftlvariable name="activeProposals" type="java.util.Collection<wisematches.server.playground.propose.GameProposal<wisematches.server.playground.scribble.ScribbleSettings>" -->
<#include "/core.ftl">

<@wm.jstable/>

<#macro gameStatus board>
    <#if board.isGameActive()>
        <#if board.getPlayerTurn().getPlayerId() == principal.getId()>
        <span class="player"><a
                href="/playground/scribble/board?b=${board.getBoardId()}"><strong><@message code="game.status.move_you"/></strong></a></span>
            <#else>
            <@message code="game.status.move_opp" args=["${playerManager.getPlayer(board.getPlayerTurn().getPlayerId()).nickname!}"]/>
        </#if>
    </#if>
</#macro>

<@wm.playground id="activeGamesWidget">

<table width="100%" height="30px">
    <tr>
        <td width="100%" nowrap="nowrap">
            <#if player != principal>
                <div class="title">
                <@message code="game.dashboard.label"/> <@message code="separator.for"/> <@wm.player player=player showState=true showType=true/>
                </div>
            </#if>
        </td>
        <td nowrap="nowrap" valign="top">
            <#if player == principal>
                <a href="/playground/scribble/history"><@message code="game.past.history.label"/></a>
            </#if>
        </td>
    </tr>
</table>

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
            <#assign settings=board.gameSettings/>
            <td>
                <a href="/playground/scribble/board?b=${board.boardId}">${settings.title}</a>
            </td>
            <td><@message code="language.${settings.language}"/></td>
            <td><@gameStatus board=board/></td>
            <td class="center">
            ${gameMessageSource.formatRemainedTime(board, locale)}
            </td>
            <td>
                <#list board.playersHands as hand>
                    <div><@wm.player player=playerManager.getPlayer(hand.getPlayerId())/></div>
                </#list>
            </td>
            <td class="center">
                <#list board.playersHands as hand>
                    <div>${hand.points}</div>
                </#list>
            </td>
        </tr>
        </#list>

        <#list activeProposals as proposal>
        <tr id="proposal${proposal.id}">
            <td>${proposal.gameSettings.title}</td>
            <td><@message code="language.${proposal.gameSettings.language}"/></td>
            <td>
                <span class="player"><span class="waiting"><@message code="game.status.waiting"/></span></span>

                <div style="text-align: right;">
                    <a href="cancel?p=${proposal.id}" onclick="cancelProposal(${proposal.id}); return false;">
                    <@message code="game.proposal.cancel"/>
                    </a>
                </div>
            </td>
            <td class="center">
            ${gameMessageSource.formatMinutes(proposal.gameSettings.daysPerMove *24 * 60, locale)}
            </td>
            <td>
                <#list proposal.players as p>
                    <div>
                    <@wm.player player=playerManager.getPlayer(p)/>
                    </div>
                </#list>
                <#list (proposal.players?size)..(proposal.playersCount-1) as i>
                    <div>
                        <span class="player"><span class="waiting"><@message code="game.status.waiting"/></span></span>
                    </div>
                </#list>
            </td>
            <td class="center">
                <#list proposal.players as p>
                    <div>0</div>
                </#list>
                <#list (proposal.players?size)..(proposal.playersCount-1) as i>
                    <div>-</div>
                </#list>
            </td>
        </tr>
        </#list>
    </tbody>
</table>
</@wm.playground>

<script type="text/javascript">
    $('#dashboard').dataTable({
        "bJQueryUI": true,
        "bStateSave": true,
        "bFilter": false,
        "bSortClasses": false,
        "aaSorting": [
            [3,'asc']
        ],
        "aoColumns": [
            null,
            null,
            null,
            null,
            { "bSortable": false },
            { "bSortable": false }
        ],
        "sDom": '<"H"lCr>t<"F"ip>',
        "sPaginationType": "full_numbers",
        "oLanguage": {
        <#if player == principal>
            "sEmptyTable": "<@message code="game.dashboard.empty" args=['/playground/scribble/create', '/playground/scribble/join']/>"
        </#if>
        }
    });

    function cancelProposal(id) {
        $.ajax('cancel?p=' + id, {
            success: function(data, textStatus, jqXHR) {
                if (data.success) {
                    $("#proposal" + id).fadeOut();
                    wm.ui.showStatus("<@message code="game.proposal.canceled"/>", false);
                } else {
                    wm.ui.showStatus("<@message code="game.proposal.cancel.error"/>", true);
                }
            }
        });
    }
</script>
