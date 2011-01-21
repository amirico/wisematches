<#include "/core.ftl">

<@wisematches.html i18n=["login"] scripts=["/content/login/login.js"] styles=["/content/login/login.css"] >
<script type="text/javascript">
    Ext.onReady(function() {
        wm.account.showRecoveryAccountWindow();
    });
</script>
</@wisematches.html>