<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="words" type="wisematches.playground.scribble.Word[]" -->
<#-- @ftlvariable name="scoreEngine" type="wisematches.playground.scribble.score.ScoreEngine" -->

<#include "/core.ftl"/>

<@wm.ui.table.dtinit/>

<#assign rows=messageSource.getMessage("game.play.captions", locale).toCharArray()/>

<div>
<#assign boardId=""/>
<#if board??>
    <#assign boardId=board.boardId/>
</#if>
    Moves list for board <#if board??>(<@wm.board.name board, true/>)</#if>:
    <form action="/maintain/admin/moves" method="post">
        <label>
            <input type="text" name="b" size="10" value="${boardId}">
        </label>
        <button type="submit">Check Moves</button>
    </form>
</div>

<#if !board??>
Unknown board. Please check board's id
<#else>
    <#if words?size == 0>
    No available moves
    <#else>

    <table id="wordsList" cellpadding="5" border="1">
        <thead>
        <tr>
            <th>Word</th>
            <th>Points</th>
            <th>Formula</th>
            <th>Direction</th>
            <th>Row</th>
            <th>Column</th>
        </tr>
        </thead>
        <tbody>
            <#list words as w>
                <#assign score=scoreEngine.calculateWordScore(board, w)/>
            <tr>
                <td>${w.text}</td>
                <td>${score.points}</td>
                <td>${score.formula}</td>
                <td>${w.direction?lower_case}</td>
                <td>${rows[w.position.row]}</td>
                <td>${w.position.column+1}</td>
            </tr>
            </#list>
        </tbody>
    </table>
    </#if>
</#if>

<script type="text/javascript">
    wm.ui.dataTable('#wordsList', {
//        "bStateSave":true,
//        "bFilter":false,
//                "bSortClasses":false,
        "aaSorting": [
            [1, 'desc']
        ]
        /*
                "aoColumns":[
                    null,
                    null,
                    null,
                    null,
                    { "bSortable":false },
                    { "bSortable":false }
                ],
                "oLanguage":language
        */
    });
</script>