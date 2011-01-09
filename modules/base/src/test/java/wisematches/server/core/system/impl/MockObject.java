package wisematches.server.core.system.impl;

import java.io.Serializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MockObject implements Serializable {
    private final String value;

    public MockObject(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}