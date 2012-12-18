<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->

<#include "/core.ftl">

<#include "/content/templates/addthis.ftl"/>

<div class="shareWidget ui-helper-hidden">
    <div class="shareToolbox ui-widget-content ui-corner-all shadow" align="left" style="position: relative">
    <#if principal?? && board.getPlayerHand(principal.id)??>
        <@addthis title="share.board.my.label" description="share.board.my.description" args=[principal.nickname] counter=false/>
        <div class="shareHandElement ui-helper-hidden ui-state-active"
             style="position: absolute; top: 24px; left: 0; padding: 2px">
            <input type="checkbox" id="shareHandInput" name="shareHandInput" style="vertical-align: text-bottom;">
            <label for="shareHandInput" style="padding-left: 3px"><@message code="share.board.tiles.label"/></label>
        </div>
    <#else>
        <@addthis title="share.board.other.label" description="share.board.other.description" counter=false/>
    </#if>
    </div>
</div>

<script type="text/javascript">
    addthis.addEventListener('addthis.ready', function () {
        $(".shareWidget").slideDown('fast');
    });

    <#if principal?? && board.getPlayerHand(principal.id)??>
    wm.scribble.share = function (board) {
        var firstInitiated = false;
        var shareTilesInput = $(".shareToolbox input");

        var updateShareURL = function () {
            var url;
            if (shareTilesInput.is(':checked')) {
                var s = "";
                $.each(board.getHandTiles(), function (i, t) {
                    s += t.letter;
                });
                url = wm.util.url.extend(null, 't', s, true);
            } else {
                url = wm.util.url.remove(null, 't');
            }
            addthis.update('share', 'url', url);
        };

        addthis.addEventListener('addthis.ready', function () {
            if (!firstInitiated) {
                $(".shareToolbox").hover(function () {
                    updateShareURL();
                    $(".shareToolbox .shareHandElement").show("blind", {}, 'fast');
                }, function () {
                    $(".shareToolbox .shareHandElement").hide("blind");
                });
                $(shareTilesInput).change(function () {
                    updateShareURL();
                });
                updateShareURL();
                firstInitiated = true;
            }
        });
    }(board);
    </#if>
</script>
