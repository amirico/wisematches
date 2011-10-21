<#include "/core.ftl">

<table class="common-settings ui-widget-content ui-state-default shadow ui-corner-all" style="background-image: none;"
       width="100%">
    <tr>
        <td style="padding-top: 4px; width: 10px;">
            <input id="cleanMemory" name="cleanMemory" type="checkbox" checked="checked" value="true">
        </td>
        <td>
            <label for="cleanMemory">Auto clean up wrong memory words</label>

            <div class="sample">After a move all wrong word in memory will be removed automatically</div>
        </td>
    </tr>

    <tr>
        <td style="padding-top: 4px; width: 10px;">
            <input id="checkWords" name="checkWords" type="checkbox" checked="checked" value="true">
        </td>
        <td>
            <label for="checkWords">Auto check placed words</label>

            <div class="sample">When you place or enter a word it will be after checked in 5 seconds</div>
        </td>
    </tr>

    <tr>
        <td colspan="2">
            <label for="checkWords">Tiles View:</label>

            <div style="padding-left: 10px">
                <div class="tiles-set-prev ui-state-default"
                     style="height: 50px; width: 16px; display: inline-block; position: relative;">
                    <div class="ui-icon ui-icon-arrow-1-w" style="position: absolute;top: 17px;"></div>
                </div>

                <div class="tiles-set-view" style="background-color: #558be7; display: inline-block; padding: 3px">
                    <div class="tile-set-classic" style="width: 242px; height: 44px;"></div>
                </div>
            <#--style="width: 242px; height: 44px; background: url(/resources/images/scribble/board/tiles_1.png)-->
            <#--no-repeat; display: inline-block;">-->

                <div class="tiles-set-next ui-state-default"
                     style="height: 50px; width: 16px; display: inline-block; position: relative;">
                    <div class="ui-icon ui-icon-arrow-1-e" style="position: absolute; top: 17px"></div>
                </div>
            </div>
        <#--<div style="width: 242px; height: 44px; background: url(/resources/images/scribble/board/tiles_2.png) no-repeat"></div>-->
        <#--
            <label for="checkWords">Auto check placed words</label>

            <div class="sample">When you place or enter a word it will be after checked in 5 seconds</div>
-->
        </td>
    </tr>

    <tr>
        <td></td>
        <td align="left">
            <button name="save" type="submit" value="submit"><@message code="account.modify.save"/></button>
        </td>
    </tr>
</table>

<script type="text/javascript">
    var selected = 0;
    var tilesSet = ['tile-set-classic', 'tile-set-classic2'];

    var checkButtons = function() {
        if (selected == 0) {
            $(".tiles-set-prev").attr('disabled', 'disabled');
        } else {
            $(".tiles-set-prev").removeAttr('disabled');
        }

        if (selected == tilesSet.length - 1) {
            $(".tiles-set-next").attr('disabled', 'disabled');
        } else {
            $(".tiles-set-next").removeAttr('disabled');
        }
    };

    $(".tiles-set-prev").hover(
            function() {
                $(this).removeClass('ui-state-default').addClass('ui-state-hover');
            },
            function() {
                $(this).removeClass('ui-state-hover').addClass('ui-state-default');
            }).click(function() {
                if (selected > 0) {
                    selected--;
                    $(".tiles-set-view div").attr('class', tilesSet[selected]);
                    checkButtons();
                }
            });

    $(".tiles-set-next").hover(
            function() {
                $(this).removeClass('ui-state-default').addClass('ui-state-hover');
            },
            function() {
                $(this).removeClass('ui-state-hover').addClass('ui-state-default');
            }).click(function() {
                if (selected < tilesSet.length - 1) {
                    selected++;
                    $(".tiles-set-view div").attr('class', tilesSet[selected]);
                    checkButtons();
                }
            });

    checkButtons();
</script>