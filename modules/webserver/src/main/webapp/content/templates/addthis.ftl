<#-- @ftlvariable name="addThisCode" type="java.lang.String" -->

<#macro addthis href="" counter=false>
    <#if addThisCode??>
    <div class="addthis_toolbox addthis_default_style">
        <a class="addthis_button_facebook"></a>
        <a class="addthis_button_vk"></a>
        <a class="addthis_button_google_plusone_share"></a>
        <a class="addthis_button_odnoklassniki_ru"></a>
        <a class="addthis_button_mymailru"></a>
        <a class="addthis_button_twitter"></a>
        <a class="addthis_button_compact"></a>
        <#if counter><a class="addthis_counter addthis_bubble_style"></a></#if>
    </div>
    <script type="text/javascript">
        var addthis_config = {
            "data_track_addressbar":true,
            "ui_language":"${locale.language?lower_case}"
        };

            <#if href?? && href?has_content>
            var addthis_share = {
                "url":"${href}"
            };
            </#if>
    </script>
    <script type="text/javascript"
            src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=${addThisCode}"></script>
    </#if>
</#macro>