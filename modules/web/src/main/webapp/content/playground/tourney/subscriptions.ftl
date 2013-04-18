<#-- @ftlvariable name="announce" type="wisematches.playground.tourney.regular.Tourney" -->

<#-- @ftlvariable name="tourney" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="tourneyLanguage" type="wisematches.core.Language" -->
<#-- @ftlvariable name="tourneySubscriptions" type="wisematches.playground.tourney.regular.RegistrationRecord[]" -->

<#include "/core.ftl">

<@wm.ui.table.dtinit/>

<@wm.ui.playground id="tourneyWidget">
<table id="tourney" width="100%">
    <tr>
        <td width="100%" valign="top">
            <div id="subscriptions">
                <@wm.ui.table.header align="left">
                    <@message code="tourney.tourney.label"/> > <@message code="tourney.subscribe.label"/>
                </@wm.ui.table.header>

                <@wm.ui.table.toolbar align="left">
                    <div class="ui-state-hover" style="border: none; background: none; font-size: large">
                        <@message code="tourney.subscribes.tourney"/> <@wm.tourney.tourney tourney.id, false/>
                    </div>
                    <div>
                        <@message code="tourney.subscribes.players"/>: <@wm.tourney.language tourneyLanguage/>
                    </div>
                </@wm.ui.table.toolbar>

                <@wm.ui.table.content>
                    <table width="100%" class="display">
                        <thead>
                        <tr>
                            <th><@message code="tourney.player.label"/></th>
                            <th><@message code="tourney.level.label"/></th>
                            <th><@message code="tourney.round.label"/></th>
                            <th width="100%"></th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list tourneySubscriptions as s>
                            <tr>
                                <td><@wm.player.name personalityManager.getMember(s.player)/></td>
                                <td><@wm.tourney.section s.section/></td>
                                <td><@message code="tourney.round.label"/> ${s.round?string}</td>
                                <td>&nbsp;</td>
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
    wm.ui.dataTable('#tourney #subscriptions table', {
        "bSortClasses": false,
        "aoColumns": [
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": false }
        ]
    });
</script>