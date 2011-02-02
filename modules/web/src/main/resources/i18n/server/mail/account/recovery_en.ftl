<#-- @ftlvariable name="recoveryToken" type="java.lang.String" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="WiseMatches Account Assistance">
<p>
    To initiate the process for resetting the password for your ${player.nickname} WiseMatches Account, visit the link
    below
</p>

<p>
<@mail.url path='account/validation.html?language=${player.language.code()}&action=recovery&token=${recoveryToken}'/>
</p>
<p>
    If clicking on the link above does not work, copy and paste the URL in a new browser window instead. Thank you for
    using WiseMatches.
</p>

</@mail.html>