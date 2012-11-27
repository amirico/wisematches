<#-- @ftlvariable name="announce" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="participated" type="wisematches.playground.tourney.regular.TourneyGroup[]" -->

<#include "/core.ftl">
<#include "scriplet.ftl">

<@wm.jstable/>

<#assign pid=principal.id/>

<@wm.playground id="tourneyWidget">
<table id="tourney" width="100%">
    <tr>
        <td width="100%" valign="top">
            <div id="participated">
                <@wm.dtHeader align="left">
                    <@message code="tourney.participated.label"/>
                </@wm.dtHeader>

                <@wm.dtToolbar>
                    <div>
                        <a href="/playground/tourney/active"><@message code="tourney.list.active.label"/></a>
                        <a href="/playground/tourney/finished"><@message code="tourney.list.finished.label"/></a>
                    </div>
                </@wm.dtToolbar>

                <@wm.dtContent>
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
                                    <@tourneyName tourneyId=groupId.roundId.divisionId.tourneyId link=true/>
                                </td>
                                <td>
                                    <@languageName language=groupId.roundId.divisionId.language/>
                                </td>
                                <td>
                                    <@sectionName section=groupId.roundId.divisionId.section/>
                                </td>
                                <td>
                                    <@roundName roundId=g.id.roundId link=true/>
                                </td>
                                <td>
                                    <@groupName groupId=g.id link=true/>
                                </td>

                                <td align="center">
                                    <#list g.players as p>
                                        <#if p != pid>
                                            <div>
                                                <@wm.player player=playerManager.getPlayer(p)/>
                                            </div>
                                        </#if>
                                    </#list>
                                </td>
                                <td align="center">
                                    <#list g.players as p>
                                        <#if p != pid>
                                            <div>
                                                <#assign gid=g.getGameId(pid, p)/>
                                                <a href="/playground/scribble/board?b=${gid?string}">Игра
                                                    #${gid?string}</a>
                                            </div>
                                        </#if>
                                    </#list>
                                </td>
                                <td align="center">
                                    <#list g.players as p>
                                        <#if p != pid>
                                            <div>
                                            ${g.getScores(p) - g.getScores(pid)}
                                            </div>
                                        </#if>
                                    </#list>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </@wm.dtContent>

                <@wm.dtFooter/>
            </div>
        </td>
        <#if announce??>
            <td valign="top">
                <#include "announce.ftl">
            </td>
        </#if>
    </tr>
</table>
</@wm.playground>

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