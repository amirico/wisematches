package wisematches.client.gwt.app.client.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import wisematches.client.gwt.app.client.ApplicationFrame;
import wisematches.client.gwt.app.client.ApplicationFrameView;
import wisematches.client.gwt.app.client.ApplicationTool;
import wisematches.client.gwt.app.client.ToolReadyCallback;
import wisematches.client.gwt.app.client.settings.services.ApplicationSettingsService;
import wisematches.client.gwt.app.client.settings.services.ApplicationSettingsServiceAsync;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class SettingsManagerTool implements ApplicationTool {
    private ApplicationFrame applicationFrame;

    private final Map<String, Settings> settingsMap = new HashMap<String, Settings>();

    public void registerJSCallbacks() {
    }

    public void initializeTool(final ApplicationFrame applicationFrame, final ToolReadyCallback callback) {
        this.applicationFrame = applicationFrame;
        callback.toolReady(this);
    }

    protected void settingsChanged(Settings s, ParameterInfo info) {
        GWT.log("Settings changed for frame view: " + s + ", values: " + info, null);

        if (info.isServerParameter()) {
            ApplicationSettingsService.App.getInstance().saveSettings(s.getFrameViewId(), s.getServerSettings(), new AsyncCallback<Void>() {
                public void onFailure(Throwable throwable) {
                    // ExceptionHandler.showSystemError(throwable);
                    //Do nothing
                }

                public void onSuccess(Void o) {
                    //Do nothing
                }
            });
        } else {
            Cookies.setCookie(s.getFrameViewId(), s.getClientSettings());
        }
    }

    public void loadSettings(final String frameViewId, final AsyncCallback<Settings> callback) {
        final Settings settings = settingsMap.get(frameViewId);
        if (settings != null) {
            callback.onSuccess(settings);
        } else {
            final ApplicationSettingsServiceAsync serviceAsync = ApplicationSettingsService.App.getInstance();
            serviceAsync.loadSettings(frameViewId, new AsyncCallback<String>() {
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                public void onSuccess(String result) {
                    final ApplicationFrameView item = applicationFrame.getApplicationFrameView(frameViewId);
                    final ParameterInfo[] infos = item.getParametersInfos();

                    final String cookie = Cookies.getCookie(frameViewId);
                    final Settings s = new Settings(SettingsManagerTool.this, frameViewId, infos, result, cookie);
                    settingsMap.put(frameViewId, s);

                    callback.onSuccess(s);
                }
            });
        }
    }
}
