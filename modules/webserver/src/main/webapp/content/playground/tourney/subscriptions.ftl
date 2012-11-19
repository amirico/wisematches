<#-- @ftlvariable name="announce" type="wisematches.playground.tourney.regular.Tourney" -->

<#-- @ftlvariable name="tourney" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="tourneyLanguage" type="wisematches.personality.Language" -->
<#-- @ftlvariable name="tourneySubscriptions" type="wisematches.playground.tourney.regular.TourneySubscription[]" -->
<#-- @ftlvariable name="languages" type="wisematches.personality.Language[]" -->

<#include "/core.ftl">
<#include "scriplet.ftl">

<@wm.jstable/>

<@wm.playground id="tourneyWidget">
<table id="tourney" width="100%">
    <tr>
        <td width="100%" valign="top">
            <div id="subscriptions">
                <@wm.dtHeader align="left">
                    <@message code="tourney.subscriptions.label"/>
                </@wm.dtHeader>

                <@wm.dtToolbar align="left">
                    <div class="ui-state-hover" style="border: none; background: none; font-size: large">
                        <@message code="tourney.subscribes.tourney"/> <@tourneyName tourneyId=tourney.id link=false/>
                    </div>
                    <div>
                        <@message code="tourney.subscribes.players"/>: <@languageName language=tourneyLanguage/>
                    </div>
                </@wm.dtToolbar>

                <@wm.dtContent>
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
                                <td><@wm.player player=playerManager.getPlayer(s.player)/></td>
                                <td><@sectionName section=s.section/></td>
                                <td><@message code="tourney.round.label"/> ${s.round?string}</td>
                                <td>&nbsp;</td>
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