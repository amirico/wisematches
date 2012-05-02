<#-- @ftlvariable name="searchColumns" type="java.lang.String[]" -->
<#-- @ftlvariable name="searchEntityDescriptor" type="wisematches.playground.search.descriptive.SearchableDescriptor" -->
<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->

<#include "/core.ftl">

<@wm.jstable/>

<@wm.playground id="pastGamesWidget">
    <@wm.dtHeader>
        <#if player != principal>
            <@message code="game.player"/> <@wm.player player=player showState=true showType=true/>
        <#else><@message code="game.menu.games.label"/></#if>
    > <@message code="game.past.history.label"/>
    </@wm.dtHeader>

    <@wm.dtToolbar>
        <#if player == principal>
        <a href="/playground/scribble/active"><@message code="game.dashboard.label"/></a>
        <#else>
        <a href="/playground/scribble/active?p=${player.id}"><@message code="game.dashboard.label"/></a>
        </#if>
    </@wm.dtToolbar>

    <@wm.dtContent>
    <table id="history" width="100%" class="display">
        <thead>
        <tr>
            <#list searchColumns as c>
                <th><@message code="game.past.history.column.${c}"/></th>
            </#list>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
    </@wm.dtContent>

    <@wm.dtFooter/>
</@wm.playground>

<script type="text/javascript">
    var history = new wm.game.History(${player.id},
            [<#list searchColumns as c>
                <#assign d=searchEntityDescriptor.getProperty(c)!""/>
                {
                    "sName":'${c}',
                    "mDataProp":'${c}',
                "bSortable": <#if d?has_content>${d.sortable()?string}<#else>false</#if>
                }<#if c_has_next>,</#if></#list>],
            {
                "sEmptyTable":"<@message code="game.past.history.empty" args=['/playground/scribble/create', '/playground/scribble/join']/>"
            }
    );

    $(".data-table-toolbar div").buttonset();
</script>