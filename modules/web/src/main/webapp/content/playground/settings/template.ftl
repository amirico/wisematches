<#include "/core.ftl">

<form action="/account/modify/save" method="post">
    <div class="profile">
        <div id="settings" class="shadow">
            <ul class="settings-tabs">
                <li><a href="#commonTab"><@message code="account.modify.common.label"/></a></li>
                <li><a href="#notificationsTab"><@message code="account.modify.notice.label"/></a></li>
                <li><a href="#boardTab"><@message code="account.modify.board.label"/></a></li>
                <li><a href="#socialTab"><@message code="account.modify.social.label"/></a></li>
            </ul>

            <div id="commonTab" class="ui-state-default" style="background-image: none">
            <#include "common.ftl"/>
            </div>
            <div id="notificationsTab" class="ui-state-default" style="background-image: none">
            <#include "notifications.ftl"/>
            </div>
            <div id="boardTab" class="ui-state-default" style="background-image: none">
            <#include "board.ftl"/>
            </div>
            <div id="socialTab" class="ui-state-default" style="background-image: none">
            <#include "social.ftl"/>
            </div>

            <div class="ui-tabs-panel">
            <@wm.ui.field path="settings.openedTab">
                <input type="hidden" id="openedTab" name="openedTab"/>
            </@wm.ui.field>
                <button name="save" type="submit" value="submit"><@message code="account.modify.save"/></button>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">
    $("#openedTab").val(location.hash.substring(1));

    $("#settings").tabs().bind("tabsselect", function (event, ui) {
        location.hash = ui.tab.hash;
        $("#openedTab").val(ui.tab.hash.substring(1));
    });
</script>

