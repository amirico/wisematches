<#-- @ftlvariable name="country" type="wisematches.personality.profile.countries.Country" -->
<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="profile" type="wisematches.personality.profile.PlayerProfile" -->
<#-- @ftlvariable name="statistics" type="wisematches.playground.scribble.tracking.ScribbleStatistics" -->
<#-- @ftlvariable name="ratingChart" type="wisematches.server.web.utils.RatingChart" -->
<#-- @ftlvariable name="boardSettings" type="wisematches.playground.scribble.settings.BoardSettings" -->
<#include "/core.ftl">

<#include "/content/playground/messages/scriplet.ftl">
<#include "/content/playground/blacklist/scriplet.ftl">
<#include "/content/playground/friends/scriplet.ftl">

<#macro undefined value>
    <#if value==0><@message code="profile.undefined"/><#else>${value}</#if>
</#macro>
<link rel="stylesheet" type="text/css" href="/content/playground/scribble/scribble.css"/>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>

<div style="width: 100%">
<div class="profile shadow ui-state-default">
<div class="content shadow ui-state-default">
<div class="title">
    <div class="player">
    <#if (profile.realName?? && profile.realName?has_content)>
        <strong>${profile.realName}</strong>
    <#else>
        <strong>${player.nickname}</strong>
    </#if>
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
<#assign p_wins=0 p_loses=0 p_draws=0/>
<#assign active=statistics.activeGames wins=statistics.wins loses=statistics.loses draws=statistics.draws rated=statistics.ratedGames/>
<#if rated != 0>
    <#assign p_wins=(wins/rated*100)?round p_loses=(loses/rated*100)?round p_draws=100-p_wins-p_loses/>
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
                    <td class="ui-state-default">${wins} <span>(${p_wins?string("0")}%)</span></td>
                    <td class="ui-state-default">${loses} <span>(${p_loses?string("0")}%)</span></td>
                    <td class="ui-state-default">${draws} <span>(${p_draws?string("0")}%)</span></td>
                    <td class="ui-state-default">${rated}</td>
                </tr>
                <tr>
                    <td class="ui-state-default" style="padding: 0" colspan="5">
                        <div class="sample"
                             style="font-size: x-small;"><@message code="profile.excluded.unrated" args=[statistics.unratedGames]/></div>
                    </td>
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
                        <div><@undefined value=statistics.averageRating?round/></div>
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
                        <div><@undefined value=statistics.averageOpponentRating?round/></div>
                    </div>
                    <div>
                        <div><@message code="profile.rating.op.low"/>:</div>
                        <div>
                        <#if statistics.lowestLostOpponentId==0>
                            <@message code="profile.undefined"/>
                        <#else>
                        ${statistics.lowestLostOpponentRating}
                            <#assign llp=playerManager.getPlayer(statistics.lowestLostOpponentId)!""/>
                            <#if llp?has_content>( <@wm.player player=llp showType=false/>)</#if>
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
                            <#if hwp?has_content>( <@wm.player player=hwp showType=false/>
                                )</#if>
                        </#if>
                        </div>
                    </div>
                </div>
            </div>
            <div style="text-align: right; width: 300px">
                <div id="ratingChart" style="width:300px; height:150px">
                    <div class="loading-image" style="height: 150px"></div>
                </div>
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
                        <div>${active}</div>
                    </div>
                    <div>
                        <div><@message code="profile.finished"/>:</div>
                        <div>${statistics.finishedGames}</div>
                    </div>
                </div>
                <div class="ui-layout-table" style="padding-top: 10px">
                    <div>
                        <div><@message code="profile.resigned"/>:</div>
                        <div>${statistics.resigned}</div>
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
                            ${gameMessageSource.formatTimeMinutes(statistics.averageMoveTime/1000/60?round, locale)}
                            <#else>
                            <@message code="profile.undefined"/>
                        </#if>
                        </div>
                    </div>
                    <div>
                        <div><@message code="profile.avg.moves"/>:</div>
                        <div><@undefined value=statistics.averageMovesPerGame?round/></div>
                    </div>
                </div>
            </div>
            <div style="vertical-align: top; width: 300px">
                <span class="sample"><@message code="profile.successfulness"/></span>

                <div id="gamesChart" style="width:300px; height:75px">
                    <div class="loading-image" style="height: 50px"></div>
                </div>

                <span class="sample"><@message code="profile.punctual"/></span>

                <div id="timeoutsChart" style="width:300px; height:75px">
                    <div class="loading-image" style="height: 50px"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="statistic ui-corner-all ui-state-default shadow">
    <div class="ui-layout-table">
        <div>
            <div><@message code="profile.points.allHandBonuses"/>:</div>
            <div>${statistics.allHandTilesBonuses}</div>
        </div>
        <div>
            <div><@message code="profile.points.avg"/>:</div>
            <div><@undefined value=statistics.averagePoints?round/></div>
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

    <div class="ui-layout-table ${boardSettings.tilesClass}" style="padding-top: 10px;">
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

