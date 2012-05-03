<#-- @ftlvariable name="pageName" type="java.lang.String" -->
<#-- @ftlvariable name="messageManager" type="wisematches.playground.message.MessageManager" -->
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
                        <div style="width: 24px; display: inline-block; vertical-align: bottom; padding-bottom: 2px">
                            <g:plusone size="small" annotation="none"></g:plusone>
                        </div>
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
                                <a href="/playground/scribble/active"><@message code="game.menu.games.label"/></a>
                                <a href="/playground/scribble/join"><@message code="game.menu.join.label"/></a>
                                <a href="/playground/scribble/create"><@message code="game.menu.create.label"/></a>
                            </div>

                            <div style="padding-left: 30px; display: inline-block;">
                            <#assign messageManager=springMacroRequestContext.webApplicationContext.getBean("messageManager")!""/>
                            <#if messageManager?has_content>
                                <#assign newMessagesCount=messageManager.getNewMessagesCount(principal)/>
                                <#assign newMessages=newMessagesCount?? && newMessagesCount !=0/>
                                <a href="/playground/messages/view">
                                    <#if newMessages><img src="/resources/images/dashboard/newMessageIcon.png"
                                                          style="width: 16px; height: 16px; padding-right: 5px; padding-bottom: 2px; vertical-align: bottom;"
                                                          alt=""/></#if><@message code="game.menu.messages.label"/><#if newMessages>
                                    <strong>(${newMessagesCount})</strong>
                                </#if>
                                </a>
                            </#if>
                                <a href="/playground/friends/view"><@message code="game.menu.friends.label"/></a>
                                <a href="/playground/blacklist/view"><@message code="game.menu.blacklist.label"/></a>
                            </div>

                            <div style="padding-left: 30px; display: inline-block;">
                                <a href="/playground/players"><@message code="game.menu.players.label"/></a>
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
