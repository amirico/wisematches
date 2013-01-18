<#-- @ftlvariable name="context" type="wisematches.server.web.services.dictionary.ChangeSuggestion" -->
<#import "../../utils.ftl" as util>

<p>
    Ваше изменение в словаре было принято. Слово <strong>${context.word}</strong> было
<#if context.suggestionType == "ADD">добавлено в словарь<#elseif context.suggestionType == "REMOVE">удалено из
    словаря<#else>обновлено</#if>.
</p>
<p>
    Спасибо за помощь в редактирование словаря.
</p>