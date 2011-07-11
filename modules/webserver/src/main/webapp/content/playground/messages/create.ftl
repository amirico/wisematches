<#include "/core.ftl">
<#-- @ftlvariable name="recipient" type="wisematches.personality.player.Player" -->

<@wm.playground id="privateMessageDialog">
<div class="ui-widget-header ui-corner-all shadow">
    Send a private message
</div>
<div class="ui-widget-content ui-corner-all shadow" style="margin: 0">
    <div class="info-description">
        Please note that in case of abuse you can be blocked or removed.
    </div>

    <form action="/playground/messages/send" method="POST">
        <div class="ui-layout-table" style="width: 100%">
            <div>
                <div style="vertical-align: top; padding-right: 5px">
                    To:
                </div>
                <div style="width: 100%; padding-right: 40px">
                <@wm.player player=recipient/>

                <@wm.field path="form.msgRecipient">
                    <input type="hidden" name="msgRecipient" id="msgRecipient" value="${recipient.id}">
                <#--<input type="checkbox" id="rememberMe" name="rememberMe" value="true"-->
                <#--<#if spring.stringStatusValue=="true">checked="checked"</#if>/>-->
                </@wm.field>
                </div>
            </div>
            <div>
                <div style="vertical-align: top; padding-right: 5px">
                    Message:
                </div>
                <div style="padding-right: 40px">
                <@wm.field path="form.msgText">
                    <textarea id="msgText" name="msgText"
                              style="width: 100%; height: 300px">${spring.stringStatusValue}</textarea>
                </@wm.field>
                </div>
            </div>
            <div>
                <div></div>
                <div>
                    <button style="margin-left: 0" type="submit">Sent message</button>
                </div>
            </div>
        </div>
    </form>
</div>
</@wm.playground>