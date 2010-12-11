package wisematches.client.gwt.app.client.settings;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ParameterInfo {
    private final String name;
    private final String shortName;
    private final Class valueType;
    private final Object defaultValue;
    private final boolean serverParameter;

    public <T> ParameterInfo(String name, String shortName, Class<T> valueType, boolean serverParameter) {
        this(name, shortName, valueType, null, serverParameter);
    }

    public <T> ParameterInfo(String name, String shortName, Class<T> valueType, T defaultValue, boolean serverParameter) {
        this.name = name;
        this.shortName = shortName;
        this.defaultValue = defaultValue;
        this.valueType = valueType;
        this.serverParameter = serverParameter;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public Class getType() {
        return valueType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public boolean isServerParameter() {
        return serverParameter;
    }

    @Override
    public String toString() {
        return "ParameterInfo{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", valueType=" + valueType +
                ", defaultValue=" + defaultValue +
                ", serverParameter=" + serverParameter +
                '}';
    }
}
