<#-- @ftlvariable name="requests" type="wisematches.playground.tournament.TournamentSubscription[]"-->
<#-- @ftlvariable name="announcement" type="wisematches.playground.tournament.TournamentAnnouncement" -->
<#include "/core.ftl">

<style type="text/css">
    .data-table-toolbar div, .data-table-toolbar a {
        white-space: nowrap;
    }

    #tournaments td, #tournaments th {
        white-space: nowrap;
    }
</style>

<@wm.jstable/>

<@wm.playground id="tournamenstWidget">
    <@wm.dtHeader>
    <div style="text-align: right">
        <div style="float: left; display: inline-block;">
            Tournament > My Active
        </div>
        <div style="display: inline-block; text-align: right">
            <a href="/info/tournament" style="white-space: nowrap;">Tournament Rules</a>
        </div>
    </div>
    </@wm.dtHeader>

    <@wm.dtToolbar>
    <div style="float: left;">
        <a href="/playground/tournament/subscription?a=${announcement.number}">
            Subscribe to ${announcement.number}st WiseMatches Tournament
        </a>
    </div>
    <div>
        <a href="/playground/tournament/active">Active Tournaments</a>
        <a href="/playground/tournament/history">Completed Tournaments</a>
    </div>
    </@wm.dtToolbar>

    <@wm.dtContent>
    <table id="tournaments" width="100%" class="display">
        <thead>
        <tr>
            <th width="100%">Tournament Name</th>
            <th>Start Date</th>
            <th>Section</th>
            <th>Active Round</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><a href="/playground/tournament/view?t=">1st WiseMatches Tournament</a></td>
            <td>17th Aug 2012</td>
            <td>Casual (rating < 1300)</td>
            <td><a href="/playground/tournament/round?t=&r=">Round 3</a></td>
        </tr>
        </tbody>
    </table>
    </@wm.dtContent>

    <@wm.dtFooter>
    The ${announcement.number}th WiseMatches
    Tournament is going to be started on ${gameMessageSource.formatDate(announcement.scheduledDate, locale)}
        <#if !requests?? || requests?size == 0>
        You are <a href="/playground/tournament/subscription?a=${announcement.number}">not subscribed</a> to the
        tournament.
        <#else>
            <#assign request=requests[0]/>
        You are already <a href="/playground/tournament/subscription?a=${request.tournament}">subscribed</a>
        to '${request.section}' section on '${request.language}' language.
        </#if>
    </@wm.dtFooter>
</@wm.playground>

<script type="text/javascript">
    wm.ui.dataTable('#tournaments', {
        "bSortClasses":false,
        "aoColumns":[
            { "bSortable":true },
            { "bSortable":true },
            { "bSortable":true },
            { "bSortable":false }
        ]
    });
</script>