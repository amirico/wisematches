package wisematches.client.gwt.app.client.content.dashboard;

import wisematches.client.gwt.app.client.ApplicationFrame;
import wisematches.client.gwt.app.client.ApplicationTool;
import wisematches.client.gwt.app.client.ToolReadyCallback;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class DashboardTool implements ApplicationTool {
//    private static DashboardTool instance;
//    private ApplicationFrame applicationFrame;
//
//    public DashboardTool() {
//        if (instance != null) {
//            throw new NullPointerException("This tool already exist");
//        }
//        instance = this;
//    }
//
//    public native void registerJSCallbacks() /*-{
//    $wnd.createNewGame = @wisematches.client.gwt.app.client.content.dashboard.DashboardTool::createNewGame();
//    $wnd.joinToGame = @wisematches.client.gwt.app.client.content.dashboard.DashboardTool::joinToGame(Ljava/lang/String;);
//    }-*/;
//
//    public void initializeTool(ApplicationFrame applicationFrame, ToolReadyCallback callback) {
//        this.applicationFrame = applicationFrame;
//        callback.toolReady(this);
//    }
//
//    private static void createNewGame() {
//        final DashboardWidget widget = instance.applicationFrame.getApplicationFrameView(DashboardWidget.ITEM_ID);
//        widget.createNewGame();
//    }
//
//    private static void joinToGame(String boardId) {
//        final DashboardWidget widget = instance.applicationFrame.getApplicationFrameView(DashboardWidget.ITEM_ID);
//        widget.joinToGame(boardId);
//    }

	@Override
	public void registerJSCallbacks() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void initializeTool(ApplicationFrame applicationFrame, ToolReadyCallback callback) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
