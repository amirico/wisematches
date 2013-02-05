<#-- @ftlvariable name="advertisementBlock" type="wisematches.server.services.reclame.AdvertisementBlock" -->

<#if advertisementBlock?? && advertisementBlock?has_content>
    <#if advertisementBlock.provider.name() == 'GOOGLE'>
    <script type="text/javascript"><!--
    google_ad_client = "${advertisementBlock.client}";
    google_ad_slot = "${advertisementBlock.slot}";
    google_ad_width = ${advertisementBlock.width};
    google_ad_height = ${advertisementBlock.height};
    //-->
    </script>
    <script type="text/javascript"
            src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
    </script>
    <#else>
    </#if>
</#if>