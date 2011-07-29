<#-- @ftlvariable name="restricted" type="java.lang.Boolean" -->
<#-- @ftlvariable name="playRobotsOnly" type="java.lang.Boolean" -->
<#-- @ftlvariable name="gamesCount" type="java.lang.Integer" -->
<#-- @ftlvariable name="opponentsCount" type="java.lang.Integer" -->
<#-- @ftlvariable name="robotPlayers" type="wisematches.server.player.computer.robot.RobotPlayer[]" -->
<#include "/core.ftl">

<#--<script type="text/javascript">-->
<#--$(document).ready(function() {-->
<#--$('#selectRobotPlayer').hover(-->
<#--function() {-->
<#--$(this).find('ul').stop(true, true);-->
<#--$(this).find('ul').fadeIn('fast');-->
<#--},-->
<#--function() {-->
<#--$(this).find('ul').fadeOut('fast');-->
<#--}-->
<#--);-->
<#--});-->

<#--function addOneMoreOpponent() {-->
<#--var i1 = $('#op2');-->
<#--var i2 = $('#op3');-->
<#--if (i1.is(':hidden')) {-->
<#--$("#oi2").val("");-->
<#--i1.fadeIn('fast');-->
<#--} else if (i2.is(':hidden')) {-->
<#--$("#oi3").val("");-->
<#--i2.fadeIn('fast');-->
<#--}-->

<#--var t = 1;-->
<#--if (i1.is(':visible')) {-->
<#--t++;-->
<#--}-->
<#--if (i2.is(':visible')) {-->
<#--t++;-->
<#--}-->
<#--if (t == ${opponentsCount}) {-->
<#--$('#oia').hide();-->
<#--}-->
<#--}-->

<#--function changeOpponent(pos, player) {-->
<#--if (player == undefined) {-->
<#--$("#wao" + pos).hide();-->
<#--$("#oi" + pos).val("");-->
<#--$("#on" + pos).html("<@message code="game.create.opponents.wait.human.label"/>");-->
<#--} else {-->
<#--$("#wao" + pos).show();-->
<#--$("#oi" + pos).val(player.id);-->
<#--$("#on" + pos).html(player.name + " (" + player.rating + ")");-->
<#--}-->
<#--}-->

<#--function removeOpponent(pos) {-->
<#--if (pos == 2) {-->
<#--if ($('#op3').is(':visible')) {-->
<#--$('#op3').fadeOut('fast', function() {-->
<#--$('#oia').show();-->
<#--$("#on2").html($("#on3").html());-->
<#--$("#oi2").val($("#oi3").val());-->
<#--$("#on3").html("<@message code="game.create.opponents.wait.human.label"/>");-->
<#--$("#oi3").val("no");-->
<#--});-->
<#--} else {-->
<#--$('#op2').fadeOut('fast', function() {-->
<#--$('#oia').show();-->
<#--$("#on2").html("<@message code="game.create.opponents.wait.human.label"/>");-->
<#--$("#oi2").val("no");-->
<#--});-->
<#--}-->
<#--} else if (pos == 3) {-->
<#--$('#op3').fadeOut('fast', function() {-->
<#--$('#oia').show();-->
<#--$("#on3").html("<@message code="game.create.opponents.wait.human.label"/>");-->
<#--$("#oi3").val("no");-->
<#--});-->
<#--}-->
<#--}-->
<#--</script>-->

