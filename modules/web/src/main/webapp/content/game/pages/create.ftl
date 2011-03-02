<#-- @ftlvariable name="robotPlayers" type="wisematches.server.player.computer.robot.RobotPlayer[]" -->
<#include "/core.ftl">
<#import "../macros.ftl" as game>
<#import "/content/utils.ftl" as utils>

<script type="text/javascript">
    $(document).ready(function() {
        $('#selectRobotPlayer').hover(
                function() {
                    $(this).find('ul').stop(true, true);
                    $(this).find('ul').fadeIn('fast');
                },
                function() {
                    $(this).find('ul').fadeOut('fast');
                }
                );
    });

    function addOneMoreOpponent() {
        var i1 = $('#op2');
        var i2 = $('#op3');
        if (i1.is(':hidden')) {
            $("#oi2").val("");
            i1.fadeIn('fast');
        } else if (i2.is(':hidden')) {
            $("#oi3").val("");
            i2.fadeIn('fast');
        }

        if (i1.is(':visible') && i2.is(':visible')) {
            $('#oia').hide();
        }
        return false;
    }

    function changeOpponent(pos, player) {
        if (player == undefined) {
            $("#wao" + pos).hide();
            $("#oi" + pos).val("");
            $("#on" + pos).html("<@message code="game.create.opponents.wait.human.label"/>");
        } else {
            $("#wao" + pos).show();
            $("#oi" + pos).val(player.id);
            $("#on" + pos).html(player.name + " (" + player.rating + ")");
        }
        return false;
    }

    function removeOpponent(pos) {
        if (pos == 2) {
            if ($('#op3').is(':visible')) {
                $('#op3').fadeOut('fast', function() {
                    $('#oia').show();
                    $("#on2").html($("#on3").html());
                    $("#oi2").val($("#oi3").val());
                    $("#on3").html("<@message code="game.create.opponents.wait.human.label"/>");
                    $("#oi3").val("no");
                });
            } else {
                $('#op2').fadeOut('fast', function() {
                    $('#oia').show();
                    $("#on2").html("<@message code="game.create.opponents.wait.human.label"/>");
                    $("#oi2").val("no");
                });
            }
        } else if (pos == 3) {
            $('#op3').fadeOut('fast', function() {
                $('#oia').show();
                $("#on3").html("<@message code="game.create.opponents.wait.human.label"/>");
                $("#oi3").val("no");
            });
        }
    }
</script>

<table>
<tr>
<td width="160" valign="top">
    Adds will be here. Also other information.
</td>
<td valign="top">
<div id="create-game">
<div>
    <div class="ui-widget-header ui-corner-top">
    <@message code="game.create.label"/>
    </div>
    <div class="ui-widget-content ui-corner-bottom">
        <div class="info-description"><@message code="game.create.description"/></div>
        <form id="form" class="form" action="/game/create.html" method="post">
            <table>
                <tr>
                    <td>
                    <#--@declare id="title"-->
                        <label for="title"><@message code="game.create.title.label"/>:</label>
                    </td>
                    <td><@wisematches.fieldInput path="create.title" value="game.create.title.default"/></td>
                </tr>
                <tr>
                    <td>
                        <label for="boardLanguage"><@message code="game.create.language.label"/>:</label>
                    </td>
                    <td>
                    <@wisematches.field path="create.boardLanguage">
                        <select id="boardLanguage" name="boardLanguage" style="width: 170px;">
                            <#list ["en", "ru"] as l>
                                <option value="${l}" <#if (l==wisematches.statusValue)>selected="selected"</#if>>
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
                                <option value="${l}"
                                        <#if (l==(wisematches.statusValue)?number)>selected="selected"</#if>>
                                <@utils.daysAsString days=l/>
                                </option>
                            </#list>
                        </select>
                    </@wisematches.field>
                        <span class="sample"><@message code="game.create.daysPerMove.description"/></span>
                    </td>
                </tr>

                <tr>
                    <td>
                        <label><@message code="game.create.opponents.label"/>:</label>
                    </td>
                    <td>
                        <span><@message code="game.create.opponents.description"/>:</span>
                    </td>
                </tr>

            <#assign visibleOpponents = 0/>
            <#list 1..3 as n>
            <@wisematches.field path="create.opponent${n}">
                <#assign visible=wisematches.statusValue != "no"/>
                <#if visible><#assign visibleOpponents=visibleOpponents+1/></#if>
                <tr id="op${n}" <#if !visible>style="display: none;"</#if>>
                    <td></td>
                    <td>
                        <input type="hidden" id="oi${n}" name="opponent${n}" value="${wisematches.statusValue}">
                        &raquo;
                        <span class="player">
                        <span id="on${n}" class="nickname">
                            <#if visible && wisematches.statusValue?has_content>
                                <#assign player=playerManager.getPlayer(wisematches.statusValue?number)/>
                            <@message code="game.player.${player.nickname}"/> (${player.rating?string.computer})
                                <#else>
                                <@message code="game.create.opponents.wait.human.label"/>
                            </#if>
                        </span>
                            </span>
                        <span id="wao${n}"
                              <#if !visible || !wisematches.statusValue?has_content>style="display: none;"</#if>>
                            <@message code="separator.or"/>
                                <a href="javascript: changeOpponent(${n}, null);"><@message code="game.create.opponents.wait.human.label"/></a>
                        </span>
                    <@message code="separator.or"/>
                        <#if n==1>
                            <span id="selectRobotPlayer">
                            <a href="#"><@message code="game.create.opponents.wait.robot.label"/></a>
                                <ul id="robotsList">
                                    <#list robotPlayers as robot>
                                        <li>&raquo;
                                            <a href="javascript: changeOpponent(${n}, {id:'${robot.id}', name:'<@wisematches.message code="game.player.${robot.nickname}"/>', rating:'${robot.rating?string.computer}'});">
                                            <@game.player player=robot showType=false/>
                                            </a>
                                        </li>
                                    </#list>
                                </ul>
                                </span>
                            <#else>
                                <a href="javascript: removeOpponent(${n});"><@message code="game.create.opponents.wait.clear.label"/></a>
                        </#if>
                    </td>
                </tr>
            </@wisematches.field>
            </#list>

                <tr id="oia" <#if visibleOpponents==3>style="display: none"</#if>>
                    <td></td>
                    <td>
                        <a href="javascript: addOneMoreOpponent();"><@message code="game.create.opponents.wait.more.label"/></a>
                    </td>
                </tr>

                <tr>
                    <td>
                        <label>Limitations:</label>
                    </td>
                    <td>
                        <span>You can play with one robot or from one to three real people:</span>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <div>
                            <label>Rating limits:</label>
                            min
                            <select name="minRating">
                                <option value="0">no limit</option>
                            <#list ['900','950','1000','1050','1100','1150','1200','1250'] as r>
                                <option value="${r}">${r}</option>
                            </#list>
                            </select>
                            , max
                            <select name="maxRating">
                            <#list ['1350','1400','1450','1500','1550','1600','1650','1700','1750','1800'] as r>
                                <option value="${r}">${r}</option>
                            </#list>
                                <option value="0">no limit</option>
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