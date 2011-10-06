<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#-- @ftlvariable name="scribbleStatisticManager" type="wisematches.playground.tracking.PlayerStatisticManager" -->
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
            Время вашего хода истекло и игра была прервана.
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

<#if context.ratedGame>
<p>
    Рейтинги всех игроков (за исключением роботов) были пересчитаны:
<table>
    <tr>
        <td></td>
        <td align="center"><b>Очки</b></td>
        <td align="center"><b>Старый Рейтинг</b></td>
        <td align="center"><b>Новый Рейтинг</b></td>
    </tr>
    <#list context.ratingChanges as change>
        <tr>
            <td><@notify.player player=change.playerId/> <#if change.playerId==principal.id>(<b>Вы</b>)</#if></td>
            <td align="center">${change.points}</td>
            <td align="center">${change.oldRating}</td>
            <td align="center">${change.newRating}</td>
        </tr>
    </#list>
</table>
</p>
<p>
    Вы можете узнать больше о способе расчета рейтингов здесь: <@notify.link href="info/rating"/>.
</p>
<#else>
<p>
    Данная игра не влияет на рейтинг и ваш рейтинг не изменился
    и равен ${context.getRatingChange(context.getPlayerHand(principal.id)).newRating?string}.
</p>
</#if>
