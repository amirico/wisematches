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
<div>
    <table class="games-info" width="90%">
        <thead>
        <tr>
            <td class="ui-state-default ui-corner-tl">
                <span>Rating</span>
            </td>
            <td class="ui-state-default" style="vertical-align:top;">
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
</div>

<div style="padding-top: 10px; width: 90%; text-align: left ;">
    <div style="display: inline-block; vertical-align: top;">
        <div class="statistic">
            <div class="ui-state-default ui-corner-all">
                Games Statistic
            </div>
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

        <div class="statistic">
            <div class="ui-state-default ui-corner-all">
                Ratings Statistic
            </div>
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

        <div class="statistic">
            <div class="ui-state-default ui-corner-all">
                Turns Statistic
            </div>
            <div class="ui-layout-table">
                <div>
                    <div>Turns Count:</div>
                    <div>${movesStatistic.turnsCount}</div>
                </div>
                <div>
                    <div>Words Count:</div>
                    <div>${movesStatistic.wordsCount}</div>
                </div>
                <div>
                    <div>Passes Count:</div>
                    <div>${movesStatistic.passesCount}</div>
                </div>
                <div>
                    <div>Exchanges Count:</div>
                    <div>${movesStatistic.exchangesCount}</div>
                </div>
            </div>
        </div>

        <div class="statistic">
            <div class="ui-state-default ui-corner-all">
                Points Statistic
            </div>
            <div class="ui-layout-table">
                <div>
                    <div>Min Points:</div>
                    <div>${movesStatistic.minPoints}</div>
                </div>
                <div>
                    <div>Average Points:</div>
                    <div>${movesStatistic.avgPoints}</div>
                </div>
                <div>
                    <div>Max Points:</div>
                    <div>${movesStatistic.maxPoints}</div>
                </div>
            </div>
        </div>

        <div class="statistic">
            <div class="ui-state-default ui-corner-all">
                Words Statistic
            </div>
            <div class="ui-layout-table">
                <div>
                    <div>Average word's length:</div>
                    <div>${movesStatistic.averageWordLength}</div>
                </div>
                <div>
                    <div>Last longest word:</div>
                    <div style="position: relative;">
                    <#--${movesStatistic.lastLongestWord!""}-->
                    <#list ['a','b','c','d','a','b','c','d','a','b','c','d','a','b','c','d'] as ch>
                        <div class="tile cost6"
                             style="background-position: -22px 0; top: 0; left: ${ch_index*22}px">
                            <span>${ch?upper_case}</span></div>
                    </#list>
                    </div>
                </div>
                <div>
                    <div>Last Valuable Word:</div>
                    <div>${movesStatistic.lastValuableWord!""}</div>
                </div>
            </div>
        </div>
    </div>

    <div style="display: inline-block; vertical-align: top; float: right;">
        <strong>Rating graph (past year):</strong>
        <br>
        <img width="300" height="150"
             src="http://chart.apis.google.com/chart?chf=bg,s,67676700&chls=1&chg=8.33,25&chco=FFCC33&chxt=x,y&cht=lxy&chs=300x150&chxs=0,676767,11.5,0,lt,676767|1,676767,15.5,0,l,676767&chxl=0:|<#list chart.monthIndexes as m>|<#if m_index %2 ==0><@message code="month.names.${m}"/></#if></#list>1:|${chart.minRating?string("0")}|${((chart.maxRating+3*chart.minRating)/4)?string("0")}|${((chart.maxRating + chart.minRating)/2)?string("0")}|${((3*chart.maxRating+chart.minRating)/4)?string("0")}|${chart.maxRating?string("0")}&chd=e:${chart.encodedPoints},${chart.encodedRatingsAvg}"
             alt="Rating Chart">
    </div>
</div>

<div style="padding-top: 10px; text-align: center;">
    <a href="">Games in progress</a> | <a href="">Past game history</a>
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
</div>

<script type="text/javascript">
    $(".profile button").button();
</script>
