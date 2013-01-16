<#-- @ftlvariable name="announce" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="participated" type="wisematches.playground.tourney.regular.TourneyGroup[]" -->

<#include "/core.ftl">

<@wm.ui.table.dtinit/>

<#assign pid=principal.id/>

<@wm.ui.playground id="tourneyWidget">
<table id="tourney" width="100%">
    <tr>
        <td width="100%" valign="top">
            <div id="participated">
                <@wm.ui.table.header align="left">
                    <@message code="tourney.tourney.label"/> > <@message code="tourney.participated.label"/>
                </@wm.ui.table.header>

                <@wm.ui.table.toolbar>
                    <div class="wm-ui-buttonset">
                        <a href="/playground/tourney/active"><@message code="tourney.all.active.label"/></a>
                        <a href="/playground/tourney/finished"><@message code="tourney.all.finished.label"/></a>
                    </div>
                </@wm.ui.table.toolbar>

                <@wm.ui.table.content>
                    <table width="100%" class="display">
                        <thead>
                        <tr>
                            <th width="100%"><@message code="tourney.tourney.label"/></th>
                            <th><@message code="tourney.language.label"/></th>
                            <th><@message code="tourney.level.label"/></th>
                            <th><@message code="tourney.round.label"/></th>
                            <th><@message code="tourney.group.label"/></th>
                            <th><@message code="tourney.opponent.label"/></th>
                            <th><@message code="tourney.game.label"/></th>
                            <th><@message code="tourney.success.label"/></th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list participated as g>
                                <#assign groupId=g.id/>
                            <tr>
                                <td>
                                    <@wm.tourney.tourney groupId.roundId.divisionId.tourneyId, true/>
                                </td>
                                <td>
                                    <@wm.tourney.language groupId.roundId.divisionId.language/>
                                </td>
                                <td>
                                    <@wm.tourney.section groupId.roundId.divisionId.section/>
                                </td>
                                <td>
                                    <@wm.tourney.round g.id.roundId, true/>
                                </td>
                                <td>
                                    <@wm.tourney.group g.id , true/>
                                </td>

                                <td align="center">
                                    <#list g.players as p>
                                        <#if p != pid>
                                            <div>
                                                <@wm.player.name playerManager.getPlayer(p)/>
                                            </div>
                                        </#if>
                                    </#list>
                                </td>
                                <td align="center">
                                    <#list g.players as p>
                                        <#if p != pid>
                                            <div>
                                                <#assign gid=g.getGameId(pid, p)?string/>
                                                <@wm.board.href gid>#${gid?string}</@wm.board.href>
                                            </div>
                                        </#if>
                                    </#list>
                                </td>
                                <td align="center">
                                    <#list g.players as p>
                                        <#if p != pid>
                                            <div>
                                            ${g.getPlayerScores(p) - g.getPlayerScores(pid)}
                                            </div>
                                        </#if>
                                    </#list>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </@wm.ui.table.content>

                <@wm.ui.table.footer/>
            </div>
        </td>
        <#if announce??>
            <td valign="top">
                <#include "announce.ftl">
            </td>
        </#if>
    </tr>
</table>
</@wm.ui.playground>

<script type="text/javascript">
    wm.ui.dataTable('#tourney #participated table', {
        "bSortClasses": false,
        "aoColumns": [
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": false },
            { "bSortable": false },
            { "bSortable": false }
        ]
    });
</script>