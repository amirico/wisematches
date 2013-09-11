<#-- @ftlvariable name="reclaims" type="wisematches.playground.dictionary.WordReclaim[]" -->
<#include "/core.ftl"/>

<@wm.ui.table.dtinit/>

<#assign moderator=false/>
<@wm.security.authorize granted="moderator"><#assign moderator=true/></@wm.security.authorize>

<@wm.ui.playground id="dictionaryWidget">
    <@wm.ui.table.header>
        <@message code="dict.label"/> > <@message code="expectant.label"/>
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar align="left">
    </@wm.ui.table.toolbar>

    <@wm.ui.table.content>
    <table id="dictionaryChanges" width="100%" class="display">
        <thead>
        <tr>
            <#if moderator>
                <th>
                    <label for="selectAll"></label><input title="select all messages" type="checkbox" id="selectAll"
                                                          name="selectAll" value="false">
                </th>
            </#if>
            <th>
                <@message code="suggestion.word.label"/>
            </th>
            <th>
                <@message code="suggestion.type.label"/>
            </th>
            <th>
                <@message code="suggestion.player.label"/>
            </th>
            <th>
                <@message code="suggestion.date.label"/>
            </th>
            <th>
                <@message code="suggestion.attributes.label"/>
            </th>
            <th width="100%">
                <@message code="suggestion.definition.label"/>
            </th>
        </tr>
        </thead>
        <tbody>
            <#list reclaims as s>
            <tr class="ui-state-default" valign="top">
                <#if moderator>
                    <td>
                        <label>
                            <input type="checkbox" name="suggestion" value="${s.id?string}">
                        </label>
                    </td>
                </#if>
                <td>
                    <#if moderator>
                        <a href="#"
                           onclick="modifyWordEntry(this); return false;">${s.word}</a>
                    <#else>
                    ${s.word}
                    </#if>
                </td>
                <td class="type">
                    <@message code="suggestion.type.${s.resolutionType.name()?lower_case}.label"/>
                </td>
                <td class="requester">
                    <@wm.player.name personalityManager.getPerson(s.requester) false false true/>
                </td>
                <td class="date">
                ${messageSource.formatDate(s.requestDate, locale)}
                </td>
                <td class="attributes">
                    <#if s.attributes??>
                        <#list s.attributes as a><span
                                class="${a.name()}"><@message code="dict.word.attribute.${a.name()?lower_case}.label"/></span></#list>
                    </#if>
                </td>
                <td class="definition" width="100%">
                ${s.definition!""?html?replace("\n", "<br>")}
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
    </@wm.ui.table.content>

    <@wm.ui.table.statusbar align="left">
        <#if moderator>
        <button id="approveChanges" class="wm-ui-button" type="submit" style="margin-left: 0">
            <@message code="suggestion.approve.label"/>
        </button>
        <button id="rejectChanges" class="wm-ui-button" type="submit" style="margin-left: 0">
            <@message code="suggestion.reject.label"/>
        </button>
        </#if>
    &nbsp;
    </@wm.ui.table.statusbar>

    <@wm.ui.table.footer/>
</@wm.ui.playground>

<script type="text/javascript">
    wm.ui.dataTable('#dictionaryChanges', {
        "bSortClasses": false,
        "aaSorting": [
            [ <#if moderator>5<#else>4</#if>, "desc" ]
        ],
        "aoColumns": [
        <#if moderator>
            { "bSortable": false },
        </#if>
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": false },
            { "bSortable": false }
        ]
    });
</script>

<#if moderator>
    <#include "card.ftl"/>

<script type="text/javascript">
    function modifyWordEntry(link) {
        link = $(link);
        var row = link.parent().parent();

        var id = row.find("input[name='suggestion']").val();
        var word = $.trim(link.text());
        var definition = $.trim(row.find('.definition').text());
        var attributes = $.map(row.find('.attributes span'), function (e, i) {
            return e.className;
        });

        dictionaryManager.editWordEntry({
            id: id,
            word: word,
            language: 'ru',
            definition: definition,
            attributes: attributes
        });
    }

    $(document).ready(function () {
        var widget = $("#dictionaryWidget");

        $("#predefinedComments").change(function () {
            $("#commentary").val($("#predefinedComments").find("option:selected").text());
        });

        var sendRequest = function (type, commentary) {
            var input = $("input[name='suggestion']:checked");
            var ids = [];
            $.each(input, function (i, v) {
                ids.push($(v).val());
            });

            $.post('/playground/dictionary/resolve.ajax?type=' + type, JSON.stringify({type: type, ids: ids, commentary: commentary}), function (result) {
                if (result.success) {
                    wm.util.url.reload();
                } else {
                    wm.ui.unlock(widget, result.message, true);
                }
            });
        };

        $("#selectAll").click(function () {
            var checkBoxes = $("input[name='suggestion']");
            checkBoxes.prop("checked", !checkBoxes.prop("checked"));
        });

        $("#approveChanges").click(function () {
            sendRequest('approve', null);
        });

        $("#rejectChanges").click(function () {
            var dlg = $("#resolutionCommentForm").dialog({
                title: "Process with a comment",
                width: 550,
                modal: true,
                resizable: false,
                buttons: [
                    {
                        text: "Process",
                        click: function () {
                            wm.ui.lock(widget, "Processing your request, please wait...");
                            sendRequest('reject', $("#commentary").val());
                        }
                    },
                    {
                        text: "<@message code="button.cancel"/>",
                        click: function () {
                            dlg.dialog("close");
                            wm.ui.unlock(widget);
                        }
                    }
                ]
            });
        });
    });
</script>

<div id="resolutionCommentForm" class="ui-helper-hidden">
    <table width="100%">
        <tr>
            <td>
                <label for="commentary">
                    Please enter your comment:
                </label>
            </td>
        </tr>
        <tr>
            <td>
                <select id="predefinedComments" style="width: 100%">
                    <option>Разрешены только именя существительные единственного числа (если есть), именительного
                        падежа.
                    </option>
                    <option>Разрешены только слова основной формы (запрещены уменьшительные, увеличительные,
                        ласкательние и т.п. формы)
                    </option>
                    <option>Слово не найдено в основных словарях русского языка</option>
                    <option>Описание совпадает с текущим</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <textarea id="commentary" name="commentary" style="width: 100%;" rows="10"></textarea>
            </td>
        </tr>
    </table>
</div>
</#if>
