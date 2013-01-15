<#-- @ftlvariable name="moderator" type="boolean" -->
<#-- @ftlvariable name="waitingSuggestions" type="wisematches.server.web.services.dictionary.ChangeSuggestion[]" -->
<#include "/core.ftl"/>

<@wm.ui.table.dtinit/>

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
                <th>
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
            <#list waitingSuggestions as s>
            <tr class="ui-state-default">
                <#if moderator>
                    <td>
                        <input type="checkbox" name="suggestion" value="${s.id?string}">
                    </td>
                </#if>
                <td>
                ${s.word}
                </td>
                <td>
                    <@message code="language.${s.language?lower_case}"/>
                </td>
                <td>
                ${gameMessageSource.formatDate(s.requestDate, locale)}
                </td>
                <td>
                    <@message code="dict.word.type.${s.suggestionType.name()?lower_case}.label"/>
                </td>
                <td>
                    <#if s.attributes??>
                        <#list s.attributes as a><@message code="dict.word.attribute.${a.name()?lower_case}.label"/> </#list>
                    </#if>
                </td>
                <td width="100%">
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
        <#else>
        &nbsp;
        </#if>
    </@wm.ui.table.statusbar>

    <@wm.ui.table.footer/>
</@wm.ui.playground>

<script type="text/javascript">
    wm.ui.dataTable('#dictionaryChanges', {
        "bSortClasses": false,
        "aoColumns": [
            <#if moderator>{ "bSortable": false },</#if>
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": false }
        ]
    });

    <#if moderator>
    $(document).ready(function () {
        var processRequest = function (type) {
            var widget = $("#dictionaryWidget");
            wm.ui.lock(widget);
            var input = $("input[name='suggestion']:checked");
            var ids = [];
            $.each(input, function (i, v) {
                ids.push($(v).val());
            });

            $.post('/playground/dictionary/processChangeRequest.ajax?type=' + type, JSON.stringify({type: type, ids: ids}), function (result) {
                if (result.success) {
                    wm.util.url.reload();
                } else {
                    wm.ui.unlock(widget, result.message, true);
                }
            });
        };

        $("#approveChanges").click(function () {
            processRequest('approve');
        });

        $("#rejectChanges").click(function () {
            processRequest('reject');
        });
    });
    </#if>
</script>