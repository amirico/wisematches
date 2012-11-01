<#-- @ftlvariable name="language" type="wisematches.personality.Language" -->
<#-- @ftlvariable name="sections" type="wisematches.playground.tourney.regular.TourneySection[]" -->
<#-- @ftlvariable name="activeTourneys" type="wisematches.playground.tourney.regular.Tourney[]" -->
<#-- @ftlvariable name="finishedTourneys" type="wisematches.playground.tourney.regular.Tourney[]" -->

<#-- @ftlvariable name="announcement" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="subscription" type="wisematches.playground.tourney.regular.TourneySubscription" -->
<#-- @ftlvariable name="subscriptions" type="wisematches.playground.tourney.regular.TourneySubscriptions" -->

<#include "/core.ftl">

<style type="text/css">

    .data-table-toolbar div, .data-table-toolbar a {
        white-space: nowrap;
    }

    #announcedTourneys table td, #activeTourneys table td {
        white-space: nowrap;
    }
</style>

<@wm.jstable/>

<@wm.playground id="tourneysWidget">
<table width="100%">
    <tr>
        <td width="100%" valign="top">
            <div id="activeTourneys">
                <@wm.dtHeader align="right">
                    <div style="float: left; display: inline-block;">
                        Турнир > Мои Текущие Турниры
                    </div>
                    <div style="display: inline-block; text-align: right">
                        <a href="/info/tourney" style="white-space: nowrap;">Правила Турниров</a>
                    </div>
                </@wm.dtHeader>

                <@wm.dtToolbar>
                    <div>
                        <a href="/playground/tourney/active">All active Tournaments</a>
                        <a href="/playground/tourney/history">All finished Tournaments</a>
                    </div>
                </@wm.dtToolbar>

                <@wm.dtContent>
                    <table width="100%" class="display">
                        <thead>
                        <tr>
                            <th width="100%">Tournament Name</th>
                            <th nowrap="nowrap">Start Date</th>
                            <th nowrap="nowrap">Section</th>
                            <th nowrap="nowrap">Active Round</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list activeTourneys as tourney>
                            <tr>
                                <td><a href="/playground/tourney/view?t=">${tourney.number} WiseMatches Tourney
                                    {${tourney.id}</a>
                                </td>
                                <td nowrap="nowrap">${gameMessageSource.formatDate(tourney.scheduledDate, locale)}</td>
                                <td nowrap="nowrap"></td>
                                <td nowrap="nowrap">Waiting Subscription</td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </@wm.dtContent>

                <@wm.dtFooter/>
            </div>
        </td>

        <#if announcement??>
            <td valign="top">
                <div id="announcedTourneys">
                    <@wm.dtHeader align="left">
                        <div>
                            Турнир > Анонс
                        </div>
                    </@wm.dtHeader>

                    <@wm.dtToolbar align="center">
                        <div style="padding: 0; margin: 0; background: transparent; border: none;"
                             class="ui-state-active">
                            <div style="font-size: 160%; display: block">
                            ${announcement.getNumber()}${gameMessageSource.getNumeralEnding(announcement.getNumber(), locale)}
                                Турнир WiseMatches
                            </div>
                            <div>
                                начинается
                                через ${gameMessageSource.formatRemainedTime(announcement.getScheduledDate(), locale)}
                            </div>
                        </div>
                    </@wm.dtToolbar>

                    <@wm.dtContent wrap=true>
                        <div class="ui-widget-content"
                             style=" border-width: 2px; border-left: none; border-right: none;">
                            <div>
                                <table id="announcedTourney">
                                    <tr>
                                        <td>Язык:</td>
                                        <td>
                                            <a href="/account/modify#tourneyTab"><@message code="language.${language.name()?lower_case}"/></a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Начало:</td>
                                        <td>${gameMessageSource.formatDate(announcement.getScheduledDate(), locale)}</td>
                                    </tr>
                                    <tr>
                                        <td>Игроков:</td>
                                        <td>${subscriptions.getPlayers(language)}</td>
                                    </tr>
                                    <tr>
                                        <td>Ваша секция:</td>
                                        <td>
                                        <span style="color: #c54c31; font-weight: bold">
                                            <#if subscription??>
                                                <@message code="tourney.section.${subscription.section.name()?lower_case}"/>
                                            <#else>
                                                не зарегистрированы
                                            </#if>
                                        </span>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div>
                                <button style="width: 100%">
                                <span style="color: #c46100; font-weight: bold">
                                    <#if subscription??>Отказаться<#else>Зарегистрироваться</#if>
                                </span>
                                </button>
                            </div>
                        </div>
                    </@wm.dtContent>

                    <@wm.dtFooter>
                    </@wm.dtFooter>
                </div>
            </td>
        </#if>
    </tr>
</table>
</@wm.playground>

<script type="text/javascript">
    wm.ui.dataTable('#activeTourneys table', {
        "bSortClasses":false,
        "aoColumns":[
            { "bSortable":true },
            { "bSortable":true },
            { "bSortable":true },
            { "bSortable":false }
        ]
    });

    $("#announcedTourneys").find("button").button();
</script>