<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="activeBoards" type="java.util.Collection<wisematches.playground.scribble.ScribbleBoard>" -->
<#-- @ftlvariable name="activeProposals" type="java.util.Collection<wisematches.server.playground.propose.GameProposal<wisematches.server.playground.scribble.ScribbleSettings>" -->
<#include "/core.ftl">

<#macro gameStatus board>
    <#if board.isGameActive()>
        <#if board.getPlayerTurn().getPlayerId() == principal.getId()>
        <span class="player"><a
                href="/playground/scribble/board?b=${board.getBoardId()}"><b><@message code="game.status.move_you"/></b></a></span>
            <#else>
            <@message code="game.status.move_opp" args=["${playerManager.getPlayer(board.getPlayerTurn().getPlayerId()).nickname!}"]/>
        </#if>
    </#if>
</#macro>

<table width="100%">
    <tr>
        <td width="160px" valign="top">
        <#include "/content/templates/advertisement.ftl">
        </td>
        <td valign="top">
            <div style="float: right;">
                <button id="refreshDashboard" onclick="window.location.reload()">
                <@message code="refresh.label"/>
                </button>
            </div>

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
                <tr>
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
                            <div><@wm.player player=playerManager.getPlayer(hand.getPlayerId()) showRating=false/></div>
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
                <tr>
                    <td>${proposal.gameSettings.title}</td>
                    <td><@message code="language.${proposal.gameSettings.language}"/></td>
                    <td>
                        <span class="player"><span class="waiting"><@message code="game.status.waiting"/></span></span>
                    </td>
                    <td class="center">
                    ${gameMessageSource.formatMinutes(proposal.gameSettings.daysPerMove *24 * 60, locale)}
                    </td>
                    <td>
                        <#list proposal.players as p>
                            <div>
                            <@wm.player player=playerManager.getPlayer(p) showRating=false/>
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
        </td>
        <td width="160px" valign="top"></td>
    </tr>
</table>

<script type="text/javascript">
    $("#refreshDashboard").button({icons: {primary: 'ui-icon-refresh'}});

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
            "sEmptyTable": "<@message code="game.dashboard.empty" args=['/playground/scribble/create', '/playground/scribble/join']/>"
        }
    });
</script>
