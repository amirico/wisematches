<#-- @ftlvariable name="player" type="wisematches.core.Personality" -->
<#include "/core.ftl"/>

<@wm.ui.table.dtinit/>

<@wm.ui.playground id="dictionaryWidget">
    <@wm.ui.table.header>
        <#if player != principal><@message code="game.player"/> <@wm.player.name player/> > </#if>
        <@message code="dict.label"/> > <@message code="suggestion.label"/>
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar align="left">
    <#--
        <div id="suggestionStates" class="wm-ui-buttonset">
            <#list states as a>
                <input id="suggestionState${a}" name="suggestionStates" type="radio" value="${a}"/>
                <label for="suggestionState${a}"><@message code="suggestion.state.${a?lower_case}"/></label>
            </#list>
        </div>

        <div id="suggestionTypes" class="wm-ui-buttonset">
            <#list types as a>
                <input id="suggestionType${a}" name="suggestionTypes" type="checkbox" value="${a}" checked="checkeds"/>
                <label for="suggestionType${a}"><@message code="suggestion.type.${a?lower_case}"/></label>
            </#list>
        </div>
    -->
    </@wm.ui.table.toolbar>

    <@wm.ui.table.content>
    <table id="dictionaryChanges" width="100%" class="display">
        <thead>
        <tr>
            <th>
                <@message code="suggestion.word.label"/>
            </th>
            <th>
                <@message code="suggestion.type.label"/>
            </th>
            <th>
                <@message code="suggestion.state.label"/>
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
        </tbody>
    </table>
    </@wm.ui.table.content>

    <@wm.ui.table.statusbar align="left">
    &nbsp;
    </@wm.ui.table.statusbar>

    <@wm.ui.table.footer/>
</@wm.ui.playground>

<script type="text/javascript">
    wm.ui.dataTable('#dictionaryChanges', {
        "bSortClasses": false,
        "bProcessing": true,
        "bServerSide": true,
        "aoColumns": [
            { "mDataProp": 'word', "sClass": 'word', "bSortable": true },
            { "mDataProp": 'suggestionType', "sClass": 'type', "bSortable": true},
            { "mDataProp": 'suggestionState', "sClass": 'state', "bSortable": true },
            { "mDataProp": 'requester', "sClass": 'requester', "bSortable": true,
                mRender: function (data, type, row) {
                    return wm.ui.player(data);
                } },
            { "mDataProp": 'requestDate', "sClass": 'date', "bSortable": true,
                mRender: function (data, type, row) {
                    return data.text;
                } },
            { "mDataProp": 'attributes', "sClass": 'attributes', "bSortable": false },
            { "mDataProp": 'definition', "sClass": 'definition', "bSortable": false }
        ],
        "sAjaxSource": "/playground/dictionary/personalWordEntries.ajax",
        "fnServerData": function (sSource, aoData, fnCallback) {
            $.fn.dataTable.defaults.fnServerData(sSource + "?pid=" + ${player.id}, aoData, fnCallback);
        }
    });
</script>
