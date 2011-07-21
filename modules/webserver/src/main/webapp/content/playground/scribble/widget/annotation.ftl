<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->

<#include "/core.ftl">

<div class="ui-widget" style="padding-top: 10px">
    <div id="annotationControls" class="ui-widget-header ui-corner-all shadow">

        <div><b>Annotations</b></div>
    </div>
    <div class="ui-widget-content ui-corner-all shadow">
        <div class="ui-widget-content" style="border-width: 0; border-bottom-width:  1px; padding: 0">
            <div style="float: right;">
                <div id="annotationNext" style="background: none; border: none; display: inline-block; cursor: pointer">
                    <div class="ui-icon ui-icon-circle-arrow-w"></div>
                </div>
                <div id="annotationPrev"
                     style="background: none; border: none; display: inline-block; cursor: pointer">
                    <div class="ui-icon ui-icon-circle-arrow-e"></div>
                </div>
            </div>

            <div><b><@wm.player player=playerManager.getPlayer(1001)/></b></div>
        </div>

        <div style="padding-top: 5px;">
            This is annotation.
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $("#annotationNext").hover(function() {
                    $("#annotationNext").addClass("ui-state-hover");
                },
                function() {
                    $("#annotationNext").removeClass("ui-state-hover");
                });

        $("#annotationPrev").hover(function() {
                    $("#annotationPrev").addClass("ui-state-hover");
                },
                function() {
                    $("#annotationPrev").removeClass("ui-state-hover");
                });


        /*
                $("#annotationControls div").hover(
                        function() {
                            $(this).parent().removeClass();
                        },
                        function() {

                        })
        */
    });
</script>