<#-- @ftlvariable name="profile" type="wisematches.server.personality.player.Player" -->
<#include "/core.ftl">

<#assign statistic=profile.playerStatistic/>

<style type="text/css">
    .profile .title {
        border-bottom: 1px solid #c0d2d2;
    }

    .profile .player {
        font-size: 28px;
        font-family: Lucida, serif;
    }

    .profile .player .nickname {
        color: #a52a2a;
        font-size: 28px;
        font-weight: bold;
        font-family: Lucida, serif;
    }

    .profile .registered {
        font-size: 10px;
        color: gray;
    }

    .profile .games-count {
        border-spacing: 1px;
    }

    .profile .games-count th {
        padding: 5px;
        background-color: #006666;
    }

    .profile .games-count td {
        color: yellow;
        padding: 5px 15px;
        font-weight: bold;
        text-align: center;
        background-color: #cccccc;
    }

    .profile .games-count .percents {
        color: gray;
        font-weight: normal;
    }
</style>

<#macro gamesCount value>
<div>
    <#if value!=0>
        <span class="absolute">${value?string.computer}</span><span
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
<div class="title">
    <div class="player">
        Player's profile: <span class="nickname">${profile.nickname}</span>
    </div>
    <div class="registered">Registered since Feb-06 (5 years stage)</div>
</div>

<div style="padding-top: 10px"></div>

<table>
    <tr>
        <td valign="bottom">
            <div><b>Finished Games:</b> ${statistic.finishedGames?string.computer}</div>
        </td>
        <td valign="bottom">
            <b>Current Rating:</b> ${profile.rating?string.computer}
        </td>
    </tr>
    <tr>
        <td>
        <#assign wins=statistic.wonGames?string.computer/>
        <#assign losts=statistic.lostGames?string.computer/>
        <#assign draws=statistic.drawGames?string.computer/>
        <#if statistic.finishedGames==0>
            <#assign winPercent=0?string.computer/>
            <#assign lostPercent=0?string.computer/>
            <#assign drawPercent=0?string.computer/>
            <#else>
                <#assign winPercent=(statistic.wonGames/statistic.finishedGames*100)?string.computer/>
                <#assign lostPercent=(statistic.lostGames/statistic.finishedGames*100)?string.computer/>
                <#assign drawPercent=(statistic.drawGames/statistic.finishedGames*100)?string.computer/>
        </#if>
            <img src="http://chart.apis.google.com/chart?chs=300x125&cht=p3&chco=3FA83F|AA0033|FFCC33&chd=t:${winPercent},${lostPercent},${drawPercent}&chdl=wins|losts|draws&chdlp=b&chl=${wins}+(${winPercent}%25)|${losts}+(${lostPercent}%25)|${draws}+(${drawPercent}%25)"
                 width="300" height="125" alt=""/>

        </td>
        <td>
            <img src="http://chart.apis.google.com/chart?chxl=0:|Jan|Feb|Mar|Jun|Jul|Aug|1:|100|50|0|2:|100|75|50|25|0&chxs=0,00AA00,14,0.5,l,676767&chxt=x,r,y&chs=500x125&cht=lc&chco=FF0000,0000FF&chd=s:DJGPMeGPVPYbekb,3483ghhasdfsdf&chg=20,25&chls=1,6,3|3,3,4"
                 width="500" height="125" alt=""/>
        </td>
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
        ${profile.rating?string.computer}
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
        ${statistic.finishedGames?string.computer}
        </td>
    </tr>
    </tbody>
</table>

<table>
    <tr>
        <td>Total games in progress:</td>
        <td>${statistic.activeGames?string.computer}</td>
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
        <td>${gamesRating.averageRating?string.computer}</td>
    </tr>
    <tr>
        <td>Highest rating:</td>
        <td>${gamesRating.highestRating?string.computer}</td>
    </tr>
    <tr>
        <td>Lowest rating:</td>
        <td>${gamesRating.lowestRating?string.computer}</td>
    </tr>
    <tr>
        <td>Average opponent rating:</td>
        <td>${gamesRating.averageOpponentRating?string.computer}</td>
    </tr>
    <tr>
        <td>Highest rating won against:</td>
        <td>${gamesRating.highestWonOpponentRating?string.computer}
                        <#--(${playerManager.getPlayer(gamesRating.highestWonOpponentId).nickname})-->
        </td>
    </tr>
    <tr>
        <td>Lowest rating lost against:</td>
        <td>${gamesRating.lowestLostOpponentRating?string.computer}
            (${playerManager.getPlayer(gamesRating.lowestLostOpponentId).nickname})
        </td>
    </tr>
    <tr>
        <td>Average moves per game:</td>
        <td>${gamesRating.averageMovesPerGame?string.computer}</td>
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

<div>Profile / Personal info: [ <a href="">edit profile</a> ]</div>
<table border="1">
    <tr>
        <td>Real name:</td>
        <td></td>
    </tr>
    <tr>
        <td>Primary Language:</td>
        <td></td>
    </tr>
    <tr>
        <td>City:</td>
        <td></td>
    </tr>
    <tr>
        <td>Country:</td>
        <td></td>
    </tr>
    <tr>
        <td>Time zone:</td>
        <td></td>
    </tr>
    <tr>
        <td>Age:</td>
        <td></td>
    </tr>
    <tr>
        <td>Gender:</td>
        <td></td>
    </tr>
</table>
</td>
<td width="160px" valign="top"></td>
</tr>
</table>