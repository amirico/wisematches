<#-- @ftlvariable name="serverHostName" type="java.lang.String" -->
<#macro html subject>
<html>
<head>
    <title>${subject}</title>
</head>
<body>
<p><#include "header_ru.ftl"></p>

<p><#nested/></p>

<p><#include "footer_ru.ftl"></p>
</body>
</html>
</#macro>

<#macro board board>
<@link href="playground/scribble/board?b=${board.boardId}">#${board.boardId} (${board.gameSettings.title})</@link>
</#macro>

<#macro player pid>
<@link href="playground/profile/view?p=${pid}">
<em>${gameMessageSource.getPlayerNick(playerManager.getPlayer(pid), locale)}</em></@link>
</#macro>

<#macro link href><a href="http://${serverHostName}/${href}"><#nested/></a></#macro>

<#macro host>${serverHostName}</#macro>

<#macro mailto box><a href="mailto:${box}@${serverHostName}">${box}@${serverHostName}</a></#macro>

<#macro url path><a href="http://www.${serverHostName}/${path}">http://www.${serverHostName}/${path}</a></#macro>