<#-- @ftlvariable name="suggestions" type="wisematches.server.services.dictionary.ChangeSuggestion[]" -->
<#-- @ftlvariable name="dictionaryLanguage" type="wisematches.core.Language" -->
<#include "/core.ftl"/>

<@wm.ui.table.dtinit/>

<#assign moderator=false/>
<#assign readOnlySuggestion=false/>
<@wm.security.authorize granted="moderator"><#assign moderator=true/></@wm.security.authorize>

<@wm.ui.playground id="dictionaryWidget">
    <@wm.ui.table.header>
        <@message code="dict.label"/> > <@message code="dict.changes.label"/>
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar align="left">
    <#--
        <table width="100%">
            <tr>
                <td align="left">
                    <div class="wm-ui-buttonset">
                        <input id="check1" type="checkbox"><label for="check1">B</label>
                        <input id="check2" type="checkbox"><label for="check2">B</label>
                    </div>
                </td>
                <td align="right">
                    <div class="wm-ui-buttonset">
                        <input type="radio"/>
                        <input type="radio"/>
                        <input type="radio"/>
                    </div>
                </td>
            </tr>
        </table>
    -->
    &nbsp;
    </@wm.ui.table.toolbar>

    <@wm.ui.table.content>
    <table id="dictionaryChanges" width="100%" class="display">
        <thead>
        <tr>
            <#if moderator>
                <th></th>
                <th>
                    <@message code="dict.changes.player.label"/>
                </th>
            </#if>
            <th>
                <@message code="dict.changes.word.label"/>
            </th>
            <th>
                <@message code="dict.changes.language.label"/>
            </th>
            <th>
                <@message code="dict.changes.added.label"/>
            </th>
            <th>
                <@message code="dict.changes.type.label"/>
            </th>
            <th>
                <@message code="dict.changes.attributes.label"/>
            </th>
            <th width="100%">
                <@message code="dict.changes.definition.label"/>
            </th>
        </tr>
        </thead>
        <tbody>
            <#list suggestions as s>
            <tr class="ui-state-default" valign="top">
                <#if moderator>
                    <td>
                        <label>
                            <input type="checkbox" name="suggestion" value="${s.id?string}">
                        </label>
                    </td>
                    <td>
                        <@wm.player.name personalityManager.getPerson(s.requester) false false true/>
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
                <td class="language">
                    <@message code="language.${s.language?lower_case}"/>
                </td>
                <td class="requested">
                ${messageSource.formatDate(s.requestDate, locale)}
                </td>
                <td class="action">
                    <@message code="dict.word.type.${s.suggestionType.name()?lower_case}.label"/>
                </td>
                <td class="attributes">
                    <#if s.attributes??>
                        <#list s.attributes as a><span
                                class="${a.name()}"><@message code="dict.word.attribute.${a.name()?lower_case}.label"/></span></#list>
                    </#if>
                </td>
                <td class="definition" width="100%">
                ${s.definition!""}
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
    </@wm.ui.table.content>

    <@wm.ui.table.statusbar align="left">
        <#if moderator>
        <button id="approveChanges" class="wm-ui-button" type="submit" style="margin-left: 0">
            <@message code="dict.changes.approve.label"/>
        </button>
        <button id="rejectChanges" class="wm-ui-button" type="submit" style="margin-left: 0">
            <@message code="dict.changes.reject.label"/>
        </button>
        </#if>
    &nbsp;
    </@wm.ui.table.statusbar>

    <@wm.ui.table.footer/>
</@wm.ui.playground>

<script type="text/javascript">
    wm.ui.dataTable('#dictionaryChanges', {
        "bSortClasses": false,
        "aoColumns": [
        <#if moderator>
            { "bSortable": false },
            { "bSortable": false },
        </#if>
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": false }
        ]
    });

    <#if moderator>
    var dictionaryLanguage = '${dictionaryLanguage}';

    function modifyWordEntry(link) {
        link = $(link);
        var row = link.parent().parent();

        var id = row.find("input[name='suggestion']").val();
        var word = $.trim(link.text());
        var definition = $.trim(row.find('.definition').text());
        var attributes = $.map(row.find('.attributes span'), function (e, i) {
            return e.className;
        });

        dictionarySuggestion.modifyWordEntry({
            id: id,
            word: word,
            definitions: [
                {text: definition, attributes: attributes}
            ]
        });
    }

    $(document).ready(function () {
        var widget = $("#dictionaryWidget");

        var sendRequest = function (type, commentary) {
            var input = $("input[name='suggestion']:checked");
            var ids = [];
            $.each(input, function (i, v) {
                ids.push($(v).val());
            });

            $.post('/playground/dictionary/resolveWordEntry.ajax?type=' + type, JSON.stringify({type: type, ids: ids, commentary: commentary}), function (result) {
                if (result.success) {
                    wm.util.url.reload();
                } else {
                    wm.ui.unlock(widget, result.message, true);
                }
            });
        };

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
                            sendRequest('reject', $("#resolutionCommentForm").find("textarea").val());
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
    </#if>
</script>

<#if moderator>
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
                <textarea id="commentary" name="commentary" style="width: 100%;" rows="10"></textarea>
            </td>
        </tr>
    </table>
</div>
</#if>

<#if moderator><#include "card.ftl"/></#if>
