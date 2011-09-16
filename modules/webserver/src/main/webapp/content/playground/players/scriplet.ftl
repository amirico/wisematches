<#-- @ftlvariable name="scriplet" type="java.lang.Boolean" -->
<#-- @ftlvariable name="area" type="wisematches.server.web.services.search.player.PlayerSearchArea" -->
<#-- @ftlvariable name="areas" type="java.util.List<wisematches.server.web.services.search.player.PlayerSearchArea>" -->
<#include "/core.ftl">

<@wm.jstable/>

<#if !scriplet??><#assign scriplet=true/></#if>

<div id="searchPlayerWidget" <#if scriplet>class="ui-helper-hidden"</#if>>
    <div id="searchTypes">
    <#list areas as a>
        <input id="search${a.name()}" name="searchTypes" type="radio" value="${a.name()}"
               <#if a=area>checked="checked"</#if>/>
        <label for="search${a.name()}"><@message code="search.type.${a.name()?lower_case}"/></label>
    </#list>
    </div>

    <div id="searchContent">
        <div id="searchResult">
            <table style="width: 100%; padding-bottom: 5px;">
                <thead>
                <tr>
                    <th width="100%" nowrap="nowrap"><@message code="search.column.player"/></th>
                    <th nowrap="nowrap"><@message code="search.column.rating"/></th>
                    <th nowrap="nowrap"><@message code="search.column.lastMoveTime"/></th>
                    <th nowrap="nowrap"><@message code="search.column.language"/></th>
                    <th nowrap="nowrap"><@message code="search.column.active"/></th>
                    <th nowrap="nowrap"><@message code="search.column.finished"/></th>
                    <th nowrap="nowrap"><@message code="search.column.avgMoveTime"/></th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script type="text/javascript">
    var playerSearch = new wm.game.Search(${scriplet?string}, {
        title: '<@message code="search.label"/>',
        close: '<@message code="button.close"/>'
    });
</script>
