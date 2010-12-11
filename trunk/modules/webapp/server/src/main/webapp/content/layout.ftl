<#include "/core.ftl">

<#macro html title="WiseMatches" styles=[] scripts=[] i18n=[] headers=[]>
<html>
<head>
    <title id="page-title">${title}</title>
    <meta http-equiv="Content-type" content="text/html; charset=UTF-8">

    <link rel="stylesheet" type="text/css" href="/content/common/core.css">
    <link rel="stylesheet" type="text/css" href="/ext/resources/css/ext-all.css">
    <#list styles as style>
        <link rel="stylesheet" href="${style}">
    </#list>

    <#list i18n as i>
        <script type="text/javascript" src="/i18n/${i}/<@spring.message code="locale"/>.js"></script>
    </#list>
    <script type="text/javascript" src="/ext/adapter/ext/ext-base.js"></script>
    <script type="text/javascript" src="/ext/ext-all-debug.js"></script>
    <script type="text/javascript" src="/content/common/core.js"></script>
    <#list scripts as script>
        <script type="text/javascript" src="${script}"></script>
    </#list>

    <#list headers as header>
        <#include "${header}">
    </#list>
</head>
<body style="padding-left: 2px; padding-right: 2px;">
    <#nested>
</body>
</html>
</#macro>