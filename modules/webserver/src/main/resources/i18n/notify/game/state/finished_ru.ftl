<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../utils.ftl" as notify>

<p> Игра <@notify.board board=context/> была завершена</p>

<p>
<#switch context.gameResolution>
    <#case "RESIGNED">
        <#if context.playerTurn.playerId == principal.id>
            Игра была прервана вами.
            <#else>
                Игрок <@notify.player player=context.playerTurn.playerId/> прервал
                игру.
        </#if>
        <#break>
    <#case "TIMEOUT">
        <#if context.playerTurn.playerId == principal.id>
            Время вашего хода истекло и игра была notifyна.
            <#else>
                Время хода игрока <@notify.player player=context.playerTurn.playerId/>
                истекло и игра была прервана.
        </#if>
        <#break>
    <#case "STALEMATE">
        В игре нет победителя. Ничья.
        <#break>
    <#default>
        Победитель/победители в игре:
        <#list context.wonPlayers as w>
        <@notify.player player=w.playerId/><#if w_has_next>, </#if></#list>
</#switch>
</p>