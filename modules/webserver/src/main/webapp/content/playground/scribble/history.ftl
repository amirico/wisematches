<#-- @ftlvariable name="columns" type="java.lang.String[]" -->
<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->

<#include "/core.ftl">

<@wm.jstable/>

<@wm.playground id="pastgames">
    <#if player != principal>
    <div class="title">
    <@message code="game.past.history.label"/> <@message code="separator.for"/> <@wm.player player=player showState=true showType=true/>
    </div>
    </#if>

<table id="history" width="100%" class="display">
    <thead>
    <tr>
        <#list columns as c>
            <th><@message code="game.past.history.column.${c}"/></th>
        </#list>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>
</@wm.playground>

<script type="text/javascript">
    $('#history').dataTable({
        "bJQueryUI": true,
        "bStateSave": true,
        "bFilter": false,
        "bSortClasses": false,
        "aaSorting": [
            [0,'asc']
        ],
        "aoColumns": [
            null,
            null,
            {"fnRender": function (oObj) {
                var rc = oObj.aData[2];
                var res = '';
                res += '<div class="rating ' + (rc < 0 ? 'down' : rc == 0 ? 'same' : 'up') + '">';
                res += '<div class="change"><sub>' + (rc < 0 ? '' : '+') + rc + '</sub></div>';
                res += '</div>';
                return res;
            }},
            { "fnRender": function (oObj) {
                var res = "";
                var opponents = oObj.aData[3];
                for (var i in opponents) {
                    res += wm.ui.player(oObj.aData[3][i]);
                    if (i != opponents.length - 1) {
                        res += ', ';
                    }
                }
                return res;
            }},
            {"fnRender": function (oObj) {
                var id = oObj.aData[6];
                var state = oObj.aData[4];
                if (id != 0) {
                    return "<a href='/playground/scribble/board?b=" + id + "'>" + state + "</a>";
                } else {
                    return state;
                }
            }},
            null
        ],
        "iDisplayStart": 0,
        "bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": "/playground/scribble/history<#if player!=principal>?p=${player.id}</#if>",
        "sDom": '<"H"lCr>t<"F"ip>',
        "sPaginationType": "full_numbers",
        "fnServerData": function (sSource, aoData, fnCallback) {
            var data = {};
            for (var i in aoData) {
                data[aoData[i]['name']] = aoData[i]['value'];
            }
            $.post(sSource, $.toJSON(data), function (json) {
                fnCallback(json)
            });
        },
        "oLanguage": {
            "sEmptyTable": "<@message code="game.past.history.empty" args=['/playground/scribble/create', '/playground/scribble/join']/>",
            "sProcessing": "<@message code="game.past.history.processing"/>"
        }
    });
</script>
