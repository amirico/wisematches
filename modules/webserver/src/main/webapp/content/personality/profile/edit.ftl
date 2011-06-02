<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="profile" type="wisematches.personality.profile.PlayerProfile" -->
<#include "/core.ftl">

<style type="text/css">
    .asd div {
        padding-top: 10px;
        padding-right: 20px;
        padding-bottom: 10px;
    }

    .asd:hover {
        background: red;
    }
</style>

<div class="shadow ui-state-highlight" style="width: 100%; text-align: center; padding: 5px;">
    Click on the parts of your profile you want to edit.
    <button onclick="wm.util.url.redirect('/playground/profile/view')">Done editing</button>
</div>

<div style="width: 100%">
    <div class="profile shadow ui-state-default">
        <div class="content shadow ui-state-default">
            <div class="ui-layout-table">
                <div class="asd">
                    <div>Introduction</div>
                    <div>Put a little about yourself here so people know they've found the correct Sergey.</div>
                </div>

                <div class="asd">
                    <div>Gender</div>
                    <div>ASdqwe asd</div>
                </div>
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