<#-- @ftlvariable name="board" type="wisematches.playground.GameBoard" -->
<#-- @ftlvariable name="resolution" type="wisematches.playground.GameResolution" -->
<#-- @ftlvariable name="winners" type="java.util.Collection<wisematches.playground.GamePlayerHand>" -->
<#import "../../utils.ftl" as util>
<#assign board=context.board/>
<#assign resolution=context.resolution/>
<#assign winners=context.winners/>

<p> Игра <@util.board board=board/> была завершена</p>

<p>
<#switch resolution>
    <#case "RESIGNED">
        <#if board.playerTurn.playerId == principal.id>
            Игра была прервана вами.
        <#else>
            Игрок <@util.player player=board.playerTurn.playerId/> прервал
            игру.
        </#if>
        <#break>
    <#case "TIMEOUT">
        <#if board.playerTurn.playerId == principal.id>
            Время вашего хода истекло и игра была прервана.
        <#else>
            Время хода игрока <@util.player player=board.playerTurn.playerId/>
            истекло и игра была прервана.
        </#if>
        <#break>
    <#case "STALEMATE">
        В игре нет победителя. Ничья.
        <#break>
    <#default>
        Победитель/победители в игре:
        <#list winners as w>
            <@util.player player=w.playerId/><#if w_has_next>, </#if></#list>
</#switch>
</p>

<#if board.rated>
<p>
    Рейтинги всех игроков (за исключением роботов) были пересчитаны:
<table>
    <tr>
        <td></td>
        <td align="center"><b>Очки</b></td>
        <td align="center"><b>Старый Рейтинг</b></td>
        <td align="center"><b>Новый Рейтинг</b></td>
    </tr>
    <#list board.ratingChanges as change>
        <tr>
            <td><@util.player player=change.playerId/> <#if change.playerId==principal.id>(<b>Вы</b>)</#if></td>
            <td align="center">${change.points}</td>
            <td align="center">${change.oldRating}</td>
            <td align="center">${change.newRating}</td>
        </tr>
    </#list>
</table>
</h>
<p>
    Вы можете узнать больше о способе расчета рейтингов здесь: <@util.link href="info/rating"/>.
</p>
<#else>
<p>
    Данная игра не влияет на рейтинг и ваш рейтинг не изменился
    и равен ${board.getRatingChange(board.getPlayerHand(principal.id)).newRating?string}.
</p>
</#if>
