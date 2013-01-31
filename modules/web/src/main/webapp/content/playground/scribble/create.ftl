<#-- @ftlvariable name="playRobotsOnly" type="java.lang.Boolean" -->
<#-- @ftlvariable name="maxOpponents" type="java.lang.Integer" -->
<#-- @ftlvariable name="robotPlayers" type="wisematches.core.Robot[]" -->
<#include "/core.ftl">

<@wm.ui.table.dtinit/>

<@wm.ui.playground id="createGameWidget">
<div id="createGame">
    <@wm.ui.table.header>
        <@message code="game.menu.games.label"/> > <@message code="game.create.label"/>
    </@wm.ui.table.header>

    <@wm.ui.table.toolbar>
    <table width="100%">
        <tr>
            <td align="left">
                <div class="wm-ui-buttonset">
                    <a href="/playground/scribble/active"><@message code="game.dashboard.label"/></a>
                    <a href="/playground/scribble/join"><@message code="game.join.label"/></a>
                </div>
            </td>
            <td align="right">
            </td>
        </tr>
    </table>
    </@wm.ui.table.toolbar>

<form id="form" class="form" action="/playground/scribble/create" method="post">
    <@wm.ui.table.content wrap=true>
    <table class="create-game" width="100%">
    <tr>
        <td>
        <#--@declare id="title"-->
            <label for="title"><@message code="game.title.label"/>:</label>
        </td>
        <td width="100%"><@wm.ui.input path="create.title" value="game.create.title.default"/></td>
    </tr>

    <tr>
        <td>
            <label for="boardLanguage"><@message code="game.language.label"/>:</label>
        </td>
        <td>
            <@wm.ui.field path="create.boardLanguage">
                <select id="boardLanguage" name="boardLanguage" style="width: 170px;">
                    <#list ["en", "ru"] as l>
                        <option value="${l}" <#if (l==wm.ui.statusValue)>selected="selected"</#if>>
                            <@message code="language.${l?lower_case}"/>
                        </option>
                    </#list>
                </select>
            </@wm.ui.field>
        </td>
    </tr>

    <tr>
        <td>
            <label for="daysPerMove">
                <@message code="game.time.label"/>:
            </label>
        </td>
        <td>
            <@wm.ui.field path="create.daysPerMove">
                <select id="daysPerMove" name="daysPerMove" style="width: 170px;">
                    <#list [2,3,4,5,7,10,14] as l>
                        <option value="${l}"
                                <#if (l==(wm.ui.statusValue)?number)>selected="selected"</#if>>${gameMessageSource.formatTimeMinutes(l*24*60, locale)}</option>
                    </#list>
                </select>
            </@wm.ui.field>
            <span class="sample"><@message code="game.create.time.description"/></span>
        </td>
    </tr>

    <tr>
        <td>
            <label><@message code="game.opponents.label"/>:</label>
        </td>
        <td>
            <div id="radio" class="wm-ui-buttonset">
                <@wm.ui.field path="create.createTab">
                    <#assign createTab=wm.ui.statusValue?lower_case/>
                    <#assign availableTypes=["robot", "wait", "challenge"]/>
                    <#if playRobotsOnly><#assign availableTypes=["robot"]/></#if>
                    <#list availableTypes as t>
                        <input id="createTab${t?cap_first}" type="radio"
                               name="createTab" value="${t?upper_case}"
                               <#if createTab==t>checked="checked"</#if>/>
                        <label for="createTab${t?cap_first}"><@message code="game.create.opponent.${t}"/></label>
                    </#list>
                </@wm.ui.field>
            </div>
        </td>
    </tr>

    <tr>
        <td></td>
        <td>
            <div id="robotForm" class="create-form <#if createTab!="robot">ui-helper-hidden</#if>">
                <table width="100%" style="border-spacing: 5px">
                    <@wm.ui.field path="create.robotType">
                        <#list robotPlayers as robot>
                            <#assign t=robot.robotType/>
                            <tr>
                                <td nowrap="nowrap">
                                    <input id="robotType${t}" name="robotType" type="radio" value="${t}"
                                           <#if wm.ui.statusValue==t?upper_case>checked="checked"</#if>/>
                                    <label for="robotType${t}">
                                        <@wm.player.name player=robot showType=false/> (${robot.rating})
                                    </label>
                                </td>
                                <td width="100%">
                                    - <@message code="game.create.robot.${t?lower_case}.description"/>
                                </td>
                            </tr>
                        </#list>
                    </@wm.ui.field>
                </table>
            </div>

            <div id="waitingForm" class="create-form <#if createTab!="wait">ui-helper-hidden</#if>">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td style="padding-right: 10px">
                            <@wm.ui.field path="create.opponentsCount">
                                <#list 1..maxOpponents as c>
                                    <div style="padding: 5px;white-space: nowrap;">
                                        <input type="radio"
                                               id="opponentsCount${c}" name="opponentsCount" value="${c}"
                                               <#if c?string==wm.ui.statusValue>checked="checked"</#if>>
                                        <label for="opponentsCount${c}"
                                               style="color: #a52a2a;"><@message code="game.create.opponents.${c}"/></label>
                                    </div>
                                </#list>
                            </@wm.ui.field>
                        </td>
                        <td>
                            <table cellpadding="2" cellspacing="0">
                                <tr>
                                    <td>
                                        <label><@message code="game.create.limits.rating.label"/>:</label>
                                    </td>
                                    <td>
                                        <@wm.ui.field path="create.minRating" id="minRatingDiv">
                                            <@message code="game.create.limits.rating.min"/>
                                            <select name="minRating">
                                                <option value="0"
                                                        <#if "0"==wm.ui.statusValue>selected="selected"</#if>><@message code="game.create.limits.no"/></option>
                                                <#list ['900','950','1000','1050','1100','1150','1200','1250'] as r>
                                                    <option value="${r}">${r}</option>
                                                </#list>
                                            </select>
                                        </@wm.ui.field>
                                        <@wm.ui.field path="create.maxRating" id="maxRatingDiv">
                                            , <@message code="game.create.limits.rating.max"/>
                                            <select name="maxRating">
                                                <#list ['1350','1400','1450','1500','1550','1600','1650','1700','1750','1800'] as r>
                                                    <option value="${r}"
                                                            <#if r==wm.ui.statusValue>selected="selected"</#if>>${r}</option>
                                                </#list>
                                                <option value="0"
                                                        <#if "0"==wm.ui.statusValue>selected="selected"</#if>><@message code="game.create.limits.no"/></option>
                                            </select>
                                        </@wm.ui.field>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label><@message code="game.create.limits.games.finished"/>:</label>
                                    </td>
                                    <td>
                                        <@wm.ui.field id="completedDiv" path="create.completed">
                                            <select name="completed">
                                                <option value="0"
                                                        <#if "0"==wm.ui.statusValue>selected="selected"</#if>><@message code="game.create.limits.no"/></option>
                                                <#list [1, 5, 10, 20, 50, 100] as g>
                                                    <option value="${g}">${g}
                                                            <@message code="game.create.limits.game"/>${gameMessageSource.getWordEnding(g, locale)}</option>
                                                </#list>
                                            </select>
                                        </@wm.ui.field>

                                        <div class="sample"><@message code="game.create.limits.games.finished.description"/></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label><@message code="game.create.limits.games.timeouts"/>:</label></td>
                                    <td>
                                        <@wm.ui.field id="timeoutsDiv" path="create.timeouts">
                                            <select name="timeouts">
                                                <option value="0"
                                                        <#if "0"==wm.ui.statusValue>selected="selected"</#if>><@message code="game.create.limits.no"/></option>
                                                <#list [50, 25, 10, 5, 3] as g>
                                                    <option value="${g}">< ${g} %</option>
                                                </#list>
                                            </select>
                                        </@wm.ui.field>
                                        <div class="sample"><@message code="game.create.limits.games.timeouts.description"/></div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>

            <div id="challengeForm"
                 class="create-form  <#if createTab!="challenge">ui-helper-hidden</#if>">
                <div style="display: inline-block; min-width: 200px">
                    <#assign opponentsCount=0/>
                    <@wm.ui.field id="opponentsList" path="create.opponents">
                        <#list wm.ui.statusValue?split(",") as p>
                            <#if p?has_content>
                                <#assign opponentsCount=opponentsCount+1/>
                                <div>
                                    <#assign player=playerManager.getPlayer(p?number)!""/>
                                    <@wm.player.name player=player showState=false/>
                                    <input type="hidden" name="opponents" value="${player.id}"/>
                                </div>
                            </#if>
                        </#list>
                    </@wm.ui.field>

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
                        <@wm.ui.field id="challengeMessage" path="create.challengeMessage">
                            <textarea name="challengeMessage"
                                      style="width: 300px; height: 100px">${wm.ui.statusValue}</textarea>
                        </@wm.ui.field>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    </table>

    <input name="rotten" type="hidden" value="true"/>
    </@wm.ui.table.content>

    <@wm.security.observed>
        <@wm.ui.table.statusbar align="left">
        <button class="wm-ui-button"
                onclick="create.submitForm(); return false;"><@message code="game.create.submit"/></button>
        </@wm.ui.table.statusbar>
    </@wm.security.observed>
</form>

    <@wm.ui.table.footer>
        <@wm.security.info code="game.create.forbidden"/>
    </@wm.ui.table.footer>
</div>

    <#include "/content/playground/players/search/scriplet.ftl">
</@wm.ui.playground>

<script type="text/javascript">
    var create = new wm.game.Create(${maxOpponents}, ${opponentsCount}, playerSearch, {
        waiting: "<@message code="game.create.waiting.label"/>"
    });
</script>