<@wm.playground>
<div id="createGame">
    <div>
        <#if restricted>
        <@wm.restriction style="margin-bottom: 10px"><@message code="game.create.forbidden" args=[gamesCount, '/playground/scribble/active', '/account/membership']/></@wm.restriction>
        </#if>

        <div class="ui-widget-header ui-corner-all shadow">
        <@message code="game.create.label"/>
        </div>
        <div class="ui-widget-content ui-corner-all shadow" style="margin: 0">
            <form id="form" class="form" action="/playground/scribble/create" method="post">
                <table class="create-game ui-widget-content ui-state-default shadow ui-corner-all"
                       style="background-image: none;"
                       width="100%">
                    <tr>
                        <td colspan="2">
                        <@message code="game.create.description"/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                        <#--@declare id="title"-->
                            <label for="title"><@message code="game.title.label"/>:</label>
                        </td>
                        <td width="100%"><@wm.fieldInput path="create.title" value="game.create.title.default"/></td>
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
                                            <#if (l==(wm.statusValue)?number)>selected="selected"</#if>>${gameMessageSource.formatMinutes(l*24*60, locale)}</option>
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
                            <div id="radio">
                                <input type="radio" id="opponentTypeRobot" name="type" value="robot" checked="checked"/>
                                <label for="opponentTypeRobot">Play with Robot</label>
                                <input type="radio" id="opponentTypeWait" name="type" value="wait">
                                <label for="opponentTypeWait">Wait an Opponent</label>
                                <input type="radio" id="opponentTypeChallenge" name="type" value="challenge"/>
                                <label for="opponentTypeChallenge">Challenge an Opponent</label>
                            </div>
                        </td>
                    </tr>

                    <tr>
                        <td></td>
                        <td>
                            <div id="robotForm" class="create-form ui-helper-hidden">
                                <table width="100%" style="border-spacing: 5px">
                                    <#list robotPlayers as robot>
                                        <tr>
                                            <td nowrap="nowrap">
                                                <input id="robotType${robot.robotType}" name="robotType" type="radio"/>
                                                <label for="robotType${robot.robotType}">
                                                <@wm.player player=robot showType=false/> (${robot.rating})
                                                </label>
                                            </td>
                                            <td width="100%">
                                                - <@message code="game.create.robot.${robot.robotType?lower_case}.description"/>
                                            </td>
                                        </tr>
                                    </#list>
                                </table>
                            </div>

                            <div id="waitingForm" class="create-form ui-helper-hidden">
                                <div style="float: left;">
                                    <div style="padding: 5px">
                                        <input type="radio" id="opponentsCount1" name="opponentsCount">
                                        <label for="opponentsCount1" style="color: #a52a2a;">One opponent</label>
                                    </div>
                                    <div style="padding: 5px">
                                        <input type="radio" id="opponentsCount2" name="opponentsCount">
                                        <label for="opponentsCount2" style="color: #a52a2a;">Two opponents</label>
                                    </div>
                                    <div style="padding: 5px">
                                        <input type="radio" id="opponentsCount3" name="opponentsCount">
                                        <label for="opponentsCount3" style="color: #a52a2a;">Three opponents</label>
                                    </div>
                                </div>

                                <div>
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
                                </div>
                            </div>

                            <div id="challengeForm" class="create-form ui-helper-hidden">
                                <div style="float: left;">
                                    <div style="padding: 5px">
                                    <@wm.player player=principal/>
                                    </div>
                                </div>

                                <div>
                                    You can challenge an opponent from his/her profile at this moment.
                                </div>
                            </div>
                        </td>
                    </tr>

                    <tr>
                        <td></td>
                        <td class="ui-state-default shadow"></td>
                    </tr>

                    <#if !restricted>
                        <tr>
                            <td></td>
                            <td>
                                <button><@message code="game.create.submit"/></button>
                            </td>
                        </tr>
                    </#if>
                </table>
            </form>
        </div>
    </div>
</div>
</@wm.playground>

<script type="text/javascript">
    $(function() {
        $("#createGame #radio").buttonset();

        $("#opponentTypeRobot").change(function() {
            $(".create-form").slideUp();
            $("#robotForm").slideDown();
        });

        $("#opponentTypeWait").change(function() {
            $(".create-form").slideUp();
            $("#waitingForm").slideDown();
        });

        $("#opponentTypeChallenge").change(function() {
            $(".create-form").slideUp();
            $("#challengeForm").slideDown();
        });
    });
</script>