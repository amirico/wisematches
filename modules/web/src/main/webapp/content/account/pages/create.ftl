<#include "/core.ftl">

<form action="/account/create.html" method="post">
    <table cellpadding="0" cellspacing="0" border="0">
        <tr>
            <td>
                Nickname
            </td>
            <td>
            <@spring.formInput "registration.nickname"/>
                <@spring.bind path="registration.nickname"/>
            </td>
        </tr>
        <tr>
            <td>
                EMail
            </td>
            <td>
            <@spring.formInput "registration.email"/>
                <#--<@spring.bind path="email"/>-->
            </td>
        </tr>
        <tr>
            <td>
                Password
            </td>
            <td>
            <@spring.formPasswordInput "registration.password"/>
                <#--<@spring.bind path="password"/>-->
            </td>
        </tr>
        <tr>
            <td>
            </td>
            <td>
            ${spring.status}
            <#list spring.status.errorMessages as error> <b>${error}</b> <br> </#list>
            </td>
        </tr>
        <tr>
            <td>
            </td>
            <td>
                <input type="submit" value="submit"/>
            </td>
        </tr>
    </table>
</form>