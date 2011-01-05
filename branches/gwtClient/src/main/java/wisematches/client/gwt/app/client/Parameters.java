package wisematches.client.gwt.app.client;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class Parameters {
    private final HashMap<String, String> parameters = new HashMap<String, String>();

    private Parameters() {
    }

    public int getInt(String name) {
        return Integer.parseInt(parameters.get(name));
    }

    public long getLong(String name) {
        return Long.parseLong(parameters.get(name));
    }

    public String getString(String name) {
        return parameters.get(name);
    }

    public boolean getBoolean(String name) {
        return Boolean.valueOf(parameters.get(name));
    }

    public static Parameters decode(String parameters) {
        if (parameters == null) {
            return null;
        }


        final Parameters res = new Parameters();

        int startIndex = 0;
        int index = parameters.indexOf('&');
        if (index == -1) {
            index = parameters.length();
        }
        while (startIndex != index) {
            String s = parameters.substring(startIndex, index);
            int k = s.indexOf('=');
            if (k == -1) {
                throw new IllegalArgumentException("Incorrect token in parameters: " + s);
            }

            res.parameters.put(s.substring(0, k), s.substring(k + 1));

            if (index == parameters.length()) {
                break;
            }

            startIndex = index + 1;
            index = parameters.indexOf('&', startIndex);
            if (index == -1) {
                index = parameters.length();
            }
        }
        return res;
    }

    public String encode() {
        if (parameters.size() == 0) {
            return "";
        }

        String res = "";
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            res += entry.getKey() + "=" + entry.getValue() + "&";
        }
        return res.substring(0, res.length() - 1);
    }

    public boolean contains(String name) {
        return parameters.containsKey(name);
    }

    @Override
    public String toString() {
        return encode();
    }
}
