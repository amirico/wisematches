<#-- @ftlvariable name="pageName" type="java.lang.String" -->
<#include "/core.ftl">

<#assign headerTitle="game.header"/>

<@wisematches.html title=headerTitle>
<style type="text/css">
    @import "/jquery/css/table_jui.css";
    @import "/jquery/css/table_col_reorder.css";
    @import "/jquery/css/table_cov_vis.css";

    body {
        margin: 0 !important;
    }
</style>

<script type="text/javascript" src="/jquery/dataTables.min.js"></script>
<script type="text/javascript" src="/jquery/dataTables-colVis.min.js"></script>
<script type="text/javascript" src="/jquery/dataTables-colReorder.min.js"></script>

    <#include "header.ftl">
    <#include "pages/${pageName}.ftl">

<#--
<div style="text-align:center;">
    <span class="copyrights"><@message "copyrights.label"/></span>
</div>
-->
</@wisematches.html>