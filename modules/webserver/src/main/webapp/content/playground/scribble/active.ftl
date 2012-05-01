<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="activeBoards" type="java.util.Collection<wisematches.playground.scribble.ScribbleBoard>" -->
<#-- @ftlvariable name="activeProposals" type="java.util.Collection<wisematches.server.playground.propose.GameProposal<wisematches.server.playground.scribble.ScribbleSettings>" -->
<#include "/core.ftl">

<@wm.jstable/>

<style type="text/css">
    #dashboard.dataTable th {
        border: none;
    }

    .first {
        background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAMAAADzjKfhAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAAZQTFRFZmZm////c0tZqAAAAAJ0Uk5T/wDltzBKAAAAHklEQVR42mJgYAQBBgYGKMUApRigFBIfLg9kAAQYAAMdABu5MVT6AAAAAElFTkSuQmCC') no-repeat center center !important;
        padding-right: 3px;
    }

    .previous {
        background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAMAAADzjKfhAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAwBQTFRFZmZm////AgICAwMDBAQEBQUFBgYGBwcHCAgICQkJCgoKCwsLDAwMDQ0NDg4ODw8PEBAQEREREhISExMTFBQUFRUVFhYWFxcXGBgYGRkZGhoaGxsbHBwcHR0dHh4eHx8fICAgISEhIiIiIyMjJCQkJSUlJiYmJycnKCgoKSkpKioqKysrLCwsLS0tLi4uLy8vMDAwMTExMjIyMzMzNDQ0NTU1NjY2Nzc3ODg4OTk5Ojo6Ozs7PDw8PT09Pj4+Pz8/QEBAQUFBQkJCQ0NDRERERUVFRkZGR0dHSEhISUlJSkpKS0tLTExMTU1NTk5OT09PUFBQUVFRUlJSU1NTVFRUVVVVVlZWV1dXWFhYWVlZWlpaW1tbXFxcXV1dXl5eX19fYGBgYWFhYmJiY2NjZGRkZWVlZmZmZ2dnaGhoaWlpampqa2trbGxsbW1tbm5ub29vcHBwcXFxcnJyc3NzdHR0dXV1dnZ2d3d3eHh4eXl5enp6e3t7fHx8fX19fn5+f39/gICAgYGBgoKCg4ODhISEhYWFhoaGh4eHiIiIiYmJioqKi4uLjIyMjY2Njo6Oj4+PkJCQkZGRkpKSk5OTlJSUlZWVlpaWl5eXmJiYmZmZmpqam5ubnJycnZ2dnp6en5+foKCgoaGhoqKio6OjpKSkpaWlpqamp6enqKioqampqqqqq6urrKysra2trq6ur6+vsLCwsbGxsrKys7OztLS0tbW1tra2t7e3uLi4ubm5urq6u7u7vLy8vb29vr6+v7+/wMDAwcHBwsLCw8PDxMTExcXFxsbGx8fHyMjIycnJysrKy8vLzMzMzc3Nzs7Oz8/P0NDQ0dHR0tLS09PT1NTU1dXV1tbW19fX2NjY2dnZ2tra29vb3Nzc3d3d3t7e39/f4ODg4eHh4uLi4+Pj5OTk5eXl5ubm5+fn6Ojo6enp6urq6+vr7Ozs7e3t7u7u7+/v8PDw8fHx8vLy8/Pz9PT09fX19vb29/f3+Pj4+fn5+vr6+/v7/Pz8/f39/v7+////AADF2QAAAAJ0Uk5T/wDltzBKAAAAH0lEQVR42mJgBII0RkYGEAWh0yB0WhoqHy4PVg8QYAC1igaCyzmL4AAAAABJRU5ErkJggg==') no-repeat center center !important;
        padding-right: 3px;
    }

    .next {
        padding-left: 3px;
        background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAMAAADzjKfhAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAwBQTFRFZmZm////AgICAwMDBAQEBQUFBgYGBwcHCAgICQkJCgoKCwsLDAwMDQ0NDg4ODw8PEBAQEREREhISExMTFBQUFRUVFhYWFxcXGBgYGRkZGhoaGxsbHBwcHR0dHh4eHx8fICAgISEhIiIiIyMjJCQkJSUlJiYmJycnKCgoKSkpKioqKysrLCwsLS0tLi4uLy8vMDAwMTExMjIyMzMzNDQ0NTU1NjY2Nzc3ODg4OTk5Ojo6Ozs7PDw8PT09Pj4+Pz8/QEBAQUFBQkJCQ0NDRERERUVFRkZGR0dHSEhISUlJSkpKS0tLTExMTU1NTk5OT09PUFBQUVFRUlJSU1NTVFRUVVVVVlZWV1dXWFhYWVlZWlpaW1tbXFxcXV1dXl5eX19fYGBgYWFhYmJiY2NjZGRkZWVlZmZmZ2dnaGhoaWlpampqa2trbGxsbW1tbm5ub29vcHBwcXFxcnJyc3NzdHR0dXV1dnZ2d3d3eHh4eXl5enp6e3t7fHx8fX19fn5+f39/gICAgYGBgoKCg4ODhISEhYWFhoaGh4eHiIiIiYmJioqKi4uLjIyMjY2Njo6Oj4+PkJCQkZGRkpKSk5OTlJSUlZWVlpaWl5eXmJiYmZmZmpqam5ubnJycnZ2dnp6en5+foKCgoaGhoqKio6OjpKSkpaWlpqamp6enqKioqampqqqqq6urrKysra2trq6ur6+vsLCwsbGxsrKys7OztLS0tbW1tra2t7e3uLi4ubm5urq6u7u7vLy8vb29vr6+v7+/wMDAwcHBwsLCw8PDxMTExcXFxsbGx8fHyMjIycnJysrKy8vLzMzMzc3Nzs7Oz8/P0NDQ0dHR0tLS09PT1NTU1dXV1tbW19fX2NjY2dnZ2tra29vb3Nzc3d3d3t7e39/f4ODg4eHh4uLi4+Pj5OTk5eXl5ubm5+fn6Ojo6enp6urq6+vr7Ozs7e3t7u7u7+/v8PDw8fHx8vLy8/Pz9PT09fX19vb29/f3+Pj4+fn5+vr6+/v7/Pz8/f39/v7+////AADF2QAAAAJ0Uk5T/wDltzBKAAAAH0lEQVR42mJgZExjBAIGIJ0Go9NgdBoqHyoPUg8QYAC3HgaCbKesXAAAAABJRU5ErkJggg==') no-repeat center center !important;
    }

    .last {
        padding-left: 3px;
        background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAMAAADzjKfhAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAAZQTFRFZmZm////c0tZqAAAAAJ0Uk5T/wDltzBKAAAAHElEQVR42mJgYAQBBiCA0QwwmgGVD5WHqAcIMAADAwAbezQSkgAAAABJRU5ErkJggg==') no-repeat center center !important;
    }

    .asd {
        padding: 2px;
    }

    .dataTables_wrapper {
        text-align: right;
        /*background-color: #ddd;*/
    }

    .dataTables_length {
        padding-right: 10px;
    }

    .dataTables_length, .dataTables_info, .dataTables_paginate {
        width: auto;
        float: none;
        text-align: right;
        display: inline-block;
        padding-left: 10px;
    }
