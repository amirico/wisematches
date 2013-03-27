<#include "/core.ftl">

<script type="text/javascript">
    wm.i18n.extend({<#list Language.values() as l>'language.${l.code}': '<@message code="language."+l.code/>',</#list> locale: '${locale}', 'button.close': '<@message code="button.close"/>', 'button.ok': '<@message code="button.ok"/>', 'button.cancel': '<@message code="button.cancel"/>'});
</script>
