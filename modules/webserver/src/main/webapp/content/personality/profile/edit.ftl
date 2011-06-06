<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="profile" type="wisematches.personality.profile.PlayerProfile" -->
<#include "/core.ftl">

<style type="text/css">
    .editable div {
        padding-top: 10px;
        padding-right: 20px;
        padding-bottom: 10px;
    }

    .editable div.player {
        padding: 0 !important;
    }

    .player.editor-label {
        display: none;
    }

    .editable:hover {
        cursor: pointer;
        background: #dfeffc;
    }

    #profileEditorDialog {
        padding-left: 20px;
        padding-right: 20px;
    }

    #profileEditorName, #profileEditorContent {
        vertical-align: top;
    }

    #profileEditorContent {
        padding-bottom: 10px;
    }

    #profileEditorContent .ui-datepicker {
        display: block !important;
    }

    #profileEditorControls .ui-button-text {
        padding-top: 2px !important;
        padding-bottom: 2px !important;
    }

    #profileEditorContent, #profileEditorControls {
        text-align: left;
        padding-left: 20px;
    }

    div.ui-datepicker, .ui-datepicker td {
        font-size: 12px;
        line-height: normal;
    }

    .notification {
        display: none;
    }
</style>

<#macro editor id header empty value="" classes="">
<div id="${id}-row" class="editable ${classes}">
    <div class="editor-label ${classes}">${header}</div>
    <div class="editor-view<#if !value?has_content> sample</#if> ${classes}"><#if value?has_content>${value}<#else>${empty}</#if></div>
    <input name="${id}" type="hidden" value="${value}">
</div>
</#macro>

<div class="notification shadow ui-state-highlight" style="text-align: center; padding: 5px;">
    Click on the parts of your profile you want to edit.
    <button onclick="wm.util.url.redirect('/playground/profile/view')">Done editing</button>
</div>

<div style="width: 100%">
    <form>
        <div class="profile shadow ui-state-default">
            <div class="content shadow ui-state-default">
                <div class="title">
                <@editor id="realName" header="" value=profile.realName empty="Your real name" classes="player"/>
                </div>

                <div class="ui-layout-table">
                <@editor id="comments" header="Introduction" value=profile.comments empty="Put a little about yourself here."/>

            <@editor id="gender" header="Gender" value=profile.gender empty="Undefined gender"/>

            <@editor id="birthday" header="Birthday" value=profile.birthday empty="Your birthday date"/>

            <@editor id="countryCode" header="Country" value=profile.countryCode empty="Country where do you live"/>

            <@editor id="primaryLanguage" header="Primary language" value=profile.primaryLanguage empty="Your primary language for games"/>
                </div>
            </div>

            <div class="info">
                <div class="photo">
                    <img style="width: 200px; height: 200px;"
                         src="/resources/images/player/noPlayer200.png" alt="Photo">

                    <div style="text-align: center;"><a href="asd">change photo</a></div>
                </div>
            </div>
        </div>
    </form>
</div>

<div id="profileEditorDialog" class="ui-widget-content" style="display: none;">
    <div class="ui-layout-table">
        <div>
            <div id="profileEditorName"></div>
            <div>
                <div id="profileEditorContent" style="width: 100%"></div>
                <div id="profileEditorControls">
                    <button id="profileEditorSave">Save</button>
                    <button id="profileEditorCancel">Cancel</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    wm.profile = {};

    wm.profile.Language = function() {
        var editor = $('<select><option value="en">English</option><option value="ru">Russian</option></select>');

        this.createEditor = function(valueInfo) {
            return editor.val(valueInfo.value);
        };

        this.getValue = function() {
            return editor.val();
        };

        this.getDisplayValue = function() {
            return editor.children("option:selected").text();
        };
    };

    wm.profile.Gender = function() {
        var editor = $('<select><option value="m">Male</option><option value="f">Female</option><option value="o">Other</option></select>');

        this.createEditor = function(valueInfo) {
            return editor.val(valueInfo.value);
        };

        this.getValue = function() {
            return editor.val();
        };

        this.getDisplayValue = function() {
            return editor.children("option:selected").text();
        };
    };

    wm.profile.Introduction = function() {
        var editor = $('<input label="sfasfasdf">');

        this.createEditor = function(valueInfo) {
            return editor.val(valueInfo.value);
        };

        this.getValue = function() {
            return editor.val();
        };

        this.getDisplayValue = function() {
            return editor.val();
        };
    };

    wm.profile.Birthday = function() {
        var editor = $("<div></div>");
        editor.datepicker({
            changeMonth: true,
            changeYear: true,
            dateFormat: 'dd-mm-yy',
            yearRange: '1900:2011'
        });

        this.createEditor = function(valueInfo) {
            return editor.datepicker("setDate", valueInfo.value);
        };

        this.getValue = function() {
            return $.datepicker.formatDate('dd-mm-yy', editor.datepicker("getDate"));
        };

        this.getDisplayValue = function() {
            return $.datepicker.formatDate('DD, MM d, yy', editor.datepicker("getDate"));
        };
    };

    wm.profile.editor = new function() {
        var editingElement;
        var editingHandler;

        $('#profileEditorSave').click(function() {
            setViewInfo(editingElement, {
                view: editingHandler.getDisplayValue(),
                value: editingHandler.getValue()
            });

            $.blockUI({ message: "<h1>Remote call in progress...</h1>" });

            var values = {};
            $.each($('form').serializeArray(), function(i, field) {
                values[field.name] = field.value;
            });
            alert($.toJSON(values));

            $.ajax({
                url: 'save',
                cache: false,
                data: $.toJSON(values),
                complete: function() {
                    $.unblockUI();
                }
            });
        });

        $('#profileEditorCancel').click(function() {
            $.unblockUI();
            return false;
        });

        var getViewInfo = function(view) {
            return {
                label: $(view).children(".editor-label").text(),
                view: $(view).children(".editor-view").html(),
                value: $(view).children("input").val()
            };
        };

        var setViewInfo = function(view, info) {
            $(view).children(".editor-view").html(info.view);
            $(view).children("input").val(info.value);
        };

        this.openEditor = function(view, callback) {
            editingElement = view;
            editingHandler = callback;
            var viewInfo = getViewInfo(view);

            $("#profileEditorName").text(viewInfo.label);
            $("#profileEditorContent").append(callback.createEditor(viewInfo));

            var offset = $(view).offset();

            $.blockUI({
                centerX: false,
                centerY: false,
                message: $("#profileEditorDialog"),
                fadeIn: false,
                fadeOut: false,
                blockMsgClass: 'shadow',
                css: {
                    width: 'auto',
                    left: offset.left + 5,
                    top: offset.top + 5
                },
                draggable: false,
                onUnblock: function() {
                    $("#profileEditorName").empty();
                    $("#profileEditorContent").empty();
                }
            });
        };
    };

    $('.editable').click(function() {
        if (this.id == 'realName-row') {
            wm.profile.editor.openEditor(this, new wm.profile.Introduction());
        } else if (this.id == 'gender-row') {
            wm.profile.editor.openEditor(this, new wm.profile.Gender());
        } else if (this.id == 'primaryLanguage-row') {
            wm.profile.editor.openEditor(this, new wm.profile.Language());
        } else if (this.id == 'comments-row') {
            wm.profile.editor.openEditor(this, new wm.profile.Introduction());
        } else if (this.id == 'birthday-row') {
            wm.profile.editor.openEditor(this, new wm.profile.Birthday());
        }
    });
</script>