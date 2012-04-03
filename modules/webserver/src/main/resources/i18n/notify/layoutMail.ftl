<#-- @ftlvariable name="template" type="java.lang.String" -->

${gameMessageSource.getMessage("notify.mail.header", locale)} <b>${principal.nickname}</b>.

<#include "${template?replace('.', '/')}.ftl">

<p>
<hr><br>
${gameMessageSource.getMessage("notify.mail.footer", locale)}
</p>