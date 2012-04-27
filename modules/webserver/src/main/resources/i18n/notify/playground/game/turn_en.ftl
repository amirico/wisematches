<#-- @ftlvariable name="context" type="java.util.Map<String,Object>" -->
<#-- @ftlvariable name="board" type="wisematches.playground.GameBoard" -->
<#-- @ftlvariable name="changes" type="java.util.List<wisematches.playground.GameMove>" -->
<#assign board=context.board/>
<#assign changes=context.changes/>
<#import "../../utils.ftl" as util>

<p>
    It's your turn again in the game <@util.board board=board/>. You have
    <em>${gameMessageSource.formatRemainedTime(board, locale)}</em> to make a turn or game will be timed out
    and you will be defeated.
</p>

<p>
    Your <#if changes?size==1>opponent has<#else>opponents have</#if> made a move:
<ul>
<#list changes as move>
    <li>
        <@util.player player=move.playerMove.playerId/> has
        <#switch move.playerMove.class.simpleName>
            <#case "ExchangeTilesMove">
                exchanged ${move.playerMove.tilesIds?size} tile(s)
                <#break>
            <#case "MakeWordMove">
                made word '<em>${move.playerMove.word.text}</em>' to ${move.points} points
                <#break>
            <#case "PassTurnMove">
                passed a turn
                <#break>
        </#switch>
        <#if move_has_next>;<#else>.</#if>
    </li>
</#list>
</ul>
</p>

<p>
    Please note that more than one game has been updated but only one email has been sent.
</p>