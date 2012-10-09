<#-- @ftlvariable name="googlePlusEnabled" type="java.lang.Boolean" -->

<#if googlePlusEnabled??>
<script type="text/javascript">
    window.___gcfg = {lang:'${locale}'};
    (function () {
        var po = document.createElement('script');
        po.type = 'text/javascript';
        po.async = true;
        po.src = 'https://apis.google.com/js/plusone.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(po, s);
    })();
</script>
</#if>