<#-- @ftlvariable name="robotPlayers" type="wisematches.server.player.computer.robot.RobotPlayer[]" -->
<#include "/core.ftl">

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

<table width="100%">
<tr>
<td width="160" valign="top">
    Adds will be here. Also other information.
</td>
<td valign="top">
<div id="create-game">
<div>
    <div class="ui-widget-header ui-corner-all">
    <@message code="game.create.label"/>
    </div>
    <div class="ui-widget-content ui-corner-all">
        <div class="info-description"><@message code="game.create.description"/></div>
        <form id="form" class="form" action="/game/create.html" method="post">
            <table>
                <tr>
                    <td>
                    <#--@declare id="title"-->
                        <label for="title"><@message code="game.title.label"/>:</label>
                    </td>
                    <td><@wm.fieldInput path="create.title" value="game.create.title.default"/></td>
                </tr>

                <tr>
                    <td>
                        <label for="boardLanguage"><@message code="game.language.label"/>:</label>
                    </td>
                    <td>
                    <@wm.field path="create.boardLanguage">
                        <select id="boardLanguage" name="boardLanguage" style="width: 170px;">
                            <#list ["en", "ru"] as l>
                                <option value="${l}" <#if (l==wm.statusValue)>selected="selected"</#if>>
                                <@message code="language.${l}"/>
                                </option>
                            </#list>
                        </select>
                    </@wm.field>
                    </td>
                </tr>

                <tr>
                    <td>
                        <label for="daysPerMove">
                        <@message code="game.time.label"/>:
                        </label>
                    </td>
                    <td>
                    <@wm.field path="create.daysPerMove">
                        <select id="daysPerMove" name="daysPerMove" style="width: 170px;">
                            <#list [2,3,4,5,7,10,14] as l>
                                <option value="${l}"
                                        <#if (l==(wm.statusValue)?number)>selected="selected"</#if>>${gameMessageSource.getRemainedTime(l*24*60, locale)}</option>
                            </#list>
                        </select>
                    </@wm.field>
                        <span class="sample"><@message code="game.create.time.description"/></span>
                    </td>
                </tr>

                <tr>
                    <td>
                        <label><@message code="game.opponents.label"/>:</label>
                    </td>
                    <td>
                        <span><@message code="game.create.opponents.description"/>:</span>
                    </td>
                </tr>

            <#assign visibleOpponents = 0/>
            <#list 1..3 as n>
            <@spring.bind path="create.opponent${n}"/>
                <#assign visible=spring.stringStatusValue != "no"/>
                <#if visible><#assign visibleOpponents=visibleOpponents+1/></#if>
                <tr id="op${n}" <#if !visible>style="display: none;"</#if>>
                    <td></td>
                    <td>
                    <@wm.field path="create.opponent${n}">
                        <input type="hidden" id="oi${n}" name="opponent${n}" value="${wm.statusValue}"/>
                        &raquo;
                        <span class="player">
                        <span id="on${n}" class="nickname">
                            <#if visible && wm.statusValue?has_content>
                                <#assign player=playerManager.getPlayer(wm.statusValue?number)/>
                            <@message code="game.player.${player.nickname}"/> (${player.rating?string.computer})
                                <#else>
                                <@message code="game.create.opponents.wait.human.label"/>
                            </#if>
                        </span>
                            </span>
                        <span id="wao${n}"
                              <#if !visible || !wm.statusValue?has_content>style="display: none;"</#if>>
                            <@message code="separator.or"/>
                                <a href="javascript: changeOpponent(${n}, null)"><@message code="game.create.opponents.wait.human.label"/></a>
                        </span>
                    <@message code="separator.or"/>
                        <#if n==1>
                            <div id="selectRobotPlayer">
                                <a href="create.ftl#"><@message code="game.create.opponents.wait.robot.label"/></a>
                                <ul id="robotsList">
                                    <#list robotPlayers as robot>
                                        <li>&raquo;
                                            <a href="javascript: changeOpponent(${n}, {id:'${robot.id}', name:'<@wm.message code="game.player.${robot.nickname}"/>', rating:'${robot.rating?string.computer}'})">
                                            <@wm.player player=robot showType=false/>
                                            </a>
                                        </li>
                                    </#list>
                                </ul>
                            </div>
                            <#else>
                                <a href="javascript: removeOpponent(${n})"><@message code="game.create.opponents.wait.clear.label"/></a>
                        </#if>
                    </@wm.field>
                    </td>
                </tr>
            </#list>

                <tr id="oia" <#if visibleOpponents==3>style="display: none"</#if>>
                    <td></td>
                    <td>
                        <a href="javascript: addOneMoreOpponent()"><@message code="game.create.opponents.wait.more.label"/></a>
                    </td>
                </tr>

                <tr>
                    <td>
                        <label><@message code="game.create.limits.label"/>:</label>
                    </td>
                    <td>
                        <span><@message code="game.create.limits.description"/>:</span>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <div>
                            <label><@message code="game.create.limits.rating.label"/>:</label>
                        <@wm.field path="create.minRating" id="minRatingDiv">
                        <@message code="game.create.limits.rating.min"/>
                            <select name="minRating">
                                <option value="0"
                                        <#if "0"==wm.statusValue>selected="selected"</#if>><@message code="game.create.limits.rating.no"/></option>
                                <#list ['900','950','1000','1050','1100','1150','1200','1250'] as r>
                                    <option value="${r}">${r}</option>
                                </#list>
                            </select>
                        </@wm.field>
                        <@wm.field path="create.maxRating" id="maxRatingDiv">
                            , <@message code="game.create.limits.rating.max"/>
                            <select name="maxRating">
                                <#list ['1350','1400','1450','1500','1550','1600','1650','1700','1750','1800'] as r>
                                    <option value="${r}"
                                            <#if r==wm.statusValue>selected="selected"</#if>>${r}</option>
                                </#list>
                                <option value="0"
                                        <#if "0"==wm.statusValue>selected="selected"</#if>><@message code="game.create.limits.rating.no"/></option>
                            </select>
                        </@wm.field>
                        </div>
                    </td>
                </tr>

                <tr>
                    <td></td>
                    <td>
                        <button><@message code="game.create.submit"/></button>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>