</style>

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
<div>
    <div class="ui-widget-header ui-corner-top"
         style="border-top-color: #CCC; border-left-color: #CCC; border-right-color: #CCC; border-bottom-width:0">
        <div>
            Мои Игры >
            <#if player != principal>
                <@message code="game.dashboard.label"/> <@message code="separator.for"/> <@wm.player player=player showState=true showType=true/>
            <#else>
                Текущие игры
            </#if>
        </div>
    </div>

    <div style="height: 3px; background-color: blue; border-right: 1px solid #CCC;border-left: 1px solid #CCC;">
    </div>

        <div class="ui-widget-content"
        style="border: 0; border-right: 1px solid #CCC;border-left: 1px solid #CCC;>
    <#if player == principal>
            <a href="/playground/scribble/history"><@message code="game.past.history.label"/></a>
    </#if>
</div>

<div class="ui-widget-content"
     style="padding: 0; border-width: 2px; margin: 0; border-left: 1px solid #CCC; border-right: 1px solid #CCC;">
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
                        <a href="decline?p=${proposal.id}" onclick="cancelProposal(${proposal.id}); return false;">
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
                        <#if p??>
                            <@wm.player player=playerManager.getPlayer(p)/>
                        <#else>
                            <span class="player"><span
                                    class="waiting"><@message code="game.status.waiting"/></span></span>
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
</div>

<div class="ui-corner-bottom"
     style="height: 3px; border-bottom: 1px solid #CCC; border-right: 1px solid #CCC;border-left: 1px solid #CCC;">
</div>
</div>
</@wm.playground>

<script type="text/javascript">
    wm.ui.dataTable('#dashboard', {
        "bStateSave":true,
        "bFilter":false,
        "bSortClasses":false,
        "aaSorting":[
            [3, 'asc']
        ],
        "aoColumns":[
            null,
            null,
            null,
            null,
            { "bSortable":false },
            { "bSortable":false }
        ],
        "sDom":'<<t><"asd"lip>>',
        "sPaginationType":"full_numbers",
        "oLanguage":{
            "sLengthMenu":"Показать строки: _MENU_",
            "sInfo":"_START_ - _END_ из _TOTAL_",
            "sInfoEmpty":"Showing 0 to 0 of 0 records",
            "sInfoFiltered":"(filtered from _MAX_ total records)",
        <#if player == principal>
            "sEmptyTable":"<@message code="game.dashboard.empty" args=['/playground/scribble/create', '/playground/scribble/join']/>"
        </#if>
        }
    });

    function cancelProposal(id) {
        $.ajax('decline.ajax?p=' + id, {
            success:function (data, textStatus, jqXHR) {
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
