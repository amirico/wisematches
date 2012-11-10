<#-- @ftlvariable name="sections" type="wisematches.playground.tourney.regular.TourneySection[]" -->
<#-- @ftlvariable name="statistics" type="wisematches.playground.tracking.Statistics" -->

<#-- @ftlvariable name="actives" type="wisematches.playground.tourney.regular.Tourney[]" -->
<#-- @ftlvariable name="announce" type="wisematches.playground.tourney.regular.Tourney" -->

<#include "/core.ftl">

<style type="text/css">
    #tourney .announce .description {
        margin: 0;
        border: none;
        padding: 0;
        background: transparent;
    }

    #tourney .announce .description div {
        display: block;
    }

    #tourney .announce .description .number {
        font-size: 160%;
    }

    #tourney .announce .data-table-content {
        border-style: solid !important;
    }

    #tourney .announce .subscription {
        display: none;
    }

    .tourney-subscribed {
        padding-left: 5px;
    }

    .tourney-unsubscribed {
        padding-left: 5px;
    }

    .data-table-toolbar div, .data-table-toolbar a {
        white-space: nowrap;
    }

    #changeSubscriptionDialog table {
        border-spacing: 5px !important;
    }

    #announcedTourneys table td, #activeTourneys table td, #changeSubscriptionDialog table td {
        white-space: nowrap;
        vertical-align: top;
    }

    .ui-dialog .ui-dialog-buttonset {
        width: 100%;
        float: none;
        text-align: right;
    }

    .ui-dialog #unsubscribeButton {
        float: left;
        margin-left: 10px;
    }
</style>


<#macro sectionName section>
    <@message code="tourney.section.${section.name()?lower_case}.label"/>
</#macro>

<#macro sectionInfo section short=true>
    <#if section.anyRating>
        <#if short>
            <@message code="tourney.rating.any.label"/>
        <#else>
            <@message code="tourney.rating.any.description"/>
        </#if>
    <#else>
        <#if short>
            <@message code="tourney.rating.limit.label" args=[section.topRating]/>
        <#else>
            <@message code="tourney.rating.limit.description" args=[section.topRating]/>
        </#if>
    </#if>
</#macro>

<@wm.jstable/>

<@wm.playground id="tourney">
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
                            <#list actives as tourney>
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

        <#if announce??>
            <td valign="top">
                <div id="announce" class="announce">
                    <@wm.dtHeader align="left">
                        Тирнир
                        > ${announce.getNumber()}${gameMessageSource.getNumeralEnding(announce.getNumber(), locale)}
                        Турнир WiseMatches
                    </@wm.dtHeader>

                    <@wm.dtToolbar align="center" class="description ui-state-active">
                        <div class="number">
                        ${announce.getNumber()}${gameMessageSource.getNumeralEnding(announce.getNumber(), locale)}
                            Турнир WiseMatches
                        </div>
                        <div class="scheduled">
                            начинается ${gameMessageSource.formatDate(announce.getScheduledDate(), locale)}
                            (через ${gameMessageSource.formatRemainedTime(announce.getScheduledDate(), locale)})
                        </div>
                    </@wm.dtToolbar>

                    <@wm.dtContent wrap=true class="subscription">
                        <table>
                            <tr>
                                <td>
                                    Язык:
                                </td>
                                <td>
                                    <span id="announceLanguage" class="language"> - </span>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Игроков:
                                </td>
                                <td>
                                    <span id="announcePlayers"> - </span>
                                </td>
                            </tr>
                            <tr>
                                <td>Уровень:</td>
                                <td>
                                    <span id="announceSection" class="section"> - </span>
                                </td>
                            </tr>
                        </table>
                    </@wm.dtContent>

                    <@wm.dtFooter class="subscription">
                        <button style="width: 100%; color: #c46100; font-weight: bold">
                            Изменить
                        </button>
                    </@wm.dtFooter>
                </div>
            </td>
        </#if>
    </tr>
</table>
</@wm.playground>

