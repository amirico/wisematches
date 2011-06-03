<#-- @ftlvariable name="player" type="wisematches.personality.player.Player" -->
<#-- @ftlvariable name="profile" type="wisematches.personality.profile.PlayerProfile" -->
<#include "/core.ftl">

<style type="text/css">
    .asd div {
        padding-top: 2px;
        padding-bottom: 2px;
        padding-right: 5px;
    }
</style>

<div style="width: 100%">
    <div class="profile shadow ui-state-default">
        <div class="content shadow ui-state-default">
            <div class="info-header">
                <div class="info-label">Profile Modification</div>
            </div>

            <form>
                <div class="ui-layout-table">
                    <div class="asd">
                        <div>Real Name:</div>
                        <div>${profile.realName!"please enter your real name"}</div>
                    </div>
                    <div class="asd">
                        <div>Information:</div>
                        <div>${profile.comments!"small information about you"}</div>
                    </div>
                    <div class="asd">
                        <div>Birthday:</div>
                        <div>${profile.birthday!"your birthday"}</div>
                    </div>
                    <div class="asd">
                        <div><label for="gender">Gender:</label></div>
                        <div>
                            <select id="gender" name="gender">
                                <option>Male</option>
                                <option>Female</option>
                                <option>Other</option>
                            </select>
                        ${profile.gender!"your gender"}
                        </div>
                    </div>
                    <div class="asd">
                        <div>Country:</div>
                        <div>${profile.countryCode!"country where do you live"}</div>
                    </div>
                    <div class="asd">
                        <div>Primary Language:</div>
                        <div>${profile.primaryLanguage!"your primary language"}</div>
                    </div>
                </div>

                <div>
                    <button type="submit">Save</button>
                    <button onclick="wm.util.url.redirect('/playground/profile/view')">Cancel</button>
                </div>
            </form>
        </div>

    <#include "public.ftl"/>
    </div>
</div>