<#-- @ftlvariable name="serverHostName" type="java.lang.String" -->
<#macro html subject>

<p><#nested/></p>

<p><#include "mail/footer_ru.ftl"></p>
</body>
</html>
</#macro>

<#macro board board><@link href="playground/scribble/board?b=${board.boardId}">#${board.boardId} (${board.gameSettings.title})</@link></#macro>

<#macro player pid><@link href="playground/profile/view?p=${pid}">
<em>${gameMessageSource.getPlayerNick(playerManager.getPlayer(pid), locale)}</em></@link></#macro>

<#macro link href target=""><a href="http://${serverHostName}/${href}"
                               <#if target?has_content>target="${target}"</#if>><#local content><#nested></#local><#if content?size != 0>${content}<#else>
    http://${serverHostName}/${href}</#if></a></#macro>

<#macro mailto box><a href="mailto:${box}@${serverHostName}">${box}@${serverHostName}</a></#macro>