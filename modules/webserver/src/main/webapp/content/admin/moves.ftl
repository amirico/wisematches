<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="words" type="wisematches.playground.scribble.Word[]" -->
<#-- @ftlvariable name="scoreEngine" type="wisematches.playground.scribble.score.ScoreEngine" -->

<#if !board??>
Unknown board. Please check board's id
<#else>
    <#if words?size == 0>
    No available moves
    <#else>
    <table cellpadding="5" border="1">
        <#list words as w>
            <#assign score=scoreEngine.calculateWordScore(w, board)/>
            <tr>
                <td>${w.text}</td>
                <td>${score.points}</td>
                <td>${score.formula}</td>
                <td>${w.direction?lower_case}</td>
                <td>${w.position}</td>
            </tr>
        </#list>
    </table>
    </#if>
</#if>
