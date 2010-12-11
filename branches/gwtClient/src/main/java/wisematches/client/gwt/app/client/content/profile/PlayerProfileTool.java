package wisematches.client.gwt.app.client.content.profile;

import wisematches.client.gwt.app.client.ApplicationFrame;
import wisematches.client.gwt.app.client.ApplicationTool;
import wisematches.client.gwt.app.client.ToolReadyCallback;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerProfileTool implements ApplicationTool {
	private static PlayerProfileTool instance;
	private ApplicationFrame configuration;

	public native void registerJSCallbacks() /*-{
    $wnd.showPlayerProfile = @wisematches.client.gwt.app.client.content.profile.PlayerProfileTool::showPlayerProfile(Ljava/lang/String;);
    $wnd.editProfile = @wisematches.client.gwt.app.client.content.profile.PlayerProfileTool::editProfile();
    }-*/;

	public void initializeTool(ApplicationFrame configuration, ToolReadyCallback callback) {
		this.configuration = configuration;
		callback.toolReady(this);
		instance = this;
	}

	private static void showPlayerProfile(String profileId) {
//        instance.configuration.activate(PlayerProfileWidget.ITEM_ID, Parameters.decode("playerId=" + profileId));
	}

	private static void editProfile() {
//        final ApplicationFrame applicationFrame = instance.configuration;
//        final PlayerProfileWidget profileWidget = applicationFrame.getApplicationFrameView(PlayerProfileWidget.ITEM_ID);
//        profileWidget.editProfile();
	}
}
