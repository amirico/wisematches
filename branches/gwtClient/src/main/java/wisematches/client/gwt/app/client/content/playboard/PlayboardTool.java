package wisematches.client.gwt.app.client.content.playboard;

import wisematches.client.gwt.app.client.ApplicationFrame;
import wisematches.client.gwt.app.client.ApplicationTool;
import wisematches.client.gwt.app.client.ToolReadyCallback;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayboardTool implements ApplicationTool {
/*
    private static PlayboardTool instance;
    private ApplicationFrame configuration;

    public PlayboardTool() {
        if (instance != null) {
            throw new NullPointerException("This tool already exist");
        }
        instance = this;
    }

    public native void registerJSCallbacks() */
/*-{
    $wnd.openBoardGame = @wisematches.client.gwt.app.client.content.playboard.PlayboardTool::openBoardGame(Ljava/lang/String;);
    $wnd.selectHistoryMove = @wisematches.client.gwt.app.client.content.playboard.PlayboardTool::selectHistoryMove(II);
    $wnd.playboardSelectWord = @wisematches.client.gwt.app.client.content.playboard.PlayboardTool::playboardSelectWord(II);
    $wnd.playboardRemoveWord = @wisematches.client.gwt.app.client.content.playboard.PlayboardTool::playboardRemoveWord(II);
    $wnd.quickOpenBoards = @wisematches.client.gwt.app.client.content.playboard.PlayboardTool::quickOpenBoards();
    $wnd.playboardCreateNewGame = @wisematches.client.gwt.app.client.content.playboard.PlayboardTool::playboardCreateNewGame();
    }-*//*
;

    public void initializeTool(ApplicationFrame configuration, ToolReadyCallback callback) {
        this.configuration = configuration;
        callback.toolReady(this);
    }

    */
/**
 * Returns HTML {@code <a>...</a>} link to open board with specified id. Specified
 * title parameter will be used as name of hyperlink.
 *
 * @param bean the information about game.
 * @return HTML code to open a board.
 *//*

    public static String getOpenGameBoardLink(DashboardItemBean bean) {
        return getOpenGameBoardLink(bean.getBoardId(), bean.getTitle());
    }

    */
/**
 * Returns HTML {@code <a>...</a>} link to open board with specified id. Specified
 * title parameter will be used as name of hyperlink.
 *
 * @param boardid the board id.
 * @param title   the hiperlink content.
 * @return HTML code to open a board.
 *//*

    public static String getOpenGameBoardLink(long boardid, String title) {
        return getOpenGameBoardLink(String.valueOf(boardid), title);
    }

    */

	/**
	 * Returns HTML {@code <a>...</a>} link to open board with specified id. Specified
	 * title parameter will be used as name of hyperlink.
	 *
	 * @param boardid the board id.
	 * @param title   the hiperlink content.
	 * @return HTML code to open a board.
	 *//*

    public static String getOpenGameBoardLink(String boardid, String title) {
        return "<a ext:qtip=\"" + APP.ttpOpenGame() + "\" href=\"javascript:window.openBoardGame('" +
                boardid + "');\">" + title + " #" + boardid + "</a>";
    }


    private static void selectHistoryMove(int boardId, int wordId) {
        PlayboardWidget pw = getPlayboardWidget();
        pw.selectHistoryWord(boardId, wordId);
    }

    private static void openBoardGame(String boardId) {
        final ApplicationFrame frame = instance.configuration;
        final ApplicationFrameView view = frame.getActiveFrameView();
        if (view instanceof PlayboardWidget) {
            final PlayboardWidget widget = (PlayboardWidget) view;
            widget.openGameBoard(Long.parseLong(boardId));
        } else {
            frame.activate(PlayboardWidget.ITEM_ID, Parameters.decode("boardid=" + boardId));
        }
    }


    private static void playboardCreateNewGame() {
        getPlayboardWidget().playboardCreateNewGame();
    }

    private static void quickOpenBoards() {
        getPlayboardWidget().quickOpenBoards();
    }

    private static void playboardSelectWord(int boardId, int wordId) {
        PlayboardWidget pw = getPlayboardWidget();
        pw.selectMemoryWord(boardId, wordId);
    }

    private static void playboardRemoveWord(int boardId, int wordId) {
        PlayboardWidget pw = getPlayboardWidget();
        pw.removeMemoryWord(boardId, wordId);
    }

    private static PlayboardWidget getPlayboardWidget() {
        return instance.configuration.getApplicationFrameView(PlayboardWidget.ITEM_ID);
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
