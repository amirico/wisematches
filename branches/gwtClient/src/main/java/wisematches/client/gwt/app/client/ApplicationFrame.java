package wisematches.client.gwt.app.client;

/**
 * If a main application interface. Each application has only one frame.
 * <p/>
 * The {@code ApplicationFrame} contains set of {@code ApplicationFrameView}s which in general
 * are UI panel and can be displayed on screen. At one moment only one panel can be active and shown
 * on the screen.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface ApplicationFrame {
    /**
     * Activates frame view item by it's id and with specified parameters. This is asynchronous operation.
     *
     * @param frameViewId the frame view id.
     * @param parameters  the activation parameters. If no parameters {@code null} value can be passed.
     * @throws NullPointerException if {@code frameViewId} is null.
     */
    void activate(String frameViewId, Parameters parameters);

    /**
     * Returns active frame view item. If no activate item {@code null} value will be returned.
     *
     * @return the active frame view item or {@code null} if no active frame view.
     */
    ApplicationFrameView getActiveFrameView();

    /**
     * Returns {@code ApplicationFrameView} by it's id. If frame view with specified id is unknown {@code null}
     * value will be returned.
     *
     * @param frameViewId the id of frame view.
     * @param <T>         the type of returned application type.
     * @return the frame view by specified id or {@code null} if one is unknown.
     * @throws ClassCastException if requested application type is not equals with requared.
     */
    <T extends ApplicationFrameView> T getApplicationFrameView(String frameViewId);

    /**
     * Returns application tool by it's type.
     *
     * @param type the type of application tool.
     * @param <T>  the type of application tool.
     * @return the application tool by specified type of {@code null} if required tool is unknown.
     */
    <T extends ApplicationTool> T getApplicationTool(Class<? extends T> type);
}