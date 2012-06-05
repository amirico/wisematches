<#include "/core.ftl">

<link rel="stylesheet" type="text/css" href="/jquery/css/table_jui.css"/>

<@wm.playground id="tournamentWidget">
    <@wm.dtHeader>
    Tournament > Subscription
    </@wm.dtHeader>

    <@wm.dtToolbar>
    <a href="/info/tournament" style="white-space: nowrap;">Tournament Rules</a>
    </@wm.dtToolbar>

    <@wm.dtContent>
    <div class="data-table-content ui-widget-content">
        This is content
    </div>
    </@wm.dtContent>

    <@wm.dtFooter>
    If at the time the tournament starts, your top rating (over the past 90 days) exceeded the rating
    limit for the section you have signed up for, you will be automatically moved to the
    next higher section.
    </@wm.dtFooter>
</@wm.playground>
