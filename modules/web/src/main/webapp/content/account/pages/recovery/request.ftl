<#include "/core.ftl">

<div id="recovery" style="width: 550px">
    <div id="info-recovery-header" class="info-header">
        <div class="info-label"><@message code="account.recovery.request.label"/></div>

        <div class="info-description"><@message code="account.recovery.request.description"/></div>
    </div>

    <div id="info-recovery-form">
        <form id="recoveryForm" action="request.html" method="post">
            <div>
            <#--@declare id="email"-->
                <label class="label" for="email"><@message code="account.register.email.label"/>:</label>
            <@wm.fieldInput path="recovery.email"/>
                <span class="sample"><@message code="account.recovery.email.description"/></span>
            </div>
            <div>
                <button id="recoveryAccount"
                        name="recoveryAccount"
                        type="submit"
                        value="submit"><@message code='account.recovery.submit.label'/></button>
            </div>
        </form>
    </div>

    <div id="info-recovery-footer">
        <div class="info-description"><@message code="account.recovery.request.info.description"/></div>
    </div>
</div>
