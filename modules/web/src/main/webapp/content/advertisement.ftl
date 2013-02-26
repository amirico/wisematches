<#-- @ftlvariable name="advertisementBlockId" type="java.lang.String" -->
<#-- @ftlvariable name="advertisementManager" type="wisematches.server.services.reclame.AdvertisementManager" -->

<#if !principal.type.member || principal.membership.basic>
    <#assign advertisementBlock=advertisementManager.getAdvertisementBlock(advertisementBlockId, locale)!""/>
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
</#if>
