<#-- @ftlvariable name="board" type="wisematches.playground.scribble.ScribbleBoard" -->

<#include "/core.ftl">

<div id="annotationWidget" class="ui-widget" style="padding-top: 10px; max-width: 450px;">
    <div id="annotationControls" class="ui-widget-header ui-corner-all shadow">
        <div style="float: right">
            <a href="#" onclick="wm.scribble.annotations.addNew();"
               style="color: #1D5987; border-bottom: 1px dashed; text-decoration: none">add new note</a>
        </div>
        <div><b>Board Notes (7)</b></div>
    </div>

    <div id="annotationArea" class="ui-widget-content ui-corner-all shadow ui-helper-hidden">
        <div>
            <textarea style="width:  100%; height: 100px;"></textarea>
        </div>
        <div style="text-align: right;">
            <button>Submit</button>
            <button onclick="wm.scribble.annotations.cancelNew()">Cancel</button>
        </div>
    </div>

    <div id="annotationView" class="ui-widget-content ui-corner-all shadow ui-helper-hidden">
        <div>
            <div style="float: right;">
                <div id="annotationNext" class="ui-state-disabled"
                     style="background: none; border: none; display: inline-block; cursor: pointer">
                    <div class="ui-icon ui-icon-circle-arrow-w"></div>
                </div>
                <div style="display: inline-block; vertical-align: top;">
                    1 of 7
                </div>
                <div id="annotationPrev"
                     style="background: none; border: none; display: inline-block; cursor: pointer">
                    <div class="ui-icon ui-icon-circle-arrow-e"></div>
                </div>
            </div>

            <div><@wm.player player=playerManager.getPlayer(1001)/></div>
        </div>

        <div class="ui-widget-content"
             style="padding-top: 0; margin-left: -5px; margin-right: -5px;border-width: 0; border-bottom-width:  1px;"></div>

        <div id="annotationControl" style="padding-top: 10px; text-align: justify;">
            This is annotation.
            Дорогие игроки, пожалуйста, обратите внимание, что наша игра все еще находится в стадии активной разработки.
            Вполне воз.
            Дорогие игроки, пожалуйста, обратите внимание, что наша игра все еще находится в стадии активной разработки.
            Вполне воз.
            Дорогие игроки, пожалуйста, обратите внимание, что наша игра все еще находится в стадии активной разработки.
            Вполне воз.
            Дорогие игроки, пожалуйста, обратите внимание, что наша игра все еще находится в стадии активной разработки.
            Вполне воз
        </div>
    </div>
</div>

<script type="text/javascript">
    wm.scribble.annotations = new function() {
        this.addNew = function() {
            $("#annotationView").hide();
            $("#annotationArea").slideDown('slow');
            return false;
        };

        this.cancelNew = function() {
            $("#annotationArea").slideUp('slow');
        };
    };

    $("#annotationWidget button").button();

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