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

<#macro host>${serverHostName}</#macro>

<#macro mailto box><a href="mailto:${box}@${serverHostName}">${box}@${serverHostName}</a></#macro>

<#macro url path><a href="http://www.${serverHostName}/${path}">http://www.${serverHostName}/${path}</a></#macro>