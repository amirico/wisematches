<#-- @ftlvariable name="player" type="wisematches.core.Personality" -->
<#include "/core.ftl"/>

<@wm.ui.table.dtinit/>
<#assign suggestionStates=["WAITING", "APPROVED", "REJECTED"]/>

<@wm.ui.playground id="dictionaryWidget">
    <@wm.ui.table.header>
        <#if player != principal><@message code="game.player"/> <@wm.player.name player/> > </#if>
        <@message code="dict.label"/> > <@message code="suggestion.label"/>
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar align="left">
    <form id="filterForm">
        <input type="hidden" name="pid" value="${player.id}"/>

        <div id="suggestionStates" class="wm-ui-buttonset">
            <#list suggestionStates as a>
                <input id="suggestionState${a}" name="state" type="radio" value="${a}"
                       <#if a_index==0>checked="checked"</#if>/>
                <label for="suggestionState${a}"><@messageCapFirst code="suggestion.state.${a?lower_case}.label"/></label>
            </#list>
        </div>

    <#--
            <div id="suggestionTypes" class="wm-ui-buttonset">
                <#list ["CREATE", "REMOVE", "UPDATE"] as a>
                    <input id="suggestionType${a}" name="type" type="checkbox" value="${a}" checked="checked"/>
                    <label for="suggestionType${a}"><@messageCapFirst code="suggestion.type.${a?lower_case}.label"/></label>
                </#list>
            </div>
    -->
    </form>
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
    var filterForm = $("#filterForm");

    var langs = {
    <#list WordAttribute.values() as wa>
        '${wa.name()}': "<@message code="dict.word.attribute.${wa.name()?lower_case}.label"/>",
    </#list>
    <#list ["CREATE", "REMOVE", "UPDATE"] as t>
        '${t}': '<@message code="suggestion.type.${t?lower_case}.label"/>'<#if t_has_next>,</#if>
    </#list>
    };

    var changesTable = wm.ui.dataTable('#dictionaryChanges', {
        "bSortClasses": false,
        "bProcessing": true,
        "bServerSide": true,
        "aoColumns": [
            { "mDataProp": 'word', "sClass": 'word', "bSortable": true },
            { "mDataProp": 'suggestionType', "sClass": 'type', "bSortable": true,
                mRender: function (data, type, row) {
                    return langs[data];
                }
            },
            { "mDataProp": 'requester', "sClass": 'requester', "bSortable": true,
                mRender: function (data, type, row) {
                    return wm.ui.player(data);
                } },
            { "mDataProp": 'requestDate', "sClass": 'date', "bSortable": true,
                mRender: function (data, type, row) {
                    return data.text;
                } },
            { "mDataProp": 'attributes', "sClass": 'attributes', "bSortable": false,
                mRender: function (data, type, row) {
                    if (data == null) {
                        return null;
                    }
                    return langs[data];
                }
            },
            { "mDataProp": 'definition', "sClass": 'definition', "bSortable": false }
        ],
        "sAjaxSource": "/playground/dictionary/personalWordEntries.ajax",
        "fnServerData": function (sSource, aoData, fnCallback) {
            $.fn.dataTable.defaults.fnServerData(sSource + "?" + filterForm.serialize(), aoData, fnCallback);
        }
    });
    filterForm.find("input").change(function () {
        changesTable.fnDraw();
    });
</script>
