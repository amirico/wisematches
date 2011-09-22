<#-- @ftlvariable name="searchColumns" type="java.lang.String[]" -->
<#-- @ftlvariable name="searchEntityDescriptor" type="wisematches.playground.search.DesiredEntityDescriptor" -->
<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->

<#include "/core.ftl">

<@wm.jstable/>

<@wm.playground id="pastGamesWidget">
    <#if player != principal>
    <div class="title">
    <@message code="game.past.history.label"/> <@message code="separator.for"/> <@wm.player player=player showState=true showType=true/>
    </div>
    </#if>

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
</@wm.playground>

<script type="text/javascript">
    var history = new wm.game.History(${player.id},
            [<#list searchColumns as c>
                <#assign d=searchEntityDescriptor.getAttribute(c)!""/>
                {
                    "sName": '${c}',
                    "mDataProp": '${c}',
                "bSortable": <#if d?has_content>${d.sortable()?string}<#else>false</#if>
                }<#if c_has_next>,</#if></#list>],
            {
                "sEmptyTable": "<@message code="game.past.history.empty" args=['/playground/scribble/create', '/playground/scribble/join']/>",
                "sProcessing": "<@message code="game.past.history.processing"/>"
            }
    );
</script>
