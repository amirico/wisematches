<#-- @ftlvariable name="player" type="wisematches.server.player.Player" -->
<#-- @ftlvariable name="playerManager" type="wisematches.server.player.PlayerManager" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->
<#-- @ftlvariable name="activeBoards" type="java.util.Collection<wisematches.server.gameplaying.scribble.board.ScribbleBoard>" -->
<#include "/core.ftl">

<style type="text/css" title="currentStyle">
    @import "/jquery/css/table_jui.css";
    @import "/jquery/css/table_col_reorder.css";
    @import "/jquery/css/table_cov_vis.css";

    .dataTables_length {
        width: auto;
    }

    .dataTables_info {
        padding-top: 0;
        width: auto !important;
    }

    .dataTables_paginate {
    /*padding-top: 0;*/
    }

    #dashboard_wrapper .fg-toolbar {
        font-size: 0.8em
    }

    #theme_links span {
        float: left;
        padding: 2px 10px;
    }
</style>

<script type="text/javascript" src="/jquery/dataTables.min.js"></script>
<script type="text/javascript" src="/jquery/dataTables-colVis.min.js"></script>
<script type="text/javascript" src="/jquery/dataTables-colReorder.min.js"></script>
<script type="text/javascript">
    $(document).ready(function() {

        $('#dashboard').dataTable({
            "bJQueryUI": true,
            "bStateSave": true,
            "sDom": '<"H"lCr>t<"F"ip>',
            "sPaginationType": "full_numbers"
        });
    });
</script>

<#macro gameStatus board>
    <#if board.getGameState() == "IN_PROGRESS">
        <#if board.getPlayerTrun().getPlayerId() == player.getId()>
        <@message code="game.status.move_you"/>
            <#else>
            <@message code="game.status.move_opp" args="${playerManager.getPlayer(board.getPlayerTrun().getPlayerId()).nickname!}"/>
        </#if>
        <#else>
        <@message code="game.status.${board.getGameState().name()?lower_case}"/>
    </#if>
</#macro>

<style type="text/css">
    #dashboard th, #dashboard td {
        padding: 2px 5px;
        vertical-align: top;
        white-space: nowrap;
    }
</style>

<table width="100%">
    <tr>
        <td width="150px">
            Adds will be here. Also other information.
        </td>
        <td>
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
                    <td>
                        <#list board.playersHands as hand>
                            <div>${hand.points}</div>
                        </#list>

                        <#assign waitingCount = board.gameSettings.maxPlayers - board.playersHands?size/>
                        <#list 0..waitingCount as i>
                            <#if !i_has_next><#break/></#if>
                            <div>-</div>
                        </#list>
                    </td>
                    <td>
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