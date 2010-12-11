package wisematches.client.gwt.app.client.content.player;

import wisematches.client.gwt.app.client.ApplicationFrame;
import wisematches.client.gwt.app.client.ApplicationTool;
import wisematches.client.gwt.app.client.ToolReadyCallback;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerSessionTool implements ApplicationTool {
/*
    private PlayerInfoBean playerInfoBean;

    public void registerJSCallbacks() {
    }

    public void initializeTool(final ApplicationFrame applicationFrame, final ToolReadyCallback callback) {
        // This code can be changed to Dictionary.get("applicationProperties").get("playerId");
        final PlayerInfoServiceAsync service = PlayerInfoService.App.getInstance();
        service.getSessionPlayer(new AsyncCallback<PlayerInfoBean>() {
            public void onFailure(Throwable throwable) {
                ExceptionHandler.showSystemError(throwable);
                callback.toolReady(PlayerSessionTool.this);
            }

            public void onSuccess(PlayerInfoBean playerInfoBean) {
                PlayerSessionTool.this.playerInfoBean = playerInfoBean;
                callback.toolReady(PlayerSessionTool.this);
            }
        });
    }

    public long getCurrentPlayer() {
        return playerInfoBean.getPlayerId();
    }

    public PlayerInfoBean getPlayerInfoBean() {
        return playerInfoBean;
    }
*/

	@Override
	public void registerJSCallbacks() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void initializeTool(ApplicationFrame applicationFrame, ToolReadyCallback callback) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}