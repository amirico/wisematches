package wisematches.client.gwt.app.client.content;

import wisematches.client.gwt.app.client.ApplicationFrame;
import wisematches.client.gwt.app.client.ApplicationTool;
import wisematches.client.gwt.app.client.Parameters;
import wisematches.client.gwt.app.client.ToolReadyCallback;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class SwitchFrameViewTool implements ApplicationTool {
    private ApplicationFrame applicationFrame;

    private static SwitchFrameViewTool widgetTool;

    public SwitchFrameViewTool() {
        if (widgetTool != null) {
            throw new NullPointerException("This tool already exist");
        }
        widgetTool = this;
    }

    public native void registerJSCallbacks() /*-{
    $wnd.showApplicationItem = @wisematches.client.gwt.app.client.content.SwitchFrameViewTool::showApplicationItem(Ljava/lang/String;Ljava/lang/String;);
    $wnd.showMessage = @wisematches.client.gwt.app.client.content.SwitchFrameViewTool::showMessage(Ljava/lang/String;);
    }-*/;

    public void initializeTool(ApplicationFrame applicationFrame, ToolReadyCallback callback) {
        this.applicationFrame = applicationFrame;
        callback.toolReady(this);
    }

    private static void showApplicationItem(String id, String parameters) {
        widgetTool.applicationFrame.activate(id, Parameters.decode(parameters));
    }

    private static void showMessage(String message) {
    }
}
