package wisematches.client.gwt.core.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This is {@code RuntimeException} that can be processed in {@code SecureSpringController}
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GWTRuntimeException extends RuntimeException implements IsSerializable {
    public GWTRuntimeException() {
    }

    public GWTRuntimeException(String s) {
        super(s);
    }

    public GWTRuntimeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public GWTRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
