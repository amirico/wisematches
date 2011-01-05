package wisematches.client.gwt.login.client.services;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class CreateAccountResult implements IsSerializable {
    private Status status;
    private String message;

    public CreateAccountResult() {
    }

    public CreateAccountResult(Status status) {
        this.status = status;
    }

    public CreateAccountResult(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static enum Status implements IsSerializable {
        SUCCESS,
        USERNAME_BUSY,
        USERNAME_INADMISSIBLE,
        EMAIL_BUSY,
        USERNAME_INVALID,
        EMAIL_INVALID,
        UNKNOWN_ERROR
    }

    @Override
    public String toString() {
        return "CreateAccountResult{status-" + status + ", message-" + message + "}";
    }
}
