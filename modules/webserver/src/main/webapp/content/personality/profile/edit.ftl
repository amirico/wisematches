<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="profile" type="wisematches.personality.profile.PlayerProfile" -->
<#include "/core.ftl">

<style type="text/css">
    .ui-editor-item div {
        padding-top: 10px;
        padding-right: 20px;
        padding-bottom: 10px;
    }

    .ui-editor-item div.player {
        padding: 0 !important;
    }

    .ui-editor-item.player .ui-editor-label {
        display: none;
    }

    .ui-editor-item:hover {
        cursor: pointer;
        background: #dfeffc;
    }

    .ui-widget-editor {
        padding-left: 20px;
        padding-right: 20px;
    }

    .ui-editor-content {
        padding-bottom: 10px;
    }

    .ui-editor-content .ui-datepicker {
        display: block !important;
    }

    .ui-editor-content div.ui-datepicker, .ui-editor-content .ui-datepicker td {
        font-size: 12px;
        line-height: normal;
    }

    .ui-editor-controls .ui-button-text {
        padding-top: 2px !important;
        padding-bottom: 2px !important;
    }

    .ui-editor-content, .ui-editor-controls {
        text-align: left;
        padding-left: 20px;
    }

    .notification {
        display: none;
    }
</style>

<#macro editor id label empty value="" classes="">
<div id="${id}" class="ui-editor-item ${classes}">
    <div class="ui-editor-label ${classes}">${label}</div>
    <div label="${empty}"
         class="ui-editor-view<#if !value?has_content> sample</#if> ${classes}"><#if value?has_content>${value}<#else>${empty}</#if></div>
    <input name="${id}" type="hidden" value="${value}">
</div>
</#macro>

<div class="notification shadow ui-state-highlight" style="text-align: center; padding: 5px;">
    Click on the parts of your profile you want to edit.
    <button onclick="wm.util.url.redirect('/playground/profile/view')">Done editing</button>
</div>

<div style="width: 100%">
    <div class="profile shadow ui-state-default">
        <div class="content shadow ui-state-default">
            <div class="title">
            <@editor id="realName" label="" value=profile.realName empty="Your real name" classes="player"/>
            </div>

            <div class="ui-layout-table">
            <@editor id="comments" label="Introduction" value=profile.comments empty="Put a little about yourself here."/>

            <@editor id="gender" label="Gender" value=profile.gender empty="Undefined gender"/>

            <@editor id="birthday" label="Birthday" value=profile.birthday empty="Your birthday date"/>

            <@editor id="countryCode" label="Country" value=profile.countryCode empty="Country where do you live"/>

            <@editor id="primaryLanguage" label="Primary language" value=profile.primaryLanguage empty="Your primary language for games"/>
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
    var editorController = new wm.ui.editor.Controller($('.profile'), {
        realName: {
            type: 'text'
        },
        comments: {
            type: 'text'
        },
        gender: {
            type: 'select',
            values: {
                male: 'Male',
                female: 'Female',
                other: 'Other'
            }
        },
        birthday: {
            type: 'date',
            opts: {
                changeMonth: true,
                changeYear: true,
                dateFormat: 'dd-mm-yy',
                yearRange: '1900:2011'

            }
        },
        primaryLanguage: {
            type: 'select',
            values: {
                en: 'English',
                ru: 'Russian'
            }
        }
    });
    /*
    wm.profile = {};

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
            wm.profile.editor.openEditor(this, new wm.ui.editor.TextEditor());
        } else if (this.id == 'gender-row') {
            wm.profile.editor.openEditor(this, new wm.ui.editor.SelectEditor({
                male: 'Male',
                female: 'Female',
                other: 'Other'
            }));
        } else if (this.id == 'primaryLanguage-row') {
            wm.profile.editor.openEditor(this, new wm.ui.editor.SelectEditor({
                en: 'English',
                ru: 'Russian'
            }));
        } else if (this.id == 'comments-row') {
            wm.profile.editor.openEditor(this, new wm.ui.editor.TextEditor());
        } else if (this.id == 'birthday-row') {
            wm.profile.editor.openEditor(this, new wm.ui.editor.DateEditor({
                changeMonth: true,
                changeYear: true,
                dateFormat: 'dd-mm-yy',
                yearRange: '1900:2011'
            }));
        }
    });
*/
</script>