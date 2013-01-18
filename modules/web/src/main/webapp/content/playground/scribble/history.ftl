<#-- @ftlvariable name="searchColumns" type="java.lang.String[]" -->
<#-- @ftlvariable name="searchEntityDescriptor" type="wisematches.core.search.descriptive.SearchableDescriptor" -->
<#-- @ftlvariable name="player" type="wisematches.core.personality.Player" -->

<#include "/core.ftl">

<@wm.ui.table.dtinit/>

<@wm.ui.playground id="pastGamesWidget">
    <@wm.ui.table.header>
        <#if player != principal>
            <@message code="game.player"/> <@wm.player.name player/>
        <#else><@message code="game.menu.games.label"/></#if>
    > <@message code="game.past.history.label"/>
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar>
    <table width="100%">
        <tr>
            <td align="left">
                <div class="wm-ui-buttonset">
                    <a href="/playground/scribble/active<#if player != principal>?p=${player.id}</#if>"><@message code="game.dashboard.label"/></a>
                    <a href="/playground/scribble/join"><@message code="game.join.label"/></a>
                    <a href="/playground/scribble/create"><@message code="game.create.label"/></a>
                </div>
            </td>
            <td align="right">
            </td>
        </tr>
    </table>
    </@wm.ui.table.toolbar>

    <@wm.ui.table.content>
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
    </@wm.ui.table.content>

    <@wm.ui.table.footer/>
</@wm.ui.playground>

<script type="text/javascript">
    var history = new wm.game.History(${player.id},
            [<#list searchColumns as c>
                <#assign d=searchEntityDescriptor.getProperty(c)!""/>
                {
                    "sName": '${c}',
                    "mDataProp": '${c}',
                "bSortable": <#if d?has_content>${d.sortable()?string}<#else>false</#if>
                }<#if c_has_next>,</#if></#list>],
            {
                "sEmptyTable": "<@message code="game.past.history.empty" args=['/playground/scribble/create', '/playground/scribble/join']/>"
            }
    );
</script>