<#-- @ftlvariable name="context" type="java.util.Map<String,Object>" -->
<#-- @ftlvariable name="board" type="wisematches.playground.GameBoard" -->
<#-- @ftlvariable name="changes" type="java.util.List<wisematches.playground.GameMove>" -->
<#assign board=context.board/>
<#assign changes=context.changes/>
<#import "../../utils.ftl" as util>

<p>
    Ход в игре <@util.board board=board/> был передан вам. У вас есть
    <em>${gameMessageSource.formatRemainedTime(board, locale)}</em> чтобы совершить ход. В противном
    случае игра будет прарвана и вам засчитано поражение.
</p>

<p>
    <#if changes?size==1>Ваш противник завершил свой ход<#else>Ваши противники завершили свои ходы</#if>:
<ul>
<#list changes as move>
    <li>
        <@util.player player=move.playerMove.playerId/>
        <#switch move.playerMove.class.simpleName>
            <#case "ExchangeTilesMove">
                обменял(а) ${move.playerMove.tilesIds?size} фишку(-ек)
                <#break>
            <#case "MakeWordMove">
                составил(а) словов '<em>${move.playerMove.word.text}</em>' на ${move.points} очков
                <#break>
            <#case "PassTurnMove">
                пропустил(а) свой ход
                <#break>
        </#switch>
        <#if move_has_next>;<#else>.</#if>
    </li>
</#list>
</ul>
</p>

<p>
    Обратите внимание, что состояние более одной игры могло быть изменено но только одно письмо было отправлено.
</p>