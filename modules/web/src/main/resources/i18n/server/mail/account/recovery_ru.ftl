<#-- @ftlvariable name="recoveryToken" type="java.lang.String" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="WiseMatches Account Assistance">
<p>
    Чтобы изменить пароль аккаунта WiseMatches ${player.nickname} нажмите на расположенную ниже ссылку
</p>

<p>
<@mail.url path='account/validation.html?language=${player.language.code()}&action=recovery&token=${recoveryToken}'/>
</p>
<p>
    Если эта ссылка не работает, откройте новое окно браузера, а затем скопируйте и вставьте URL-адрес в адресную
    строку.
    Спасибо за то, использование WiseMatches.
</p>

</@mail.html>