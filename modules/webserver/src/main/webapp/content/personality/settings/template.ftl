<#include "/core.ftl">

<form action="/account/modify/save" method="post">
    <div class="profile">
        <div id="settings" class="shadow">
            <ul class="settings-tabs">
                <li><a href="#commonTab"><@message code="account.modify.common.label"/></a></li>
                <li><a href="#notificationsTab"><@message code="account.modify.notice.label"/></a></li>
            </ul>
            <div id="commonTab" class="ui-state-default">
            <#include "common.ftl"/>
            </div>
            <div id="notificationsTab" class="ui-state-default">
            <#include "notifications.ftl"/>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">
    $("#settings").tabs();
</script>
