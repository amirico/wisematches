<#include "/core.ftl">
<#-- @ftlvariable name="recipient" type="wisematches.personality.player.Player" -->


<div>
    Please enter a message text for the player below. Please keep in mind that in case of abuse or spam your
    account can be blocked.
</div>

<div class="ui-layout-table" style="width: 100%">
    <div>
        <div style="vertical-align: top; padding-right: 5px">
        <@message code="messages.to.label"/>:
        </div>
        <div style="width: 100%; padding-right: 40px">
        <#if recipient??><@wm.player player=recipient/></#if>

        <@wm.field path="form.msgRecipient">
            <#if recipient??>
                <input type="hidden" name="msgRecipient" id="msgRecipient" value="${recipient.id}">
            </#if>
        </@wm.field>
        </div>
    </div>
    <div>
        <div style="vertical-align: top; padding-right: 5px">
        <@message code="messages.text.label"/>:
        </div>
        <div style="padding-right: 40px">
        <@wm.field path="form.msgText">
            <textarea id="msgText" name="msgText"
                      style="width: 100%;" rows="10">${spring.stringStatusValue}</textarea>
        </@wm.field>
        </div>
    </div>
</div>



<#--
<@wm.playground id="privateMessageDialog">
<div class="ui-widget-header ui-corner-all shadow"><@message code="messages.label"/></div>
<div class="ui-widget-content ui-corner-all shadow" style="margin: 0">
    <div class="info-description"><@message code="messages.description"/></div>

    <form action="/playground/messages/send" method="POST">
        <div class="ui-layout-table" style="width: 100%">
            <div>
                <div style="vertical-align: top; padding-right: 5px">
                <@message code="messages.to.label"/>:
                </div>
                <div style="width: 100%; padding-right: 40px">
                    <#if recipient??><@wm.player player=recipient/></#if>

                <@wm.field path="form.msgRecipient">
                    <#if recipient??>
                        <input type="hidden" name="msgRecipient" id="msgRecipient" value="${recipient.id}">
                    </#if>
                </@wm.field>
                </div>
            </div>
            <div>
                <div style="vertical-align: top; padding-right: 5px">
                <@message code="messages.text.label"/>:
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
                    <button style="margin-left: 0" type="submit"><@message code="messages.send.label"/></button>
                </div>
            </div>
        </div>
    </form>
</div>
</@wm.playground>-->
