<#-- @ftlvariable name="sections" type="wisematches.playground.tourney.regular.TourneySection[]" -->
<#-- @ftlvariable name="languages" type="wisematches.personality.Language[]" -->
<#-- @ftlvariable name="statistics" type="wisematches.playground.tracking.Statistics" -->

<#-- @ftlvariable name="announce" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="subscription" type="wisematches.playground.tourney.regular.RegistrationRecord" -->
<#-- @ftlvariable name="subscriptions" type="wisematches.playground.tourney.regular.RegistrationsSummary" -->

<#include "/core.ftl">
<#include "scriplet.ftl">

<#assign pid=principal.id/>

<div id="subscriptionView" class="announce">
<#if subscription??>
    <#assign subscriptionStateClass="tourney-state"/>
<#else>
    <#assign subscriptionStateClass="tourney-state ui-state-disabled"/>
</#if>

<@wm.ui.table.header align="left">
    <@message code="tourney.tourney.label"/> > <@message code="tourney.announce.label"/>
</@wm.ui.table.header>

<@wm.ui.table.toolbar align="center">
    <div class="tourney-name ui-state-active">
        <@tourneyName tourneyId=announce.id link=false/>
    </div>
    <div style="text-align: right; width: 100%">
        <a href="/info/tourney" style="white-space: nowrap;">
            <@message code="tourney.rules.label"/>
        </a>
    </div>
</@wm.ui.table.toolbar>

<@wm.ui.table.content wrap=true>
    <table>
        <tr>
            <td>
                <label><@message code="tourney.announce.starts.label"/>:</label>
            </td>
            <td>
                                    <span>
                                    ${gameMessageSource.formatDate(announce.getScheduledDate(), locale)}
                                    </span>
                <br>
                                    <span class="sample">
                                        (<@message code="separator.in"/>
                                    ${gameMessageSource.formatRemainedTime(announce.getScheduledDate(), locale)})
                                    </span>
            </td>
        </tr>
        <tr>
            <td>
                <label><@message code="tourney.announce.players.label"/>:</label>
            </td>
            <td>
                <div class="opponents ui-layout-table" style="border-spacing: 0">
                    <#list languages?reverse as l>
                        <div>
                            <div style="font-weight: normal">
                                <a href="/playground/tourney/subscriptions?t=${announce.number}&l=${l.code()}">
                                    <@languageName language=l/>
                                </a>
                            </div>
                            <div style="padding-left: 5px; padding-right: 5px;">-</div>
                            <div class="subscriptionDetails${l}">
                                <a href="/playground/tourney/subscriptions?t=${announce.number}&l=${l.code()}">
                                ${subscriptions.getPlayers(l)?string}
                                </a>
                            </div>
                        </div>
                    </#list>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div class="${subscriptionStateClass}">
                    <label><@message code="tourney.announce.section.label"/>:</label>
                </div>
            </td>
            <td>
                <div class="${subscriptionStateClass}">
                                <span id="announceSection" class="section">
                                    <#if subscription??>
                                        <@sectionName section=subscription.section/>
                                    <#else>
                                        <@message code="tourney.section.unspecified"/>
                                    </#if>
                                </span>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div class="${subscriptionStateClass}">
                    <label><@message code="tourney.announce.language.label"/>:</label>
                </div>
            </td>
            <td>
                <div class="${subscriptionStateClass}">
                                    <span id="announceLanguage" class="language">
                                        <#if subscription??>
                                           <@languageName language=subscription.language/>
                                        <#else>
                                            <@message code="tourney.section.unspecified"/>
                                        </#if>
                                    </span>
                </div>
            </td>
        </tr>
    </table>
</@wm.ui.table.content>

<@wm.security.observed>
    <@wm.ui.table.statusbar align="left">
        <button style="width: 100%">
            <#if subscription??>
                        <@message code="tourney.subscribe.refuse"/>
                    <#else>
                <@message code="tourney.subscribe.accept"/>
            </#if>
        </button>
    </@wm.ui.table.statusbar>
