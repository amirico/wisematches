<#-- @ftlvariable name="headerTitle" type="java.lang.String" -->
<#include "/core.ftl">

<#macro html styles=[] scripts=[] i18n=[] headers=[]>
<html>
<head>
    <title id="page-title"><@message code="${headerTitle!'wisematches.title'}"/></title>
    <meta http-equiv="Content-type" content="text/html; charset=UTF-8">

    <link rel="stylesheet" type="text/css" href="/content/common/core.css">
    <link rel="stylesheet" type="text/css" href="/ext/resources/css/ext-all.css">
    <#list styles as style>
        <link rel="stylesheet" href="${style}">
    </#list>

    <script type="text/javascript" src="/i18n/common/<@message code="locale"/>.js"></script>
    <#list i18n as i>
        <script type="text/javascript" src="/i18n/${i}/<@message code="locale"/>.js"></script>
    </#list>
    <script type="text/javascript" src="/ext/debug/ext-base-debug.js"></script>
    <script type="text/javascript" src="/ext/debug/ext-all-debug.js"></script>

    <script type="text/javascript" src="/ext/ext-ux-wm.js"></script>
    <script type="text/javascript" src="/content/common/core.js"></script>

    <!-- Some extension. TODO: add only for a few pages where it's really required. &ndash;&gt; -->
    <script type="text/javascript" src="/content/account/account.js"></script>

    <script type="text/javascript" src="/dwr/engine.js"></script>
    <script type="text/javascript" src="/dwr/util.js"></script>

    <#list scripts as script>
        <script type="text/javascript" src="${script}"></script>
    </#list>

    <#list headers as header>
        <#include "${header}">
    </#list>
</head>
<body>
    <#nested>
</body>
</html>
</#macro>