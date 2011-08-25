<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "/notice/macro.ftl" as notice>

<@notice.html subject="Игра была завершена">
<p> Игра &lt;@notify.board board=context/&gt; была завершена</p>

<p>
    &amp;amp;lt;#switch context.gameResolution&amp;amp;gt;
    &amp;amp;lt;#case &amp;amp;quot;RESIGNED&amp;amp;quot;&amp;amp;gt;
    &amp;amp;lt;#if context.playerTurn.playerId == principal.id&amp;amp;gt;
    Игра была прервана вами.
    &amp;amp;lt;#else&amp;amp;gt;
    Игрок &amp;amp;lt;@notify.player pid=context.playerTurn.playerId/&amp;amp;gt; прервал
    игру.
    &amp;amp;lt;/#if&amp;amp;gt;
    &amp;amp;lt;#break&amp;amp;gt;
    &amp;amp;lt;#case &amp;amp;quot;TIMEOUT&amp;amp;quot;&amp;amp;gt;
    &amp;amp;lt;#if context.playerTurn.playerId == principal.id&amp;amp;gt;
    Время вашего хода истекло и игра была notifyна.
    &amp;amp;lt;#else&amp;amp;gt;
    Время хода игрока &amp;amp;lt;@notice.player pid=context.playerTurn.playerId/&amp;amp;gt;
    истекло и игра была прервана.
    &amp;amp;lt;/#if&amp;amp;gt;
    &amp;amp;lt;#break&amp;notify;
    &amp;amp;lt;#case &amp;amp;quot;STALEMATE&amp;amp;quot;&amp;amp;gt;
    В игре нет победителя. Ничья.
    &amp;amp;lt;#break&amp;amp;gt;
    &amp;amp;lt;#default&amp;amp;gt;
    Победитель/победители в игре:
    &amp;amp;lt;#list context.wonPlayers as w&amp;amp;gt;
    &amp;amp;lt;@notice.player pid=w.playerId/&amp;amp;gt;&amp;amp;lt;#if w_has_next&amp;amp;gt;, &amp;amp;lt;/#if&amp;amp;gt;&amp;amp;lt;/#list&amp;amp;gt;
    &amp;amp;lt;/#switch&amp;amp;gt;
</p>
</@notice.html>