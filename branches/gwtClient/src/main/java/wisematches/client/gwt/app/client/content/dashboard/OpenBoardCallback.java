package wisematches.client.gwt.app.client.content.dashboard;

import wisematches.client.gwt.app.client.ApplicationFrame;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class OpenBoardCallback implements CreateGameCallback {
	private final ApplicationFrame applicationFrame;

	public OpenBoardCallback(ApplicationFrame applicationFrame) {
		this.applicationFrame = applicationFrame;
	}

	public void gameCreate(long boardId) {
//        final Parameters parameters = Parameters.decode(PlayboardWidget.BOARD_ID_PARAMETER + "=" + boardId);
//        applicationFrame.activate(PlayboardWidget.ITEM_ID, parameters);
	}
}
