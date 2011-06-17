<#-- @ftlvariable name="pageName" type="java.lang.String" -->
<#include "/core.ftl">

<table id="header" class="ui-widget-content shadow" style="background: none; border-width: 0;border-bottom-width: 1px;">
    <tr>
        <td width="178px" height="72px">
            <img id="header-image" src="/resources/images/logo.png" width="178" height="72" alt="logo"/>
        </td>
        <td width="100%" height="72px">
            <table style="height: 100%; width: 100%">
                <tr>
                    <td valign="top" align="right">
                    <#if principal.membership == 'GUEST'>
                        <span class="player computer">
                        <span class="nickname"><@message code="game.player.guest"/></span>
                        </span>
                        <#else>
                            <span class="player member">
                                <a href="/playground/profile/view">
                                    <span class="nickname">${principal.nickname} (${principal.account.email})</span>
                                </a>
                            </span>
                    </#if>
                        |
                        <a href="/account/logout"><@message code="account.signout.label"/></a>
                    </td>
                </tr>
                <tr>
                    <td valign="bottom">
                        <div id="game-toolbar" class="" align="right">
                            <div style="float: left;">
                                <button id="dashboardButton"
                                        onclick="wm.util.url.redirect('/playground/scribble/active')">
                                <@message code="game.menu.games.label"/>
                                </button>
                                <button id="gameboardButton"
                                        onclick="wm.util.url.redirect('/playground/scribble/join')">
                                <@message code="game.menu.join.label"/>
                                </button>
                                <button id="createButton"
                                        onclick="wm.util.url.redirect('/playground/scribble/create')">
                                <@message code="game.menu.create.label"/>
                                </button>
                            </div>
                        <#--
                            <div style="float: left;">
                                <button id="messagesButton" onclick="wm.util.url.redirect('/profile/messages')">
                                <@message code="game.menu.messages.label"/>
                                </button>
                            </div>
-->
                        <#--
                            <div style="float: left;">
                                <button id="tournamentsButton" onclick="wm.util.url.redirect('/game/tournaments')">
                                <@message code="game.menu.tournaments.label"/>
                         `       </button>
                            </div>
-->
                            <div>
                            <#if principal.membership != 'GUEST'>
                                <button id="modifyButton" onclick="wm.util.url.redirect('/account/modify')">
                                <@message code="game.menu.settings.label"/>
                                </button>
                            </#if>
                                <button onclick="wm.util.url.redirect('/info/help')">
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
