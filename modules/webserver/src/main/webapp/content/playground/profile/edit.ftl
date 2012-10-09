<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="countries" type="java.util.Collection<wisematches.personality.profile.countries.Country>" -->
<#-- @ftlvariable name="profile" type="wisematches.personality.profile.PlayerProfile" -->
<#-- @ftlvariable name="profileForm" type="wisematches.server.web.controllers.personality.profile.form.PlayerProfileForm" -->
<#include "/core.ftl">

<script type="text/javascript" src="/jquery/js/fileuploader.js"></script>

<div class="notification shadow ui-state-highlight" style="text-align: center; padding: 5px;">
<@message code="profile.edit.description"/>
    <button onclick="wm.util.url.redirect('/playground/profile/view?p=${profile.playerId}')"><@message code="profile.edit.done"/></button>
</div>

<div style="width: 100%">
    <div class="profile shadow ui-state-default">
        <div class="content shadow ui-state-default">
        <#if profileForm.gender??><#assign genderName=springMacroRequestContext.getMessage("gender."+profileForm.gender)/></#if>
        <#if profileForm.primaryLanguage??><#assign languageName=springMacroRequestContext.getMessage("language."+profileForm.primaryLanguage)/></#if>
        <#if profileForm.birthday??><#assign birthdayName=gameMessageSource.formatDate(profile.birthday, locale)/></#if>

            <div class="title">
            <@wm.editor id="realName" code="profile.edit.realname" value=profileForm.realName classes="player"/>
            </div>

            <div class="ui-layout-table">
            <@wm.editor id="comments" code="profile.edit.comments" value=profileForm.comments/>

<@wm.editor id="gender" code="profile.edit.gender" value=profileForm.gender view=genderName/>

<@wm.editor id="birthday" code="profile.edit.birthday" value=profileForm.birthday view=birthdayName/>

<@wm.editor id="countryCode" code="profile.edit.country" value=profileForm.countryCode view=profileForm.country/>

<@wm.editor id="primaryLanguage" code="profile.edit.language" value=profileForm.primaryLanguage view=languageName/>
            </div>
        </div>

        <div class="info">
            <div class="photo">
                <img class="shadow" style="width: 200px; height: 200px;"
                     src="/playground/profile/image/view?pid=${player.id}" alt="Photo">

                <div class="remove">
                    <img title="<@message code="profile.edit.photo.remove.label"/>" src="/resources/images/close.png"
                         onclick="wm.ui.profile.removeProfilePhoto()"/>
                </div>

                <div>
                    <a href="#"
                       onclick="wm.ui.profile.chooseProfilePhoto()"><@message code="profile.edit.photo.change.label"/></a>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="changeProfileDialog" class="qq-uploader ui-helper-hidden">
    <table width="100%">
        <tr>
            <td valign="top" align="left">
                <div class="info-header"><b><@message code="profile.edit.photo.label"/></b></div>

                <div class="info-description"><@message code="profile.edit.photo.description"/></div>

                <div class="qq-upload-failed-text ui-state-error-text"></div>

                <div class="qq-upload-button ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
                    <span class="ui-button-text"><@message code="profile.edit.photo.select"/></span>
                </div>

                <div class="qq-upload-list"></div>
            </td>

            <td valign="top" align="center">
                <div class="qq-upload-drop-area preview">
                    <img class="shadow" width="200px" height="200px"
                         src="/playground/profile/image/view?pid=${player.id}&preview=true">
                </div>
                <div class="sample">(<@message code="profile.edit.photo.drop"/>)</div>
            </td>
        </tr>
    </table>
</div>