<div id="subscriptionDialog" class="ui-helper-hidden">
    <form id="subscriptionForm" name="changeSubscriptionForm">
        <div>
            <div class="subscribed">
                Вы еще не зарегистрированны в данном турнире. Что бы сделать это сейчас, выберите
                подходящий язык и уровень противников и нажмите кнопку "Зарегистрироваться".
            </div>
            <div class="unsubscribed">
                Вы уже зарегистрированы в этом турнире. Вы можете изменить язык и/или уровень либо
                удалить регистрацию использую соответствующие кнопки внизу.
            </div>
        </div>
        <div>
            <table>
                <tr>
                    <td>
                        <label for="subscriptionLanguage">Язык Игр:</label>
                    </td>
                    <td width="100%">
                        <select id="subscriptionLanguage" name="language" style="width: 170px;">
                        <#list ["EN", "RU"] as l>
                            <option value="${l}">
                                <@message code="language.${l?lower_case}"/>
                            </option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>Уровень:</label>
                    </td>
                    <td>
                        <table id="subscriptionSection">
                        <#list sections?reverse as s>
                            <tr>
                                <td style="vertical-align: middle">
                                    <input type="radio" id="subscriptionSection${s.name()}"
                                           name="section" value="${s.name()}"/>
                                </td>
                                <td>
                                    <label id="subscriptionSectionLabel${s.name()}"
                                           for="subscriptionSection${s.name()}">
                                        -
                                        <span class="section">
                                            <@sectionName section=s/>
                                        </span>
                                        <span class="sample">(<@sectionInfo section=s short=false/>)</span>
                                    </label>
                                </td>
                                <td>
                                    (<span class="players"></span>)
                                </td>
                            </tr>
                        </#list>
                        </table>
                    </td>
                </tr>
            </table>
        </div>
        <div>
            <div class="sample">Ваш текущий рейтин - ${statistics.rating?string}</div>
        </div>
    </form>
</div>

<script type="text/javascript">
    wm.tourney = $.extend({}, wm.tourney, new function () {
        var sectionNames = [];
        var languageNames = [];
        var sectionDescriptions = [];

        var announce = ${announce.number?string};
        var subscription;
        var subscriptions;

        var subscriptionForm = $("#changeSubscriptionForm");
        var subscriptionDialog = $("#changeSubscriptionDialog");

        var init = function () {
            var sections = subscriptionForm.find("#tourneySection input");
            $.each(sections, function (i, section) {
                var sectionName = $(section).val();
                var spans = subscriptionForm.find("#tourneySectionLabel" + sectionName + " span");
                sectionNames[sectionName] = $(spans[0]).text();
                sectionDescriptions[sectionName] = $(spans[1]).text();
            });
            var languages = subscriptionForm.find("option");
            $.each(languages, function (i, section) {
                var o = $(section);
                languageNames[o.val()] = o.text();
            });
        };

        var updateView = function (data) {
            subscription = data.subscritpion;
            subscriptions = data.subscritpions;


        };

        var changeSubscription = function (unsubscribe) {
            var widget = subscriptionDialog.closest(".ui-dialog");
            var tournamentWidget = $("#tournamentWidget");
            var data = unsubscribe ? null : $.toJSON(subscriptionForm.serializeObject());
            wm.ui.lock(tournamentWidget, "Выполнение регистрации. Подождите, пожалуйста...");
            $.post("/playground/tourney/changeSubscription.ajax?t=" + announce, data,
                    function (response) {
                        if (response.success) {
                            wm.ui.unlock(widget, "Подписка успешно обновлена");
                            updateView(response.data);
                            subscriptionDialog.dialog("close");
                        } else {
                            wm.ui.unlock(tournamentWidget, response.summary, true);
                        }
                    }, 'json');
        };

        this.subscribe = function () {
            subscriptionDialog.dialog({
                id: "jQueryDialog",
                title: "Регистрация участия в турнире",
                width: 550,
                minHeight: 350,
                modal: true,
                resizable: false,
                buttons: [
                    {
                        class: "tourney-unsubscribed",
                        text: "Удалить Регистрацию",
                        click: function () {
                            changeSubscription(true);
                        }
                    },
                    {
                        class: "tourney-unsubscribed",
                        text: "Зарегистрироваться",
                        click: function () {
                            changeSubscription(false);
                        }
                    },
                    {
                        text: "<@message code="button.cancel"/>",
                        click: function () {
                            subscriptionDialog.dialog("close");
                        }

                    }
                ]
            });
        };

        init();
    })
    ;

    wm.ui.dataTable('#activeTourneys table', {
        "bSortClasses": false,
        "aoColumns": [
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": true },
            { "bSortable": false }
        ]
    });

    $('#announce').find('button').button().click(function () {
        wm.tourney.subscribe();
    });
</script>