<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../utils.ftl" as notify>

<p> Ход в игре <@notify.board board=context/> был передан вам</p>

<p>
    У вас есть <em>${gameMessageSource.formatRemainedTime(context, locale)}</em> чтобы совершить ход. В противном
    случае игра будет прарвана и вам засчитано поражение.
</p>

<p>
    Обратите внимание, что состояние более одной игры могло быть изменено но только одно письмо было послано.
</p>