<#-- @ftlvariable name="gamesCount" type="java.lang.Integer" -->
<#-- @ftlvariable name="restricted" type="java.lang.Boolean" -->
<#-- @ftlvariable name="activeBoards" type="java.util.Collection<wisematches.playground.scribble.ScribbleBoard>" -->
<#-- @ftlvariable name="activeProposals" type="java.util.Collection<wisematches.server.web.controllers.playground.scribble.view.GameProposalView>" -->
<#include "/core.ftl">

<@wm.jstable/>

<script type="text/javascript">
    $(document).ready(function () {
        wm.ui.dataTable('#gameboard', {
            "bStateSave":true,
            "bFilter":false,
            "bSort":false,
            "bSortClasses":false,
            "oLanguage":{
                "sEmptyTable":"<@message code="game.gameboard.empty" args=["/playground/scribble/create"]/>"
            }
        });
    });
</script>

<div id="join-game">
<@wm.playground id="waitingGamesWidget">
    <@wm.dtHeader>
        <@message code="game.menu.games.label"/> > <@message code="game.join.label"/>
    </@wm.dtHeader>

    <@wm.dtToolbar>
        <#if joinError??>
            <div class="ui-state-error-text error-msg" style="float: left;">
                <@message code=joinError args=joinErrorArgs/>
            </div>
        </#if>
        <div>
            <a href="/playground/scribble/create"><@message code="game.create.label"/></a>
        </div>
    </@wm.dtToolbar>

    <@wm.dtContent>
        <table id="gameboard" width="100%" class="display">
            <thead>
            <tr>
                <th><@message code="game.title.label"/></th>
                <th><@message code="game.language.label"/></th>
                <th><@message code="game.time.label"/></th>
                <th><@message code="game.opponents.label"/></th>
                <th><@message code="game.restrictions.label"/></th>
            </tr>
            </thead>
            <tbody>
                <#list activeProposals as proposal>
                    <#assign settings=proposal.proposal.gameSettings/>
                <tr>
                    <td>${settings.title}</td>
                    <td><@message code="language.${settings.language}"/></td>
                    <td align="center">${gameMessageSource.formatMinutes(settings.daysPerMove*24*60,locale)}</td>
                    <td>
                        <#list proposal.proposal.players as p>
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
                        <#if proposal.blacklisted || restricted>
                            <span class="game-join-error"><@message code="game.join.err.forbidden.label"/></span>
                        <#elseif principal.membership == "GUEST">
                            <span class="game-join-error"><@message code="game.join.err.guest.label"/></span>
                        <#elseif proposal.violations?? && !proposal.violations.empty>
                            <#list proposal.violations as violation>
                                <#assign errorCode="game.join.err.${violation.code}"/>
                                <#if violation.operator?? && violation.operator?has_content><#assign errorCode="${errorCode}.${violation.operator}"/></#if>
                                <#assign errorCode="${errorCode}.label"/>

                                <div class="game-join-error">${gameMessageSource.getMessage(errorCode, locale, violation.expected, violation.received)}</div>
                            </#list>
                        <#else>
                            <a href="/playground/scribble/join?p=${proposal.proposal.id}">&raquo; <@message code="game.join.accept.label"/></a>
                            <#if proposal.proposal.proposalType == "CHALLENGE">
                                <br>
                                <a href="/playground/scribble/decline?p=${proposal.proposal.id}">&raquo; <@message code="game.join.decline.label"/></a>
                            </#if>
                        </#if>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </@wm.dtContent>

    <@wm.dtFooter>
        <#if restricted>
            <div class="sample" style="padding: 5px;">
                <@message code="game.create.forbidden" args=[gamesCount, '/playground/scribble/active', '/account/membership']/>
            </div>
        </#if>
    </@wm.dtFooter>
</@wm.playground>
</div>