<#-- @ftlvariable name="profile" type="wisematches.server.personality.player.Player" -->
<#-- @ftlvariable name="chart" type="wisematches.server.web.utils.RatingChart" -->
<#include "/core.ftl">

<#assign statistic=profile.playerStatistic/>
<#assign playerProfile=profile.playerProfile/>

<div style="width: 100%">
    <div class="profile shadow ui-state-default">
        <div class="content shadow ui-state-default">
            <div class="title">
                <div class="player">
                    Player's profile: <b>${playerProfile.realName!profile.nickname}</b>
                </div>
                <div class="registered">Registered since Feb-06 (5 years stage)</div>
            </div>
            <div class="edit">
                <button onclick="">Edit Profile</button>
            </div>

        <#assign p_wins=0 p_loses=0 p_draws=0 p_timeouts=0/>
        <#assign wins=statistic.wonGames loses=statistic.lostGames draws=statistic.drawGames timeouts=statistic.timeouts finished=statistic.finishedGames/>
        <#if statistic.finishedGames!=0>
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
            <#assign gamesRating=statistic.ratingsStatistic/>
                <div class="ui-layout-table" style="display: inline-block; vertical-align: top;">
                    <div>
                        <div>Total games in progress:</div>
                        <div>${statistic.activeGames}</div>
                    </div>
                    <div>
                        <div>Timeouts:</div>
                        <div>${timeouts} <span>(${p_timeouts}%)</div>
                    </div>
                    <div>
                        <div>Average moves per game:</div>
                        <div>${gamesRating.averageMovesPerGame}</div>
                    </div>
                    <div>
                        <div>Average time per move:</div>
                        <div>${gameMessageSource.formatMinutes(statistic.averageTurnTime/1000/60, locale)}</div>
                    </div>

                    <div style="height:10px;"></div>

                    <div>
                        <div>Average rating:</div>
                        <div>${gamesRating.average}</div>
                    </div>
                    <div>
                        <div>Highest rating:</div>
                        <div>${gamesRating.highest}</div>
                    </div>
                    <div>
                        <div>Lowest rating:</div>
                        <div>${gamesRating.lowest}</div>
                    </div>
                    <div>
                        <div>Average opponent rating:</div>
                        <div>${gamesRating.averageOpponentRating}</div>
                    </div>
                    <div>
                        <div>Highest rating won against:</div>
                        <div>
                        ${gamesRating.highestWonOpponentRating}
                        <#assign hwp=playerManager.getPlayer(gamesRating.highestWonOpponentId)!""/>
                        <#if hwp?has_content>( <@wm.player player=hwp showRating=false showType=false/>)</#if>
                        </div>
                    </div>
                    <div>
                        <div>Lowest rating lost against:</div>
                        <div>
                        ${gamesRating.lowestLostOpponentRating}
                        <#assign llp=playerManager.getPlayer(gamesRating.lowestLostOpponentId)!""/>
                        <#if hwp?has_content>( <@wm.player player=llp showRating=false showType=false/>)</#if>
                        </div>
                    </div>
                </div>

                <div style="display: inline-block; vertical-align: top; float: right;">
                    <b>Rating graph (past year):</b>
                    <br>
                    <img width="300" height="150"
                         src="http://chart.apis.google.com/chart?chf=bg,s,67676700&chls=1&chg=8.33,25&chco=FFCC33&chxt=x,y&cht=lxy&chs=300x150&chxs=0,676767,11.5,0,lt,676767|1,676767,15.5,0,l,676767&chxl=0:|<#list chart.monthIndexes as m>|<#if m_index %2 ==0><@message code="month.names.${m}"/></#if></#list>1:|${chart.minRating}|${chart.maxRating}&chd=e:${chart.encodedDates},${chart.encodedRatingsAvg}"
                         alt="Rating Chart">
                <#--
                    <img width="300" height="150"
                         src="http://chart.apis.google.com/chart?chf=bg,s,67676700&chls=1,2,2|1|1,2,2&chg=8.33,25&chco=008000,FFCC33,AA0033&chxt=x,y&cht=lxy&chs=300x150&chxs=0,676767,11.5,0,lt,676767|1,676767,15.5,0,l,676767&chxl=0:|<#list chart.monthIndexes as m><#if m_index %2 ==0>AA_${m}</#if>|</#list>1:${chart.minRating}|${chart.maxRating}&chd=e:${chart.encodedDates},${chart.encodedRatingsMax},${chart.encodedDates},${chart.encodedRatingsAvg},${chart.encodedDates},${chart.encodedRatingsMin}"
                         alt="Rating Chart">
-->
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
                <div><b>${profile.nickname}</b></div>
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