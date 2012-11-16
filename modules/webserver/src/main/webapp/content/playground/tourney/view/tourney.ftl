<#-- @ftlvariable name="tourney" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="divisionsTree" type="java.util.Map<wisematches.playground.tourney.regular.TourneyDivision, java.util.List<wisematches.playground.tourney.regular.TourneyRound>>" -->

<#include "/core.ftl">
<#include "../scriplet.ftl">

<@wm.playground id="tourneyEntity">
    <@tourneyName tourneyId=tourney link=false/>

    <#list divisionsTree.entrySet() as entity>
    <div>
    ${entity.key.language} + ${entity.key.section}
        <div style="padding-left: 10px">
            <#list entity.value as r>
            ${r.round},
            </#list>
        </div>
    </div>
    </#list>
</@wm.playground>