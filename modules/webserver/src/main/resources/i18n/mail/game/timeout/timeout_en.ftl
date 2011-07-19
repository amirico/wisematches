<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "../../macro.ftl" as mail>

<@mail.html subject="Turn time is running out">
<p> Your move time is running out for board <@mail.board board=context/>
</p>

<p>
    You have <em>${timeLimitMessage}</em> to make a turn or game will be terminated and you will be defeated.
</p>

</@mail.html>