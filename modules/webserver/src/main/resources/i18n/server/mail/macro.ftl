<#-- @ftlvariable name="serverHostName" type="java.lang.String" -->
<#macro html subject>
<html>
<head>
    <title>${subject}</title>
</head>
<body>
<p><#include "header.ftl"></p>

<p><#nested/></p>

<p><#include "footer.ftl"></p>
</body>
</html>
</#macro>

<#macro board board>
<a href="http://${serverHostName}/playground/scribble/board?b=${board.boardId}">
    #${board.boardId} (${board.gameSettings.title})
</a>
</#macro>

<#macro player pid>
<a href="http://${serverHostName}/playground/profile/view?p=${pid}"><em>${gameMessageSource.getPlayerNick(playerManager.getPlayer(pid), locale)}</em></a>
</#macro>

<#macro host>${serverHostName}</#macro>

<#macro mailto box><a href="mailto:${box}@${serverHostName}">${box}@${serverHostName}</a></#macro>

<#macro url path><a href="http://www.${serverHostName}/${path}">http://www.${serverHostName}/${path}</a></#macro>