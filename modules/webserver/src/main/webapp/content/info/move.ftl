<#-- @ftlvariable name="viewMode" type="java.lang.Boolean" -->
<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->
<#-- @ftlvariable name="ratings" type="java.util.Collection<wisematches.playground.GameRatingChange>" -->
<#-- @ftlvariable name="memoryWords" type="java.util.Collection<wisematches.playground.scribble.Word>" -->

<#assign principal=testPlayer/>
<#include "/core.ftl">

<style type="text/css">
    #board0>table {
        width: 100%;
    }

    #board0>table>tbody>tr>td {
        padding-top: 10px;
        vertical-align: top;
    }

    #board0>table>tbody>tr>td>div {
        padding-top: 0 !important;
    }

    #board0 p b {
        color: #551F1F;
        font-size: 16px;
    }
</style>


<script type="text/javascript">
    var scribbleController = new function() {
        this.execute = function(widget, type, params, data, callback) {
            if (widget == 'memory') {
                if (type == 'load') {
                    callback({success:true, data:{
                        words:[
                        <#list memoryWords as w>
                            {
                                text:'${w.text}',
                                direction:'${w.direction.name()}',
                                position:{row:${w.position.row}, column:${w.position.column}},
                                tiles:[
                                    <#list w.tiles as t>
                                        {number: ${t.number}, letter:'${t.letter?upper_case}', cost: ${t.cost}}<#if t_has_next>,</#if>
                                    </#list>
                                ]
                            }<#if w_has_next>,</#if>
                        </#list>
                        ]}
                    });
                }
                else {
                    callback({success:true});
                }
            }
            else {
                callback({success:false, summary:"This is just example board. You can not do any real actions."});
            }
        }
    };
</script>

<#include "/content/playground/scribble/scriplet.ftl"/>

<div id="board${board.boardId}" class="playboard">
    <div id="info-move">
        <div id="info-move-header" class="info-header">
            <div id="info-move-label" class="info-label"><@messageCapFirst code="info.rules.move.label"/></div>

            <div id="info-move-description" class="info-description">
                <p>
                    Игровое поле Эрудита разделено на несколько областей каждая из которых отображает определенную
                    информацию
                    об игре.
                </p>

                <p>Основной областью является <i>Игровая Доска Эрудит</i> на которой распологаются фишки для игры и
                    под которой находятся элементы управления игрой.</p>
            </div>
        </div>
    </div>

    <table>
        <tr>
            <td width="430px">
            <@wm.widget class="scribbleBoard" title="Игровая Доска Эрудит"/>
            </td>
            <td>
                <p><b>Игровая Доска Эрудит</b> является основным элементом игры и, фактически, ест сама игра. Игровая
                    доска разделена на две части: доска и кнопки управления.</p>

                <p>
                    Доска представляет собой таблицу из 225 ячеек (15 строк и 15 столбцов) и вышей 'руки', размещенной
                    под таблицей. Ваша задача составить слово использую все видимые фишки
                    используя два основных правила:
                </p>
                <ul>
                    <li>Вы должны использовать как минимум одну фишку уже находящуюся на доске;
                    <li>Вы должны использовать как минимум одну фишку из фашей 'руки'.
                </ul>
                <p>
                    <i>Обратите внимание, что эти правила не действую для первого хода. В это случае вы должны составить
                        слово использую только фишки из 'руки' и оно обязано пересекать центральную ячейку.</i>
                </p>

                <p>Вы можете выбрать фишки на поле, нажав на них мышкой и использовать фишки из 'руки' перетаскивая их
                    на доску. Ваше слово должно распологаться либо горизонтально, либо вертикально слева на право либо
                    сверху вниз, соответственно.</p>

                <p>Когда ваше слово размещено вы можете использовать кнопки управления для завершения хода.</p>
            <#--<@message code="game.tip.board.playboard"/>-->
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td width="430px">
            <#include "/content/playground/scribble/widget/controls.ftl"/>
            </td>
            <td>
                <p><b></b></p>

                <p></p>

                <p></p>

                <p></p>

                <p></p>
            <#--<@message code="game.tip.board.controls"/>-->
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td>
                <p><b>Игроки</b> содержит список всех игроков принимающих участие, их заработанные очки и время,
                    оставшееся на ход (только для игрока, чей ход в данный момент)</p>
            <#--<@message code="game.tip.board.players"/>-->
            </td>
            <td width="280px">
            <#include "/content/playground/scribble/widget/players.ftl"/>
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td width="280px">
            <#include "/content/playground/scribble/widget/progress.ftl"/>
            </td>
            <td>
                <p><b>Прогресс</b> содержит информацию о текущей игре.</p>

                <p>Основным элементов здесь является индикатор состояния, показывающий количество использованных фишек,
                    оставшихся фишек в банке и фишек на руках всех игроков.</p>
            <#--<@message code="game.tip.board.progress"/>-->
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td>
                <p><b>Выделенное Слово</b> воказывает информацию о выбранных фишках и выделенном слове (если есть):</p>
                <ul>
                    <li><i>Фишки</i> - показывает выбранные фишки которые, обратите внимание, не всегда могут составлять
                        слово.
                    </li>
                    <li><i>Слово</i> - показывает составленное слово.</li>
                    <li><i>Очки</i> - показывает очки, которые вы заработаете за составленное слово и формулу расчета.
                    </li>
                </ul>
                </p>
                <p>Панель так же предоставляет возможность проверить составленное слово, находится ли оно в словаре либо
                    нет.</p>
            <#--<@message code="game.tip.board.selection"/>-->
            </td>
            <td width="280px">
            <#include "/content/playground/scribble/widget/selection.ftl"/>
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td width="280px">
            <#include "/content/playground/scribble/widget/memory.ftl"/>
            </td>
            <td>
                <p><b>Блокнот</b> позволяет вам запоминать составленные слова для последующего использования.</p>
                <ul>
                    <li>Для добавления слова в блокнот составьте его на доске и назмиже кнопку <span
                            class="ui-icon icon-memory-add"></span>.
                    </li>
                    <li>Для удаления слова нажмите на иконку <span class="ui-icon icon-memory-remove"></span>
                        находящуюся справа от слова или используйте кнопку <span
                                class="ui-icon icon-memory-clear"></span> для удаления всех слов.
                    </li>
                    <li>Что бы составить ранее запомненное слово нажмите на него либо на иконку <span
                            class="ui-icon icon-memory-select"></span> находящую справа от соответствующего слова.
                    </li>
                </ul>
                <p>После каждого хода все ваши запомненные слова проверяются и те, которые не могут быть больше
                    составлены зачеркиваются.</p>

                <p>Количество слов которое вы можете запомнить для одно игры ограниченно и определяется в соответствии с
                    вашим <a href="/account/membership">членством</a>.</p>
            <#--<@message code="game.tip.board.memory"/>-->
            </td>
        </tr>
    </table>

    <table>
        <tr>
            <td>
                <p><b>История Ходов</b> содержит информацию о всех хода в игре.</p>

                <p>Панель содержит таблицу в каждой строке которой содержить номер хода, имя игрока, составленное слово
                    и заработанные очки. Вы можете выделить ранее составленное слово на доске нажав на соответствующее
                    слово в истории.</p>
            <#--<@message code="game.tip.board.history"/>-->
            </td>
            <td width="280px">
            <#include "/content/playground/scribble/widget/history.ftl"/>
            </td>
        </tr>
    </table>
</div>

<script type="text/javascript">
    $("#board${board.boardId} .scribbleBoard .ui-widget-content").prepend(board.getBoardElement());
</script>
