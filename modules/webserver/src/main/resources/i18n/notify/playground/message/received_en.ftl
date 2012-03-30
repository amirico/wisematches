<#-- @ftlvariable name="context" type="wisematches.playground.message.impl.HibernateMessage" -->
<#import "../utils.ftl" as notify>

<p>
    You have got new message <#if context.sender !=0>from user <@notify.player player=context.sender/></#if>
</p>

---------
<p>${context.text}</p>
---------
<br>

<p>Please note that you can have more than one new message</p>