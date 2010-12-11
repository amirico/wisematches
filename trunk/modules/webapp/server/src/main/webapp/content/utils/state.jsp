<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Simple jsp page</title>
    <META HTTP-EQUIV="Refresh" CONTENT="5">
</head>
<body>

<div>Current server time: <%=new java.util.Date()%>
</div>

<br>

<div>Memory info</div>
<div style="margin-left: 15px;">
    <div>
        Max memory: <%=Runtime.getRuntime().maxMemory() / 1024%>Mb
    </div>
    <div>
        Total memory: <%=Runtime.getRuntime().totalMemory() / 1024%>Mb
    </div>
    <div>
        Free memory: <%=Runtime.getRuntime().freeMemory() / 1024%>Mb
    </div>
</div>
</body>
</html>
