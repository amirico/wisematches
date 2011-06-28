<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="Game has been finished">
<p> A game <@mail.board board=context/> has been finished</p>

<p>
    <#switch context.gameResolution>
        <#case "RESIGNED">
            <#if context.playerTurn.playerId == principal.id>
                You have resigned a game.
                <#else>
                    The player <@mail.player pid=context.playerTurn.playerId/> has
                    resigned a game.
            </#if>
            <#break>
        <#case "TIMEOUT">
            <#if context.playerTurn.playerId == principal.id>
                Your move time run up and the game has been interrupted.
                <#else>
                    The player <@mail.player pid=context.playerTurn.playerId/> move
                    time run up and the game has been interrupted.
            </#if>
            <#break>
        <#case "STALEMATE">
            There is no winner. Stalemate.
            <#break>
        <#default>
            The following player/players are won the game:
            <#list context.wonPlayers as w>
            <@mail.player pid=w.playerId/><#if w_has_next>, </#if></#list>
    </#switch>
</p>
</@mail.html>