<#-- @ftlvariable name="context" type="wisematches.playground.GameBoard" -->
<#import "/notice/macro.ftl" as notice>

<@notice.html subject="Game has been finished">
<p> A game &lt;@notify.board board=context/&gt; has been finished</p>

<p>
    &amp;amp;lt;#switch context.gameResolution&amp;amp;gt;
    &amp;amp;lt;#case &amp;amp;quot;RESIGNED&amp;amp;quot;&amp;amp;gt;
    &amp;amp;lt;#if context.playerTurn.playerId == principal.id&amp;amp;gt;
    You have resigned a game.
    &amp;amp;lt;#else&amp;amp;gt;
    The player &amp;amp;lt;@notify.player pid=context.playerTurn.playerId/&amp;amp;gt; has
    resigned a game.
    &amp;amp;lt;/#if&amp;amp;gt;
    &amp;amp;lt;#break&amp;amp;gt;
    &amp;amp;lt;#case &amp;amp;quot;TIMEOUT&amp;amp;quot;&amp;amp;gt;
    &amp;amp;lt;#if context.playerTurn.playerId == principal.id&amp;amp;gt;
    Your move time run up and the game has bnotifyterrupted.
    &amp;amp;lt;#else&amp;amp;gt;
    The player &amp;amp;lt;@notice.player pid=context.playerTurn.playerId/&amp;amp;gt; move
    time run up and the game has been interrupted.
    &amp;amp;lt;/#if&amp;amp;gt;
    &amp;amp;lt;#break&amp;amp;gt;
    &amp;amnotifycase &amp;amp;quot;STALEMATE&amp;amp;quot;&amp;amp;gt;
    There is no winner. Stalemate.
    &amp;amp;lt;#break&amp;amp;gt;
    &amp;amp;lt;#default&amp;amp;gt;
    The following player/players have won the game:
    &amp;amp;lt;#list context.wonPlayers as w&amp;amp;gt;
    &amp;amp;lt;@notice.player pid=w.playerId/&amp;amp;gt;&amp;amp;lt;#if w_has_next&amp;amp;gt;, &amp;amp;lt;/#if&amp;amp;gt;&amp;amp;lt;/#list&amp;amp;gt;
    &amp;amp;lt;/#switch&amp;amp;gt;
</p>
</@notice.html>