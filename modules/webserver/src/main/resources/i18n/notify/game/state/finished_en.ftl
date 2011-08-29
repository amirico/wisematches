<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../utils.ftl" as notify>

<p> A game <@notify.board board=context/> has been finished</p>

<p>
<#switch context.gameResolution>
    <#case "RESIGNED">
        <#if context.playerTurn.playerId == principal.id>
            You have resigned a game.
            <#else>
                The player <@notify.player player=context.playerTurn.playerId/> has
                resigned a game.
        </#if>
        <#break>
    <#case "TIMEOUT">
        <#if context.playerTurn.playerId == principal.id>
            Your move time run up and the game has been interrupted.
            <#else>
                The player <@notify.player player=context.playerTurn.playerId/> move
                time run up and the game has been interrupted.
        </#if>
        <#break>
    <#case "STALEMATE">
        There is no winner. Stalemate.
        <#break>
    <#default>
        The following player/players have won the game:
        <#list context.wonPlayers as w>
        <@notify.player player=w.playerId/><#if w_has_next>, </#if></#list>
</#switch>
</p>