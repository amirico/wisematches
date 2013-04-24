<#-- @ftlvariable name="player" type="wisematches.core.Personality" -->
<#-- @ftlvariable name="boards" type="java.util.Collection<wisematches.playground.BoardDescription>" -->
<#-- @ftlvariable name="proposals" type="java.util.Collection<wisematches.playground.propose.GameProposal<wisematches.server.playground.scribble.ScribbleSettings>" -->
<#include "/core.ftl">

<#macro gameStatus board>
    <#if board.active>
        <#if board.playerTurn == principal>
            <@wm.board.href board.boardId><strong><@message code="game.status.move_you"/></strong></@wm.board.href>
        <#else>
            <@message code="game.status.move_opp" args=["${messageSource.getPersonalityNick(board.getPlayerTurn(), locale)!}"]/>
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
            <#if boards??>
                <#list boards as board>
                <tr id="board${board.boardId}">
                    <#assign settings=board.settings/>
                    <td><@wm.board.name board, true, false/></td>
                    <td><@message code="language.${settings.language?lower_case}"/></td>
                    <td><@gameStatus board=board/></td>
                    <td class="center">
                    ${messageSource.formatRemainedTime(board, locale)}
                    </td>
                    <td>
                        <#list board.players as p>
                            <div><@wm.player.name p/></div>
                        </#list>
                    </td>
                    <td class="center">
                        <#list board.players as p>
                            <#assign hand=board.getPlayerHand(p)/>
                            <div>${hand.points}</div>
                        </#list>
                    </td>
                </tr>
                </#list>
            </#if>

            <#if proposals??>
                <#list proposals as proposal>
                <tr id="proposal${proposal.id}">
                    <td>${proposal.settings.title}</td>
                    <td><@message code="language.${proposal.settings.language?lower_case}"/></td>
                    <td style="white-space: normal;">
                        <#assign privateNotJoined=proposal.proposalType.private && !proposal.isPlayerJoined(principal)/>
                        <div>
                        <span class="player waiting">
                <span class="nickname">
                    <#if privateNotJoined><@message code="game.status.challenge"/><#else><@message code="game.status.waiting"/></#if>
                </span>
                    </span>
                        </div>

                        <#if proposal.proposalType.private>
                            <#if !proposal.isPlayerJoined(principal) && proposal.commentary?has_content>
                                <div class="sample"
                                     style="white-space: normal; padding-left: 5px">${proposal.commentary}</div>
                            </#if>
                        <#else>
                            <#if proposal.initiator == principal && proposal.playerCriterion?has_content>
                                <div class="sample"
                                     style="white-space: normal; padding-left: 5px">
                                    <#list proposal.playerCriterion as criterion>
                                    ${messageSource.formatCriterion(criterion, locale, true)}<#if criterion_has_next>
                                        <br></#if>
                                    </#list>
                                </div>
                            </#if>
                        </#if>

                        <div style="text-align: right">
                            <#if proposal.containsPlayer(principal) && !proposal.isPlayerJoined(principal)>
                                <a href="#" onclick="proposal.accept(${proposal.id}); return false;">
                                    <@message code="game.proposal.accept"/>
                                </a>
                                &nbsp;
                                <a href="#" onclick="proposal.decline(${proposal.id}); return false;">
                                    <@message code="game.proposal.decline"/>
                                </a>
                            </#if>

                            <#if proposal.initiator == principal>
                                <a href="#" onclick="proposal.cancel(${proposal.id}); return false;">
                                    <@message code="game.proposal.cancel"/>
                                </a>
                            </#if>
                        </div>
                    </td>
                    <td class="center">
                    ${messageSource.formatTimeMinutes(proposal.settings.daysPerMove *24 * 60, locale)}
                    </td>
                    <td>
                        <#list proposal.players as p>
                        <div>
                            <#if p??>
                                <@wm.player.name player=p waiting=!proposal.isPlayerJoined(p)/>
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
            </#if>
        </tbody>
    </table>
    </@wm.ui.table.content>

    <@wm.ui.table.footer/>
</@wm.ui.playground>

<script type="text/javascript">
    wm.ui.dataTable('#dashboard', {
        "bStateSave": true,
        "bFilter": false,
        "bSortClasses": false,
        "aaSorting": [
            [3, 'asc']
        ],
        "aoColumns": [
            null,
            null,
            null,
            null,
            { "bSortable": false },
            { "bSortable": false }
        ]
    <#if player == principal>,
        "oLanguage": { "sEmptyTable": "<@message code="game.dashboard.empty" args=['/playground/scribble/create', '/playground/scribble/join']/>" }
    </#if>
    });

    var proposal = new wm.game.Proposal($("#activeGamesWidget"), {
        accepted: "<@message code="game.proposal.accepted"/>",
        accepting: "<@message code="game.proposal.accepting"/>",
        declined: "<@message code="game.proposal.declined"/>",
        declining: "<@message code="game.proposal.declining"/>",
        cancelled: "<@message code="game.proposal.cancelled"/>",
        cancelling: "<@message code="game.proposal.cancelling"/>"});
</script>
