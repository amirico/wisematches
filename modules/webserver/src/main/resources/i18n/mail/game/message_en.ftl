<#-- @ftlvariable name="context" type="wisematches.playground.message.impl.HibernateMessage" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="You have got new message">

<p>You have got new message <#if context.sender !=0>from user <@mail.player pid=context.sender/></#if>
</p>

---------
<p>${context.text}</p>
---------
<br>

<p>Please note that you can have more than one new message</p>
</@mail.html>