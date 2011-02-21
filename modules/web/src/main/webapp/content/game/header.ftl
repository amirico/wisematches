<#include "/core.ftl">

<table id="game-header">
    <tr>
        <td width="178px" height="72px">
            <img id="header-image" src="/resources/images/logo.png" width="178px" height="72px"/>
        </td>
        <td width="100%" height="72px">
            <table style="height: 100%; width: 100%">
                <tr>
                    <td valign="top" align="right" colspan="2">
                        <b><@security.authentication property="principal.nickname"/>
                            (<@security.authentication property="principal.username"/>)</b>
                        |
                        <a href="/account/logout.html">Logout</a>
                    </td>
                </tr>
                <tr>
                    <td valign="bottom" align="left" style="white-space: nowrap;">
                        <a class="mb" href="/game/dashboard.html">Dashboard</a>
                        <a class="mb" href="/game/gameboard.html">Gameboard</a>
                        <a class="mb" href="/game/tournament.html">Tournaments</a>
                    </td>
                    <td valign="bottom" align="right" style="white-space: nowrap;">
                        <a class="mb" href="/account/modify.html">Settings</a>
                        |
                        <a href="/info/general.html">Help</a>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>