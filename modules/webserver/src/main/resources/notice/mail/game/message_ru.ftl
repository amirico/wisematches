<#-- @ftlvariable name="context" type="wisematches.playground.message.impl.HibernateMessage" -->
<#import "/notice/macro.ftl" as notice>

<@notice.html subject="You have got new message">

<p>Вы получили новое сообщение <#if context.senderName !=0>от
    пользователя <@notice.player pid=context.senderName/></#if>
</p>

---------
<p>${context.text}</p>
---------
<br>

<p>Пожалуйста обратите внимания что у вас может быть более одного нового сообщения</p>
</@notice.html>