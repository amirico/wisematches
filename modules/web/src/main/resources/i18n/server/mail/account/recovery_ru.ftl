<#-- @ftlvariable name="recoveryToken" type="java.lang.String" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="Служба Аккаунтов WiseMatches">
<p>
    Чтобы изменить пароль аккаунта WiseMatches ${player.nickname} нажмите на расположенную ниже ссылку
</p>

<p>
<@mail.url path='${confirmationUrl}?language=${player.language.code()}&token=${recoveryToken}'/>
</p>
<p>
    Если эта ссылка не работает, откройте новое окно браузера, а затем скопируйте и вставьте URL-адрес в адресную
    строку.
    Спасибо за то, использование WiseMatches.
</p>

<br>
<p>
    Если вы начинали процедуру восстановления пароля WiseMatches и получение данного письма вызывает у вас недоумение,
    это значит что кто-то пытается взламать ваш WiseMatches аккаунт. Мы рекомендуем обратиться в службу поддержки
    WiseMatches по адресу: <@mail.mailto box="account-support"/>.
</p>
</@mail.html>