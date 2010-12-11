package wisematches.client.gwt.app.client;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface ApplicationTool {
    void registerJSCallbacks();

    void initializeTool(ApplicationFrame applicationFrame, ToolReadyCallback callback);
}
