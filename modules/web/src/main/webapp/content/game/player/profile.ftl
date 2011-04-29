<#-- @ftlvariable name="profile" type="wisematches.server.personality.player.Player" -->
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
            <#assign gamesRating=statistic.allGamesStatisticRating/>
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
                        <div>${gamesRating.averageRating}</div>
                    </div>
                    <div>
                        <div>Highest rating:</div>
                        <div>${gamesRating.highestRating}</div>
                    </div>
                    <div>
                        <div>Lowest rating:</div>
                        <div>${gamesRating.lowestRating}</div>
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
                    <img src="http://chart.apis.google.com/chart?chf=bg,s,67676700&chxl=0:|Jan|Mar|May|Jul|Sep|Oct|Dec|1:|1200|1300|1400|1500|1600&chxr=0,0,12|1,1200,1600&chxs=0,3072F3,14,0,lt,676767&chxt=x,y&chs=300x150&cht=lc&chco=3072F3&chd=s:XYejlsgibXbV&chg=9,10&chls=1"
                         width="300" height="150" alt=""/>

                <#--<img src="http://chart.apis.google.com/chart?chf=bg,s,67676700&chxl=0:|Jan|Feb|Mar|Jun|Jul|Aug|1:|100|50|0|2:|100|75|50|25|0&chxs=0,00AA00,14,0.5,l,676767&chxt=x,r,y&chs=500x125&cht=lc&chco=FF0000,0000FF&chd=s:DJGPMeGPVPYbekb,3483ghhasdfsdf&chg=20,25&chls=1,6,3|3,3,4"-->
                <#--width="300" height="125" alt="ratings graph"/>-->
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