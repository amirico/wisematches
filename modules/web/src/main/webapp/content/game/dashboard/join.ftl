<#-- @ftlvariable name="activeBoards" type="java.util.Collection<wisematches.server.playground.scribble.board.ScribbleBoard>" -->
<#-- @ftlvariable name="activeProposals" type="java.util.Collection<wisematches.server.playground.propose.GameProposal<wisematches.server.playground.scribble.board.ScribbleSettings>" -->
<#include "/core.ftl">

<script type="text/javascript">
    $(document).ready(function() {
        $("#refreshGameboard").button({icons: {primary: 'ui-icon-refresh'}});

        $('#gameboard').dataTable({
                    "bJQueryUI": true,
                    "bStateSave": true,
                    "bFilter": false,
                    "bSort": false,
                    "bSortClasses": false,
                    "sDom": '<"H"lCr>t<"F"ip>',
                    "sPaginationType": "full_numbers",
                    "oLanguage": {
                        "sEmptyTable": "<@message code="game.gameboard.empty" args=["/game/create.html"]/>"
                    }
                });
    });
</script>

<div id="join-game">
    <table width="100%">
        <tr>
            <td width="160px" valign="top">
            <#include "/content/ops/advertisement.ftl">
            </td>
            <td valign="top">
                <table width="100%">
                    <tr>
                        <td width="100%" nowrap="nowrap">
                        <@spring.bind path="join"/>
                        <#if spring.status.error>
                            <div class="ui-state-error ui-corner-all">
                                <#list spring.status.errorMessages as msg>
                                    <div class="ui-state-error-text error-msg">${msg}</div>
                                </#list>
                            </div>
                        </#if>
                        </td>
                        <td nowrap="nowrap">
                            <button id="refreshGameboard" onclick="window.location.reload()">
                            <@message code="refresh.label"/>
                            </button>
                        </td>
                    </tr>
                </table>

                <table id="gameboard" width="100%" class="display">
                    <thead>
                    <tr>
                        <th><@message code="game.title.label"/></th>
                        <th><@message code="game.language.label"/></th>
                        <th><@message code="game.time.label"/></th>
                        <th><@message code="game.opponents.label"/></th>
                        <th><@message code="game.join.label"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list activeProposals as proposal>
                    <tr>
                        <td>${proposal.gameSettings.title}</td>
                        <td><@message code="language.${proposal.gameSettings.language}"/></td>
                        <td align="center">${gameMessageSource.formatMinutes(proposal.gameSettings.daysPerMove*24*60,locale)}</td>
                        <td>
                            <#list proposal.players as p>
                                <div>
                                <@wm.player player=playerManager.getPlayer(p) showRating=true/>
                                </div>
                            </#list>
                            <#list (proposal.players?size)..(proposal.playersCount-1) as i>
                                <div>
                                    <span class="player"><span
                                            class="waiting"><@message code="game.status.waiting"/></span></span>
                                </div>
                            </#list>
                        </td>
                        <td class="center">
                            <#assign msg=gameMessageSource.formatJoinException(proposal, player, locale)!""/>
                            <#if msg?has_content>
                                <span class="game-join-error">${msg}</span>
                                <#else>
                                    <a href="/game/gameboard.html?p=${proposal.id}">&raquo; <@message code="game.join.label"/></a>
                            </#if>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </td>
            <td width="160px" valign="top"></td>
        </tr>
    </table>
</div>