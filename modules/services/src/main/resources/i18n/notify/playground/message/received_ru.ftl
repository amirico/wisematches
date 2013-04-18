<#-- @ftlvariable name="context.sender" type="wisematches.core.Personality" -->
<#-- @ftlvariable name="context.message" type="wisematches.server.services.message.impl.HibernateMessage" -->
<#import "../../utils.ftl" as notify>

<#if context.message.notification>
<p>${context.message.text}</p>
<#else>
<p>
    Вы получили новое сообщение <#if context.sender??>от пользователя <@notify.player context.sender/></#if>:
</p>

<blockquote>${context.message.text}</blockquote>
</#if>

<br>

<p>Пожалуйста обратите внимания что у вас может быть более одного нового сообщения</p>