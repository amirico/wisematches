<#-- @ftlvariable name="vocabulary" type="wisematches.playground.vocabulary.Vocabulary" -->
<#-- @ftlvariable name="distribution" type="wisematches.server.web.controllers.playground.vocabulary.view.VocabularyDistribution" -->

<#include "/core.ftl"/>

<link rel="stylesheet" type="text/css" href="/jquery/css/jquery.jscrollpane.css" xmlns="http://www.w3.org/1999/html"/>
<script type="text/javascript" src="/jquery/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="/jquery/js/jquery.jscrollpane.min.js"></script>

<style type="text/css">
    .asd .ui-widget-content {
        padding: 0;
    }

    .scroll-pane {
        width: 100%;
        height: 200px;
        overflow: auto;
    }

    .horizontal-only {
        height: auto;
        max-height: 200px;
    }

    #vocabulary .ui-widget-content div {
        display: block;
    }
</style>

<@wm.ui.table.dtinit/>

<@wm.ui.playground id="vocabularyWidget">
<div id="vocabulary">
    <@wm.ui.table.header>
        Игровой Словарь > ${vocabulary.name}
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar align="left">
    ${vocabulary.description}
        <span class="sample">${gameMessageSource.formatDate(vocabulary.modificationDate, locale)}</span>

        <table width="100%">
            <tr>
                <td align="left" valign="middle">
                    <label for="vocabularySearch">Поиск:</label>
                    <input id="vocabularySearch" type="text" value="" size="30"/>
                </td>

                <td align="right" valign="middle">
                    <button>Добавить Новое Слово</button>
                </td>
            </tr>
        </table>
    </@wm.ui.table.toolbar>

    <@wm.ui.table.statusbar align="left">
        <div class="words">
            <div>
                <#list distribution.firstLevel as l>
                <#--<#assign cnt=distribution.getLettersCount(l)/>-->
                <#--<#if cnt != 0>-->
                    <a href="#${l?upper_case}" onclick="return false;">${l?upper_case}</a>
                <#--</#if>-->
                </#list>
            </div>
        </div>
    </@wm.ui.table.statusbar>

    <@wm.ui.table.content wrap=true class="asd">
        <table width="100%">
            <tr>
                <td valign="top" class="ui-widget-content" style="border-width: 0 1px 0 0; display: none;">
                <#--<div class="words" style="padding-left: 2px; padding-right: 2px;">-->
                        <#--<div><a href="#Аа">Aа</a></div>-->
                        <#--<div><a href="#Аб">Aб</a></div>-->
                        <#--<div><a href="#Ав">Aв</a></div>-->
                        <#--<div><a href="#Аг">Aг</a></div>-->
                        <#--<div><a href="#Ад">Aд</a></div>-->
                    <#--</div>-->
                </td>
                <td width="100%">
                    <div id="scrollbar1" class="scroll-pane" style="display: inline-block">
                        <table width="100%">
                        </table>
                    </div>
                </td>
            </tr>
        </table>
    </@wm.ui.table.content>

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

    function loadVocabulary(prefix) {
        var t = $("#scrollbar1").find("table");
        wm.ui.lock(scroll, "Loading vocabulary");

        var wce = $("#wordsCount").find("span");
        wce.text("загрузка списка слов");
        t.empty();
        if (prefix.length == 0) {
            wm.ui.unlock(scroll);
        } else {
            $.post("/playground/vocabulary/load.ajax?l=ru&p=" + prefix, null, function (words) {
                wce.text(words.length);

                $.each(words, function (i, v) {
                    t.append($("<tr>" +
                            "<td><a href='asd'>" + v.text + "<a/></td>" +
                            "<td>" + v.gender + "</td>" +
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
        }, 500));
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