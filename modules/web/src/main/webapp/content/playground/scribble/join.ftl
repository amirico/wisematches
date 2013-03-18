<#-- @ftlvariable name="proposals" type="java.util.Map.Entry<wisematches.playground.propose.GameProposal<wisematches.playground.scribble.ScribbleSettings>, wisematches.playground.propose.CriterionViolation[]>[]" -->
<#-- @ftlvariable name="globalViolations" type="wisematches.playground.propose.CriterionViolation[]" -->

<#include "/core.ftl">

<@wm.ui.table.dtinit/>

<div id="join-game">
<@wm.ui.playground id="waitingGamesWidget">
    <@wm.ui.table.header>
        <@message code="game.menu.games.label"/> > <@message code="game.join.label"/>
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar>
        <table width="100%">
            <tr>
                <td align="left">
                    <div class="wm-ui-buttonset">
                        <a href="/playground/scribble/active"><@message code="game.dashboard.label"/></a>
                        <a href="/playground/scribble/create"><@message code="game.create.label"/></a>
                    </div>
                </td>
                <td align="right">
                </td>
            </tr>
        </table>
    </@wm.ui.table.toolbar>

    <@wm.ui.table.content>
        <table id="gameboard" width="100%" class="display">
            <thead>
            <tr>
                <th><@message code="game.title.label"/></th>
                <th><@message code="game.language.label"/></th>
                <th><@message code="game.time.label"/></th>
                <th><@message code="game.opponents.label"/></th>
                <th><@message code="game.join.action.label"/></th>
            </tr>
            </thead>
            <tbody>
                <#list proposals as entry>
                    <#assign proposal=entry.key/>
                    <#assign violations=entry.value!""/>
                <tr>
                    <td>${proposal.settings.title}</td>
                    <td><@message code="language.${proposal.settings.language?lower_case}"/></td>
                    <td align="center">${messageSource.formatTimeMinutes(proposal.settings.daysPerMove*24*60,locale)}</td>
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
                    <td>
                        <#if violations?has_content>
                            <#list violations as violation>
                                <div class="game-join-error">${messageSource.formatViolation(violation, locale, true)}</div>
                            </#list>
                        <#else>
                            <#if proposal.proposalType.private>
                                <a href="#accept"
                                   onclick="proposal.accept(${proposal.id}); return false;">&raquo; <@message code="game.join.accept.label"/></a>
                                <br>
                                <a href="#decline"
                                   onclick="proposal.decline(${proposal.id}); return false;">&raquo; <@message code="game.join.decline.label"/></a>
                            <#else>
                                <a href="#join"
                                   onclick="proposal.accept(${proposal.id}); return false;">&raquo; <@message code="game.join.join.label"/></a>
                            </#if>
                        </#if>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </@wm.ui.table.content>

    <@wm.ui.table.footer>
        <#if globalViolations??>
            <#list globalViolations as v>
                <div class="ui-state-error-text">
                ${messageSource.formatViolation(v, locale, false)}
                </div>
            </#list>
        </#if>
    </@wm.ui.table.footer>
</@wm.ui.playground>

    <script type="text/javascript">
        wm.ui.dataTable('#gameboard', {
            "bStateSave": true,
            "bFilter": false,
            "bSort": false,
            "bSortClasses": false,
            "oLanguage": {"sEmptyTable": "<@message code="game.gameboard.empty" args=["/playground/scribble/create"]/>"}
        });

        var proposal = new wm.game.Proposal($("#waitingGamesWidget"), {
            accepted: "<@message code="game.proposal.accepted"/>",
            accepting: "<@message code="game.proposal.accepting"/>",
            declined: "<@message code="game.proposal.declined"/>",
            declining: "<@message code="game.proposal.declining"/>",
            cancelled: "<@message code="game.proposal.cancelled"/>",
            cancelling: "<@message code="game.proposal.cancelling"/>"});
    </script>
</div>