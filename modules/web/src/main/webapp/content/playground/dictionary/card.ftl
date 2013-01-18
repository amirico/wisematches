<#-- @ftlvariable name="dictionaryLanguage" type="wisematches.core.Language" -->
<#-- @ftlvariable name="wordAttributes" type="wisematches.playground.dictionary.WordAttribute[]" -->

<#include "/core.ftl"/>

<div id="wordEntryEditor" class="ui-helper-hidden">
    <form>
        <input id="action" name="action" type="hidden" value="VIEW"/>
        <input name="language" type="hidden" value="${language.name()}"/>

        <table width="100%" cellpadding="3">
            <tr>
                <td width="100%" valign="top">
                    <div class="create" style="display: none">
                        <input id="word" class="word-input" name="word" type="text" value="">
                    </div>
                    <div class="create">
                        <span class="word-view"></span>
                    </div>
                </td>
                <td>
                </td>
            </tr>
            <tr>
                <td valign="top" width="100%">
                    <div class="edit" style="display: none">
                        <textarea id="definition" class="definition-input" name="definition"
                                  style="width: 100%; height: 200px"></textarea>
                    </div>
                    <div class="view" style="width: 100%; height: 200px">
                        <span class="attributes-view sample"></span>

                        <span class="definition-view"></span>
                    </div>
                </td>
                <td valign="top" nowrap="nowrap">
                    <div class="edit attributes-input" style="display: none">
                    <#list wordAttributes as wa>
                        <div>
                            <input id="${wa.name()}" name="attributes" type="checkbox" value="${wa.name()}">
                            <label for="${wa.name()}">
                                <@message code="dict.word.attribute.${wa.name()?lower_case}.label"/>
                                -
                                <span class="sample"><@message code="dict.word.attribute.${wa.name()?lower_case}.description"/></span>
                            </label>
                        </div>
                    </#list>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <span class="warn sample ui-helper-hidden">(<@message code="dict.suggest.warn"/>)</span>
                </td>
            </tr>
        </table>
    </form>
</div>

<script type="text/javascript">
    <#assign readOnlySuggestion=!principal?? || !principal.playerType.member/>

    var dictionarySuggestion = new wm.game.dict.Suggestion('${dictionaryLanguage.code()}', ${readOnlySuggestion?string}, {
    <#list wordAttributes as wa>
        "${wa.name()}": "<@message code="dict.word.attribute.${wa.name()?lower_case}.label"/>",
    </#list>
        'title.add': "<@message code="dict.suggest.title.add"/>",
        'title.view': "<@message code="dict.suggest.title.view"/>",
        'title.edit': "<@message code="dict.suggest.title.edit"/>",
        'add': "<@message code="dict.suggest.add.label"/>",
        'save': "<@message code="dict.suggest.save.label"/>",
        'edit': "<@message code="dict.suggest.edit.label"/>",
        'remove': "<@message code="dict.suggest.remove.label"/>",
        'waiting': "<@message code="dict.suggest.sent"/>",
        'remove.title': "<@message code="dict.suggest.confirm.label"/>",
        'remove.confirm': "<@message code="dict.suggest.confirm.description"/>"
    });
</script>