<#-- @ftlvariable name="context" type="wisematches.playground.message.Message" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="You have got new message">

<p>You have got new message <#if context.sender !=0>from user <@mail.player pid=context.sender/></#if>
</p>

---------
<p><strong>${context.subject}</strong></p>
<p>${context.body}</p>
---------
<br>

<p>Please note that you can have more than one new message</p>
</@mail.html>