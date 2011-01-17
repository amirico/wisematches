<#include "/core.ftl">

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
                            <li style="selected">
                                <a href="/info/rules.html"><@messageCap code="info.rules.scribble.label"/></a>
                            </li>
                            <li>
                                <a href="/info/tournament.html"><@messageCap code="info.rules.tournament.label"/></a>
                            </li>
                            <li>
                                <a href="/info/rating.html"><@messageCap code="info.rules.rating.label"/></a>
                            </li>
                            <li class="x-panel-body-noheader separator"></li>
                        </ul>
                    </li>
                    <li>
                        <ul id="main">
                            <li><a href="/info/about.html"><@messageCap code="info.principles.label"/></a></li>
                            <li><a href="/info/features.html"><@messageCap code="info.features.label"/></a></li>
                            <li class="x-panel-body-noheader separator"></li>
                        </ul>
                    </li>
                    <li>
                        <ul id="policies">
                            <li><a href="/info/terms.html"><@message code="info.policies.terms_of_use.label"/></a></li>
                            <li><a href="/info/policy.html"><@message code="info.policies.privacy_policy.label"/></a>
                            </li>
                            <li><a href="/info/naming.html"><@message code="info.policies.naming.label"/></a></li>
                        </ul>
                    </li>

                    <li class="x-panel-body-noheader separator" style="border-color: #dad7d7;"></li>
                <@security.authorize access="isAuthenticated()">
                    <li>
                        <b><@message code="info.continue.label"/></b>
                        <ul id="continue">
                            <li><a href="/game/dashboard.html"><@message code="info.open.dashboard.label"/></a></li>
                            <li><a href="/game/gameboard.html"><@message code="info.open.gameboard.label"/></a></li>
                            <li><a href="/game/playboard.html"><@message code="info.open.playboard.label"/></a></li>
                        </ul>
                    </li>
                </@security.authorize>
                <@security.authorize access="not isAuthenticated()">
                    <li>
                        <b><@message code="info.start.label"/></b>
                        <ul id="start">
                            <li>
                                <button id="createAnAccount" onclick="wm.util.url.redirect('/account/create.html')">
                                <@message code="account.register.label"/>
                                </button>
                            </li>
                            <li style="color: gray; font-size: small;"><@message code="separator.or"/></li>
                            <li><a href="/account/authGuest.html"><@message code="account.guest.label"/></a></li>
                        </ul>
                    </li>
                </@security.authorize>
                </ul>
            </div>
        </td>
        <td class="x-panel" style="vertical-align: top; border-left-width: 1px;">
            <div id="info-content">
            <#include "/content/info/modelConverter.ftl">
            </div>
        </td>
    </tr>
</table>