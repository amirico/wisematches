<#include "/core.ftl">

<script type="text/javascript">
    wm.messages = $.extend({}, wm.messages, new function() {
        var sendRequest = function(id, reply) {
            var dlg = $("#privateMessageDialog");
            dlg.html('<div class="loading-image" style="height: 350px"></div>');
            dlg.load("/playground/messages/create?dialog=true&pid=" + id + "&reply=" + reply, function() {
                $("#sendPrivateMessage").button("enable");
            });
            dlg.dialog({
                title: "<@message code="messages.send.label"/>",
                width: 550,
                minHeight : 350,
                modal: true,
                resizable: false,
                buttons: [
                    {
                        id: "sendPrivateMessage",
                        text: "<@message code="messages.send.label"/>",
                        disabled: true,
                        click:function() {
                            wm.ui.showStatus("<@message code="messages.status.sending"/>", false, true);
                            dlg.closest(".ui-dialog").block({ message: ""});

                            var msg = $("#privateMessageDialog textarea").val();
                            $.post('/playground/messages/send.ajax', $.toJSON({pid: id, reply: reply, message: msg}), function(result) {
                                dlg.closest(".ui-dialog").unblock();
                                if (result.success) {
                                    wm.ui.showStatus("<@message code="messages.status.sent"/>");
                                    dlg.dialog("close");
                                } else {
                                    wm.ui.showStatus(result.summary, true);
                                }
                            });
                        }
                    },
                    {
                        text: "<@message code="button.cancel"/>",
                        click: function() {
                            dlg.dialog("close");
                        }

                    }
                ]
            });
        };

        this.reply = function(original) {
            sendRequest(original, true);
            return false;
        };

        this.create = function(pid) {
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