package wisematches.client.gwt.app.client.content.stats.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import wisematches.client.gwt.app.client.content.player.PlayerInfoBean;
import wisematches.client.gwt.app.client.content.stats.SiteStatisticBean;

public interface ApplicationStatisticService extends RemoteService {
    PlayerInfoBean[] getTopRatedPlayers();

    SiteStatisticBean getSiteStatisticBean();

    /**
     * Utility/Convenience class.
     * Use ApplicationStatisticService.App.getInstance() to access static instance of ApplicationStatisticServiceAsync
     */
    public static class App {
        private static final ApplicationStatisticServiceAsync ourInstance;

        static {
            ourInstance = (ApplicationStatisticServiceAsync) GWT.create(ApplicationStatisticService.class);
            ((ServiceDefTarget) ourInstance).setServiceEntryPoint("/rpc/ApplicationStatisticService");
        }

        public static ApplicationStatisticServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
