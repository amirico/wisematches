<#-- @ftlvariable name="original" type="wisematches.playground.message.Message" -->
<#-- @ftlvariable name="recipient" type="wisematches.personality.player.Player" -->
<#include "/core.ftl">

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

        <@wm.field path="form.pid">
            <#if recipient??><@wm.player player=recipient/></#if>
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
        <@wm.field path="form.message">
            <textarea name="message"
                      style="width: 100%;" rows="10">${spring.stringStatusValue}</textarea>
        </@wm.field>
        </div>
    </div>
<#if original??>
    <div>
        <div></div>
        <div style="font-style: italic;">
            This is replay to the message of <@wm.player player=playerManager.getPlayer(original.sender)/> received
        ${gameMessageSource.formatDate(original.created, locale)} ${gameMessageSource.formatTime(original.created, locale)}
        </div>
    </div>
</#if>
</div>