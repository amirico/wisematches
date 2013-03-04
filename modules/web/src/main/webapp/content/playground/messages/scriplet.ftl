<#include "/core.ftl">

<script type="text/javascript">
    wm.messages = $.extend({}, wm.messages, new function () {
        var sendRequest = function (id, reply) {
            var dlg = $("#privateMessageDialog");
            dlg.html('<div class="loading-image" style="height: 350px"></div>');
            dlg.load("/playground/messages/create?dialog=true&pid=" + id + "&reply=" + reply, function () {
                var restriction = dlg.find(".restriction");
                if (restriction.length != 0) {
                    $("#sendPrivateMessage").hide();
                } else {
                    $("#sendPrivateMessage").show().button("enable");
                }
            });
            dlg.dialog({
                title: "<@message code="messages.send.label"/>",
                width: 550,
                minHeight: 350,
                modal: true,
                resizable: false,
                buttons: [
                    {
                        id: "sendPrivateMessage",
                        text: "<@message code="messages.send.label"/>",
                        disabled: true,
                        click: function () {
                            var widget = dlg.closest(".ui-dialog");
                            wm.ui.lock(widget, "<@message code="messages.status.sending"/>");
                            var msg = $("#privateMessageDialog").find("textarea").val();
                            $.post('/playground/messages/send.ajax', JSON.stringify({pid: id, reply: reply, message: msg}), function (result) {
                                if (result.success) {
                                    wm.ui.unlock(widget, "<@message code="messages.status.sent"/>");
                                    dlg.dialog("close");
                                } else {
                                    wm.ui.unlock(widget, result.message, true);
                                }
                            });
                        }
                    },
                    {
                        text: "<@message code="button.cancel"/>",
                        click: function () {
                            dlg.dialog("close");
                        }

                    }
                ]
            });
        };

        this.reply = function (original) {
            sendRequest(original, true);
            return false;
        };

        this.create = function (pid) {
            sendRequest(pid, false);
            return false;
        };
    });
</script>

<div id="privateMessageDialog" class='private-message'></div>

<#macro privateMessage pid>
<a href="#" onclick="wm.messages.create(${pid})"><#nested/></a>
</#macro>

<#macro replyMessage pid>
<a href="#" onclick="wm.messages.reply(${pid})"><#nested/></a>
</#macro>