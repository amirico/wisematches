<#-- @ftlvariable name="socialProviders" type="java.lang.String[]" -->
<#-- @ftlvariable name="connections" type="java.util.Map<String, org.springframework.social.connect.Connection<?>[]>" -->

<#include "/core.ftl">

<div class="common-settings ui-widget-content ui-state-default shadow ui-corner-all" style="background-image: none;">
    <table width="100%">
        <thead>
        <tr>
            <th>Сервис</th>
            <th width="100%">Имя</th>
            <th>Действия</th>
        </tr>
        </thead>

        <tbody>
        <#assign count=0/>
        <#list socialProviders as p>
            <#list connections[p] as c>
                <#assign count=count+1/>
            <tr>
                <td nowrap="nowrap" style="vertical-align: middle">
                    <a class="social-view-link" href="${c.profileUrl}"><i
                            class="social-logo social-icon-${p}"></i></a>
                </td>
                <td width="100%" align="left" style="vertical-align: middle">
                    <a href="${c.profileUrl}">${c.displayName}</a>
                </td>
                <td style="vertical-align: middle">
                    <input name="providerId" value="${c.key.providerId}" type="hidden"/>
                    <input name="providerUserId" value="${c.key.providerUserId}" type="hidden"/>

                    <button class="socialSettingsRemove" type="button">
                        Удалить
                    </button>
                </td>
            </tr>
            </#list>
        </#list>

        <#if count==0>
        <tr>
            <td colspan="3">
                У вас нет ни одного подключенного профиля социальных сетей. Что бы иметь возможность входить
                в магазин с помощью вашего социального профиля, пожалуйста, добавьте к вашему аккаунту.
            </td>
        </tr>
        </#if>
        </tbody>
    </table>

    <div style="padding-top: 20px">
        <div class="social-signin">
            <h2 style="margin: 0; padding: 0">Добавить профиль</h2>
        <#list socialProviders as p>
            <a class="social-signin-link" href="/account/social/start?provider=${p}"><i
                    class="social-icon-24 social-icon-${p}"></i></a>
        </#list>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(".socialSettingsRemove").click(function () {
        $(this).closest("tr").remove();
    });
</script>