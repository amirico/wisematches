<#-- @ftlvariable name="context" type="wisematches.playground.message.impl.HibernateMessage" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="You have got new message">

<p>Вы получили новое сообщение <#if context.sender !=0>от пользователя <@mail.player pid=context.sender/></#if>
</p>

---------
<p>${context.text}</p>
---------
<br>

<p>Пожалуйста обратите внимания что у вас может быть более одного нового сообщения</p>
</@mail.html>