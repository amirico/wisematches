<#-- @ftlvariable name="context.message" type="wisematches.server.services.message.impl.HibernateMessage" -->
<#import "../../utils.ftl" as notify>

<#if context.message.notification>
<p>${context.message.text}</p>
<#else>
<p>
    You have got new message <#if context.message.sender !=0>from
    user <@notify.player player=context.message.sender/></#if>:
</p>
<blockquote>${context.message.text}</blockquote>
</#if>
<br>
<p>Please note that you can have more than one new message</p>