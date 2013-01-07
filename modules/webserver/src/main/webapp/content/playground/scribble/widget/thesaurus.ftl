<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="boardSettings" type="wisematches.playground.scribble.settings.BoardSettings" -->
<#include "/core.ftl">

<#include "/content/playground/dictionary/card.ftl"/>

<@wm.ui.widget class="thesaurusWidget" title="game.thesaurus.label" help="board.thesaurus">
<table width="100%">
    <tr>
        <td width="100%" valign="middle">
            <input class="word-value" style="width: 100%;">
        </td>
        <td nowrap="nowrap" align="center">
            <button class="word-control" style="width: 22px; height: 22px"></button>
        </td>
    </tr>
</table>
</@wm.ui.widget>

<script type="text/javascript">
    var thesaurus = new wm.scribble.Thesaurus(board, dictionarySuggestion, ${boardSettings.checkWords?string});
</script>