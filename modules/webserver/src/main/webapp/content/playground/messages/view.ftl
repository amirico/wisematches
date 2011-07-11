<#-- @ftlvariable name="messages" type="java.util.Collection<wisematches.playground.message.Message>" -->
<#include "/core.ftl">

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
        <th>Date/Actions</th>
        <th width="100%">Message</th>
    </tr>
    </thead>
    <tbody>
        <#list messages as m>
        <tr>
            <td valign="top"><input type="checkbox" name="m${m.id}"></td>
            <td valign="top">
            ${m.created!""}
            </td>
            <td valign="top">
                From: ${m.sender} at ${m.created?string}
                <hr>
            ${m.body!""}
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
