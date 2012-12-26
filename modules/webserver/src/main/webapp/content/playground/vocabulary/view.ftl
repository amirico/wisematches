<#-- @ftlvariable name="words" type="String[]" -->
<#-- @ftlvariable name="distribution" type="wisematches.server.web.controllers.playground.vocabulary.view.VocabularyDistribution" -->

<#include "/core.ftl"/>

<link rel="stylesheet" type="text/css" href="/jquery/css/jquery.jscrollpane.css"/>
<script type="text/javascript" src="/jquery/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="/jquery/js/jquery.jscrollpane.min.js"></script>

<style type="text/css">

    .jspHorizontalBar,
    .jspVerticalBar,
    .jspTrack {
        background: #eeeef4;
    }

    .jspDrag {
        background: #bbd;

        -moz-border-radius: 10px;
        -webkit-border-radius: 10px;
        border-radius: 10px;
    }

    .jspTrack .jspActive,
    .jspTrack .jspHover,
    .jspDrag:hover {
        background: #8B8B9F;
    }

    .jspArrow {
        border: none;

        background: url(/jquery/css/redmond/images/ui-icons_469bdd_256x240.png) no-repeat;

        -moz-border-radius: 10px;
        -webkit-border-radius: 10px;
        border-radius: 10px;
    }

    .jspVerticalBar>.jspActive,
    .jspArrow:hover {
        background-image: url('/jquery/css/redmond/images/ui-icons_217bc0_256x240.png');
    }

    .jspVerticalBar>.jspDisabled,
    .jspVerticalBar>.jspDisabled:hover,
    .jspHorizontalBar>.jspDisabled,
    .jspHorizontalBar>.jspDisabled:hover {
        background-color: transparent;
        background-image: url('/jquery/css/redmond/images/ui-icons_d8e7f3_256x240.png');
    }

    .jspVerticalBar .jspArrow {
        height: 15px;
    }

    .jspHorizontalBar .jspArrow {
        width: 15px;
    }

    .jspArrowUp {
        background-position: 0 0;
    }

    .jspArrowDown {
        background-position: -64px 0 !important;
    }

    .jspArrowLeft {
        background-position: -96px 0 !important;
    }

    .jspArrowRight {
        background-position: -32px 0 !important;
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
</style>

<@wm.ui.table.dtinit/>

<@wm.ui.playground id="vocabularyWidget">
<div id="vocabulary">
    <@wm.ui.table.header>
        Игровой Словарь > Основной Словарь
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar align="left">
        <button>Добавить Новое Слово</button>
    </@wm.ui.table.toolbar>

    <@wm.ui.table.statusbar align="left">
        <div class="words">
            <#list distribution.letters as l>
                <#assign cnt=distribution.getLettersCount(l)/>
                <#if cnt != 0>
                    <a href="#${l?upper_case}">${l?upper_case} <sub>${cnt}</sub></a>
                </#if>
            </#list>
        </div>
    </@wm.ui.table.statusbar>

    <@wm.ui.table.content wrap=true>
    <#--<div class=" ui-corner-all ui-state-default shadow" style="background: none">-->
        <div id="scrollbar1" class="scroll-pane">
            <table width="100%">
                <#list words as w>
                    <tr>
                        <td>${w}</td>
                    </tr>
                </#list>
            </table>
        </div>
    <#--</div>-->
    </@wm.ui.table.content>

    <@wm.ui.table.footer>

    </@wm.ui.table.footer>
</div>
</@wm.ui.playground>

<script type="text/javascript">
    $("#vocabulary").find("button").button({icons: {primary: 'ui-icon-circle-plus'}});

    var scroll = $("#scrollbar1").jScrollPane({showArrows: true, horizontalGutter: 10, hideFocus: true});
    var api = scroll.data('jsp');

    $(".words a").click(function () {
        var p = $(this).attr('href').substring(1);

        $.post("/playground/vocabulary/load.ajax?l=ru&p=" + p, null, function (json) {
            var t = $("#scrollbar1 table");
            t.empty();
            $.each(json, function (i, v) {
                t.append($("<tr><td>" + v + "</td></tr>"));
            });
            api.reinitialise();
        });
    });

    $(document).ready(function () {
    });
</script>