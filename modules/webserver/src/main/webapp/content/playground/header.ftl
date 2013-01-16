<#-- @ftlvariable name="pageName" type="java.lang.String" -->
<#-- @ftlvariable name="messageManager" type="wisematches.playground.message.MessageManager" -->
<#-- @ftlvariable name="newMessagesCount" type="java.lang.Integer" -->
<#include "/core.ftl">

<#assign hasNewFeatues=false/>

<table id="header" class="ui-widget-content shadow"
       style="background: none; border-width: 0;border-bottom-width: 1px;">
    <tr>
        <td width="178px" height="72px">
            <img id="header-image" src="<@s "/images/logo.png"/>" width="178" height="72" alt="logo"/>
        </td>
        <td width="100%" height="72px">
            <table style="height: 100%; width: 100%;">
                <tr>
                    <td valign="top" align="right">
                    <@wm.player.name principal/>
                        |
                        <a href="/account/membership"><@message code="membership.label"/></a>
                    <#if hasNewFeatues>
                        |
                        <a href="/info/features"
                           style="color: #FF3300; font-weight: bold;"><@message code="game.menu.features.label"/></a>
                    </#if>
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
                        <div id="gameToolbar" align="left" style="display: none">
                            <div class="wm-ui-buttonset">
                                <a href="/playground/scribble/active"><@message code="game.menu.games.label"/></a>
                                <a href="/playground/scribble/join"><@message code="game.menu.join.label"/></a>
                                <a href="/playground/scribble/create"><@message code="game.menu.create.label"/></a>
                            </div>

                            <div class="wm-ui-buttonset">
                            <#assign messageManager=springMacroRequestContext.webApplicationContext.getBean("messageManager")!""/>
                            <#if messageManager?has_content>
                                <#assign newMessagesCount=messageManager.getNewMessagesCount(principal)/>
                                <#assign newMessages=newMessagesCount?? && newMessagesCount !=0/>
                                <a href="/playground/messages/view">
                                    <#if newMessages><img src="<@s "images/dashboard/newMessageIcon.png"/>"
                                                          style="width: 16px; height: 16px; padding-right: 5px; padding-bottom: 2px; vertical-align: bottom;"
                                                          alt=""/></#if><@message code="game.menu.messages.label"/><#if newMessages>
                                    <strong>(${newMessagesCount})</strong>
                                </#if>
                                </a>
                            </#if>
                            </div>

                            <div class="wm-ui-buttonset">
                                <a href="/playground/tourney"><@message code="game.menu.tourneys.label"/></a>
                            </div>

                            <div class="wm-ui-splitbutton">
                                <a href="/playground/dictionary"><@message code="game.menu.dictionary.label"/></a>
                                <ul class="ui-helper-hidden">
                                    <li>
                                        <a href="/playground/dictionary?l=ru"><@message code="language.ru"/></a>
                                    </li>
                                    <li>
                                        <a href="/playground/dictionary?l=en"><@message code="language.en"/></a>
                                    </li>
                                </ul>
                            </div>

                            <div class="wm-ui-splitbutton">
                                <a href="/playground/players"><@message code="game.menu.players.label"/></a>
                                <ul class="ui-helper-hidden">
                                    <li>
                                        <a href="/playground/friends/view"><@message code="game.menu.friends.label"/></a>
                                    </li>
                                    <li>
                                        <a href="/playground/blacklist/view"><@message code="game.menu.blacklist.label"/></a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<#include "guest.ftl"/>