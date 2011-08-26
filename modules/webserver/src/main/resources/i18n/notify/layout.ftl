<#-- @ftlvariable name="code" type="java.lang.String" -->
<#-- @ftlvariable name="publisher" type="java.lang.String" -->

<#include "${publisher}_header.ftl">
<#include "${code?replace('.', '/')}.ftl">
<#include "${publisher}_footer.ftl">