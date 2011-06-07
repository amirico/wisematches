<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="profileForm" type="wisematches.server.web.controllers.personality.profile.form.PlayerProfileForm" -->
<#include "/core.ftl">

<#--TODO: move to common macroses-->

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
            <@editor id="realName" label="" value=profileForm.realName empty="Your real name" classes="player"/>
            </div>

            <div class="ui-layout-table">
            <@editor id="comments" label="Introduction" value=profileForm.comments empty="Put a little about yourself here."/>

            <@editor id="gender" label="Gender" value=profileForm.gender empty="Undefined gender"/>

            <@editor id="birthday" label="Birthday" value=profileForm.birthday empty="Your birthday date"/>

            <@editor id="countryCode" label="Country" value=profileForm.countryCode empty="Country where do you live"/>

            <@editor id="primaryLanguage" label="Primary language" value=profileForm.primaryLanguage empty="Your primary language for games"/>
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
    $("#freeow2").freeow('', "Saving profile...", {
        classes: [ "ui-state-error ui-corner-bottom"],
        showStyle: {opacity: .95},
        autoHideDelay: 10000
    });

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
</script>