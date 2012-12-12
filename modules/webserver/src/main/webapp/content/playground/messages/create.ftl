<#-- @ftlvariable name="original" type="wisematches.playground.message.impl.HibernateMessage" -->
<#-- @ftlvariable name="recipient" type="wisematches.personality.player.Player" -->
<#include "/core.ftl">

<div><@message code="messages.description"/></div>

<div class="ui-layout-table" style="width: 100%">
    <div>
        <div style="vertical-align: top; padding-right: 5px">
        <@message code="messages.to.label"/>:
        </div>
        <div style="width: 100%; padding-right: 40px">

        <@wm.ui.field path="form.pid">
            <#if recipient??><@wm.player.name player=recipient/></#if>
            <#if recipient??>
                <input type="hidden" name="msgRecipient" id="msgRecipient" value="${recipient.id}">
            </#if>
        </@wm.ui.field>
        </div>
    </div>
    <div>
        <div style="vertical-align: top; padding-right: 5px">
        <@message code="messages.text.label"/>:
        </div>
        <div style="padding-right: 40px">
        <@wm.ui.field path="form.message">
            <textarea name="message"
                      style="width: 100%;" rows="10">${wm.ui.statusValue}</textarea>
        </@wm.ui.field>
        </div>
    </div>
<#--
<#if original??>
    <div>
        <div></div>
        <div style="font-style: italic;">
            This is reply to the message of <@wm.player.name player=playerManager.getPlayer(original.sender)/> received
        ${gameMessageSource.formatDate(original.created, locale)} ${gameMessageSource.formatTime(original.created, locale)}
        </div>
    </div>
</#if>
-->
</div>

<@wm.security.info code="messages.create.forbidden"/>