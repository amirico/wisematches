<#-- @ftlvariable name="robotPlayers" type="wisematches.server.player.computer.robot.RobotPlayer[]" -->
<#include "/core.ftl">

<style type="text/css">
    .subnav {
        border: 1px solid #79b7e7;
        background: #dfeffc;
        padding: 0;
    }

    .subnav li {
        display: block;
        margin: 1px;
        padding: 3px 10px;
        border: 1px solid #A6C9E2;
    }
</style>

<script type="text/javascript">
    $(document).ready(function() {
        $('.selectRobotPlayer').hover(
                function() {
                    $(this).find('ul').stop(true, true);
                    $(this).find('ul').fadeIn('fast');
                },
                function() {
                    $(this).find('ul').fadeOut('fast');
                }
                );
    });

    function changeOpponent(pos, id, name, rating) {
        $("#opponentId" + pos).val(id);
        $("#opponentName" + pos).html(name + " (" + rating + ")");
        return false;
    }

    function addOneMoreOpponent() {
        $("")
//        $("#opponentId" + pos).val(id);
//        $("#opponentName" + pos).html(name + " (" + rating + ")");
        return false;
    }
</script>

<table>
<tr>
<td width="150px" valign="top">
    Adds will be here. Also other information.
</td>
<td valign="top">
<div id="create-game">
<#--
    <div class="info-header">
        <div class="info-label">Create New Game</div>
    </div>

-->

<div>
<div class="ui-widget-header ui-corner-top">
    Start New Game
</div>
<div class="ui-widget-content ui-corner-bottom">
    <div class="info-description">The newly created game will be available for everyone to join (within
        the requested parameters). You will be notified when someone has joined your game.
    </div>
    <form id="form" class="form" action="/game/create.html" method="post">
        <table>
            <tr>
                <td>
                <#--@declare id="title"-->
                    <label for="title"><@message code="game.create.title.label"/>:</label>
                </td>
                <td><@wisematches.fieldInput path="create.title"/></td>
            </tr>
            <tr>
                <td>
                    <label for="language"><@message code="game.create.language.label"/>:</label>
                </td>
                <td>
                <@wisematches.field path="create.language">
                    <select id="language" name="language" style="width: 170px;">
                        <#list ["en", "ru"] as l>
                            <option value="${l}" <#if (locale==l)>selected="selected"</#if>>
                            <@message code="language.${l}"/>
                            </option>
                        </#list>
                    </select>
                </@wisematches.field>
                </td>
            </tr>

            <tr>
                <td>
                    <label for="daysPerMove">
                    <@message code="game.create.daysPerMove.label"/>:
                    </label>
                </td>
                <td>
                <@wisematches.field path="create.daysPerMove">
                    <select id="daysPerMove" name="daysPerMove" style="width: 170px;">
                        <#list [2,3,4,5,7,10,14] as l>
                            <option value="${l}" <#if (l==3)>selected="selected"</#if>>
                            ${l} days
                            </option>
                        </#list>
                    </select>
                </@wisematches.field>
                    <span class="sample"><@message code="game.create.daysPerMove.description"/></span>
                </td>
            </tr>

            <tr>
                <td>
                <#--@declare id="opponents"-->
                    <label for="opponents"><@message code="game.create.opponents.label"/>
                        :</label>
                </td>
                <td>
                    <span>You can play with one robot or from one to three real people:</span>
                </td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <input type="hidden" id="opponentId1" name="opponentId1" value="">
                    &raquo; <span id="opponentName1"
                                  style="color: #a52a2a; font-weight: bold;">Wait an opponent</span>
                    or
                    <a href="javascript: alert('asd')">challenge a player</a>
                    or
                        <span class="selectRobotPlayer"
                              style="white-space: nowrap; position: relative; padding: 0; margin: 0">
                        <a href="#">play with a robot
                            <small>▼</small>
                        </a>
                            <ul id="asd" class="subnav"
                                style="display: none; position: absolute; margin: 0; top: -35px;">
                            <#list robotPlayers as robot>
                                <li>&raquo;
                                    <a href="javascript: changeOpponent(1, '${robot.id}', '${robot.nickname}', '${robot.rating?string.computer}');">
                                    ${robot.nickname} (${robot.rating?string.computer})
                                    </a>
                                </li>
                            </#list>
                            </ul>
                            </span>
                </td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <input type="hidden" id="opponentId2" name="opponentId2" value="no">
                    &raquo; <span id="opponentName2"
                                  style="color: #a52a2a; font-weight: bold;">Wait an opponent</span>
                    or
                    <a href="javascript: alert('asd')">challenge a player</a>
                    or
                    <a href="javascript: alert('asd')">remove the opponent</a>
                </td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <input type="hidden" id="opponentId3" name="opponentId3" value="no">
                    &raquo; <span id="opponentName3"
                                  style="color: #a52a2a; font-weight: bold;">Wait an opponent</span>
                    or
                    <a href="javascript: alert('asd')">challenge a player</a>
                    or
                    <a href="javascript: alert('asd')">remove the opponent</a>
                </td>
            </tr>
            <tr>
                <td></td>
                <td><a href="javascript: alert('asd')">add one more opponent</a></td>
            </tr>

            <tr>
                <td>
                    <label>An opponent limits:</label>
                </td>
                <td>
                    <div>
                        <b>Rating limit:</b>
                        min: <select>
                        <option>1000</option>
                        <option>2000</option>
                    </select>
                        , max: <select>
                        <option>4000</option>
                        <option>8000</option>
                    </select>
                        <br>
                    </div>

                    <div>
                        <a href="asd">&raquo; more options...</a>
                    </div>
                </td>
            </tr>

            <tr>
                <td></td>
                <td id="settingsForm" style="padding-left: 30px;">
                    <div id="humanSettingsForm">
                    <#--
                                    <div>
                                        <input type="hidden" id="opponent1" name="opponent1" value="">
                                        <span style="color: #a52a2a; font-weight: bold;">Wait an opponent</span>
                                        or
                                        <a href="javascript: alert('asd')">challenge a player</a>
                                    </div>
                                    <div>
                                        <a href="javascript: alert('asd')">add second opponent</a>
                                    </div>
