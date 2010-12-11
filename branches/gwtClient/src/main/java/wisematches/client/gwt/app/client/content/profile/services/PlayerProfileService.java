package wisematches.client.gwt.app.client.content.profile.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import wisematches.client.gwt.app.client.content.profile.beans.PlayerProfileBean;
import wisematches.client.gwt.app.client.content.profile.beans.PlayerSettingsBean;
import wisematches.client.gwt.app.client.content.profile.beans.PlayerStatisticBean;

public interface PlayerProfileService extends RemoteService {
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
    PlayerProfileBean getPlayerProfile(long playerId);

    PlayerStatisticBean getPlayerStatisticBean(long playerId);


    PlayerSettingsBean getSettings();

    void updateSettings(PlayerSettingsBean bean);


    /**
     * Utility/Convenience class.
     * Use ProfileService.App.getInstance() to access static instance of ProfileServiceAsync
     */
    public static class App {
        private static final PlayerProfileServiceAsync ourInstance;

        static {
            ourInstance = (PlayerProfileServiceAsync) GWT.create(PlayerProfileService.class);
            ((ServiceDefTarget) ourInstance).setServiceEntryPoint("/rpc/ProfileService");
        }

        public static PlayerProfileServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
