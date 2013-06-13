<#-- @ftlvariable name="announce" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="groupsIterator" type="wisematches.playground.tourney.regular.TourneyEntryIterator<wisematches.playground.tourney.regular.TourneyGroup>" -->

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
                            <th width="100%" rowspan="2"><@message code="tourney.tourney.label"/></th>
                            <th rowspan="2"><@message code="tourney.language.label"/></th>
                            <th rowspan="2"><@message code="tourney.level.label"/></th>
                            <th rowspan="2"><@message code="tourney.round.label"/></th>
                            <th rowspan="2"><@message code="tourney.group.label"/></th>
                            <th colspan="2"
                                class="ui-state-default colgroup"><@message code="tourney.opponents.label"/></th>
                        </tr>
                        <tr>
                            <th><@message code="tourney.opponent.label"/></th>
                            <th><@message code="tourney.success.label"/></th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list groupsIterator.itemsEntry as e>
                                <#assign tourney=e.key/>
                                <#list e.value as group>
                                    <#assign round=group.round/>
                                    <#assign division=round.division/>
                                <tr>
                                    <td>
                                        <@wm.tourney.tourney tourney.id, true/> <@wm.tourney.dates tourney "sample"/>
                                    </td>
                                    <td style="padding-left: 30px">
                                        <@wm.tourney.language division.language/>
                                    </td>
                                    <td>
                                        <@wm.tourney.section division.section/>
                                    </td>
                                    <td>
                                        <@wm.tourney.round round.id, true, round.final/>
                                    </td>
                                    <td>
                                        <@wm.tourney.group group.id , true/>
                                    </td>

                                    <td align="left">
                                        <#list group.players as p>
                                            <#if p != pid>
                                                <div>
                                                    <@wm.player.name personalityManager.getMember(p)/>
                                                </div>
                                            </#if>
                                        </#list>
                                    </td>
                                    <td align="left">
                                        <#list group.players as p>
                                            <#if p != pid>
                                                <div>
                                                    <#assign gid=group.getGameId(pid, p)/>
                                                    <#assign success=group.getPlayerSuccess(pid, p)!""/>
                                                    <a <#if success?has_content>class="game-finished" </#if>
                                                       href="/playground/scribble/board?b=${gid}">#${gid}</a>
                                                    <#if success?has_content>
                                                        (<@wm.tourney.resolution success false/>)
                                                    </#if>
                                                </div>
                                            </#if>
                                        </#list>
                                    </td>
                                </tr>
                                </#list>
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
    wm.ui.dataTable('#participated table', {
                "fnDrawCallback": wm.ui.table.groupColumnDrawCallback('#participated'),
                "aoColumnDefs": [
                    { "bVisible": false, "aTargets": [ 0 ] }
                ],
                "aaSortingFixed": [
                    [ 0, 'asc' ]
                ],
                "bSortClasses": false,
                "aoColumns": [
                    { "bSortable": true },
                    { "bSortable": true },
                    { "bSortable": true },
                    { "bSortable": true },
                    { "bSortable": true },
                    { "bSortable": false },
                    { "bSortable": false }
                ]
            }
    );
</script>