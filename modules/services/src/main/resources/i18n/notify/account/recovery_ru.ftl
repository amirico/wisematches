<#-- @ftlvariable name="recoveryToken" type="java.lang.String" -->
<#import "../utils.ftl" as util>

<p>
    Данное письмо содержит код для смены вашего пароля. Скопируйте его в соответствующее поле восстановления и введите
    новый пароль для вашего WiseMatches аккаунта.
</p>
<p>
    Код восстановления: ${context.recoveryToken}.
</p>

<p>
    Вы так же можете восстановить пароль использую специальную ссылку:
<@util.link href='/account/recovery/confirmation?language=${context.principal.language.code()}&email=${context.principal.email}&token=${context.recoveryToken}'/>
</p>

<br>

<p>
    Если вы не начинали процедуру восстановления пароля WiseMatches и получение данного письма вызывает у вас
    недоумение,
    это значит что кто-то пытается взламать ваш WiseMatches аккаунт. Мы рекомендуем вам обратиться в службу поддержки
    WiseMatches по адресу: <@util.mailto box="account-support"/>.
</p>