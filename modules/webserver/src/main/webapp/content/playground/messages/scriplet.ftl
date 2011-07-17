<#include "/core.ftl">

<script type="text/javascript">
    wm.messages = $.extend({}, wm.messages, new function() {
        var overlayCSS = {
            '-moz-border-radius': '5px',
            '-webkit-border-radius': '5px',
            'border-radius': '5px',
            backgroundColor:'#DFEFFC'
        };

        var sendRequest = function(id, replay) {
            var dlg = $("#privateMessageDialog");
            dlg.html('<div class="loading-image" style="height: 350px"></div>');
            dlg.load("/playground/messages/create?dialog=true&pid=" + id + "&replay=" + replay, function() {
                $("#sendPrivateMessage").button("enable");
            });
            dlg.dialog({
                title: "Send Private Message",
                width: 550,
                minHeight : 350,
                modal: true,
                resizable: false,
                buttons: [
                    {
                        id: "sendPrivateMessage",
                        text: "Send message",
                        disabled: true,
                        click:function() {
                            wm.ui.showStatus("Sending message. Please wait...", false, true);
                            dlg.closest(".ui-dialog").block({ message: "", overlayCSS: overlayCSS });

                            var msg = $("#privateMessageDialog textarea").val();
                            $.post('/playground/messages/send.ajax', $.toJSON({pid: id, replay: replay, message: msg}), function(result) {
                                dlg.closest(".ui-dialog").unblock();
                                if (result.success) {
                                    wm.ui.showStatus("Message has been sent");
                                    dlg.dialog("close");
                                } else {
                                    wm.ui.showStatus(result.summary, true);
                                }
                            });
                        }
                    },
                    {
                        text: "Cancel",
                        click: function() {
                            dlg.dialog("close");
                        }

                    }
                ]
            });
        };

        this.replay = function(original) {
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

<#macro replayMessage pid>
<a href="#" onclick="wm.messages.replay(${pid})"><#nested/></a>
</#macro>