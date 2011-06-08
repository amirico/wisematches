<#-- @ftlvariable name="country" type="wisematches.personality.profile.countries.Country" -->
<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="profile" type="wisematches.personality.profile.PlayerProfile" -->
<#-- @ftlvariable name="statistics" type="wisematches.playground.scribble.tracking.ScribbleStatistics" -->
<#-- @ftlvariable name="ratingChart" type="wisematches.server.web.utils.RatingChart" -->
<#include "/core.ftl">

<#macro undefined value>
    <#if value==0><@message code="profile.undefined"/><#else>${value}</#if>
</#macro>

<div style="width: 100%">
<div class="profile shadow ui-state-default">
<div class="content shadow ui-state-default">
<div class="title">
    <div class="player">
        <strong>${profile.realName!player.nickname}</strong>
    </div>
    <div class="registered">
    <@message code="profile.registered"/>: ${gameMessageSource.formatDate(profile.creationDate, locale)}
    </div>
</div>
<#if principal.id == player.id>
<div class="edit">
    <button onclick="wm.util.url.redirect('/playground/profile/edit')"><@message code="profile.edit"/></button>
</div>
</#if>
<#assign p_wins=0 p_loses=0 p_draws=0 p_timeouts=0/>
<#assign wins=statistics.wins loses=statistics.loses draws=statistics.draws timeouts=statistics.timeouts finished=statistics.finishedGames/>
<#if finished != 0>
    <#assign p_wins=(wins/finished*100) p_loses=(loses/finished*100) p_draws=(draws/finished*100) p_timeouts=(timeouts/finished*100)/>
</#if>
<div style="width: 100%; text-align: left;">
<table width="100%" class="statistic" style="padding: 0">
    <tr>
        <td width="100%" valign="top">
            <table class="games-info" width="100%">
                <thead>
                <tr>
                    <td class="ui-state-default ui-corner-tl" width="50%">
                        <span><@message code="profile.rating"/></span>
                    </td>
                    <td class="ui-state-default">
                        <span style="border-color: #008000"><@message code="profile.wins"/></span>
                    </td>
                    <td class="ui-state-default">
                        <span style="border-color: #AA0033"><@message code="profile.loses"/></span>
                    </td>
                    <td class="ui-state-default">
                        <span style="border-color: #FF9900"><@message code="profile.draws"/></span>
                    </td>
                    <td class="ui-state-default ui-corner-tr">
                        <span><@message code="profile.total"/></span>
                    </td>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="ui-state-default">${statistics.rating}</td>
                    <td class="ui-state-default">${wins} <span>(${p_wins}%)</span></td>
                    <td class="ui-state-default">${loses} <span>(${p_loses}%)</span></td>
                    <td class="ui-state-default">${draws} <span>(${p_draws}%)</span></td>
                    <td class="ui-state-default">${finished}</td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
</table>

<div class="statistic ui-corner-all ui-state-default shadow">
    <div class="ui-layout-table" style="width: 100%">
        <div>
            <div style="vertical-align: top;">
                <div class="ui-layout-table">
                    <div>
                        <div><@message code="profile.rating.avg"/>:</div>
                        <div><@undefined value=statistics.averageRating/></div>
                    </div>
                    <div>
                        <div><@message code="profile.rating.low"/>:</div>
                        <div><@undefined value=statistics.lowestRating/></div>
                    </div>
                    <div>
                        <div><@message code="profile.rating.hi"/>:</div>
                        <div><@undefined value=statistics.highestRating/></div>
                    </div>
                </div>
                <div class="ui-layout-table" style="padding-top: 10px">
                    <div>
                        <div><@message code="profile.rating.op.avg"/>:</div>
                        <div><@undefined value=statistics.averageOpponentRating/></div>
                    </div>
                    <div>
                        <div><@message code="profile.rating.op.low"/>:</div>
                        <div>
                        <#if statistics.lowestLostOpponentId==0>
                        <@message code="profile.undefined"/>
                            <#else>
                            ${statistics.lowestLostOpponentRating}
                                <#assign llp=playerManager.getPlayer(statistics.lowestLostOpponentId)!""/>
                                <#if llp?has_content>( <@wm.player player=llp showRating=false showType=false/>)</#if>
                        </#if>
                        </div>
                    </div>
                    <div>
                        <div><@message code="profile.rating.op.hi"/>:</div>
                        <div>
                        <#if statistics.highestWonOpponentId==0>
                        <@message code="profile.undefined"/>
                            <#else>
                            ${statistics.highestWonOpponentRating}
                                <#assign hwp=playerManager.getPlayer(statistics.highestWonOpponentId)!""/>
                                <#if hwp?has_content>( <@wm.player player=hwp showRating=false showType=false/>
                                    )</#if>
                        </#if>
                        </div>
                    </div>
                </div>
            </div>
            <div style="text-align: right; width: 300px">
                <div id="ratingChart"></div>
                <div class="sample"><sub>(<@message code="profile.rating.pastyear"/>)</sub></div>
            </div>
        </div>
    </div>
