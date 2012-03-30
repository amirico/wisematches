<#-- @ftlvariable name="context" type="wisematches.playground.message.impl.HibernateMessage" -->
<#import "../utils.ftl" as notify>

<p>
    Вы получили новое сообщение <#if context.sender !=0>от пользователя <@notify.player player=context.sender/></#if>
</p>

---------
<p>${context.text}</p>
---------
<br>

<p>Пожалуйста обратите внимания что у вас может быть более одного нового сообщения</p>