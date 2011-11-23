<#-- @ftlvariable name="pageName" type="java.lang.String" -->
<#-- @ftlvariable name="newMessagesCount" type="java.lang.Integer" -->
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
                    <#--<#if false>-->
                    <#--|-->
                    <#--<a href="/info/features" style="color: red; font-weight: bold;">New Features!</a>-->
                    <#--</#if>-->
                        |
                    <#if principal.membership != 'GUEST'><a
                            href="/account/modify"><@message code="game.menu.settings.label"/></a>
                        |
                    </#if>
                        <a href="/info/help"><@message code="game.menu.help.label"/></a>
                        |
                        <a href="/account/logout"><@message code="account.signout.label"/></a>
                    </td>
                </tr>
                <tr>
                    <td valign="bottom">
                        <div id="game-toolbar" class="" align="left">
                            <div style="display: inline-block;">
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

                            <div style="padding-left: 30px; display: inline-block;">
                                <button id="messagesButton" onclick="wm.util.url.redirect('/playground/messages/view')">
                                <@message code="game.menu.messages.label"/>
                                <#assign messageManager=springMacroRequestContext.webApplicationContext.getBean("messageManager")!""/>
                                <#if messageManager?has_content>
                                    <#assign newMessagesCount=messageManager.getNewMessagesCount(principal)/>
                                    <#if newMessagesCount?? && newMessagesCount !=0>
                                        <strong>(${newMessagesCount})</strong>
                                    </#if>
                                </#if>
                                </button>
                                <button id="friendsButton"
                                        onclick="wm.util.url.redirect('/playground/friends/view')">
                                <@message code="game.menu.friends.label"/>
                                </button>
                                <button id="blacklistButton"
                                        onclick="wm.util.url.redirect('/playground/blacklist/view')">
                                <@message code="game.menu.blacklist.label"/>
                                </button>
                            </div>

                            <div style="padding-left: 30px; display: inline-block;">
                                <button id="tournamentsButton" onclick="wm.util.url.redirect('/playground/players')">
                                <@message code="game.menu.players.label"/>
                                </button>
                            </div>
                        <#--
                            <div style="float: left;">
                                <button id="tournamentsButton" onclick="wm.util.url.redirect('/playground/tournaments')">
                                <@message code="game.menu.tournaments.label"/>
                         `       </button>
                            </div>
-->
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<#include "guest.ftl"/>

<script type="text/javascript">
    $("#game-toolbar div").buttonset();
</script>