</div>

<div class="statistic ui-corner-all ui-state-default shadow">
    <div class="ui-layout-table" style="width: 100%">
        <div>
            <div style="vertical-align: top;">
                <div class="ui-layout-table">
                    <div>
                        <div><@message code="profile.active"/>:</div>
                        <div>${statistics.activeGames}</div>
                    </div>
                    <div>
                        <div><@message code="profile.finished"/>:</div>
                        <div>${statistics.finishedGames}</div>
                    </div>
                    <div>
                        <div><@message code="profile.interrupted"/>:</div>
                        <div>${statistics.timeouts}</div>
                    </div>
                </div>
                <div class="ui-layout-table" style="padding-top: 10px">
                    <div>
                        <div><@message code="profile.last"/>:</div>
                        <div>
                        <#if statistics.lastMoveTime??>
                                ${gameMessageSource.formatDate(statistics.lastMoveTime, locale)}
                                <#else>
                        <@message code="profile.undefined"/>
                        </#if>
                        </div>
                    </div>
                    <div>
                        <div><@message code="profile.avg.time"/>:</div>
                        <div>
                        <#if statistics.averageMoveTime != 0>
                            ${gameMessageSource.formatMinutes(statistics.averageMoveTime/1000/60, locale)}
                            <#else>
                        <@message code="profile.undefined"/>
                        </#if>
                        </div>
                    </div>
                    <div>
                        <div><@message code="profile.avg.moves"/>:</div>
                        <div><@undefined value=statistics.averageMovesPerGame/></div>
                    </div>
                </div>
            </div>
            <div style="vertical-align: top; width: 300px">
                <span class="sample"><@message code="profile.successfulness"/></span>

                <div id="gamesChart"></div>

                <span class="sample"><@message code="profile.punctual"/></span>

                <div id="timeoutsChart"></div>
            </div>
        </div>
    </div>
</div>

<div class="statistic ui-corner-all ui-state-default shadow">
    <div class="ui-layout-table">
        <div>
            <div><@message code="profile.points.avg"/>:</div>
            <div><@undefined value=statistics.activeGames/></div>
        </div>
        <div>
            <div><@message code="profile.points.lo"/>:</div>
            <div><@undefined value=statistics.lowestPoints/></div>
        </div>
        <div>
            <div><@message code="profile.points.hi"/>:</div>
            <div><@undefined value=statistics.highestPoints/></div>
        </div>
    </div>

    <div class="ui-layout-table" style="padding-top: 10px;">
        <div style="height: 24px;">
            <div><@message code="profile.word.long"/>:</div>
            <div style="position: relative; padding-top: 3px">
            <#if statistics.lastLongestWord??>
                <#list statistics.lastLongestWord.tiles as tile>
                    <div class="tile cost${tile.cost}"
                         style="background-position: -${tile.cost*22}px 0; top: 0; left: ${tile_index*22}px; padding: 0;">
                        <span>${tile.letter?upper_case}</span></div>
                </#list>
                <#else>
                <@message code="profile.undefined"/>
            </#if>
            </div>
        </div>
        <div style="height: 24px">
            <div><@message code="profile.word.valuable"/>:</div>
            <div style="position: relative; padding-top: 3px">
            <#if statistics.lastValuableWord??>
                <#list statistics.lastValuableWord.tiles as tile>
                    <div class="tile cost${tile.cost}"
                         style="background-position: -${tile.cost*22}px 0; top: 0; left: ${tile_index*22}px; padding: 0;">
                        <span>${tile.letter?upper_case}</span></div>
                </#list>
                <#else>
                <@message code="profile.undefined"/>
            </#if>
            </div>
        </div>
    </div>
