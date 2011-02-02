<#-- @ftlvariable name="serverHostName" type="java.lang.String" -->
<#macro html subject>
<html>
<head>
    <title>${subject}</title>
</head>
<body>
<p><#include "common/header.ftl"></p>

<p><#nested/></p>

<p><#include "common/footer.ftl"></p>
</body>
</html>
</#macro>

<#macro url path>
<a href="http://www.${serverHostName}/${path}">http://www.${serverHostName}/${path}</a>
</#macro>