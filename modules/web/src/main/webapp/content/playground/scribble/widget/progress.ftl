<#-- @ftlvariable name="boardInfo" type="wisematches.server.web.servlet.sdo.scribble.board.GameInfo" -->
<#-- @ftlvariable name="boardSettings" type="wisematches.playground.scribble.settings.BoardSettings" -->

<#include "/core.ftl">

<#macro row activeVisible=true passiveVisible=true>
    <#if !passiveVisible && !boardInfo.active> <#-- nothing to do if not active and -->
        <#return>
    </#if>

<tr class="<#if !activeVisible || !passiveVisible>state-change-marker</#if><#if boardInfo.active && !activeVisible> ui-helper-hidden</#if>">
    <#nested>
</tr>
</#macro>

<#macro separator activeVisible=true passiveVisible=true>
    <@row activeVisible=activeVisible passiveVisible=passiveVisible>
    <td colspan="2">
        <div class="ui-widget-content ui-widget-separator"></div>
    </td>
    </@row>
</#macro>

<#macro element activeVisible=true passiveVisible=true showSeparator=true>
    <#if showSeparator><@separator activeVisible=activeVisible passiveVisible=passiveVisible/></#if>
    <@row activeVisible=activeVisible passiveVisible=passiveVisible><#nested></@row>
</#macro>

<@wm.ui.widget class="gameInfo" title="game.state.label" help="board.progress">

<table width="100%" border="0">
    <@element showSeparator=false>
        <td nowrap="nowrap"><strong><@message code="game.state.started"/>:</strong></td>
        <td width="100%">
            <div class="gameStartedTime">${boardInfo.startedTime.text}</div>
        </td>
    </@element>

    <@element activeVisible=false>
        <td nowrap="nowrap"><strong><@message code="game.state.finished"/>:</strong></td>
        <td>
            <div class="gameFinishedTime">
                <#if boardInfo.finishedTime??>${boardInfo.finishedTime.text}</#if>
            </div>
        </td>
    </@element>

    <@element passiveVisible=false>
        <td nowrap="nowrap" valign="top"><strong><@message code="game.state.progress"/>:</strong></td>
        <td style="padding-top: 2px;">
            <div class="game-progress ui-progressbar">
                <div class="ui-progressbar-value ui-corner-left game-progress-board" style="width:0"></div>
                <div class="ui-progressbar-value game-progress-bank" style="width:0"></div>
                <div class="ui-progressbar-value ui-corner-right game-progress-hand" style="width:0"></div>
                <div class="game-progress-caption sample"></div>
            </div>
            <div class="sample" style="text-align: center; font-size: 10px">
                <@message code="game.state.progress.sample"/>
            </div>
        </td>
    </@element>

    <@element activeVisible=false>
        <td nowrap="nowrap" valign="top"><strong><@message code="game.state.resolution"/>:</strong></td>
        <td>
            <div class="gameResolution">
                <div class="ui-progressbar game-progress">
                    <div class="ui-progressbar-value ui-corner-all game-progress-finished game-progress-caption sample"></div>
                </div>
                <div class="sample game-resolution-player"></div>
            </div>
        </td>
    </@element>

    <@element>
        <td nowrap="nowrap"><strong><@message code="game.state.language"/>:</strong></td>
        <td><@message code="language."+boardInfo.settings.language?lower_case/></td>
    </@element>

    <@element>
        <td nowrap="nowrap"><strong><@message code="game.state.spent"/>:</strong></td>
        <td>
            <div class="spentTime">${boardInfo.spentTime.text}</div>
        </td>
    </@element>

    <#if boardInfo.relationship??>
        <@element>
            <td nowrap="nowrap"><strong><@message code="game.state.relationship"/>:</strong></td>
            <td><@wm.board.relationship boardInfo.relationship, true/></td>
        </@element>
    </#if>

    <@element passiveVisible=false>
        <td nowrap="nowrap"><strong><@message code="game.state.time"/>:</strong></td>
        <td>${boardInfo.settings.daysPerMove} ${messageSource.formatDays(boardInfo.settings.daysPerMove, locale)} <@message code="game.state.move"/></td>
    </@element>

    <#if boardInfo.settings.scratch>
        <@element>
            <td colspan="2">
                <div class="game-state-scratch">
                    <span><@message code="game.state.scratch.label"/></span>
                    <@wm.ui.info><@message code="game.state.scratch.description"/></@wm.ui.info>
                </div>
            </td>
        </@element>
    </#if>
</table>
</@wm.ui.widget>

<script type="text/javascript">
    new wm.scribble.Progress(board, {
        'resolution.finished': "<@message code="game.resolution.finished"/>",
        'resolution.stalemate': "<@message code="game.resolution.stalemate"/>",
        'resolution.moves': "<@message code="game.resolution.moves"/>",
        'resolution.resigned': "<@message code="game.resolution.resigned"/>",
        'resolution.timeout': "<@message code="game.resolution.timeout"/>",
        'resolution.for': "<@message code="game.resolution.for"/>",
        'resolution.by': "<@message code="game.resolution.by"/>"
    });
</script>