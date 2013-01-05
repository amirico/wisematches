<#-- @ftlvariable name="dictionary" type="wisematches.playground.dictionary.Dictionary" -->
<#-- @ftlvariable name="dictionaryLanguage" type="wisematches.personality.Language" -->
<#-- @ftlvariable name="waitingSuggestions" type="wisematches.server.web.services.dictionary.ChangeSuggestion[]" -->
<#-- @ftlvariable name="wordAttributes" type="wisematches.playground.dictionary.WordAttribute[]" -->
<#include "/core.ftl"/>

<link rel="stylesheet" type="text/css" href="/jquery/css/jquery.jscrollpane.css" xmlns="http://www.w3.org/1999/html"/>
<script type="text/javascript" src="/jquery/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="/jquery/js/jquery.jscrollpane.min.js"></script>

<@wm.ui.playground id="dictionaryWidget">
<div id="dictionary">
    <@wm.ui.table.header>
        <@message code="dict.label"/>
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar align="left" class="search-panel">
        <table width="100%">
            <tr>
                <td width="200px" align="left" valign="top" class="ui-widget-content ui-state-hover navigation">
                    <div>
                        <label for="dictionarySearch"><@message code="dict.search.label"/>:</label>
                        <input id="dictionarySearch" name="dictionarySearch" type="text" value="" style="width: 100%"/>
                    </div>
                </td>

                <td align="left" valign="top">
                    <div id="alphabet">
                        <div id="topAlphabet">
                            <#list dictionary.alphabet.letters as l>
                                <a href="#${l?upper_case}">${l?upper_case}</a>
                            </#list>
                        </div>
                        <div id="subAlphabet">
                            <#list dictionary.alphabet.letters as l>
                                <div id="subAlphabet${l}" style="display: none;">
                                    <#list dictionary.getAlphabet(l).letters as s>
                                        <a href="#${l}${s}">${l?upper_case}${s}</a>
                                    </#list>
                                </div>
                            </#list>
                        </div>
                    </div>
                </td>

                <td align="right" valign="bottom">
                    <button id="addNewWord" class="ui-wm-button"
                            style="white-space: nowrap"><@message code="dict.add.label"/></button>
                </td>
            </tr>
        </table>
    </@wm.ui.table.toolbar>

    <@wm.ui.table.content wrap=true class="search-panel">
        <table width="100%">
            <tr>
                <td width="200px" valign="top" class="ui-widget-content ui-state-hover navigation">
                    <div style="width: 200px !important;">
                        &nbsp;
                    <#--
                                            <label>Ожидают добавления:</label>

                                            <div style="padding-left: 10px; ">
                                                <#list waitingSuggestions as ws>
                                                ${ws.word}
                                                    <br>
                                                </#list>
                                            </div>

                                            <div style="text-align: right">
                                                <a href="/playground/dictionary/changes">список всех изменений</a>
                                            </div>
                    -->
                    </div>
                </td>
                <td>
                    <div class="scroll-pane" style="display: inline-block">
                        <table>
                        </table>
                    </div>
                </td>
            </tr>
        </table>
    </@wm.ui.table.content>

    <@wm.ui.table.statusbar align="left">
        <div id="wordsCount" class="sample">
            <strong><@message code="dict.search.result.label"/>:</strong>
            <span><@message code="dict.search.result.empty"/></span>
        </div>
    </@wm.ui.table.statusbar>

    <@wm.ui.table.footer>
    </@wm.ui.table.footer>
</div>
</@wm.ui.playground>

<#include "modify.ftl"/>

<script type="text/javascript">
    var dictionary;
    $(document).ready(function () {
        dictionary = new wm.game.dict.Dictionary('${dictionaryLanguage}', {
        <#list wordAttributes as wa>
            "${wa.name()}": "<@message code="dict.word.attribute.${wa.name()?lower_case}.label"/>",
        </#list>
            "status.words.empty": "<@message code="dict.search.result.empty"/>",
            "status.words.loading": "<@message code="dict.search.result.loading"/>"
        });

        $("#addNewWord").button({icons: {primary: 'ui-icon-circle-plus'}}).click(function () {
            dictionarySuggestion.addWordEntry();
        });
    });
</script>