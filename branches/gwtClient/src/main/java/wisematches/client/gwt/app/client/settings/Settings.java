package wisematches.client.gwt.app.client.settings;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class Settings {
    private String frameViewId;
    private SettingsManagerTool settingsManagerTool;

    private ParameterInfo[] infos;
    private Object[] values;

    private final Map<String, Integer> indexes = new HashMap<String, Integer>();
    private final Map<String, Integer> shortIndexes = new HashMap<String, Integer>();

    private final Collection<SettingsListener> listeners = new HashSet<SettingsListener>();

    private static final String NULL_VALUE = "null";

    Settings(SettingsManagerTool settingsManagerTool, String frameViewId, ParameterInfo[] infos, String serverSettings, String clientSettings) {
        this.settingsManagerTool = settingsManagerTool;
        this.frameViewId = frameViewId;

        this.infos = infos;
        if (infos != null) {
            values = new Object[infos.length];

            for (int i = 0; i < infos.length; i++) {
                ParameterInfo info = infos[i];
                indexes.put(info.getName(), i);
                shortIndexes.put(info.getShortName(), i);
                values[i] = info.getDefaultValue();
            }

            if (serverSettings != null) {
                decode(serverSettings);
            }
            if (clientSettings != null) {
                decode(clientSettings);
            }
        }
    }

    public String getFrameViewId() {
        return frameViewId;
    }

    public void addSettingsListener(SettingsListener l) {
        listeners.add(l);
    }

    public void removeSettingsListener(SettingsListener l) {
        listeners.remove(l);
    }


    public void setInt(String name, int value) {
        setValue(name, value, int.class);
    }

    public void setLong(String name, long value) {
        setValue(name, value, long.class);
    }

    public void setString(String name, String value) {
        setValue(name, value, String.class);
    }

    public void setBoolean(String name, boolean value) {
        setValue(name, value, boolean.class);
    }


    public int getInt(String name) {
        return (Integer) getValue(name, int.class);
    }

    public long getLong(String name) {
        return (Long) getValue(name, long.class);
    }

    public String getString(String name) {
        return (String) getValue(name, String.class);
    }

    public boolean getBoolean(String name) {
        return (Boolean) getValue(name, boolean.class);
    }


    public void setIntArray(String name, int[] value) {
        setValue(name, value, int[].class);
    }

    public void setLongArray(String name, long[] value) {
        setValue(name, value, long[].class);
    }

    public void setStringArray(String name, String[] value) {
        setValue(name, value, String[].class);
    }

    public void setBooleanArray(String name, boolean[] value) {
        setValue(name, value, boolean[].class);
    }


    public int[] getIntArray(String name) {
        return (int[]) getValue(name, int[].class);
    }

    public long[] getLongArray(String name) {
        return (long[]) getValue(name, long[].class);
    }

    public String[] getStringArray(String name) {
        return (String[]) getValue(name, String[].class);
    }

    public boolean[] getBooleanArray(String name) {
        return (boolean[]) getValue(name, boolean[].class);
    }


    private void setValue(final String name, final Object value, final Class type) {
        int index = getParameterIndex(name, type);
        if (!valuesEquals(value, index)) {
            final Object oldValue = values[index];
            final ParameterInfo info = infos[index];

            values[index] = value;
            settingsManagerTool.settingsChanged(this, info);

            for (SettingsListener listener : listeners) {
                listener.settingsChanged(this, info, oldValue, value);
            }
        }
    }

    private Object getValue(final String name, final Class type) {
        int index = getParameterIndex(name, type);
        return values[index];
    }

    private boolean valuesEquals(Object value, int index) {
        final Object object = values[index];
        return object != null ? object.equals(value) : value == null;
    }

    private int getParameterIndex(String name, final Class type) {
        Integer index = indexes.get(name);
        if (index == null) {
            throw new IllegalArgumentException("Unknown parameter: " + name);
        }
        final ParameterInfo parameterInfo = infos[index];
        if (parameterInfo.getType() != type) {
            throw new IllegalArgumentException("Invalid parameter " + name + " type: " + type + ". Must be: " + parameterInfo.getType());
        }
        return index;
    }


    protected String getServerSettings() {
        return encode(true);
    }

    protected String getClientSettings() {
        return encode(false);
    }


    protected void decode(String encodedString) {
        final String[] strings = separateString(encodedString, ":");
        for (String string : strings) {
            final String[] keyValue = separateString(string, "=");
            final String key = keyValue[0];
            final String value = keyValue[1];

            final Integer indexid = shortIndexes.get(key);
            if (indexid != null) {
                values[indexid] = decodeValue(value, infos[indexid].getType());
            }
        }
    }

    protected Object decodeValue(String value, Class type) {
        if (value == null || NULL_VALUE.equals(value)) {
            return null;
        }

        if (type.isArray()) {
            final String[] strings = separateString(value, "|");
            if (type.equals(int[].class)) {
                int[] res = new int[strings.length];
                for (int i = 0; i < res.length; i++) {
                    res[i] = (Integer) decodeValue(strings[i], int.class);
                }
                return res;
            } else if (type.equals(long[].class)) {
                long[] res = new long[strings.length];
                for (int i = 0; i < res.length; i++) {
                    res[i] = (Long) decodeValue(strings[i], long.class);
                }
                return res;
            } else if (type.equals(boolean[].class)) {
                boolean[] res = new boolean[strings.length];
                for (int i = 0; i < res.length; i++) {
                    res[i] = (Boolean) decodeValue(strings[i], boolean.class);
                }
                return res;
            } else if (type.equals(String[].class)) {
                String[] res = new String[strings.length];
                for (int i = 0; i < res.length; i++) {
                    res[i] = strings[i];
                }
                return res;
            } else {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        } else {
            if (type.equals(int.class)) {
                return Integer.valueOf(value);
            } else if (type.equals(long.class)) {
                return Long.valueOf(value);
            } else if (type.equals(boolean.class)) {
                return "1".equals(value);
            } else if (type.equals(String.class)) {
                return value;
            } else {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        }
    }


    protected String encode(boolean forServer) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < infos.length; i++) {
            final ParameterInfo info = infos[i];
            if (info.isServerParameter() == forServer) {
                if (b.length() != 0) {
                    b.append(':');
                }

                b.append(info.getShortName());
                b.append('=');
                b.append(encodeValue(values[i], info.getType()));
            }
        }
        return b.toString();
    }

    protected String encodeValue(Object value, Class type) {
        if (value == null) {
            return NULL_VALUE;
        }

        if (type.isArray()) {
            String res = "";
            if (type.equals(int[].class)) {
                int[] values = (int[]) value;
                for (int re : values) {
                    res += encodeValue(re, int.class) + "|";
                }
            } else if (type.equals(long[].class)) {
                long[] values = (long[]) value;
                for (long re : values) {
                    res += encodeValue(re, long.class) + "|";
                }
            } else if (type.equals(boolean[].class)) {
                boolean[] values = (boolean[]) value;
                for (boolean re : values) {
                    res += encodeValue(re, boolean.class) + "|";
                }
            } else if (type.equals(String[].class)) {
                String[] values = (String[]) value;
                for (String re : values) {
                    res += re + "|";
                }
            } else {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
            return res;
        } else if (type.equals(boolean.class)) {
            return (Boolean) value ? "1" : "0";
        } else {
            return value.toString();
        }
    }


    protected String[] separateString(String value, String separtor) {
        int count = 0;
        int start = 0;
        int index = value.indexOf(separtor);
        while (index != -1) {
            count++;
            start = index + 1;
            index = value.indexOf(separtor, start);
        }
        if (start != value.length()) {
            count++;
        }

        final String[] res = new String[count];
        start = 0;
        index = value.indexOf(separtor);
        int k = 0;
        while (index != -1) {
            res[k++] = value.substring(start, index);
            start = index + 1;
            index = value.indexOf(separtor, start);
        }
        if (start != value.length()) {
            res[k] = value.substring(start);
        }
        return res;
    }


    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Settings{");
        b.append("FrameViewId - ");
        b.append(frameViewId);
        b.append(", {");
        for (int i = 0; i < infos.length; i++) {
            ParameterInfo parameterInfo = infos[i];
            Object value = values[i];

            if (i != 0) {
                b.append(", ");
            }
            b.append(parameterInfo.getName());
            b.append("=");
            b.append(value);
        }
        b.append("}");
        b.append("}");
        return b.toString();
    }
}