</div>
</div>

<#--<div style="padding-top: 10px; text-align: center;">-->
<#--<a href="">Games in progress</a> | <a href="">Past game history</a>-->
<#--</div>-->
</div>

<div class="info">
    <div class="photo">
        <img style="width: 200px; height: 200px;"
             src="/resources/images/player/noPlayer200.png" alt="Photo">
    </div>
    <div><strong>${player.nickname}</strong></div>
    <div <#if !profile.gender??>class="undefined"</#if>>
    ${profile.gender!"gender undefined"},
    </div>
    <div <#if !profile.birthday??>class="undefined"</#if>>
    ${profile.birthday!"ages undefined"},
    </div>
    <div <#if !country??>class="undefined"</#if>>
    ${country.name!"country undefined"},
    </div>
    <div>
    ${player.timeZone.displayName}
    </div>
    <div class="<#if !profile.comments??>undefined </#if>quotation">
        &laquo; ${profile.comments!""} &raquo;
    </div>
</div>
</div>
</div>

<script type="text/javascript">
    $(".profile button").button();

    google.load('visualization', '1', {packages: ['corechart']});

    function drawRatingGraph() {
        // Create and populate the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'x');
        data.addColumn('number', '<@message code="profile.rating"/>');
        data.addRows(${ratingChart.pointsCount});

        var resolution = ${(ratingChart.pointsCount/ratingChart.monthIndexes?size)?string("0")};
        var months = [<#list ratingChart.monthIndexes as m>'<@message code="month.names.${m}"/>'<#if m_has_next>,</#if></#list>];
        for (m in months) for (i = 0; i < resolution; i++) data.setValue(m * resolution + i, 0, months[m]);

    <#list ratingChart.ratingsPoint as p>
        data.setValue(${p}, 1, ${ratingChart.ratingsAvg[p_index]});
    </#list>

        new google.visualization.LineChart(document.getElementById('ratingChart')).
                draw(data, {
            backgroundColor: 'transparent',
            interpolateNulls: true,
            legend: 'none',
            width: 300,
            height: 150,
            chartArea: {
                top: 10,
                left: 50,
                width: 280,
                height: 120
            },
            hAxis: {
                showTextEvery: resolution * 2
            },
            vAxis: {
                format: '#',
                minValue: ${ratingChart.minRating?string("0")},
                maxValue: ${ratingChart.maxRating?string("0")}
            }}
        );
    }

    function drawGamesChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('number', '<@message code="profile.wins"/>');
        data.addColumn('number', '<@message code="profile.loses"/>');
        data.addColumn('number', '<@message code="profile.draws"/>');
        data.addRows([
            [${statistics.wins}, ${statistics.loses}, ${statistics.draws}]
        ]);

        new google.visualization.BarChart(document.getElementById('gamesChart')).
                draw(data, {
            axisTitlesPosition: 'in',
            colors: ['#008000', 'AA0033', '#FF9900'],
            backgroundColor: 'transparent',
            interpolateNulls: true,
            legend: 'none',
            isStacked: true,
            width: 300,
            height: 75,
            chartArea: {
                top: 0,
                left: 0,
                width: 280,
                height: 40
            }
        });

        var data2 = new google.visualization.DataTable();
        data2.addColumn('number', '<@message code="profile.timeouts"/>');
        data2.addColumn('number', '<@message code="profile.well"/>');
        data2.addRows([
            [${statistics.loses}, ${statistics.wins}]
        ]);

        new google.visualization.BarChart(document.getElementById('timeoutsChart')).
                draw(data2, {
            colors: ['gray', '#00cc66'],
            backgroundColor: 'transparent',
            legend: 'none',
            isStacked: true,
            width: 300,
            height: 75,
            chartArea: {
                top: 0,
                left: 0,
                width: 280,
                height: 40
            }
        })
    }

    google.setOnLoadCallback(drawGamesChart);
    google.setOnLoadCallback(drawRatingGraph);
</script>
