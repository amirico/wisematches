<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "/notice/macro.ftl" as notice>

<@notice.html subject="Игра была завершена">
<p> Игра <@notice.board board=context/> была завершена</p>

<p>
    <#switch context.gameResolution>
        <#case "RESIGNED">
            <#if context.playerTurn.playerId == principal.id>
                Игра была прервана вами.
                <#else>
                    Игрок <@notice.player pid=context.playerTurn.playerId/> прервал
                    игру.
            </#if>
            <#break>
        <#case "TIMEOUT">
            <#if context.playerTurn.playerId == principal.id>
                Время вашего хода истекло и игра была прервана.
                <#else>
                    Время хода игрока <@notice.player pid=context.playerTurn.playerId/>
                    истекло и игра была прервана.
            </#if>
            <#break>
        <#case "STALEMATE">
            В игре нет победителя. Ничья.
            <#break>
        <#default>
            Победитель/победители в игре:
            <#list context.wonPlayers as w>
            <@notice.player pid=w.playerId/><#if w_has_next>, </#if></#list>
    </#switch>
</p>
</@notice.html>