<script type="text/javascript">
    wm.ui.profile = new function () {
        var dialogElement = $("#changeProfileDialog");
        var chooseButton = dialogElement.find(".qq-upload-button");
        var errorMessage = dialogElement.find(".qq-upload-failed-text");

        var cancelButton = $("<button>Cancel</button>").button();
        var updateButton = $("<button>Set as default</button>").button();

        chooseButton.hover(function () {
            chooseButton.addClass("ui-state-hover");
        }, function () {
            chooseButton.removeClass("ui-state-hover");
        });

        var updateProfileImage = function () {
            wm.ui.refreshImage($(".profile .info .photo > img"));
        };

        var updatePreviewImage = function () {
            wm.ui.refreshImage($(".qq-upload-drop-area > img"));
        };

        var showErrorMessage = function (message) {
            errorMessage.html(message).show();
        };

        var uploader = new qq.FileUploader({
            element:dialogElement[0],
            action:'image/preview',
            fileTemplate:'<div>' +
                    '<span class="qq-upload-file"></span>' +
                    '<span class="qq-upload-spinner"></span>' +
                    '<span class="qq-upload-size"></span>' +
                    '<a class="qq-upload-cancel" href="#"><@message code="button.cancel"/></a>' +
                    '</div>',
            sizeLimit:512000,
            allowedExtensions:['jpg', 'jpeg', 'png', 'gif'],
            messages:{
                typeError:"<@message code="profile.edit.error.photo.type"/>",
                sizeError:"<@message code="profile.edit.error.photo.size"/>",
                emptyError:"<@message code="profile.edit.error.photo.empty"/>",
                onLeave:"<@message code="profile.edit.error.photo.leave"/>"
            },
            onSubmit:function (id, fileName) {
                chooseButton.hide();
                errorMessage.hide();
            },
            onComplete:function (id, fileName, responseJSON) {
                chooseButton.show();
                dialogElement.find(".qq-upload-list span").empty();
                $(dialogElement.parent().find("button")[1]).button("enable");

                if (responseJSON.success) {
                    updatePreviewImage();
                } else {
                    this.showMessage(responseJSON.summary);
                }
            },
            onCancel:function (id, fileName) {
                dialogElement.find(".qq-upload-list").hide();
                chooseButton.show();
            },
            showMessage:showErrorMessage
        });

        this.removeProfilePhoto = function () {
            $.ajax('image/remove', {
                success:function (data, textStatus, jqXHR) {
                    if (data.success) {
                        updateProfileImage();
                    } else {
                        wm.ui.unlock(null, "<@message code="profile.edit.error.remove"/>", true);
                    }
                }
            });
            return false;
        };

        this.chooseProfilePhoto = function () {
            updatePreviewImage();

            dialogElement.dialog({
                title:"<@message code="profile.edit.photo.title"/>",
                modal:true,
                minWidth:550,
                height:'auto',
                resizable:false,
                buttons:[
                    {
                        text:"<@message code="button.cancel"/>",
                        click:function () {
                            $(this).dialog("close");
                        }
                    },
                    {
                        text:"<@message code="profile.edit.photo.set"/>",
                        disabled:true,
                        click:function () {
                            $.ajax('image/set', {
                                success:function (data, textStatus, jqXHR) {
                                    if (data.success) {
                                        updateProfileImage();
                                        dialogElement.dialog("close");
                                    } else {
                                        showErrorMessage(data.summary);
                                    }
                                }
                            });
                        }
                    }
                ]
            });
            return false;
        }
    };

    var editorController = new wm.ui.editor.Controller($('.profile'),
                    function (field, data, callback) {
                        wm.ui.lock(null, "<@message code="profile.edit.saving"/>");

                        $.ajax({
                            url:'save',
                            cache:false,
                            data:$.toJSON(data),
                            error:function (jqXHR, textStatus, errorThrown) {
                                callback(textStatus);
                            },
                            success:function (data, textStatus, jqXHR) {
                                if (!data.success) {
                                    wm.ui.unlock(null, "<@message code="profile.edit.error"/>: <br><b>" + data.summary + "</b>", true);
                                    callback(data.summary);
                                } else {
                                    wm.ui.unlock(null, "<@message code="profile.edit.saved"/>");
                                    callback();
                                }
                            }
                        });
                    },
                    {
                        realName:{
                            type:'text'
                        },
                        comments:{
                            type:'text'
                        },
                        gender:{
                            type:'select',
                            values:{
                                male:'<@message code="gender.male"/>',
                                female:'<@message code="gender.female"/>',
                                other:'<@message code="gender.other"/>'
                            }
                        },
                        birthday:{
                            type:'date',
                            opts:{
                                changeMonth:true,
                                changeYear:true,
                                dateFormat:'dd-mm-yy',
                                displayFormat:'MM d, yy',
                                yearRange:'1900:2011'

                            }
                        },
                        primaryLanguage:{
                            type:'select',
                            values:{
                                en:'<@message code="language.en"/>',
                                ru:'<@message code="language.ru"/>'
                            }
                        },
                        countryCode:{
                            type:'select',
                            values:{
                            <#list countries as country>
                                '${country.code}':"${country.name}"<#if country_has_next>,</#if>
                            </#list>
                            }
                        }
                    })
            ;
</script>