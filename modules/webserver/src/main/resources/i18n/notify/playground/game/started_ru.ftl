<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../utils.ftl" as util>

<p>
    Игра <@util.board board=context/>, которую вы ожидаете, началась.
</p>

<p>
    К игре
<#if context.playersHands?size == 2>присоединился один игрок:
<#else>присоединилось ${context.playersHands?size-1?string} игроков:
</#if>
<#list context.playersHands as hand>
    <#if hand.playerId != principal.id>
        <@util.player player=hand.playerId showRating=true/><#if hand_has_next>,</#if>
    </#if>
</#list>
    с которыми вы будите состязаться. Будьте вежливы и приятного времяпрепровождения.
</p>

<p>
<#if context.playerTurn.playerId == principal.id>
    <strong>Ход в этой игре передан вам</strong>. У вас есть
    <em>${gameMessageSource.formatRemainedTime(context, locale)}</em> для выполнения хода или игра
    будет прервана и вам будет засчитан проигрыш.
<#else>
    Ход был передан игроку <@util.player player=context.playerTurn.playerId/>.
</#if>
</p>