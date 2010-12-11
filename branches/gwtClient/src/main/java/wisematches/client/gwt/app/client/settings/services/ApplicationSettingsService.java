package wisematches.client.gwt.app.client.settings.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface ApplicationSettingsService extends RemoteService {
    String loadSettings(String frameViewId);

    void saveSettings(String frameViewId, String settings);

    /**
     * Utility/Convenience class.
     * Use SettingsService.App.getInstance() to access static instance of SettingsServiceAsync
     */
    public static class App {
        private static final ApplicationSettingsServiceAsync ourInstance;

        static {
            ourInstance = (ApplicationSettingsServiceAsync) GWT.create(ApplicationSettingsService.class);
            ((ServiceDefTarget) ourInstance).setServiceEntryPoint("/rpc/ApplicationSettingsService");
        }

        public static ApplicationSettingsServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
