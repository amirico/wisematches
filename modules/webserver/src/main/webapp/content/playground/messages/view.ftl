<#-- @ftlvariable name="messages" type="java.util.Collection<wisematches.playground.message.Message>" -->
<#include "/core.ftl">

<@wm.jstable/>

<table id="messagesWidget" width="100%">
    <tr>
        <td width="160px" valign="top">
        <#include "/content/templates/advertisement.ftl">
        </td>
        <td valign="top">
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
            a

            <div>
                <button style="margin-left: 0">
                    Delete Selected
                </button>
            </div>
        </td>
        <td width="160px" valign="top"></td>
    </tr>
</table>

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
