<#-- @ftlvariable name="tilesBankInfo" type="char[][]" -->
<#-- @ftlvariable name="board" type="wisematches.server.gameplaying.scribble.board.ScribbleBoard" -->

<#macro tilePlain number cost letter i j>
<div <#if number??>id="tile${number}"</#if> class="tile cost${cost}"
     style="left: ${i*22}px; top: ${j*22}px; background-position: -${cost*22}px -${j*22}px">
    <span>${letter?upper_case}</span>
</div>
</#macro>

<#macro tileObject tile i j><@tilePlain number=tile.number cost=tile.cost letter=tile.letter i=i j=j/></#macro>