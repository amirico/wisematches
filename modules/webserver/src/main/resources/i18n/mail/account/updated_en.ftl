<#-- @ftlvariable name="recoveryToken" type="java.lang.String" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="WiseMatches Account Assistance">

<p>Your WiseMatches account information was successfully updated.</p>

<p>
    The WiseMatches Team
</p>

<br>
<p>
    If you didn't updated WiseMatches account and don't recognize this message, it means someone
    forced your a account at WiseMatches. We recommend contacting a
    WiseMatches Support Team by email: <@mail.mailto box="account-support"/>
</p>

</@mail.html>