package wisematches.client.gwt.app.client.settings.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ApplicationSettingsServiceAsync {
    void loadSettings(String frameViewId, AsyncCallback<String> async);

    void saveSettings(String frameViewId, String settings, AsyncCallback async);
}
