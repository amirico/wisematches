<#-- @ftlvariable name="scriplet" type="java.lang.Boolean" -->
<#-- @ftlvariable name="searchArea" type="wisematches.server.services.relations.PlayerSearchArea" -->
<#include "/core.ftl">

<@wm.ui.table.dtinit/>

<#assign searchAreas=[PlayerSearchArea.FRIENDS, PlayerSearchArea.FORMERLY, PlayerSearchArea.PLAYERS]/>

<#if !scriplet??><#assign scriplet=true/></#if>
<div id="searchPlayerWidget" <#if scriplet>class="ui-helper-hidden" style="padding-top: 10px" </#if>>
<@wm.ui.table.toolbar>
    <div id="searchTypes" class="wm-ui-buttonset">
        <#list searchAreas as a>
            <input id="search${a.name()?lower_case?capitalize}" name="searchTypes" type="radio" value="${a.name()}"
                   <#if a=searchArea>checked="checked"</#if>/>
            <label for="search${a.name()?lower_case?capitalize}"><@message code="search.type.${a.name()?lower_case}"/></label>
        </#list>
    </div>
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