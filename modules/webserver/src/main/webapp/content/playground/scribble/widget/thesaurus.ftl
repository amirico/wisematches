<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="boardSettings" type="wisematches.playground.scribble.settings.BoardSettings" -->
<#-- @ftlvariable name="thesaurusHouse" type="wisematches.server.web.services.thesaurus.ThesaurusHouse" -->
<#include "/core.ftl">

<@wm.widget class="thesaurusWidget" title="game.thesaurus.label" help="board.thesaurus">
<table width="100%">
    <tr>
        <td width="100%"><input class="word-value" style="width: 100%"></td>
        <td nowrap="nowrap" align="center">
            <span class="word-status ui-icon icon-empty"></span>
        </td>
    </tr>
    <tr>
        <td>
            <button class="word-check"
                    onclick="thesaurus.checkWord(); return false;"><@message code="game.thesaurus.check"/></button>
        </td>
        <td>
            <button class="word-lookup" title="<@message code="game.thesaurus.lookup"/>"
                    onclick="thesaurus.lookupWord(); return false;">
                <span class="ui-icon ui-icon-search"></span>
            </button>
        </td>
    </tr>
</table>
</@wm.widget>

<script type="text/javascript">
    var thesaurus = new wm.scribble.Thesaurus(board, ${boardSettings.checkWords?string});
</script>