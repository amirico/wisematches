<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="countries" type="java.util.Collection<wisematches.personality.profile.countries.Country>" -->
<#-- @ftlvariable name="profile" type="wisematches.personality.profile.PlayerProfile" -->
<#-- @ftlvariable name="profileForm" type="wisematches.server.web.controllers.personality.profile.form.PlayerProfileForm" -->
<#include "/core.ftl">

<div class="notification shadow ui-state-highlight" style="text-align: center; padding: 5px;">
<@message code="profile.edit.description"/>
    <button onclick="wm.util.url.redirect('/playground/profile/view')"><@message code="profile.edit.done"/></button>
</div>

<#--<div style="width: 100%">-->
<#--<div class="profile shadow ui-state-default">-->
<#--<div class="content shadow ui-state-default">-->
<#--<#if profileForm.gender??><#assign genderName=springMacroRequestContext.getMessage("gender."+profileForm.gender)/></#if>-->
<#--<#if profileForm.primaryLanguage??><#assign languageName=springMacroRequestContext.getMessage("language."+profileForm.primaryLanguage)/></#if>-->
<#--<#if profileForm.birthday??><#assign birthdayName=gameMessageSource.formatDate(profile.birthday, locale)/></#if>-->

<#--<div class="title">-->
<#--<@wm.editor id="realName" code="profile.edit.realname" value=profileForm.realName classes="player"/>-->
<#--</div>-->

<#--<div class="ui-layout-table">-->
<#--<@wm.editor id="comments" code="profile.edit.comments" value=profileForm.comments/>-->

<#--<@wm.editor id="gender" code="profile.edit.gender" value=profileForm.gender view=genderName/>-->

<#--<@wm.editor id="birthday" code="profile.edit.birthday" value=profileForm.birthday view=birthdayName/>-->

<#--<@wm.editor id="countryCode" code="profile.edit.country" value=profileForm.countryCode view=profileForm.country/>-->

<#--<@wm.editor id="primaryLanguage" code="profile.edit.language" value=profileForm.primaryLanguage view=languageName/>-->
<#--</div>-->
<#--</div>-->

<#--<div class="info">-->
<#--<div class="photo">-->
<#--<img id="playerPhoto" style="width: 200px; height: 200px;"-->
<#--src="image/view?pid=${player.id}" alt="Photo">-->

<#--<div style="text-align: center;"><a href="javascript: wm.ui.profile.showSelectProfilePhoto();">change-->
<#--photo</a></div>-->
<#--</div>-->
<#--</div>-->
<#--</div>-->
<#--</div>-->

<div id="selectProfilePhoto">
    <b>Choose image to upload</b>

    <p>
        You can upload JPG, GIF or PNG files. Width and height of the image must be the same. The best size of the
        photo is 200x200 pixels.
    </p>

    <div id="imageUploader"></div>
</div>

<script type="text/javascript">
    wm.ui.profile = new function() {
        var uploader = new qq.FileUploader({
            element: $("#imageUploader")[0],
            action: 'image/edit',
            onComplete: function(id, fileName, responseJSON) {
                if (responseJSON.success) {
                    var a = $("#playerPhoto");
                    a.attr('src', a.attr('src') + '&' + new Date().getTime());
                } else {
                    $("#imageUploader").show();
                }
            },
            template: '<div class="qq-uploader">' +
                    '<div class="qq-upload-button"><button>Select file</button></div>' +
                    '<span>or</span>' +
                    '<div class="qq-upload-drop-area"><span>drop files here to upload</span></div>' +
                    '<div class="qq-upload-list"></div>' +
                    '</div>',

            fileTemplate: '<div>' +
                    '<span class="qq-upload-file"></span>' +
                    '<span class="qq-upload-spinner"></span>' +
                    '<span class="qq-upload-size"></span>' +
                    '<a class="qq-upload-cancel" href="#">Cancel</a>' +
                    '<span class="qq-upload-failed-text">Failed</span>' +
                    '</div>',
            onSubmit: function(id, fileName) {
                $("#imageUploader").hide();
            },
            onProgress: function(id, fileName, loaded, total) {
            },
            onCancel: function(id, fileName) {
            }
        });

//            _addToList: function(id, fileName) {
//                $(this._listElement).empty();
    <#---->
//                qq.FileUploader.prototype._addToList.apply(this, arguments);
//            }});

        //        qq.extend(qq.FileUploader.prototype, {
        //    );


        this.showSelectProfilePhoto = function() {
            $("#selectProfilePhoto").dialog({
                title: 'Select Profile Photo',
                width: 400,
                modal: true,
                buttons: {
                    "Cancel": function() {
                        $(this).dialog("close");
                    },
                    "Set at profile photo": function() {
                        $(this).dialog("close");
                    }
                }
            });
        }
    }
            ;

    var editorController = new wm.ui.editor.Controller($('.profile'),
            function(field, data, callback) {
                wm.ui.showStatus('<@message code="profile.edit.saving"/>');

                $.ajax({
                    url: 'save',
                    cache: false,
                    data: $.toJSON(data),
                    error: function(jqXHR, textStatus, errorThrown) {
                        callback(textStatus);
                    },
                    success: function(data, textStatus, jqXHR) {
                        if (!data.success) {
                            wm.ui.showStatus("<@message code="profile.edit.error"/>: <br><b>" + data.summary + "</b>", true);
                            callback(data.summary);
                        } else {
                            wm.ui.showStatus('<@message code="profile.edit.saved"/>');
                            callback();
                        }
                    }
                });
            },
            {
                realName: {
                    type: 'text'
                },
                comments: {
                    type: 'text'
                },
                gender: {
                    type: 'select',
                    values: {
                        male: '<@message code="gender.male"/>',
                        female: '<@message code="gender.female"/>',
                        other: '<@message code="gender.other"/>'
                    }
                },
                birthday: {
                    type: 'date',
                    opts: {
                        changeMonth: true,
                        changeYear: true,
                        dateFormat: 'dd-mm-yy',
                        displayFormat: 'MM d, yy',
                        yearRange: '1900:2011'

                    }
                },
                primaryLanguage: {
                    type: 'select',
                    values: {
                        en: '<@message code="language.en"/>',
                        ru: '<@message code="language.ru"/>'
                    }
                },
                countryCode: {
                    type: 'select',
                    values: {
    <#list countries as country>
    ${country.code}: "${country.name}"<#if country_has_next>,</#if>
    </#list>
    }
    }
    })
    ;
</script>