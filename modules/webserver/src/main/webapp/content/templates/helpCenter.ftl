<#-- @ftlvariable name="infoId" type="java.lang.String" -->
<#include "/core.ftl">

<#macro markSelected pageName><#if infoId==pageName>class="selected"</#if></#macro>

<table id="info-page">
    <tr>
        <td style="vertical-align: top;">
            <div id="info-navigation">
                <ul>
                    <li>
                        <b><@messageCapFirst code="info.readmore.label"/></b>
                    </li>
                    <li>
                        <ul id="rules">
                            <li <@markSelected "rules"/>>
                                <a href="/info/rules"><@messageCap code="info.rules.scribble.label"/></a>
                            </li>
                        <#--
                            <li <@markSelected "tournament"/>>
                                <a href="/info/tournament"><@messageCap code="info.rules.tournament.label"/></a>
                            </li>
-->
                            <li <@markSelected "rating"/>>
                                <a href="/info/rating"><@messageCap code="info.rules.rating.label"/></a>
                            </li>
                            <li class="x-panel-body-noheader separator"></li>
                        </ul>
                    </li>
                    <li>
                        <ul id="main">
                            <li <@markSelected "about"/>>
                                <a href="/info/about"><@messageCap code="info.principles.label"/></a>
                            </li>
                            <li <@markSelected "features"/>>
                                <a href="/info/features"><@messageCap code="info.features.label"/></a>
                            </li>
                            <li class="x-panel-body-noheader separator"></li>
                        </ul>
                    </li>
                    <li>
                        <ul id="policies">
                            <li <@markSelected "terms"/>>
                                <a href="/info/terms"><@message code="info.policies.terms.label"/></a>
                            </li>
                            <li <@markSelected "policy"/>>
                                <a href="/info/policy"><@message code="info.policies.policy.label"/></a>
                            </li>
                            <li <@markSelected "naming"/>>
                                <a href="/info/naming"><@message code="info.policies.naming.label"/></a>
                            </li>
                        </ul>
                    </li>

                    <li class="x-panel-body-noheader separator"
                        style="border-color: #dad7d7; padding-bottom: 5px;"></li>
                <#if principal??>
                    <li>
                        <b><@message code="info.continue.label"/></b>
                        <ul id="continue">
                            <li>
                                <a href="/playground/scribble/active"><@message code="info.open.dashboard.label"/></a>
                            </li>
                            <li>
                                <a href="/playground/scribble/join"><@message code="info.open.gameboard.label"/></a>
                            </li>
                        </ul>
                    </li>
                    <#else>
                        <li>
                            <b><@message code="info.start.label"/></b>
                            <ul id="start">
                                <li>
                                    <button id="createAnAccount" class="account-button"
                                            onclick="wm.util.url.redirect('/account/create')">
                                    <@message code="account.register.label"/>
                                    </button>
                                </li>
                                <li class="separator"><@message code="separator.or"/></li>
                                <li><a href="/account/login"><@message code="account.signin.label"/></a></li>
                                <li class="separator"><@message code="separator.or"/></li>
                                <li><a href="/account/loginGuest"><@message code="account.guest.label"/></a></li>
                            </ul>
                        </li>
                </#if>
                </ul>
            </div>
        </td>
        <td id="info-content" style="vertical-align: top;">
        <#include "/content/templates/resources.ftl">
        </td>
    </tr>
</table>