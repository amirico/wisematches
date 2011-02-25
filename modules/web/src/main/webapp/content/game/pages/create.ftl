<#include "/core.ftl">

<script type="text/javascript">
    $(document).ready(function() {
        var selectedOpponent = $("#opponents").val().charAt(0);

        $("#opponents").change(function() {
            var val = $("#opponents").val().charAt(0);
            if (selectedOpponent != val) {
                $("#settingsForm div").toggle();
                selectedOpponent = val;
            }
        });
    });
</script>

<table>
    <tr>
        <td width="150px" valign="top">
            Adds will be here. Also other information.
        </td>
        <td valign="top">
            <div id="create-game">
                <div class="info-header">
                    <div class="info-label">Create New Game</div>

                    <div class="info-description">The newly created game will be available for everyone to join (within
                        the
                        requested parameters). You will be notified when someone has joined your game.
                    </div>
                </div>

                <div>
                    <div>
                        <div class="ui-widget-header ui-corner-top">
                            Start New Game
                        </div>
                        <div class="ui-widget-content ui-corner-bottom">
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
                                        <@wisematches.field path="create.opponents">
                                            <select id="opponents" name="opponents" style="width: 170px;">
                                                <#list ["human1", "human2", "human3", "robot"] as l>
                                                    <option value="${l}">
                                                    <@message code="game.create.opponents.${l}"/>
                                                    </option>
                                                </#list>
                                            </select>
                                        </@wisematches.field>
                                            <span class="sample"><@message code="game.create.opponents.description"/></span>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td></td>
                                        <td id="settingsForm">
                                            <div id="humanSettingsForm">
                                                <b>Rating limit:</b>
                                                min: <select>
                                                <option>1000</option>
                                                <option>2000</option>
                                            </select>
                                                , max: <select>
                                                <option>4000</option>
                                                <option>8000</option>
                                            </select>
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
                            <div class="group label">Challenge Another Player</div>
                        </div>
                        <div class="ui-widget-content ui-corner-bottom">
                            TBD
                        </div>
                    </div>

                    <div>
                        <div class="ui-widget-header ui-corner-top">
                            <div class="group label">Invite a Friend</div>
                        </div>
                        <div class="ui-widget-content ui-corner-bottom">
                            TBD
                        </div>
                    </div>
                </div>
            </div>
        </td>
    </tr>
</table>