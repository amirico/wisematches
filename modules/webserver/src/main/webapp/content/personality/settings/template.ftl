<form>
    <div class="profile shadow ui-state-default">
        <div id="settings">
            <ul class="settings-tabs">
                <li><a href="#commonTab">Common</a></li>
                <li><a href="#notificationsTab">Notifications</a></li>
            </ul>
            <div id="commonTab">
            <#include "common.ftl"/>
            </div>
            <div id="notificationsTab">
            <#include "notifications.ftl"/>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">
    $("#settings").tabs();
</script>