-->
                                <#--
                                                <div>
                                                    <span style="color: #a52a2a;">#3 &nbsp;&nbsp;</span>
                                                    <span style="color: #a52a2a; font-weight: bold;">No opponent &nbsp;&nbsp;</span>
                                                    <a href="javascript: alert('asd')">add an opponent</a>
                                                </div>
-->
                                <#--<b>Rating limit:</b>-->
                                <#--min: <select>-->
                                <#--<option>1000</option>-->
                                <#--<option>2000</option>-->
                                <#--</select>-->
                                <#--, max: <select>-->
                                <#--<option>4000</option>-->
                                <#--<option>8000</option>-->
                                <#--</select>-->
                    </div>
                    <div id="robotSettingsForm" style="display: none">
                        <label for="robotType">
                        <@message code="game.create.robot.label"/>:
                        </label>
                        <select id="robotType" name="robotType" style="width: 170px">
                            <option value="dull"><@message code="game.robot.type.dull"/></option>
                            <option value="trainee"><@message code="game.robot.type.trainee"/></option>
                            <option value="expert"><@message code="game.robot.type.expert"/></option>
                        </select>
                    </div>
                </td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <button>Create Game</button>
                </td>
            </tr>
        </table>
    </form>
</div>
</div>

<div>
    <div class="ui-widget-header ui-corner-top">
        Invite a Friend
    </div>
    <div class="ui-widget-content ui-corner-bottom">
        <div class="info-description">
            If you'd like to play with someone and they don't have an account on GameKnot yet, please
            enter their e-mail address below. They won't even have to create an account to play a game
            with you!
        </div>
        <form action="">
            <table>
                <tr>
                    <td style="text-align: right;">
                    <#--@declare id="title"-->
                        <label for="title">Player's email address:</label>
                    </td>
                    <td>
                        <input size="20" type="text" value="">
                        <span class="sample">no WiseMatches account required!</span>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right;">
                    <#--@declare id="title"-->
                        <label for="title">Your real name:</label>
                    </td>
                    <td>
                        <input size="20" type="text" value="">
                        <span class="sample">for the invitation e-mail</span>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right;">
                    <#--@declare id="title"-->
                        <label for="title">Message to the player:</label>
                    </td>
                    <td>
                        <textarea rows="2" cols="40"></textarea>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <button>Challenge</button>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</div>
</td>
</tr>
</table>