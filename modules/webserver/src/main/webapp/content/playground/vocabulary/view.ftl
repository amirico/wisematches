<#-- @ftlvariable name="vocabularies" type="wisematches.playground.vocabulary.VocabularyOld[]" -->
<#include "/core.ftl"/>

<link rel="stylesheet" type="text/css" href="/jquery/css/jquery.jscrollpane.css" xmlns="http://www.w3.org/1999/html"/>
<script type="text/javascript" src="/jquery/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="/jquery/js/jquery.jscrollpane.min.js"></script>

<style type="text/css">
    .asd .ui-widget-content, .asd .ui-state-hover {
        padding: 0;
    }

    .scroll-pane {
        width: 100%;
        height: 400px;
        overflow: auto;
    }

    .horizontal-only {
        height: auto;
        max-height: 400px;
    }

    #vocabulary .ui-widget-content div {
        display: block;
    }

    #scrollbar1 table td {
        vertical-align: top;
        padding-left: 2px;
        padding-right: 2px;
        border-bottom: 1px dashed #d0e5f5;
    }
</style>

<@wm.ui.table.dtinit/>

<@wm.ui.playground id="vocabularyWidget">
<div id="vocabulary">
    <@wm.ui.table.header>
        Игровые Словари
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar align="left" class="asd">
        <table width="100%">
            <tr>
                <td align="left" valign="top" class="ui-widget-content ui-state-hover"
                    style="border-width: 0 1px 0 0; width: 200px !important; padding: 3px; background-image: none">
                    <div>
                        <strong>Поиск в словарях:</strong>
                        <input id="vocabularySearch" type="text" value="" style="width: 100%"/>
                    </div>
                </td>

                <td>
                    <div class="words">
                        <div>
                            <#list vocabularies as v>
                                <#list v.alphabet.toCharArray() as l>
                                    <a href="#${l?upper_case}" onclick="return false;">${l?upper_case}</a>
                                </#list>
                            </#list>
                        </div>
                        <div>
                            <#list ['Аа','Аб','Ав','Аг','Ад','Ас','Аж'] as l>
                                <a href="#${l}" onclick="return false;">${l}</a>
                            </#list>
                        </div>
                    </div>
                </td>

                <td align="right" valign="bottom">
                    <button>Добавить Новое Слово</button>
                </td>
            </tr>
        <#--
                    <tr>
                        <td colspan="2" style="padding-left: 204px">
                            <div class="words" style="display: inline">
                                <div style="display: inline">
                                    <#list vocabularies as v>
                                        <#list v.alphabet.toCharArray() as l>
                                            <a href="#${l?upper_case}" onclick="return false;">${l?upper_case}</a>
                                        </#list>
                                    </#list>
                                </div>
                            </div>
                        </td>
                    </tr>
        -->
        </table>
    </@wm.ui.table.toolbar>

    <@wm.ui.table.content wrap=true class="asd">
        <table width="100%">
            <tr>
                <td valign="top" class="ui-widget-content ui-state-hover"
                    style="border-width: 0 1px 0 0; width: 200px !important; padding: 3px; background-image: none">
                    <div style="padding-top: 10px">
                        <#list vocabularies as v>
                            <div>
                                <input id="${v.id}" type="checkbox" checked="checked" value="true">
                                <label for="${v.id}">${v.name}</label>
                            </div>
                        </#list>
                    </div>
                </td>
                <td>
                    <div id="scrollbar1" class="scroll-pane" style="display: inline-block">
                        <table width="100%">
                        </table>
                    </div>
                </td>
            </tr>
        </table>
    </@wm.ui.table.content>

<#--
    <@wm.ui.table.statusbar align="left">
    </@wm.ui.table.statusbar>
-->

    <@wm.ui.table.statusbar align="left">
        <div id="wordsCount" class="sample">
            <strong>Слов, начинающихся с '<em>лапка</em>':</strong> <span>выберите начальную букву либо введите начало слова</span>
        </div>
    </@wm.ui.table.statusbar>

    <@wm.ui.table.footer>
    </@wm.ui.table.footer>
</div>
</@wm.ui.playground>

<script type="text/javascript">
    $("#vocabulary").find("button").button({icons: {primary: 'ui-icon-circle-plus'}});

    function getGender(g) {
        if (g == "MASCULINE") {
            return "м."
        }
        if (g == "FEMININE") {
            return "ж."
        }
        if (g == "MASCULINE") {
            return "ср."
        }
        if (g == "COMPOSITE") {
            return "м. и ж."
        }
        return "";
    }

    function loadVocabulary(prefix) {
        var t = $("#scrollbar1").find("table");
        wm.ui.lock(scroll, "Loading vocabulary");

        var wce = $("#wordsCount").find("span");
        wce.text("загрузка списка слов");
        t.empty();
        if (prefix.length == 0) {
            wm.ui.unlock(scroll);
        } else {
            $.post("/playground/vocabulary/load.ajax?l=ru&v=ozhigov.dic&p=" + prefix, null, function (words) {
                wce.text(words.length);

                $.each(words, function (i, v) {
                    t.append($("<tr>" +
                            "<td><a href='asd'>" + v.text + "<a/></td>" +
                            "<td>" + getGender(v.gender) + "</td>" +
                            "<td>" + v.description + "</td>" +
                            "</tr>"));
                });
                api.reinitialise();
                wm.ui.unlock(scroll);
            });
        }
    }

    $("#vocabularySearch").bind("input propertychange", function (evt) {
        if (window.event && event.type == "propertychange" && event.propertyName != "value")
            return;

        var input = $(this);
        var value = input.val();
        window.clearTimeout(input.data("timeout"));
        input.data("timeout", setTimeout(function () {
            loadVocabulary(value);
        }, 800));
    });

    var scroll = $("#scrollbar1").jScrollPane({showArrows: true, horizontalGutter: 10, hideFocus: true});
    var api = scroll.data('jsp');

    $(".words a").click(function () {
        var substring = $(this).attr('href').substring(1);
        $("#vocabularySearch").val(substring);
        loadVocabulary(substring);
    });

    $(document).ready(function () {
    });
</script>