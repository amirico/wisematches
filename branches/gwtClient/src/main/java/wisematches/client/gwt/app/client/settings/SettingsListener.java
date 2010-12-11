package wisematches.client.gwt.app.client.settings;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface SettingsListener {
    void settingsChanged(Settings settings, ParameterInfo parameter, Object oldValue, Object newValue);
}
