<#-- @ftlvariable name="recoveryToken" type="java.lang.String" -->
<#-- @ftlvariable name="confirmationUrl" type="java.lang.String" -->
<#import "../utils.ftl" as util>

<p>
    To initiate the process for resetting the password for your ${principal.nickname} WiseMatches Account, visit the
    link
    below
</p>

<p>
<@util.link href='${confirmationUrl}?language=${principal.language.code()}&token=${recoveryToken}'/>
</p>
<p>
    If clicking on the link above does not work, copy and paste the URL in a new browser window instead. Thank you for
    using WiseMatches.
</p>

<br>
<p>
    If you didn't initiate recovery WiseMatches password and don't recognize this message, it means someone tried
    to force your a account at WiseMatches. We recommend contacting a
    WiseMatches Support Team by email: <@util.mailto box="account-support"/>
</p>