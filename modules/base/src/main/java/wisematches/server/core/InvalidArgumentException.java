package wisematches.server.core;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class InvalidArgumentException extends IllegalArgumentException {
    private final String argumentName;

    public InvalidArgumentException(String argumentName) {
        this.argumentName = argumentName;
    }

    public InvalidArgumentException(String argumentName, String s) {
        super(s);
        this.argumentName = argumentName;
    }

    public InvalidArgumentException(String argumentName, String message, Throwable cause) {
        super(message, cause);
        this.argumentName = argumentName;
    }

    public InvalidArgumentException(String argumentName, Throwable cause) {
        super(cause);
        this.argumentName = argumentName;
    }

    public String getArgumentName() {
        return argumentName;
    }
}
