<#include "/core.ftl">

<script type="text/javascript">
    wm.messages = new function() {
        var overlayCSS = {
            '-moz-border-radius': '5px',
            '-webkit-border-radius': '5px',
            'border-radius': '5px',
            backgroundColor:'#DFEFFC'
        };

//        var execute = function(pid, comment, callback) {
        /*
                    wm.ui.showStatus("<@message code="blacklist.status.adding"/>");
            $.post('/playground/blacklist/add.ajax', $.toJSON({person: pid, comment: comment}), function(result) {
                if (result.success) {
                    wm.ui.showStatus("<@message code="blacklist.status.added"/>");
                    if (callback != undefined) {
                        callback(pid);
                    }
                } else {
                    wm.ui.showStatus(result.summary, true);
                }
            });
*/
//        };

        this.send = function(pid) {
            var dlg = $("#privateMessageDialog");
            dlg.html('<div class="loading-image"></div>');
            dlg.load("/playground/messages/create?dialog=true&p=" + pid, function() {
                $("#sendPrivateMessage").button("enable");
            });
            dlg.dialog({
                title: "Send Private Message",
                width: 500,
                height: 350,
                modal: true,
                resizable: false,
                buttons: [
                    {
                        id: "sendPrivateMessage",
                        text: "Send message",
                        disabled: true,
                        click:function() {
                            wm.ui.showStatus("Sending message. Please wait...", false, true)
                            dlg.closest(".ui-dialog").block({ message: "", overlayCSS: overlayCSS });
                            $.post('/playground/messages/send2.ajax', $.toJSON({person: pid, message: $("#privateMessageDialog textarea").val()}), function(result) {
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
            return false;
        };
    }
</script>

<div id="privateMessageDialog" class='private-message'></div>

<#macro privateMessage pid>
<a title="Send private message to the player" href="#" onclick="wm.messages.send(${pid})">Send private message</a>
</#macro>