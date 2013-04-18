<#-- @ftlvariable name="result" type="java.lang.String" -->
<#include "/core.ftl"/>

<form action="/maintain/admin/awards" method="post">
    <div>
        <label>
            Player: <input type="text" name="p" size="10" value="">
        </label>
    </div>
    <div>
        <label>
            Award:
            <select name="a">
                <option value="tourney.winner">Tourney</option>
                <option value="robot.conqueror">Robot</option>
                <option value="dictionary.editor">Dictionary</option>
            </select>
        </label>
    </div>
    <div>
        <label>
            Weight:
            <select name="w">
            <#list AwardWeight.values() as w>
                <option value="${w.name()}">${w.name()}</option>
            </#list>
            </select>
        </label>
    </div>

<#if result??>
    <div>
        Result: ${result}
    </div>
</#if>


    <button type="submit">Award</button>
</form>
