<#include "/core.ftl">

<@wisematches.html i18n=["login"]
scripts=["/content/login/login.js", "/dwr/interface/problemsReportService.js", "/dwr/interface/accountManagementService.js"] styles=["/content/login/login.css"] >
<script type="text/javascript">
    Ext.onReady(function() {
        wm.account.showRecoveryAccountWindow();
    });
</script>
</@wisematches.html>