<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="boardSettings" type="wisematches.playground.scribble.settings.BoardSettings" -->
<#include "/core.ftl">

<#include "/content/playground/dictionary/card.ftl"/>

<@wm.ui.widget class="dictionaryWidget" title="game.dictionary.label" help="board.dictionary">
<table width="100%">
    <tr>
        <td width="100%" valign="middle">
            <label>
                <input class="word-value" style="width: 100%;">
            </label>
        </td>
        <td nowrap="nowrap" align="center">
            <button class="word-control" style="width: 22px; height: 22px"></button>
        </td>
    </tr>
</table>
</@wm.ui.widget>

<script type="text/javascript">
    var dictionary = new wm.scribble.Dictionary(board, dictionarySuggestion, ${boardSettings.checkWords?string});
</script>