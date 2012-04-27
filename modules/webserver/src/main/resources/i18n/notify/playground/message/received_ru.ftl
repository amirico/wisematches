<#-- @ftlvariable name="context.message" type="wisematches.playground.message.impl.HibernateMessage" -->
<#import "../../utils.ftl" as notify>

<#if context.message.notification>
<p>${context.message.text}</p>
<#else>
<p>
    Вы получили новое сообщение <#if context.message.sender !=0>от пользователя <@notify.player player=context.message.sender/></#if>:
</p>

<blockquote>${context.message.text}</blockquote>
</#if>

<br>

<p>Пожалуйста обратите внимания что у вас может быть более одного нового сообщения</p>