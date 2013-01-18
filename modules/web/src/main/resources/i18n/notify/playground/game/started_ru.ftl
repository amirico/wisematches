<#-- @ftlvariable name="context" type="java.util.Map<String,Object>" -->
<#-- @ftlvariable name="board" type="wisematches.playground.GameBoard" -->
<#-- @ftlvariable name="hand" type="wisematches.playground.GamePlayerHand" -->
<#assign board=context.board/>

<#import "../../utils.ftl" as util>

<p>
    Игра <@util.board board=board/>, которую вы ожидаете, началась.
</p>

<p>
    К игре
<#if board.playersHands?size == 2>присоединился один игрок:
<#else>присоединилось ${board.playersHands?size-1?string} игроков:
</#if>
<#list board.playersHands as hand>
    <#if hand.playerId != principal.id>
        <@util.player player=hand.playerId showRating=true/><#if hand_has_next>,</#if>
    </#if>
</#list>
    с которыми вы будите состязаться. Будьте вежливы и приятного времяпрепровождения.
</p>

<p>
<#if board.playerTurn.playerId == principal.id>
    <strong>Ход в этой игре передан вам</strong>. У вас есть
    <em>${gameMessageSource.formatRemainedTime(board, locale)}</em> для выполнения хода или игра
    будет прервана и вам будет засчитан проигрыш.
<#else>
    Ход был передан игроку <@util.player player=board.playerTurn.playerId/>.
</#if>
</p>