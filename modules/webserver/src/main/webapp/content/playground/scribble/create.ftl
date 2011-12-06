<#-- @ftlvariable name="restricted" type="java.lang.Boolean" -->
<#-- @ftlvariable name="playRobotsOnly" type="java.lang.Boolean" -->
<#-- @ftlvariable name="gamesCount" type="java.lang.Integer" -->
<#-- @ftlvariable name="maxOpponents" type="java.lang.Integer" -->
<#-- @ftlvariable name="robotPlayers" type="java.util.Collection<wisematches.server.player.computer.robot.RobotPlayer>" -->
<#include "/core.ftl">

<@wm.jstable/>

<@wm.playground id="createGameWidget">
<div id="createGame">
<div>
    <#if restricted>
        <@wm.restriction style="margin-bottom: 10px"><@message code="game.create.forbidden" args=[gamesCount, '/playground/scribble/active', '/account/membership']/></@wm.restriction>
    </#if>

<div class="ui-widget-header ui-corner-all shadow">
    <@message code="game.create.label"/>
</div>
<div class="ui-widget-content ui-corner-all shadow" style="margin: 0">
    <form id="form" class="form" style="margin: 0" action="/playground/scribble/create" method="post">
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
                        <@wm.field path="create.createTab">
                            <#assign createTab=wm.statusValue?lower_case/>
                            <#if playRobotsOnly><#assign availableTypes=["robot"]/><#else><#assign availableTypes=["robot", "wait", "challenge"]/></#if>
                            <#list availableTypes as t>
                                <input id="createTab${t?cap_first}" type="radio"
                                       name="createTab" value="${t?upper_case}"
                                       <#if createTab==t>checked="checked"</#if>/>
                                <label for="createTab${t?cap_first}"><@message code="game.create.opponent.${t}"/></label>
                            </#list>
                        </@wm.field>
                    </div>
                </td>
            </tr>

            <tr>
                <td></td>
                <td>
                    <div id="robotForm" class="create-form <#if createTab!="robot">ui-helper-hidden</#if>">
                        <table width="100%" style="border-spacing: 5px">
                            <@wm.field path="create.robotType">
                                <#list robotPlayers as robot>
                                    <#assign t=robot.robotType/>
                                    <tr>
                                        <td nowrap="nowrap">
                                            <input id="robotType${t}" name="robotType" type="radio" value="${t}"
                                                   <#if wm.statusValue==t?upper_case>checked="checked"</#if>/>
                                            <label for="robotType${t}">
                                                <@wm.player player=robot showType=false/> (${robot.rating})
                                            </label>
                                        </td>
                                        <td width="100%">
                                            - <@message code="game.create.robot.${t?lower_case}.description"/>
                                        </td>
                                    </tr>
                                </#list>
                            </@wm.field>
                        </table>
                    </div>

                    <div id="waitingForm" class="create-form <#if createTab!="wait">ui-helper-hidden</#if>">
                        <div style="float: left; padding-right: 10px">
                            <@wm.field path="create.opponentsCount">
                                <#list 1..maxOpponents as c>
                                    <div style="padding: 5px;white-space: nowrap;">
                                        <input type="radio"
                                               id="opponentsCount${c}" name="opponentsCount" value="${c}"
                                               <#if c?string==wm.statusValue>checked="checked"</#if>>
                                        <label for="opponentsCount${c}"
                                               style="color: #a52a2a;"><@message code="game.create.opponents.${c}"/></label>
                                    </div>
                                </#list>
                            </@wm.field>
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

                    <div id="challengeForm"
                         class="create-form  <#if createTab!="challenge">ui-helper-hidden</#if>">
                        <div style="display: inline-block; min-width: 200px">
                            <#assign opponentsCount=0/>
                            <@wm.field id="opponentsList" path="create.opponents">
                                <#list wm.statusValue?split(",") as p>
                                    <#if p?has_content>
                                        <#assign opponentsCount=opponentsCount+1/>
                                        <div>
                                            <#assign player=playerManager.getPlayer(p?number)!""/>
                                            <@wm.player player=player hideLink=true showState=false/>
                                            <input type="hidden" name="opponents" value="${player.id}"/>
                                        </div>
                                    </#if>
                                </#list>
                            </@wm.field>

                            <div id="opponentsControl"
                                 <#if (opponentsCount>=maxOpponents)>class="ui-helper-hidden"</#if>>
                                <a href='#'
                                   onclick="create.selectOpponent(); return false;"><@message code="game.create.opponent.add.label"/></a>
                            </div>
                        </div>

                        <div style="display: inline-block; vertical-align: top; padding-top: 5px; padding-left: 20px">
                            <div>
                                <@message code="game.create.opponent.add.description"/>:
                            </div>
                            <div>
                                <@wm.field id="challengeMessage" path="create.challengeMessage">
                                    <textarea name="challengeMessage"
                                              style="width: 300px; height: 100px">${wm.statusValue}</textarea>
                                </@wm.field>
                            </div>
                        </div>
                    </div>

                    <div>
                        <@wm.field path="create.commonError"/>
                    </div>
                </td>
            </tr>
        </table>

        <input name="rotten" type="hidden" value="true"/>

        <#if !restricted>
            <div style="padding-top: 5px">
                <div>
                    <button><@message code="game.create.submit"/></button>
                </div>
            </div>
        </#if>
    </form>
</div>
</div>
</div>

    <#include "/content/playground/players/scriplet.ftl">
</@wm.playground>

<script type="text/javascript">
    var create = new wm.game.Create(${maxOpponents}, ${opponentsCount}, playerSearch);
</script>