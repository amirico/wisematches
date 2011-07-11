<#-- @ftlvariable name="messages" type="java.util.Collection<wisematches.playground.message.Message>" -->
<#include "/core.ftl">


<style type="text/css">
    #messages tr .test {
        color: #cccccc;
        text-decoration: underline;
    }

    #messages tr:hover .test {
        color: inherit;
        text-decoration: underline;
    }
</style>

<@wm.jstable/>

<@wm.playground id="messagesWidget">
<div>
    <div style="float: left;">
        <button style="margin-left: 0">
            Delete Selected
        </button>
    </div>

    <div style="float: right;">
        <a href="/playground/messages/sent">Sent messages</a>
        •
        <a href="/playground/players/ignores">My ignore list</a>
    </div>
</div>

<table id="messages" width="100%" class="display">
    <thead>
    <tr>
        <th><input title="select all messages" type="checkbox" name="all"></th>
        <th>Date</th>
        <th width="100%">Message</th>
    </tr>
    </thead>
    <tbody>
        <#list messages as m>
        <tr>
            <td valign="top">
                <input type="checkbox" name="m${m.id}">
            </td>
            <td valign="top">
                <div style="white-space: nowrap;">${gameMessageSource.formatDate(m.created, locale)}</div>
                <div style="white-space: nowrap;">${gameMessageSource.formatTime(m.created, locale)}</div>
            </td>
            <td valign="top" width="100%">
                <#if m.sender != 0>
                    <div class="ui-state-default" style="background: none; border-width: 0; border-bottom-width: 2px">
                        From: <@wm.player player=playerManager.getPlayer(m.sender)/>
                    </div>
                </#if>
                <div>
                ${m.text}
                </div>

                <div class="ui-state-default" style="background: none; border: none;">
                    <div style="float: right;">
                        <a class="test" href="">Replay</a>
                        <a class="test" href="">Ignore</a>
                        <a class="test" href="">Delete</a>
                    </div>
                    <div>
                        <a class="test" href="">Previous Message</a>
                    </div>
                </div>
            </td>
        </tr>
        </#list>
    </tbody>
</table>

<div>
    <button style="margin-left: 0">
        Delete Selected
    </button>
</div>
</@wm.playground>

<script type="text/javascript">
    $("#messagesWidget button").button();

    $('#messages').dataTable({
        "bJQueryUI": true,
        "bSortClasses": false,
        "aoColumns": [
            { "bSortable": false },
            { "bSortable": false },
            { "bSortable": false }
        ],
        "sPaginationType": "full_numbers"
    });
</script>
