<#include "/core.ftl">

<div id="recovery">
    <div class="label"><@message code="account.recovery.label"/></div>

    <div class="description"><@message code="account.recovery.description"/></div>

    <div>
        <form id="recoveryForm" action="/account/recovery.html" method="post">
            <div>
            <#--@declare id="email"-->
                <label for="email"><@message code="account.register.email.label"/>:</label>
            <@wisematches.fieldInput path="recovery.email"/>
                <span class="sample"><@message code="account.register.email.description"/></span>
            </div>
            <div>
                <button id="recoveryAccount"
                        name="recoveryAccount"
                        type="submit"
                        value="submit"><@message code='account.recovery.submit.label'/></button>
            </div>
        </form>
    </div>

    <div>
        <div class="description"><@message code="account.recovery.new.description"/></div>
    </div>
</div>
