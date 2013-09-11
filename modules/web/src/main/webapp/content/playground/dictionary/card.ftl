<#-- @ftlvariable name="thesauruses" type="wisematches.playground.dictionary.Thesaurus[]" -->
<#include "/core.ftl"/>

<#assign enThesaruses={
"WordReference": "http://www.wordreference.com/definition/{word}"}
/>

<#assign ruThesaruses={
"Яндекс":"http://slovari.yandex.ru/{word}/правописание",
"Wiktionary":"http://ru.wiktionary.org/wiki/{word}"}
/>

<#assign thesaruses={"en":enThesaruses, "ru":ruThesaruses}/>

<div id="wordEntryEditor" class="ui-helper-hidden">
    <form>
        <input name="id" type="hidden" value=""/>
        <input id="action" name="action" type="hidden" value="VIEW"/>
        <input class="language-input" name="language" type="hidden" value="""/>

        <table width="100%" cellpadding="3">
            <tr>
                <td width="100%" valign="top">
                    <div class="create" style="display: none">
                        <label for="word"></label><input id="word" class="word-input" name="word" type="text" value=""
                                                         tabindex="1">
                    </div>
                    <div class="create">
                        <span class="word-view"></span>

                        <span class="attributes-view sample"></span>
                    </div>
                </td>
                <td style="display: none">
                <div class="sample" style="text-align: right">(<span class="language-view"></span>)</div>
                </td>
                <td align="right">
                <#list thesaruses?keys as lang>
                    <div class="thesauruses" id="Thesauruses${lang?upper_case}" style="display: none">
                        <#assign thesarus=thesaruses[lang]/>
                        <#list thesarus?keys as t>
                            <#assign template=thesarus[t]/>
                            <a tabindex="-1" class="thesaurus" href="${template?replace("{word}", "")}" target="_blank"
                               template="${template}">${t}</a>
                            <#if t_has_next> · </#if>
                        </#list>
                    </div>
                </#list>
                </td>
            </tr>
            <tr>
                <td valign="top" width="100%">
                    <div class="edit" style="display: none">
                        <label for="definition"></label>

                        <textarea id="definition" class="definition-input"
                                  name="definition"
                                  style="width: 100%; height: 200px"></textarea>
                    </div>
                    <div class="view" style="width: 100%; height: 200px">
                        <span class="definition-view"></span>
                    </div>
                </td>
                <td valign="top" nowrap="nowrap">
                    <div class="edit attributes-input" style="display: none">
                    <#list WordAttribute.values() as wa>
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
    var dictionaryManager = new wm.game.dict.DictionaryManager(${(!principal?? || !principal.type.member)?string}, {
    <#list WordAttribute.values() as wa>
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