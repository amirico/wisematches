<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../utils.ftl" as util>

<p> A game <@util.board board=context/> you are waiting for has been started</p>

<p>
<#if context.playerTurn.playerId == principal.id>
    <strong>It's you turn in the game</strong>. You have
    <em>${gameMessageSource.formatRemainedTime(context, locale)} </em> to make a turn or game will be timed out and
    you will be defeated.
    <#else>
        Turn was transmitted to player <@util.player player=context.playerTurn.playerId/>.
</#if>
</p>