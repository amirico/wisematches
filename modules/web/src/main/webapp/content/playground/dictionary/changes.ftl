<#-- @ftlvariable name="changeSuggestion" type="wisematches.server.services.dictionary.ChangeSuggestion[]" -->
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
            <@security.authorize ifAllGranted="moderator">
                <th></th>
                <th>
                    <@message code="dict.changes.player.label"/>
                </th>
            </@security.authorize>
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
            <#list changeSuggestion as s>
            <tr class="ui-state-default">
                <@security.authorize ifAllGranted="moderator">
                    <td>
                        <label>
                            <input type="checkbox" name="suggestion" value="${s.id?string}">
                        </label>
                    </td>
                    <td>
                        <@wm.player.name personalityManager.getPerson(s.requester) false false true/>
                    </td>
                </@security.authorize>
                <td>
                ${s.word}
                </td>
                <td>
                    <@message code="language.${s.language?lower_case}"/>
                </td>
                <td>
                ${messageSource.formatDate(s.requestDate, locale)}
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
        <@security.authorize ifAllGranted="moderator">
        <button id="approveChanges" class="wm-ui-button" type="submit" style="margin-left: 0">
            <@message code="dict.changes.approve.label"/>
        </button>
        <button id="rejectChanges" class="wm-ui-button" type="submit" style="margin-left: 0">
            <@message code="dict.changes.reject.label"/>
        </button>
        </@security.authorize>
    &nbsp;
    </@wm.ui.table.statusbar>

    <@wm.ui.table.footer/>
</@wm.ui.playground>

<script type="text/javascript">
    wm.ui.dataTable('#dictionaryChanges', {
        "bSortClasses": false,
        "aoColumns": [
        <@security.authorize ifAllGranted="moderator">
            { "bSortable": false },
            { "bSortable": false },
        </@security.authorize>
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": false }
        ]
    });

    <@security.authorize ifAllGranted="moderator">
    $(document).ready(function () {
        var processRequest = function (type) {
            var widget = $("#dictionaryWidget");
            wm.ui.lock(widget);
            var input = $("input[name='suggestion']:checked");
            var ids = [];
            $.each(input, function (i, v) {
                ids.push($(v).val());
            });

            $.post('/playground/dictionary/resolveWordEntry.ajax?type=' + type, JSON.stringify({type: type, ids: ids}), function (result) {
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
    </@security.authorize>
</script>