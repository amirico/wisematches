<#-- @ftlvariable name="context" type="wisematches.server.services.dictionary.ChangeSuggestion" -->
<#import "../../utils.ftl" as util>

<p>
    Пожалуйста, обратите внимание, что выш запрос на изменение словаря для слова <strong>${context.word}</strong> был
    отклонен.
</p>
<#if context.commentary?has_content>
<p>
    Причина отклонения: ${context.commentary}
</p>
</#if>