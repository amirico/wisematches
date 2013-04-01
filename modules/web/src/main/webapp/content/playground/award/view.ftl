<#-- @ftlvariable name="awards" type="wisematches.server.services.award.Award[]" -->
<#-- @ftlvariable name="weight" type="wisematches.server.services.award.AwardWeight" -->
<#-- @ftlvariable name="descriptor" type="wisematches.server.services.award.AwardDescriptor" -->
<#include "/core.ftl"/>

<@wm.ui.table.dtinit/>

<@wm.ui.playground id="awardWidget">
<div class="awards">
    <@wm.ui.table.header>
        <@message code="awards.label"/> > <@message code="awards.view.label"/>
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar align="left" class="ui-state-hover">
        <div class="award ${descriptor.type.name()?lower_case}">
            <table>
                <tr>
                    <td rowspan="2"><@wm.award.image descriptor weight!"" false/></td>
                    <td>
                        <h1>
                            <@message code="awards.${descriptor.type.name()?lower_case}.label"/>
                        </h1>
                    </td>
                </tr>
                <tr>
                    <td>
                        <h1 class="ui-state-active" style="border: none; background: none">
                            <@message code="awards.${descriptor.name}.label"/><#if weight??>,
                            <@message code="awards.${weight.name()?lower_case}.label"/>
                        </#if>
                        </h1>
                    </td>
                </tr>
            </table>
        </div>
    </@wm.ui.table.toolbar>

    <@wm.ui.table.content>
        <table id="awardedPlayers" width="100%">
            <thead>
            <tr>
                <th><@message code="awards.recipient.label"/></th>
                <#if !weight??>
                    <th><@message code="awards.weight.label"/></th>
                </#if>
                <th><@message code="awards.awarded.label"/></th>
                <th><@message code="awards.relationship.label"/></th>
            </tr>
            </thead>
            <tbody>
                <#list awards as a>
                <tr>
                    <td><@wm.player.name personalityManager.getPerson(a.recipient)/></td>
                    <#if !weight??>
                        <td><@message code="awards.${a.weight.name()?lower_case}.label"/></td>
                    </#if>
                    <td>${messageSource.formatDate(a.awardedDate, locale)}</td>
                    <td>
                        <#if a.relationship??>
                            <#if a.relationship.code==1>
                                <p><@wm.tourney.tourney a.relationship.id, true/></p>
                            </#if>
                        </#if>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </@wm.ui.table.content>

    <@wm.ui.table.footer>
        &nbsp;
    </@wm.ui.table.footer>
</div>
</@wm.ui.playground>

<script type="text/javascript">
    wm.ui.dataTable('#awardedPlayers', {
        "bFilter": false,
        "bSortClasses": false,
        "aaSorting": [
            [<#if !weight??>2<#else>1</#if>, 'desc']
        ]
    });
</script>
