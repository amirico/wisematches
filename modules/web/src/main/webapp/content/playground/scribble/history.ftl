<#-- @ftlvariable name="player" type="wisematches.core.Personality" -->

<#include "/core.ftl">

<@wm.ui.table.dtinit/>

<#assign columns={"title":true, "language":true, "ratingChange":false, "opponents": false, "movesCount":true, "resolution":true, "startedDate":true, "finishedDate":true}/>

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
        <tr><@wm.ui.table.dtnames columns, "game.past.history.column"/></tr>
        </thead>
        <tbody></tbody>
    </table>
    </@wm.ui.table.content>

    <@wm.ui.table.footer/>
</@wm.ui.playground>

<script type="text/javascript">
    var history = new wm.game.History(${player.id},
    <@wm.ui.table.dtobjects columns/>,
            {
                "sEmptyTable": "<@message code="game.past.history.empty" args=['/playground/scribble/create', '/playground/scribble/join']/>"
            }
    );
</script>