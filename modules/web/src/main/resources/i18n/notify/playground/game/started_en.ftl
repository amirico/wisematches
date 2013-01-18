<#-- @ftlvariable name="context" type="java.util.Map<String,Object>" -->
<#-- @ftlvariable name="board" type="wisematches.playground.GameBoard" -->
<#-- @ftlvariable name="hand" type="wisematches.playground.GamePlayerHand" -->
<#assign board=context.board/>

<#import "../../utils.ftl" as util>

<p>
    A game <@util.board board=board/> you are waiting for has been started.
</p>

<p>
    There
<#if board.playersHands?size == 2>is one player
<#else>are ${board.playersHands?size-1?string} players
</#if>
    joined to the game:
<#list board.playersHands as hand>
    <#if hand.playerId != principal.id>
        <@util.player player=hand.playerId showRating=true/><#if hand_has_next>,</#if>
    </#if>
</#list>
    who you will play with. Please be polite and have a fun.
</p>

<p>
<#if board.playerTurn.playerId == principal.id>
    <strong>It's you turn in the game</strong>. You have
    <em>${gameMessageSource.formatRemainedTime(board, locale)} </em> to make a turn or game will be timed out and
    you will be defeated.
<#else>
    Turn was transmitted to player <@util.player player=board.playerTurn.playerId/>.
</#if>
</p>