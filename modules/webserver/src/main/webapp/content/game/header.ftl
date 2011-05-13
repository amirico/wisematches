<#-- @ftlvariable name="pageName" type="java.lang.String" -->
<#include "/core.ftl">

<table id="game-header">
    <tr>
        <td width="178px" height="72px">
            <img id="header-image" src="/resources/images/logo.png" width="178" height="72" alt="logo"/>
        </td>
        <td width="100%" height="72px">
            <table style="height: 100%; width: 100%">
                <tr>
                    <td valign="top" align="right">
                    <#if player.membership == 'GUEST'>
                        <span class="player computer"><@message code="game.player.guest"/></span>
                        <#else>
                            <span class="player member">
                                <a href="/game/profile.html?p=${player.id}">
                                    <span class="nickname">${player.nickname} (${player.account.email})</span>
                                </a>
                            </span>
                    </#if>
                        |
                        <a href="/account/logout.html"><@message code="account.signout.label"/></a>
                    </td>
                </tr>
                <tr>
                    <td valign="bottom">
                        <div id="game-toolbar" class="" align="right">
                            <div style="float: left;">
                                <button id="dashboardButton" onclick="wm.util.url.redirect('/game/dashboard.html')">
                                <@message code="game.menu.games.label"/>
                                </button>
                                <button id="gameboardButton" onclick="wm.util.url.redirect('/game/gameboard.html')">
                                <@message code="game.menu.join.label"/>
                                </button>
                                <button id="createButton" onclick="wm.util.url.redirect('/game/create.html')">
                                <@message code="game.menu.create.label"/>
                                </button>
                            </div>
                            <div style="float: left;">
                                <button id="messagesButton" onclick="wm.util.url.redirect('/profile/messages.html')">
                                <@message code="game.menu.messages.label"/>
                                </button>
                            </div>
                            <div style="float: left;">
                                <button id="tournamentsButton" onclick="wm.util.url.redirect('/game/tournaments.html')">
                                <@message code="game.menu.tournaments.label"/>
                                </button>
                            </div>
                            <div style="margin: 0">
                                <button id="modifyButton" onclick="wm.util.url.redirect('/account/modify.html')">
                                <@message code="game.menu.settings.label"/>
                                </button>
                                <button onclick="wm.util.url.redirect('/info/general.html')">
                                <@message code="game.menu.help.label"/>
                                </button>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<script type="text/javascript">
    $("#game-toolbar div").buttonset();
</script>

<div style="height: 20px;"></div>