</@wm.security.observed>

<@wm.ui.table.footer class="actions">
    <@wm.security.info code="tourney.subscribe.forbidden"/>
</@wm.ui.table.footer>
</div>

<@wm.security.observed>
<div id="subscriptionDialog" class="ui-helper-hidden">
    <form id="subscriptionForm" name="changeSubscriptionForm">
        <div>
            <@message code="tourney.subscribe.desciption"/>
        </div>
        <div>
            <#if subscription??>
                <#assign subscribedSection=subscription.section/>
                <#assign subscribedLanguage=subscription.language/>
            <#else>
                <#list sections as s>
                    <#if s.isRatingAllowed(statistics.rating)>
                        <#assign subscribedSection=s/>
                        <#break>
                    </#if>
                </#list>
                <#assign subscribedLanguage=language/>
            </#if>

            <table>
                <tr>
                    <td>
                        <label for="subscriptionLanguage"><@message code="tourney.subscribe.language.label"/>
                            :</label>
                    </td>
                    <td width="100%">
                        <select id="subscriptionLanguage" name="language" style="width: 170px;">
                            <#list languages as l>
                                <option value="${l}" <#if l=subscribedLanguage>selected="selected"</#if>>
                                    <@languageName language=l/>
                                </option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label><@message code="tourney.subscribe.section.label"/>:</label>
                    </td>
                    <td>
                        <table id="subscriptionSection">
                            <#list sections?reverse as s>
                                <#assign disabled=!s.isRatingAllowed(statistics.rating)/>
                                <tr>
                                    <td style="vertical-align: middle">
                                        <input type="radio" id="subscriptionSection${s.name()}"
                                               name="section" value="${s.name()}"
                                               <#if s=subscribedSection>checked="checked"</#if>
                                               <#if disabled>disabled="disabled"</#if>/>
                                    </td>
                                    <td <#if disabled>class="ui-state-disabled"</#if>>
                                        <label id="subscriptionSectionLabel${s.name()}"
                                               for="subscriptionSection${s.name()}">
                                            -
                                                    <span class="section">
                                                        <@sectionName section=s/>
                                                    </span>
                                            <span class="sample">(<@sectionInfo section=s short=false/>)</span>
                                        </label>
                                    </td>
                                    <td <#if disabled>class="ui-state-disabled"</#if>>
                                        (<span id="subscriptionSectionPlayers${s.name()}"
                                               class="players">${subscriptions.getPlayers(subscribedLanguage, s)}</span>)
                                    </td>
                                </tr>
                            </#list>
                        </table>
                    </td>
                </tr>
            </table>
        </div>
        <div>
            <div class="sample"><@message code="tourney.rating.current.label"/>
                - ${statistics.rating?string}</div>
        </div>
    </form>
</div>

<script type="text/javascript">
    new wm.game.tourney.Subscription(${announce.number?string}, <#if subscription??>true<#else>false</#if>,
            {<#list languages as l>
                "${l.name()}": {
                    <#list sections as s>
                        "${s.name()}": ${subscriptions.getPlayers(l, s)?string} <#if s_has_next>,</#if>
                    </#list>
                }<#if l_has_next>,</#if>
            </#list>},
            {
                "register.title": "<@message code="tourney.subscribe.label"/>",
                "register.accept": "<@message code="tourney.subscribe.accept"/>",
                "register.refuse": "<@message code="tourney.subscribe.refuse"/>",
                "register.unspecified": "<@message code="tourney.section.unspecified"/>",
                "register.subscribing": "<@message code="tourney.subscribe.subscribing"/>",
                "register.subscribed": "<@message code="tourney.subscribe.subscribed"/>",
                "register.unsubscribing": "<@message code="tourney.subscribe.unsubscribing"/>",
                "register.unsubscribed": "<@message code="tourney.subscribe.unsubscribed"/>",
                "register.button": "<@message code="tourney.subscribe.subscribe"/>"
            }
    );
</script>
</@wm.security.observed>
