<#-- @ftlvariable name="player" type="wisematches.server.player.Player" -->
<#-- @ftlvariable name="playerManager" type="wisematches.server.player.PlayerManager" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#-- @ftlvariable name="activeBoards" type="java.util.Collection<wisematches.server.gameplaying.scribble.board.ScribbleBoard>" -->
<#-- @ftlvariable name="activeProposals" type="java.util.Collection<wisematches.server.gameplaying.scribble.room.proposal.ScribbleProposal>" -->
<#include "/core.ftl">
<#import "../macros.ftl" as game>
<#import "/content/utils.ftl" as utils>

<script type="text/javascript">
    $(document).ready(function() {
        $("#refreshDashboard").button({icons: {primary: 'ui-icon-refresh'}});

        $('#dashboard').dataTable({
            "bJQueryUI": true,
            "bStateSave": true,
            "bFilter": false,
            "bSort": false,
            "bSortClasses": false,
            "sDom": '<"H"lCr>t<"F"ip>',
            "sPaginationType": "full_numbers",
            "oLanguage": {
                "sEmptyTable": "<@message code="game.dashboard.empty" args=['/game/create.html', '/game/gameboard.html']/>"
            }
        });
    });
</script>

<#macro gameStatus board>
    <#if board.getGameState() == "ACTIVE">
        <#if board.getPlayerTurn().getPlayerId() == player.getId()>
        <span class="player"><a
                href="/game/playboard.html?boardId=${board.getBoardId()}"><b><@message code="game.status.move_you"/></b></a></span>
            <#else>
            <@message code="game.status.move_opp" args="${playerManager.getPlayer(board.getPlayerTurn().getPlayerId()).nickname!}"/>
        </#if>
        <#else>
        <@message code="game.status.${board.getGameState().name()?lower_case}"/>
    </#if>
</#macro>

<table width="100%">
    <tr>
        <td width="160px" valign="top">
            Adds will be here. Also other information.
        </td>
        <td valign="top">
            <div style="float: right;">
                <button id="refreshDashboard" onclick="window.location.reload()">
                <@message code="refresh.label"/>
                </button>
            </div>

            <table id="dashboard" width="100%">
                <thead>
                <tr>
                    <th width="100%"><@message code="game.title.label"/></th>
                    <th><@message code="game.language.label"/></th>
                    <th><@message code="game.status.label"/></th>
                    <th><@message code="game.opponents.label"/></th>
                    <th><@message code="game.scores.label"/></th>
                    <th><@message code="game.time.label"/></th>
                </tr>
                </thead>
                <tbody>
                <#list activeBoards as board>
                <tr>
                    <#assign settings=board.gameSettings/>
                    <td>
                        <a href="/game/playboard.html?boardId=${board.boardId}">${settings.title}</a>
                    </td>
                    <td><@message code="language.${settings.language}"/></td>
                    <td><@gameStatus board=board/></td>
                    <td>
                        <#list board.playersHands as hand>
                            <#assign player=playerManager.getPlayer(hand.getPlayerId())!/>
                            <div>
                            <@game.player player=player showRating=false/>
                            </div>
                        </#list>
                    </td>
                    <td class="center">
                        <#list board.playersHands as hand>
                            <div>${hand.points}</div>
                        </#list>
                    </td>
                    <td class="center">
                        <#list board.playersHands as hand>
                            <div>3d (!!!!)</div>
                        </#list>
                    </td>
                </tr>
                </#list>

                <#list activeProposals as proposal>
                <tr>
                    <td>${proposal.title}</td>
                    <td><@message code="language.${proposal.language}"/></td>
                    <td>
                        <span class="player"><span class="waiting"><@message code="game.status.waiting"/></span></span>
                    </td>
                    <td>
                        <#list proposal.allPlayers as p>
                            <div>
                                <#if p??>
                                    <#assign player=playerManager.getPlayer(p)/>
                                <@game.player player=player showRating=false/>
                                    <#else>
                                        <span class="player">
                                            <span class="waiting"><@message code="game.status.waiting"/></span>
                                        </span>
                                </#if>
                            </div>
                        </#list>
                    </td>
                    <td class="center">
                        <#list proposal.allPlayers as p>
                            <div>0</div></#list>
                    </td>
                    <td class="center">
                        <#list proposal.allPlayers as p>
                            <div><@utils.daysAsString days=proposal.timeLimits/></div></#list>
                    </td>
                </tr>
                </#list>
                </tbody>
            </table>
        </td>
        <td width="160px" valign="top"></td>
    </tr>
</table>