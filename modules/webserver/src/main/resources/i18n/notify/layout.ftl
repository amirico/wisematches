<#-- @ftlvariable name="template" type="java.lang.String" -->
<#-- @ftlvariable name="publisher" type="java.lang.String" -->

<#include "${publisher}_header.ftl">
<#include "${template?replace('.', '/')}.ftl">
<#include "${publisher}_footer.ftl">