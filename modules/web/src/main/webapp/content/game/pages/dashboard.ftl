<#-- @ftlvariable name="player" type="wisematches.server.player.Player" -->
<#-- @ftlvariable name="playerManager" type="wisematches.server.player.PlayerManager" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#-- @ftlvariable name="activeBoards" type="java.util.Collection<wisematches.server.gameplaying.scribble.board.ScribbleBoard>" -->
<#include "/core.ftl">

<style type="text/css">
    #dashboard th, #dashboard td {
        padding: 2px 5px;
        vertical-align: top;
        white-space: nowrap;
    }
</style>

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
                "sEmptyTable": "There are no any games to play.<br> You can <a href='/game/create.html'>create new game</a> or <a href='/game/gameboard.html'>joint to exist game.</a> "
            }
        });
    });
</script>

<#macro gameStatus board>
    <#if board.getGameState() == "IN_PROGRESS">
        <#if board.getPlayerTurn().getPlayerId() == player.getId()>
        <@message code="game.status.move_you"/>
            <#else>
            <@message code="game.status.move_opp" args="${playerManager.getPlayer(board.getPlayerTurn().getPlayerId()).nickname!}"/>
        </#if>
        <#else>
        <@message code="game.status.${board.getGameState().name()?lower_case}"/>
    </#if>
</#macro>

<table width="100%">
    <tr>
        <td width="150px" valign="top">
            Adds will be here. Also other information.
        </td>
        <td valign="top">
            <div style="float: right;">
                <button id="refreshDashboard" onclick="window.location.reload()">Refresh Table</button>
            </div>

            <table id="dashboard" width="100%">
                <thead>
                <tr>
                    <th width="100%">Title</th>
                    <th>Language</th>
                    <th>Status</th>
                    <th>Opponent</th>
                    <th>Score</th>
                    <th>Time</th>
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
                                <a href="/game/profile.html?player=${player.id}"><b>${player.nickname}</b></a>
                            </div>
                        </#list>

                        <#assign waitingCount = board.gameSettings.maxPlayers - board.playersHands?size/>
                        <#list 0..waitingCount as i>
                            <#if !i_has_next><#break/></#if>
                            <div><b><@message code="game.status.waiting"/></b></div>
                        </#list>
                    </td>
                    <td class="center">
                        <#list board.playersHands as hand>
                            <div>${hand.points}</div>
                        </#list>

                        <#assign waitingCount = board.gameSettings.maxPlayers - board.playersHands?size/>
                        <#list 0..waitingCount as i>
                            <#if !i_has_next><#break/></#if>
                            <div>-</div>
                        </#list>
                    </td>
                    <td class="center">
                        <#list board.playersHands as hand>
                            <div>3d</div>
                        </#list>

                        <#assign waitingCount = board.gameSettings.maxPlayers - board.playersHands?size/>
                        <#list 0..waitingCount as i>
                            <#if !i_has_next><#break/></#if>
                            <div>-</div>
                        </#list>
                    </td>
                </tr>
                </#list>
                </tbody>
            </table>
        </td>
    </tr>
</table>