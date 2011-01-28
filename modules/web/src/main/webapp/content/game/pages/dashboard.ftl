<#include "/core.ftl">

This is dashboard

<br>

<@security.authorize access="hasRole(\"guest\")">
<font color="red">asd</font>
</@security.authorize>

<br>


<a href="/account/modify.html">Modify Account</a>