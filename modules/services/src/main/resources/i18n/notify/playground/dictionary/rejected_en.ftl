<#-- @ftlvariable name="context" type="wisematches.server.services.dictionary.WordSuggestion" -->
<#import "../../utils.ftl" as util>

<p>
    Please note that your change suggestion for word <strong>${context.word}</strong> has been rejected.
</p>
<#if context.commentary?has_content>
<p>
    Reject reason: ${context.commentary}
</p>
</#if>