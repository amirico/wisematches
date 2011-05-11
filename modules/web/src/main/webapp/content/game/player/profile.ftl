<#-- @ftlvariable name="profile" type="wisematches.server.personality.player.Player" -->
<#-- @ftlvariable name="chart" type="wisematches.server.web.utils.RatingChart" -->
<#include "/core.ftl">

<#assign playerProfile=profile.playerProfile/>
<#assign playerStatistic=profile.playerStatistic/>

<#assign gamesStatistic=playerStatistic.gamesStatistic/>
<#assign movesStatistic=playerStatistic.movesStatistic/>
<#assign ratingsStatistic=playerStatistic.ratingsStatistic/>

<div style="width: 100%">
<div class="profile shadow ui-state-default">
<div class="content shadow ui-state-default">
    <div class="title">
        <div class="player">
            Player's profile: <strong>${playerProfile.realName!profile.nickname}</strong>
        </div>
        <div class="registered">Registered since Feb-06 (5 years stage)</div>
    </div>
    <div class="edit">
        <button onclick="">Edit Profile</button>
    </div>

<#assign p_wins=0 p_loses=0 p_draws=0 p_timeouts=0/>
<#assign wins=gamesStatistic.wins loses=gamesStatistic.loses draws=gamesStatistic.draws timeouts=gamesStatistic.timeouts finished=gamesStatistic.finished/>
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
                                <span>Rating</span>
                            </td>
                            <td class="ui-state-default">
                                <span style="border-color: #008000">Wins</span>
                            </td>
                            <td class="ui-state-default">
                                <span style="border-color: #AA0033">Loses</span>
                            </td>
                            <td class="ui-state-default">
                                <span style="border-color: #FF9900">Draws</span>
                            </td>
                            <td class="ui-state-default ui-corner-tr">
                                <span>Total</span>
                            </td>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td class="ui-state-default">${profile.rating}</td>
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
                                <div>Average rating:</div>
                                <div>${ratingsStatistic.average}</div>
                            </div>
                            <div>
                                <div>Highest rating:</div>
                                <div>${ratingsStatistic.highest}</div>
                            </div>
                            <div>
                                <div>Lowest rating:</div>
                                <div>${ratingsStatistic.lowest}</div>
                            </div>
                            <div>
                                <div>Average opponent rating:</div>
                                <div>${ratingsStatistic.averageOpponentRating}</div>
                            </div>
                            <div>
                                <div>Highest rating won against:</div>
                                <div>
                                ${ratingsStatistic.highestWonOpponentRating}
                                <#assign hwp=playerManager.getPlayer(ratingsStatistic.highestWonOpponentId)!""/>
                                <#if hwp?has_content>( <@wm.player player=hwp showRating=false showType=false/>)</#if>
                                </div>
                            </div>
                            <div>
                                <div>Lowest rating lost against:</div>
                                <div>
                                ${ratingsStatistic.lowestLostOpponentRating}
                                <#assign llp=playerManager.getPlayer(ratingsStatistic.lowestLostOpponentId)!""/>
                                <#if hwp?has_content>( <@wm.player player=llp showRating=false showType=false/>)</#if>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div style="text-align: right; width: 300px">
                        <div id="ratingChart"></div>
                        <div class="sample"><sub>(past year only)</sub></div>
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
                                <div>Total games in progress:</div>
                                <div>${gamesStatistic.active}</div>
                            </div>
                            <div>
                                <div>Timeouts:</div>
                                <div>${timeouts} <span>(${p_timeouts}%)</div>
                            </div>
                            <div>
                                <div>Average moves per game:</div>
                                <div>${gamesStatistic.averageMovesPerGame}</div>
                            </div>
                            <div>
                                <div>Average time per move:</div>
                                <div>${gameMessageSource.formatMinutes(movesStatistic.averageTurnTime/1000/60, locale)}</div>
                            </div>
                            <div>
                                <div>Last move date:</div>
                                <div>
                                <#if movesStatistic.lastMoveTime??>
                                ${gameMessageSource.formatDate(movesStatistic.lastMoveTime, locale)}
                                    <#else>
                                        no one move
                                </#if>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div style="vertical-align: top; width: 300px">
                        <span class="sample">Successfulness</span>

                        <div id="gamesChart"></div>

                        <span class="sample">Punctual</span>

                        <div id="timeoutsChart"></div>
                    </div>
                </div>
            </div>
        </div>

        <div class="statistic ui-corner-all ui-state-default shadow">
            <div class="ui-layout-table">
                <div style="height: 24px">
                    <div>Average Word:</div>
                    <div style="position: relative;">
                    <#list 1..movesStatistic.averageWordLength as i>
                        <div class="tile cost0"
                             style="top: 0; left: ${(i-1)*22}px; padding: 0;">
                            <span></span></div>
                    </#list>
                        <div style="white-space: nowrap; position: absolute; top:0; left: ${4+22*movesStatistic.averageWordLength}px">
                            <strong>(${movesStatistic.avgPoints} points)</strong>
                        </div>
                    </div>
                </div>
                <div style="height: 24px">
                    <div>Last Longest Word:</div>
                    <div style="position: relative; padding-top: 3px">
                    <#--${movesStatistic.lastLongestWord!""}-->
                    <#list ['a','b','c','d','a','b','c','d','a','b','c','d','a','b','c','d'] as ch>
                        <div class="tile cost6"
                             style="background-position: -22px 0; top: 0; left: ${ch_index*22}px; padding: 0;">
                            <span>${ch?upper_case}</span></div>
                    </#list>
                        <div style="white-space: nowrap; position: absolute; top:0; left: ${4+16*22}px">
                            (${movesStatistic.minPoints} points)
                        </div>
                    </div>
                </div>
                <div style="height: 24px">
                    <div>Last Valuable Word:</div>
                    <div style="position: relative; padding-top: 3px">
                    <#--${movesStatistic.lastValuableWord!""} -->
                    <#list ['a','b','c','d','a','b','c','d','a','b','c','d','a','b','c','d'] as ch>
                        <div class="tile cost6"
                             style="background-position: -22px 0; top: 0; left: ${ch_index*22}px; padding: 0;">
                            <span>${ch?upper_case}</span></div>
                    </#list>
                        <div style="white-space: nowrap; position: absolute; top:0; left: ${4+16*22}px">
                            (${movesStatistic.maxPoints} points)
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div style="padding-top: 10px; text-align: center;">
        <a href="">Games in progress</a> | <a href="">Past game history</a>
    </div>
