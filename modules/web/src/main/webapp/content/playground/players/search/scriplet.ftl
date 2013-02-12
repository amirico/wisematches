<#-- @ftlvariable name="scriplet" type="java.lang.Boolean" -->
<#-- @ftlvariable name="searchColumns" type="java.util.Collection<java.lang.String>" -->
<#-- @ftlvariable name="searchEntityDescriptor" type="wisematches.core.search.descriptive.SearchableDescriptor" -->
<#-- @ftlvariable name="searchArea" type="wisematches.server.services.relations.PlayerSearchArea" -->
<#-- @ftlvariable name="searchAreas" type="wisematches.server.services.relations.PlayerSearchArea[]" -->
<#include "/core.ftl">

<@wm.ui.table.dtinit/>

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
            <#list searchColumns as c>
                <th <#if c="nickname">width="100%"</#if>><@message code="search.column.${c}"/>
                    <#if c=="ratingA" || c=="ratingG">
                    <@wm.ui.info><@message code="search.column.${c}.description"/></@wm.ui.info>
                </#if>
                </th>
            </#list>
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
            [<#list searchColumns as c>{
                "sName": '${c}',
                "mDataProp": '${c}',
                "bSortable": ${searchEntityDescriptor.getProperty(c).sortable()?string}
            }<#if c_has_next>,</#if></#list>],
    ${scriplet?string}, {
                title: '<@message code="search.label"/>',
                close: '<@message code="button.close"/>'
            });
</script>