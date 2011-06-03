<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="profile" type="wisematches.personality.profile.PlayerProfile" -->
<#include "/core.ftl">

<style type="text/css">
    .editable div {
        padding-top: 10px;
        padding-right: 20px;
        padding-bottom: 10px;
    }

    .editable:hover {
        cursor: pointer;
        background: #dfeffc;
    }

    #editor .ui-button-text {
        padding-top: 2px !important;
        padding-bottom: 2px !important;
    }

    .notification {
        display: none;
    }
</style>

<#macro editor id header empty value="" classes="">
<div id="${id}" class="editable ${classes}">
    <div class="editor-view-label">${header}</div>
    <div class="editor-view-value<#if !value?has_content> sample</#if>">
        <#if value?has_content>${value}<#else>${empty}</#if>
    </div>
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
                <div id="real-name-editor"
                     class="player editable<#if !profile.realName??> sample</#if>" style="display: inline;">
                <#if profile.realName??>
                ${profile.realName}
                    <#else>
                        Change real name
                </#if>
                </div>
            </div>

            <div class="ui-layout-table">
            <@editor id="introduction-editor" header="Introduction" value=profile.comments empty="Put a little about yourself here."/>
                <div id="qweqwe" class="editor-formset" style="display: none;">
                    <label for="introduction">Introduction</label>
                    <input id="introduction" name="introduction" type="text" value="${profile.comments!""}"/>
                </div>

            <@editor id="gender-editor" header="Gender" value=profile.gender empty="Undefined gender"/>

            <@editor id="birthday-editor" header="Birthday" value=profile.birthday empty="Your birthday date"/>

            <@editor id="country-editor" header="Country" value=profile.countryCode empty="Country where do you live"/>

            <@editor id="language-editor" header="Primary language" value=profile.primaryLanguage empty="Your primary language for games"/>
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

<div id="editor" class="ui-widget-content" style="display:none;">
    <div style="padding-top: 5px;">
        <button id="yes">Save</button>
        <button id="no">Cancel</button>
    </div>
</div>

<script type="text/javascript">
    wm.profile = {};
    wm.profile.editor = new function() {
        $("#editor button").button();

        $('#yes').click(function() {
            $.blockUI({ message: "<h1>Remote call in progress...</h1>" });

            $.ajax({
                url: 'save',
                cache: false,
                complete: function() {
                    $.unblockUI();
                }
            });
        });

        $('#no').click(function() {
            $.unblockUI();
            return false;
        });

        this.openEditor = function(el) {
            $("#editor").datepicker({
                changeMonth: true,
                changeYear: true,
                yearRange: '1900:2011'
            });

            var offset = $(el).offset();

            $.blockUI({
                centerX: false,
                centerY: false,
                message: $('#editor'),
                fadeIn: false,
                fadeOut: false,
                blockMsgClass: 'shadow',
                css: {
                    width: '275px',
                    left: offset.left + 5,
                    top: offset.top + 5
                },
                draggable: false});
        }
    };

    $('.editable').click(function() {
        wm.profile.editor.openEditor(this);
        /*
        if (this.id == 'real-name-editor') {
        } else {
            alert('No editor for: ' + this.id);
        }
*/
    });
</script>