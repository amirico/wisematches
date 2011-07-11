<#include "/core.ftl">
<#-- @ftlvariable name="recipient" type="wisematches.personality.player.Player" -->

<div id="privateMessageDialog" class="ui-helper-hidden">
    <form action="/playboard/messages/send" method="POST">
        <div class="ui-layout-table">
            <div>
                <div>
                    To:
                </div>
                <div>
                <@wm.player player=recipient/>
                </div>
            </div>
            <div>
                <div>
                    Message:
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