<#--<div>-->
<#--<div class="ui-widget-header ui-corner-top">-->
<#--Invite a Friend-->
<#--</div>-->
<#--<div class="ui-widget-content ui-corner-bottom">-->
<#--<div class="info-description">-->
<#--If you'd like to play with someone and they don't have an account on GameKnot yet, please-->
<#--enter their e-mail address below. They won't even have to create an account to play a game-->
<#--with you!-->
<#--</div>-->
<#--<form action="">-->
<#--<table>-->
<#--<tr>-->
<#--<td style="text-align: right;">-->
<#--&lt;#&ndash;@declare id="title"&ndash;&gt;-->
<#--<label for="title">Player's email address:</label>-->
<#--</td>-->
<#--<td>-->
<#--<input size="20" type="text" value="">-->
<#--<span class="sample">no WiseMatches account required!</span>-->
<#--</td>-->
<#--</tr>-->
<#--<tr>-->
<#--<td style="text-align: right;">-->
<#--&lt;#&ndash;@declare id="title"&ndash;&gt;-->
<#--<label for="title">Your real name:</label>-->
<#--</td>-->
<#--<td>-->
<#--<input size="20" type="text" value="">-->
<#--<span class="sample">for the invitation e-mail</span>-->
<#--</td>-->
<#--</tr>-->
<#--<tr>-->
<#--<td style="text-align: right;">-->
<#--&lt;#&ndash;@declare id="title"&ndash;&gt;-->
<#--<label for="title">Message to the player:</label>-->
<#--</td>-->
<#--<td>-->
<#--<textarea rows="2" cols="40"></textarea>-->
<#--</td>-->
<#--</tr>-->
<#--<tr>-->
<#--<td></td>-->
<#--<td>-->
<#--<button>Challenge</button>-->
<#--</td>-->
<#--</tr>-->
<#--</table>-->
<#--</form>-->
<#--</div>-->
<#--</div>-->
</div>
</td>
<td width="160" valign="top"></td>
</tr>
</table>