</div>

<div class="info">
    <div class="photo">
        <img style="width: 200px; height: 200px;"
             src="/resources/images/player/noPlayer200.png" alt="Photo">
    </div>
    <div><strong>${profile.nickname}</strong></div>
    <div <#if !playerProfile.gender??>class="undefined"</#if>>
    ${playerProfile.gender!"gender undefined"},
    </div>
    <div <#if !playerProfile.birthday??>class="undefined"</#if>>
    ${playerProfile.birthday!"ages undefined"},
    </div>
    <div <#if !playerProfile.countryCode??>class="undefined"</#if>>
    ${playerProfile.countryCode!"country undefined"},
    </div>
    <div>
    ${profile.timeZone.displayName}
    </div>
    <div class="<#if !playerProfile.comments??>undefined </#if>quotation">
        &laquo;${playerProfile.comments!""}&raquo;
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
        data.addColumn('number', 'Rating');
        data.addRows(${chart.pointsCount});

        var resolution = ${(chart.pointsCount/chart.monthIndexes?size)?string("0")};
        var months = [<#list chart.monthIndexes as m>'<@message code="month.names.${m}"/>'<#if m_has_next>,</#if></#list>];
        for (m in months) for (i = 0; i < resolution; i++) data.setValue(m * resolution + i, 0, months[m]);

    <#list chart.ratingsPoint as p>
        data.setValue(${p?string.computer}, 1, ${chart.ratingsAvg[p_index]});
    </#list>
        data.setValue(12, 1, 1210);
        data.setValue(0, 1, 1250);

        // Create and draw the visualization.
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
                        minValue: ${chart.minRating?string("0")},
                        maxValue: ${chart.maxRating?string("0")}
                    }}
        );
    }

    function drawGamesChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('number', 'Wins');
        data.addColumn('number', 'Loses');
        data.addColumn('number', 'Draws');
        data.addRows([
            [${gamesStatistic.wins+10}, ${gamesStatistic.loses+5}, ${gamesStatistic.draws+43}]
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
        data2.addColumn('number', 'Timeouts');
        data2.addColumn('number', 'Well');
        data2.addRows([
            [${gamesStatistic.loses+576}, ${gamesStatistic.wins+2342}]
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