<div class="statistic ui-corner-all ui-state-default shadow" style="text-align: center; font-weight: normal;">
    <a href="/playground/scribble/active?p=${player.id}"><@message code="game.dashboard.label"/></a>
    |
    <a href="/playground/scribble/history?p=${player.id}"><@message code="game.past.history.label"/></a>
    |
    <a href="/playground/scribble/create?t=challenge&p=${player.id}"><@message code="game.challenge.label"/></a>
    <br>
<@privateMessage pid=player.id><@message code="messages.send.label"/></@privateMessage>
    |
<@friends pid=player.id><@message code="friends.add.label"/></@friends>
    |
<@blacklist pid=player.id><@message code="blacklist.add.label"/></@blacklist>
</div>
</div>

</div>

<div class="info">
    <div class="photo">
        <img class="shadow" style="width: 200px; height: 200px;" src="/playground/profile/image/view?pid=${player.id}"
             alt="Photo">
    </div>
    <div><strong>${player.nickname}</strong></div>
    <div>
    <#if profile.gender??>
        <@message code="gender." + profile.gender.name()?lower_case/>,
    </#if>
    <#if profile.birthday??>${gameMessageSource.getAge(profile.birthday)} <@message code="profile.edit.years"/>,</#if>
    </div>
<#if country??>
    <div>${country.name},</div>
</#if>
    <div>${player.timeZone.displayName}</div>
<#if profile.comments?? && profile.comments?has_content>
    <div class="quotation">&laquo; ${profile.comments} &raquo;</div>
</#if>
</div>
</div>
</div>

<script type="text/javascript">
    $(".profile button").button();

    google.load('visualization', '1', {packages:['corechart']});

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
                    backgroundColor:'transparent',
                    interpolateNulls:true,
                    legend:'none',
                    width:300,
                    height:150,
                    chartArea:{
                        top:10,
                        left:50,
                        width:280,
                        height:120
                    },
                    hAxis:{
                        showTextEvery:resolution * 2
                    },
                    vAxis:{
                        format:'#',
                        minValue: ${ratingChart.minRating?string("0")},
                        maxValue: ${ratingChart.maxRating?string("0")}
                    }}
        );
    }

    function drawGamesChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', '');
        data.addColumn('number', '<@message code="profile.wins"/>');
        data.addColumn('number', '<@message code="profile.loses"/>');
        data.addColumn('number', '<@message code="profile.draws"/>');
        data.addRows([
            ['', ${wins}, ${loses}, ${draws}]
        ]);

        new google.visualization.BarChart(document.getElementById('gamesChart')).
                draw(data, {
            axisTitlesPosition:'in',
            colors:['#008000', 'AA0033', '#FF9900'],
            backgroundColor:'transparent',
            legend:'none',
            isStacked:true,
            width:300,
            height:75,
            chartArea:{
                top:0,
                left:0,
                width:280,
                height:40
            }
        });

        var data2 = new google.visualization.DataTable();
        data2.addColumn('string', '');
        data2.addColumn('number', '<@message code="profile.resigned"/>');
        data2.addColumn('number', '<@message code="profile.interrupted"/>');
        data2.addColumn('number', '<@message code="profile.well"/>');
        data2.addRows([
            ['',${statistics.resigned},${statistics.timeouts}, ${statistics.finishedGames - statistics.timeouts}]
        ]);

        new google.visualization.BarChart(document.getElementById('timeoutsChart')).
                draw(data2, {
            colors:['lightgrey', 'darkgrey', '#00cc66'],
            backgroundColor:'transparent',
            legend:'none',
            isStacked:true,
            width:300,
            height:75,
            chartArea:{
                top:0,
                left:0,
                width:280,
                height:40
            }
        })
    }

    google.setOnLoadCallback(drawGamesChart);
    google.setOnLoadCallback(drawRatingGraph);
</script>
