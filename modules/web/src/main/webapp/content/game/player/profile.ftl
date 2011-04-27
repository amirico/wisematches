<#-- @ftlvariable name="profile" type="wisematches.server.personality.player.Player" -->
<#include "/core.ftl">

<#assign statistic=profile.playerStatistic/>
<#assign playerProfile=profile.playerProfile/>

<style type="text/css">
    .profile {
        width: 960px;
        position: relative;
        margin: 0 auto;
        border-width: 1px;
        -webkit-border-radius: 2px;
        -moz-border-radius: 2px;
        border-radius: 2px;
        background-image: none;
    }

    .profile .content {
        border-width: 0;
        min-height: 300px;
        margin-left: 239px;
        background: #f5f8f9 none;
        border-left-width: 1px;
        padding: 18px 10px 18px 20px;
    }

    .profile .title {
        border-bottom: 1px solid #c0d2d2;
        line-height: normal;
    }

    .profile .player {
        font-size: 28px;
        font-family: Lucida, serif;
        line-height: normal;
    }

    .profile .registered {
        font-size: 10px;
        color: gray;
        line-height: normal;
    }

    .profile .info {
        background-color: transparent;
        left: 0;
        position: absolute;
        padding: 20px 20px;
        top: 0;
        width: 200px;
    }

    .profile .edit {
        top: 20px;
        right: 12px;
        position: absolute;
    }

    .profile .photo {
        padding-bottom: 10px;
    }

    .profile .undefined {
        display: none;
    }

    .profile .quotation {
        padding-top: 10px;
        font-style: italic;
    }

    .profile .games-info td {
        padding: 5px;
        text-align: center;
    }

    .profile .games-info {
        padding-top: 10px;
    }

    .profile .games-info thead td {
        font-weight: bold;
    }

    .profile .games-info tbody td {
        color: #7a7a00;
        font-weight: bold;
        background-image: none;
    }

    .profile .games-info thead td span {
        font-weight: bold;
        border-bottom: 3px solid transparent;
    }

    .profile .games-info tbody td span {
        color: gray;
    }

    .shadow {
        -webkit-box-shadow: 0 0 3px #B6B7BB;
        -moz-box-shadow: 0 0 3px #B6B7BB;
        box-shadow: 0 0 3px #B6B7BB;
    }
</style>

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

        <#assign p_wins=0 p_loses=0 p_draws=0/>
        <#assign wins=statistic.wonGames loses=statistic.lostGames draws=statistic.drawGames finished=statistic.finishedGames/>
        <#if statistic.finishedGames!=0>
            <#assign p_wins=(wins/finished*100) p_loses=(loses/finished*100) p_draws=(draws/finished*100)/>
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


            <div>
                <div><b>Finished Games:</b> ${finished}</div>

            </div>
            <div>
                <div><b>Current Rating:</b> ${profile.rating}</div>
                <img src="http://chart.apis.google.com/chart?chf=bg,s,67676700&chxl=0:|Jan|Feb|Mar|Jun|Jul|Aug|1:|100|50|0|2:|100|75|50|25|0&chxs=0,00AA00,14,0.5,l,676767&chxt=x,r,y&chs=500x125&cht=lc&chco=FF0000,0000FF&chd=s:DJGPMeGPVPYbekb,3483ghhasdfsdf&chg=20,25&chls=1,6,3|3,3,4"
                     width="500" height="125" alt=""/>
            </div>

            <div class="info">
                <div class="photo">
                    <img style="width: 200px; height: 200px;"
                         src="https://ssl.gstatic.com/s2/profiles/images/silhouette200.png" alt="Photo">
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


<#--
<#macro gamesCount value>
<div>
    <#if value!=0>
        <span class="absolute">${value}</span><span
            class="percents">(${value/statistic.finishedGames*100}%)</span>
        <#else>
            <span class="absolute">0</span><span class="percents">(0%)</span>
    </#if>
</div>
</#macro>

<table width="100%" class="profile">
<tr>
<td width="160px" valign="top">
<#include "/content/ops/advertisement.ftl">
</td>
<td valign="top">

<div style="padding-top: 10px"></div>


<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>

<table class="games-count">
    <thead>
    <tr>
        <th class="ui-corner-tl">Rating</th>
        <th>Wins</th>
        <th>Losses</th>
        <th>Draws</th>
        <th class="ui-corner-tr">Total</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>
        ${profile.rating}
        </td>
        <td>
        <@gamesCount value=statistic.wonGames/>
        </td>
        <td>
        <@gamesCount value=statistic.lostGames/>
        </td>
        <td>
        <@gamesCount value=statistic.drawGames/>
        </td>
        <td>
        ${statistic.finishedGames}
        </td>
    </tr>
    </tbody>
</table>

<table>
    <tr>
        <td>Total games in progress:</td>
        <td>${statistic.activeGames}</td>
    </tr>
    <tr>
        <td>Last time online:</td>
        <td>???</td>
    </tr>
    <tr>
        <td>Timeouts:</td>
        <td><@gamesCount value=statistic.timeouts/></td>
    </tr>
    <tr>
        <td>Average time per move:</td>
        <td>${gameMessageSource.formatMinutes(statistic.averageTurnTime/1000/60, locale)}</td>
    </tr>
</table>


<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>


<div>
<#list profile.ratingChanges as rating>
    <div>
    ${rating.changeDate}, ${rating.oldRating}, ${rating.newRating}, ${rating.points}
    </div>
</#list>
</div>

<div>
    <a href="">Rating graph</a>
    <img src="http://chart.apis.google.com/chart?chxl=0:|Jan|Feb|Mar|Jun|Jul|Aug|1:|100|50|0|2:|100|75|50|25|0&chxs=0,00AA00,14,0.5,l,676767&chxt=x,r,y&chs=300x150&cht=lc&chco=FF0000&chd=s:HHIIIIIHG&chg=20,25&chls=1"
         width="300" height="150" alt=""/>
</div>

<#assign gamesRating=statistic.allGamesStatisticRating/>
All rated games:
<table border="1">
    <tr>
        <td>Average rating:</td>
        <td>${gamesRating.averageRating}</td>
    </tr>
    <tr>
        <td>Highest rating:</td>
        <td>${gamesRating.highestRating}</td>
    </tr>
    <tr>
        <td>Lowest rating:</td>
        <td>${gamesRating.lowestRating}</td>
    </tr>
    <tr>
        <td>Average opponent rating:</td>
        <td>${gamesRating.averageOpponentRating}</td>
    </tr>
    <tr>
        <td>Highest rating won against:</td>
        <td>${gamesRating.highestWonOpponentRating}
                        &lt;#&ndash;(${playerManager.getPlayer(gamesRating.highestWonOpponentId).nickname})&ndash;&gt;
        </td>
    </tr>
    <tr>
        <td>Lowest rating lost against:</td>
        <td>${gamesRating.lowestLostOpponentRating}
            (${playerManager.getPlayer(gamesRating.lowestLostOpponentId).nickname})
        </td>
    </tr>
    <tr>
        <td>Average moves per game:</td>
        <td>${gamesRating.averageMovesPerGame}</td>
    </tr>
</table>
(excluding unrated/short games)

<div>
    <div>
        <div><a href="">Games in progress</a></div>
        <div><a href="">Past game history</a></div>
        <div><a href="">Challenge to a game</a></div>
        <div><a href="">Notes…</a></div>
    </div>
</div>
>-->

<script type="text/javascript">
    $(".profile button").button();
</script>