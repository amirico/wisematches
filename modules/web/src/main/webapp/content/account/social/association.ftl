<#-- @ftlvariable name="accounts" type="wisematches.core.personality.player.account.Account[]" -->
<#-- @ftlvariable name="connection" type="org.springframework.social.connect.Connection" -->

<#include "/core.ftl">

<html>
<head>
<#include "/content/meta.ftl"/>
</head>
<body>
<table style="height: 28px; padding: 3px; padding-bottom: 0">
    <tr>
        <td align="left">
            <div class="header-logo">
                <img alt="logo" src="<@wm.ui.static "images/logo/logo170x70x2.png"/>"/>
            </div>
        </td>

        <td align="left" valign="middle" style="padding-left: 20px; font-size: 20px">
            <strong>WiseMatches Авторизация: Социальные сети</strong>
        </td>
    </tr>
</table>

<div class="ui-widget-content" style="height:0; padding: 0; margin: 0"></div>

<#--<hr style="padding: 0; margin: 0">-->
<#assign provider=connection.key.providerId/>

<form action="/account/social/association" method="post">
    <div>
    <#if accounts?has_content>
        <div style="text-align: center; font-size: 18px">Выбор аккаунта</div>

        <div style="padding-top: 10px; padding-bottom: 10px">
            Пожалуйста, выберите пользователя, чей аккаунт вы хотели бы использовать для работы в нашей игре:
            <table>
                <#list accounts as a>
                    <tr>
                        <td>${a.nickname}</td>
                    </tr>
                </#list>
            </table>
        </div>
    <#else>
        <div style="text-align: justify; font-size: 14px; padding: 5px">
            Для завершения регистрации и возможности принять участие в играх WiseMatches Эрудит,
            пожалуйста, проверьте либо введите необходимую информацию о себе, которую мы не смогли
            получить из вашего профиля социальной сети
    <span style="font-size: 16px">
    <i class="social-icon-16 social-icon-${provider}"
       style="vertical-align: middle"></i> <strong>${connection.displayName!""}</strong></span>:
        </div>

        <div class="ui-widget-content"
             style="padding-top: 10px; padding-bottom: 10px; border-left: none; border-right: none; background: none !important;">
            <table width="100%" cellpadding="3px">
                <tr>
                    <td colspan="2">
                        <div class="section label"><@message code="account.register.group.required.label"/></div>
                    </td>
                </tr>

                <tr>
                    <td>
                    <#--@declare id="email"-->
                        <label for="email"><@message code="account.register.email.label"/>:</label>
                    </td>
                    <td>
                        <@wm.ui.input path="registration.email"/>
                        <span class="sample"><@message code="account.register.email.description"/></span>
                    </td>
                </tr>

                <tr>
                    <td>
                    <#--@declare id="nickname"-->
                        <label for="nickname"><@message code="account.register.nickname.label"/>:</label>
                    </td>
                    <td>
                        <@wm.ui.input path="registration.nickname"/>
                        <span class="sample"><@message code="account.register.nickname.description"/></span>
                    </td>
                </tr>

                <tr>
                    <td>
                    <#--@declare id="password"-->
                        <label for="password"><@message code="account.register.pwd.label"/>:</label>
                    </td>
                    <td>
                        <@wm.ui.input path="registration.password" fieldType="hidden"/>
                    </td>
                </tr>
                <tr>
                    <td>
                    <#--@declare id="confirm"-->
                        <label for="confirm"><@message code="account.register.pwd-cfr.label"/>:</label>
                    </td>
                    <td>
                        <@wm.ui.input path="registration.confirm" fieldType="hidden"/>
                    </td>
                </tr>

                <tr>
                    <td colspan="2">
                        <hr>
                    </td>
                </tr>

                <tr>
                    <td colspan="2">
                        <div class="section label"><@message code="account.register.group.getstarted.label"/></div>
                    </td>
                </tr>

                <tr>
                    <td>
                        <label for="language"><@message code="account.register.language.label"/>:</label>
                    </td>
                    <td>
                        <@wm.ui.field path="registration.language">
                            <select id="language" name="language" style="width: 170px;">
                                <#list ["en", "ru"] as l>
                                    <option value="${l}" <#if (locale==l)>selected="selected"</#if>>
                                        <@message code="language.${l?lower_case}"/>
                                    </option>
                                </#list>
                            </select>
                        </@wm.ui.field>
                        <input type="hidden" id="timezone" name="timezone" value="0">
                        <script type="text/javascript">
                            document.getElementById('timezone').value = new Date().getTimezoneOffset();
                        </script>
                        <span class="sample"><@message code="account.register.language.description"/></span>
                    </td>
                </tr>
                <tr>
                    <td>
                    </td>
                    <td>
                        <input type="hidden" id="timezone" name="timezone" value="0">
                        <script type="text/javascript">
                            document.getElementById('timezone').value = new Date().getTimezoneOffset();
                        </script>
                        <span class="sample"><@message code="account.register.language.description"/></span>
                    </td>
                </tr>

                <tr>
                    <td>
                        <label><@message code='account.register.terms.label'/>:</label>
                    </td>
                    <td align="left">
                        <div style="padding-bottom: 10px;"><@message code="account.register.terms.description"/></div>

                        <button id="createAccount"
                                name="createAccount"
                                type="submit"
                                value="submit"><@message code='account.register.submit.label'/></button>
                    </td>
                </tr>
            </table>
        </div>
    </#if>
    <#--

        <#if !accounts?has_content>
            <hr>
            <div style="padding: 3px">
                <button name="action" value="new">
                    Создать WiseMatches Аккаунт
                </button>
                <br>
                * Если вы уже зарегистрированы в нашей игре с помощью логина/пароля либо любой другой
                социальной сети, пожалуйста, закройте данное окно и войдите в аккаунт игры ранее использованным способом.
                После этого вы сможете добавить связи с <i><@message code="account.social.type.${provider}"/></i> в ваших
                настройках.
            </div>
        </#if>
    -->
    </div>
</form>

</body>
</html>