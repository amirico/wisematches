<#-- @ftlvariable name="context" type="wisematches.server.web.services.dictionary.ChangeSuggestion" -->
<#import "../../utils.ftl" as util>

<p>
    Your change suggestion has been accepted. Word <strong>${context.word}</strong> has been
<#if context.suggestionType == "ADD">added to the dictionary<#elseif context.suggestionType == "REMOVE">removed from the
    dictionary<#else>updated</#if>.
</p>
<p>
    Thanks a lot for help with make our dictionary up to date.
</p>