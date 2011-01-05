package wisematches.client.gwt.app.client.content.profile.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import wisematches.client.gwt.app.client.content.profile.beans.PlayerProfileBean;
import wisematches.client.gwt.app.client.content.profile.beans.PlayerSettingsBean;
import wisematches.client.gwt.app.client.content.profile.beans.PlayerStatisticBean;

public interface PlayerProfileServiceAsync {

    /**
     * Returns player profile by it's id.
     * <p/>
     * Where is two possible types of profile: {@code wisematches.client.gwt.app.client.content.profile.beans.PlayerProfileBean} that contains
     * information about player and
     * {@code wisematches.client.gwt.app.client.content.profile.beans.RobotProfileBean} that contains
     * profile of robot.
     *
     * @param playerId the player id which profile should be returned.
     * @return the player profile bean or {@code null} if player is unknown.
     */
    void getPlayerProfile(long playerId, AsyncCallback<PlayerProfileBean> async);

    void getPlayerStatisticBean(long playerId, AsyncCallback<PlayerStatisticBean> async);

    void getSettings(AsyncCallback<PlayerSettingsBean> async);

    void updateSettings(PlayerSettingsBean bean, AsyncCallback<Void> async);
}
