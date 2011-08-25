<#-- @ftlvariable name="context" type="wisematches.playground.message.impl.HibernateMessage" -->
<#import "/notice/macro.ftl" as notice>

<@notice.html subject="You have got new message">

<p>You have got new message <#if context.senderName !=0>from user <@notice.player pid=context.senderName/></#if>
</p>

---------
<p>${context.text}</p>
---------
<br>

<p>Please note that you can have more than one new message</p>
</@notice.html>