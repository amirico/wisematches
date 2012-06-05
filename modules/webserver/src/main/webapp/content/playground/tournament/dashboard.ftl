<#-- @ftlvariable name="requests" type="wisematches.playground.tournament.upcoming.TournamentRequest[]"-->
<#-- @ftlvariable name="announcement" type="wisematches.playground.tournament.upcoming.TournamentAnnouncement" -->
<#include "/core.ftl">

<style type="text/css">
    #completedTournaments td, #completedTournaments th {
        white-space: nowrap;
    }
</style>

<@wm.jstable/>

<@wm.playground id="tournamentWidget">
    <@wm.dtHeader>
    <div style="text-align: right">
        <div style="float: left; display: inline-block;">
            Tournament > Dashboard
        </div>
        <div style="display: inline-block; text-align: right">
        </div>
    </div>
    </@wm.dtHeader>

    <@wm.dtToolbar>
    <a href="/info/tournament" style="white-space: nowrap;">Tournament Rules</a>
    </@wm.dtToolbar>


    <@wm.dtContent wrap=true>
    <div id="accordion" style="padding: 5px; width: auto;">
        <h3><a href="#" style="border: none">afdasdfasdf</a></h3>

        <div style="padding: 0; margin: 0">
            adsf adsf asdf
        </div>

        <h3><a href="#" style="border: none">afdasdfasdf</a></h3>

        <div style="padding: 0; margin: 0">
            <table id="completedTournaments" width="100%" class="display">
                <thead>
                <tr>
                    <th width="100%">Name</th>
                    <th>Start Date</th>
                    <th>Finish Date</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td><a href="/playground/tournament/view?t=1">1st WiseMatches Tournament</a></td>
                    <td>asdf asd</td>
                    <td>asfd 3q fasf</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    </@wm.dtContent>

<#--<div>-->
<#--<@wm.dtContent wrap=true>&nbsp;</@wm.dtContent>-->


<#--<@wm.dtContent wrap=true>-->
<#--<#if announcement??>-->
<#--${announcement.number}th WiseMatches Tournament-->
<#--${announcement.scheduledDate}-->
<#--<#if !requests?? || requests?size == 0>-->
<#--You are not subscribed. <a-->
<#--href="/playground/tournament/subscription?t=${announcement.number}&a=s">Subscribe-->
<#--Now</a>.-->
<#--<#else>-->
<#--You are not subscribed. <a-->
<#--href="/playground/tournament/subscription?t=${announcement.number}&a=u">Subscribe-->
<#--Now</a>.-->
<#--</#if>-->
<#--<#else>-->
<#--There is no active announcements yet. Please wait for a while and new tournament will be announced.-->
<#--We-->
<#--will send a notification to your email.-->
<#--</#if>-->
<#--</@wm.dtContent>-->

<#--<@wm.dtContent wrap=true>&nbsp;</@wm.dtContent>-->

<#--<@wm.dtToolbar align="left">-->
<#--Active Tournaments-->
<#--</@wm.dtToolbar>-->

<#--<@wm.dtContent wrap=true>-->
<#--asfsadfafd-->
<#--</@wm.dtContent>-->

<#--<@wm.dtContent wrap=true>&nbsp;</@wm.dtContent>-->

<#--<@wm.dtToolbar align="left">-->
<#--Completed Tournaments-->
<#--</@wm.dtToolbar>-->

<#--<@wm.dtContent>-->
<#--</@wm.dtContent>-->

<#--<@wm.dtContent wrap=true>&nbsp;</@wm.dtContent>-->
<#--</div>-->
    <@wm.dtStatusbar align="left">
    asdf asdf asdf
    </@wm.dtStatusbar>

    <@wm.dtFooter>
    This is footer
    </@wm.dtFooter>
</@wm.playground>


<#--
<#if announcement??>
<div>
    Announcement: ${announcement.number} ${announcement.scheduledDate}
</div>
</#if>
-->

<#--
<#if requests??>
    <#list requests as r>
    <div>
        Request: ${r.toString()}
    </div>
    </#list>
<#else>
<div>
    asdf asdf
</div>
</#if>-->

<script type="text/javascript">
    $(function () {
        $("#accordion").accordion();
    });

    wm.ui.dataTable('#completedTournaments', {
        "bSortClasses":false,
        "aoColumns":[
            { "bSortable":true },
            { "bSortable":true },
            { "bSortable":true }
        ]
    });
</script>