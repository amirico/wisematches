<#include "/core.ftl">

<#macro sendMessage player>
<a href="#">Send private message</a>
</#macro>

<div id="privateMessageDialog" class="ui-helper-hidden">
    <form action="/playboard/messages/send">
        <div class="ui-layout-table">
            <div>
                <div>To:</div>
                <div>Message:</div>
            </div>
            <div>
                <div>
                    asdasd
                </div>
                <div>
                    <textarea id="msg_text" wrap="soft">
                        Hello, asdasd,
                    </textarea>
                </div>
            </div>
        </div>
    </form>
</div>