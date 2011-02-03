<#-- @ftlvariable name="recoveryToken" type="java.lang.String" -->
<#-- @ftlvariable name="confirmationUrl" type="java.lang.String" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="WiseMatches Account Assistance">
<p>
    To initiate the process for resetting the password for your ${player.nickname} WiseMatches Account, visit the link
    below
</p>

<p>
<@mail.url path='${confirmationUrl}?language=${player.language.code()}&token=${recoveryToken}'/>
</p>
<p>
    If clicking on the link above does not work, copy and paste the URL in a new browser window instead. Thank you for
    using WiseMatches.
</p>

<br>
<p>
    If you didn't initiate recovery WiseMatches passwrod and don't recognize this message, it means someone tried
    to force your a account at WiseMatches. We recommend contacting a
    WiseMatches Support Team by email: <@mail.mailto box="account-support"/>
</p>

</@mail.html>