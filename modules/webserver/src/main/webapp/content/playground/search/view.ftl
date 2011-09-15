<#include "/core.ftl">

<@wm.jstable/>

<@wm.playground>
<div>
    <div class="ui-widget-header ui-corner-all shadow">
        Players Search
    </div>
    <div class="ui-widget-content ui-corner-all shadow">
        <div id="searchPlayerWidget">
            <div id="searchTypes">
                <input id="searchFriends" name="searchTypes" type="radio" value="friends" checked="checked"/>
                <label for="searchFriends"><@message code="search.type.friends"/></label>
                <input id="searchPlayedFormerly" name="searchTypes" type="radio" value="formerly"/>
                <label for="searchPlayedFormerly">Player Formerly</label>
            </div>

            <div id="searchContent">
                <div id="searchLoading" class="ui-helper-hidden">
                    <div class="loading-image" style="height: 100px"></div>
                </div>
                <div id="searchResult" class="ui-helper-hidden">
                    <table style="width: 100%; padding-bottom: 5px;">
                        <thead>
                        <tr>
                            <th width="100%" nowrap="nowrap"><@message code="search.column.player"/></th>
                            <th nowrap="nowrap"><@message code="search.column.rating"/></th>
                            <th nowrap="nowrap"><@message code="search.column.language"/></th>
                            <th nowrap="nowrap"><@message code="search.column.active"/></th>
                            <th nowrap="nowrap"><@message code="search.column.finished"/></th>
                            <th nowrap="nowrap"><@message code="search.column.avgMoveTime"/></th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</@wm.playground>

<script type="text/javascript">
    var playerSearch = new wm.Search(false, {
        en: '<@message code="language.ru"/>',
        ru: '<@message code="language.en"/>',
        title: '<@message code="search.label"/>',
        close: '<@message code="button.close"/>'
    });
</script>
