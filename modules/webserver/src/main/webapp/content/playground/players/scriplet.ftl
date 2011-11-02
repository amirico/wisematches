<#-- @ftlvariable name="scriplet" type="java.lang.Boolean" -->
<#-- @ftlvariable name="searchColumns" type="java.util.Collection<java.lang.String>" -->
<#-- @ftlvariable name="searchEntityDescriptor" type="wisematches.playground.search.DesiredEntityDescriptor" -->
<#-- @ftlvariable name="searchArea" type="wisematches.playground.search.player.PlayerSearchArea" -->
<#-- @ftlvariable name="searchAreas" type="java.util.List<wisematches.server.web.services.search.player.PlayerSearchArea>" -->
<#include "/core.ftl">

<@wm.jstable/>

<#if !scriplet??><#assign scriplet=true/></#if>

<div id="searchPlayerWidget" <#if scriplet>class="ui-helper-hidden"</#if>>
    <div id="searchTypes">
    <#list searchAreas as a>
        <input id="search${a.name()?lower_case?capitalize}" name="searchTypes" type="radio" value="${a.name()}"
               <#if a=searchArea>checked="checked"</#if>/>
        <label for="search${a.name()?lower_case?capitalize}"><@message code="search.type.${a.name()?lower_case}"/></label>
    </#list>
    </div>

    <div>
        <table id="searchResult" class="display">
            <thead>
            <tr>
            <#list searchColumns as c>
                <th <#if c="nickname">width="100%"</#if>><@message code="search.column.${c}"/>
                    <#if c=="ratingA" || c=="ratingG">
                    <@wm.info><@message code="search.column.${c}.description"/></@wm.info>
                </#if>
                </th>
            </#list>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
</div>

<script type="text/javascript">
    var playerSearch = new wm.game.Search(
            [<#list searchColumns as c>{
                "sName":'${c}',
                "mDataProp":'${c}',
                "bSortable": ${searchEntityDescriptor.getAttribute(c).sortable()?string}
            }<#if c_has_next>,</#if></#list>],
    ${scriplet?string}, {
                title:'<@message code="search.label"/>',
                close:'<@message code="button.close"/>'
            });
</script>
