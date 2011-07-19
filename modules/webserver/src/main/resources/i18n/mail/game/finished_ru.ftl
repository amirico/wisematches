<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="Игра была завершена">
<p> Игра <@mail.board board=context/> была завершена</p>

<p>
    <#switch context.gameResolution>
        <#case "RESIGNED">
            <#if context.playerTurn.playerId == principal.id>
                Игра была прервана вами.
                <#else>
                    Игрок <@mail.player pid=context.playerTurn.playerId/> прервал
                    игру.
            </#if>
            <#break>
        <#case "TIMEOUT">
            <#if context.playerTurn.playerId == principal.id>
                Время вашего хода истекло и игра была прервана.
                <#else>
                    Время хода игрока <@mail.player pid=context.playerTurn.playerId/>
                    истекло и игра была прервана.
            </#if>
            <#break>
        <#case "STALEMATE">
            В игре нет победителя. Ничья.
            <#break>
        <#default>
            Победитель/победители в игре:
            <#list context.wonPlayers as w>
            <@mail.player pid=w.playerId/><#if w_has_next>, </#if></#list>
    </#switch>
</p>
</@mail.html>