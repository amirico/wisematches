<#-- @ftlvariable name="recoveryToken" type="java.lang.String" -->
<#import "../macro.ftl" as mail>

<@mail.html subject="Служба Аккаунтов WiseMatches">

<p>Информаци о вашем WiseMatches аккаунте была успешно изменена.</p>

<p>
    Коллектив WiseMatches
</p>

<br>
<p>
    Если вы не изменяли WiseMatches аккаунт и получение данного письма вызывает у вас недоумение, это значит что кто-то
    взломал ваш WiseMatches аккаунт. Мы рекомендуем обратиться в службу поддержки
    WiseMatches по адресу: <@mail.mailto box="account-support"/>.
</p>

</@mail.html>