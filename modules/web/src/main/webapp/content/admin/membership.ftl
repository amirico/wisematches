<#-- @ftlvariable name="account" type="wisematches.core.personality.player.account.Account" -->
<#-- @ftlvariable name="membershipCard" type="wisematches.core.personality.player.membership.MembershipCard" -->

<#include "/core.ftl"/>

<div>
    <form action="/admin/membership" method="post">
    <#if !account??>
        <label>
            <input type="text" name="p" size="10" value="">
        </label>
        <button type="submit">Load player</button>
    <#else>
        <input type="hidden" name="p" value="${account.id}"/>

        <table>
            <tr>
                <td><label>Player:</label></td>
                <td>${account.id} (${account.nickname})</td>
            </tr>
            <tr>
                <td><label>Membership: </label></td>
                <td>
                    <select name="m">
                        <#list Membership.values() as m>
                            <option <#if membershipCard?? && membershipCard.membership == m>selected="selected"</#if>
                                    value="${m.name()}">${m.name()}</option>
                        </#list>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label>Expires:</label></td>
                <td><input name="d" value="<#if membershipCard??>${membershipCard.expiration}</#if>"</td>
            </tr>
        </table>
        <button type="submit">Update membership</button>
    </#if>
    </form>
</div>

