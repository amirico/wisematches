<#-- @ftlvariable name="sections" type="wisematches.playground.tourney.regular.TournamentSection[]" -->
<#-- @ftlvariable name="languages" type="wisematches.personality.Language[]" -->
<#-- @ftlvariable name="playerRating" type="java.lang.Short" -->
<#-- @ftlvariable name="subscription" type="wisematches.playground.tourney.TourneySubscription" -->
<#-- @ftlvariable name="tournament" type="wisematches.playground.tourney.Announcement" -->
<#include "/core.ftl">

<link rel="stylesheet" type="text/css" href="/jquery/css/table_jui.css"/>

<@wm.playground id="tournament">
    <@wm.dtHeader align="right">
    <div style="float: left;">
        <@message code="game.menu.tournaments.label"/> > <@message code="tournament.tournament.label"/>
    </div>
    <div>
        <a href="/info/tournament"><@message code="tournament.rules.label"/></a>
    </div>
    </@wm.dtHeader>

    <@wm.dtToolbar align="left">
    <div class="info-header">
        <div class="info-label">
            <@wm.tournament number=tournament.number short=false/>
        </div>
    </div>
    </@wm.dtToolbar>

<form id="form" class="form" action="/playground/tournament/subscription" method="post">
    <@wm.dtStatusbar align="left">
        <table class="tournament-subscribe">
            <tr>
                <td>
                    <label class="static"><@message code="tournament.tournament.start.label"/>:</label>
                </td>
                <td>
                    <span>${gameMessageSource.formatDate(tournament.scheduledDate, locale)}</span>
                </td>
            </tr>

            <tr>
                <td>
                    <label class="static"><@message code="tournament.tournament.rating.label"/>:</label>
                </td>
                <td>
                    <span>${playerRating}</span>
                </td>
            </tr>

            <tr>
                <td>
                    <label class="static"><@message code="tournament.tournament.subscription.label"/>:</label>
                </td>
                <td>
            <span>
                <#if subscription??>
                    <@message code="tournament.section.${subscription.section.name()?lower_case}.label"/>
                    (<@message code="language.${subscription.language.name()?lower_case}"/>)
                <#else>
                    <@message code="tournament.section.none.description"/>
                </#if>
            </span>
                </td>
            </tr>
        </table>
    </@wm.dtStatusbar>

    <@wm.dtContent wrap=true>
        <table class="tournament-subscribe" width="100%">
            <tr>
                <td valign="top">
                    <label for="language"><@message code="tournament.tournament.language.label"/>:</label>
                </td>
                <td>
                    <#assign language=languages[0]/>
                    <@wm.field path="form.language">
                        <select id="language" name="language" style="width: 170px;">
                            <#list languages as l>
                                <option value="${l.code()}"
                                        <#if (l==wm.statusValue)>selected="selected"<#assign language=l/></#if>>
                                    <@message code="language.${l?lower_case}"/>
                                </option>
                            </#list>
                        </select>
                    </@wm.field>

                    <div class="sample"><@message code="tournament.tournament.language.description"/></div>
                </td>
            </tr>
            <tr>
                <td valign="top">
                <#--@declare id="tournamentCategory"-->
                    <label for="section"><@message code="tournament.tournament.section.label"/>:</label>
                </td>
                <td width="100%">
                    <table class="tournament-sections">
                        <@wm.field path="form.section">
                            <#list sections as s>
                                <#assign name=s.name()/>
                                <tr>
                                    <td valign="middle">
                                        <input id="section${name}" type="radio" name="section" value="${name}"
                                               <#if (name==wm.statusValue)>checked="checked"</#if>
                                               <#if !s.isRatingAllowed(playerRating)>disabled="disabled"</#if>>
                                    </td>
                                    <td> -
                                        <label for="section${name}">
                                            <@message code="tournament.section.${name?lower_case}.label"/>

                                            <#if ((s.topRating) > 20000)>
                                                <span class="sample">
                                                    (<@message code="tournament.section.rating.any.description"/>)
                                                </span>
                                            <#else>
                                                <span class="sample">
                                                    (
                                                    <@message code="tournament.section.rating.limit.description" args=[s.topRating]/>
                                                    )
                                                </span>
                                            </#if>
                                        </label>
                                    </td>
                                    <td>
                                        <div id="sectionFullness${name}">
                                            (${tournament.getBoughtTickets(language, s)})
                                        </div>
                                    </td>
                                </tr>
                            </#list>
                        </@wm.field>
                        <tr>
                            <td>
                                <input id="sectionNONE" type="radio" name="section" value="none"
                                       <#if ("none"==wm.statusValue)>checked="checked"</#if>>
                            </td>
                            <td>
                                <label for="sectionNONE">
                                    - <@message code="tournament.section.none.label"/>
                                    <span class="sample">
                                        (<@message code="tournament.section.rating.withdraw.description"/>)
                                    </span>
                                </label>
                            </td>
                            <td></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </@wm.dtContent>

    <@wm.dtStatusbar align="left">
        <input type="hidden" name="tournament" value="${tournament.number}">
        <button onclick="tournament.subscribe(); return false;"><@message code="tournament.subscribe.label"/></button>
    </@wm.dtStatusbar>
</form>

    <@wm.dtFooter><@message code="tournament.tournament.description"/></@wm.dtFooter>
</@wm.playground>

<script type="text/javascript">
    var fullness = {
    <#list languages as l>
        '${l.code()?lower_case}':{
        <#list sections as s>
        ${s.name()}: ${tournament.getBoughtTickets(l, s)}
            <#if s_has_next>,</#if>
        </#list>
    }<#if l_has_next>,</#if>
    </#list>
    }

    var tournament = new wm.game.Tournament(fullness, {
        waiting:"<@message code="tournament.subscribing.label"/>"
    });
</script>