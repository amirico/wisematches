<#-- @ftlvariable name="scriplet" type="java.lang.Boolean" -->
<#-- @ftlvariable name="searchArea" type="wisematches.server.services.relations.players.PlayerRelationship" -->
<#include "/core.ftl">

<@wm.ui.table.dtinit/>

<style type="text/css">
    .filter div {
        display: inline-block;
    }
</style>

<#assign searchAreas=[PlayerRelationship.FRIENDS.name(), PlayerRelationship.FORMERLY.name(), 'PLAYERS']/>

<#if !scriplet??><#assign scriplet=true/></#if>
<div id="searchPlayerWidget" <#if scriplet>class="ui-helper-hidden" style="padding-top: 10px" </#if>>
<@wm.ui.table.toolbar align="left">
    <form id="filterForm">
        <table width="100%">
            <tr>
                <td valign="top" align="left">
                    <div class="simpleFilter">
                        <label for="playersSearchFilter"
                               style="font-weight: normal"><@message code="search.column.player"/>: </label>
                        <input id="playersSearchFilter" name="nickname" type="text"">
                        <button id="searchFilterDo" type="button"><@message code="search.do"/></button>
                        &nbsp;&nbsp;&nbsp;
                        <a id="playersSearchAdvanced" class="action" href="#"
                           onclick="return false;"><@message code="search.advanced.label"/></a>
                    </div>
                </td>

                <td valign="top" align="right" nowrap="nowrap">
                    <div id="searchTypes" class="wm-ui-buttonset">
                        <#list searchAreas as a>
                            <input id="search${a?lower_case?capitalize}" name="searchTypes" type="radio" value="${a}"
                                   <#if a=searchArea>checked="checked"</#if>/>
                            <label for="search${a?lower_case?capitalize}"><@message code="search.type.${a?lower_case}"/></label>
                        </#list>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <div id="advancedFilter" class="advanced-filter" style="display: none">
                        <table>
                            <tr>
                                <td><@message code="search.column.ratingA"/>:</td>
                                <td>
                                    <@message code="search.limit.min"/> <input size="5" type="text" name="ratingMin">
                                    <@message code="search.limit.max"/> <input size="5" type="text" name="ratingMax">
                                </td>
                            </tr>
                            <tr>
                                <td><@message code="search.column.activeGames"/>:</td>
                                <td>
                                    <@message code="search.limit.min"/> <input size="5" type="text" name="activeMin">
                                    <@message code="search.limit.max"/> <input size="5" type="text" name="activeMax">
                                </td>
                            </tr>
                            <tr>
                                <td><@message code="search.column.finishedGames"/>:</td>
                                <td>
                                    <@message code="search.limit.min"/> <input size="5" type="text" name="finishedMin">
                                    <@message code="search.limit.max"/> <input size="5" type="text" name="finishedMax">
                                </td>
                            </tr>
                            <tr>
                                <td><@message code="search.column.averageMoveTime"/>:</td>
                                <td>
                                    <select name="minAverageMoveTime" style="width: 100%">
                                        <option value=""><@message code="search.limit.unimportant"/></option>
                                        <option value="86400000"><@message code="search.limit.average.day"/></option>
                                        <option value="259200000"><@message code="search.limit.average.dais"/></option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td><@message code="search.column.lastMoveTime"/>:</td>
                                <td>
                                    <select name="lastMoveTime" style="width: 100%">
                                        <option value=""><@message code="search.limit.unimportant"/></option>
                                        <option value="86400000"><@message code="search.limit.last.day"/></option>
                                        <option value="604800000"><@message code="search.limit.last.week"/></option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="right" colspan="2">
                                    <button id="searchFilterApply"
                                            type="button"><@message code="search.apply"/></button>
                                    <button id="searchFilterReset"
                                            type="button"><@message code="search.reset"/></button>
                                </td>
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>
        </table>
    </form>
</@wm.ui.table.toolbar>

<@wm.ui.table.content>
    <table id="searchResult" class="display">
        <thead>
        <tr>
            <th width="100%"><@message code="search.column.player"/></th>
            <th><@message code="search.column.language"/></th>
            <th><@message code="search.column.ratingG"/> <@wm.ui.info><@message code="search.column.ratingG.description"/></@wm.ui.info></th>
            <th><@message code="search.column.ratingA"/> <@wm.ui.info><@message code="search.column.ratingA.description"/></@wm.ui.info></th>
            <th><@message code="search.column.activeGames"/></th>
            <th><@message code="search.column.finishedGames"/></th>
            <th><@message code="search.column.averageMoveTime"/></th>
            <th><@message code="search.column.lastMoveTime"/></th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</@wm.ui.table.content>

<@wm.ui.table.footer/>
</div>

<script type="text/javascript">
    var playerSearch = new wm.game.Search(
            [
                {"mDataProp": 'player', bSortable: true},
                {"mDataProp": 'language', bSortable: true},
                {"mDataProp": 'ratingG', bSortable: true},
                {"mDataProp": 'ratingA', bSortable: true},
                {"mDataProp": 'activeGames', bSortable: true},
                {"mDataProp": 'finishedGames', bSortable: true},
                {"mDataProp": 'averageMoveTime', bSortable: true},
                {"mDataProp": 'lastMoveTime', bSortable: true}
            ],
    ${scriplet?string},
            {
                title: '<@message code="search.label"/>',
                close: '<@message code="button.close"/>'
            });
</script>