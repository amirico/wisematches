package wisematches.client.gwt.core.client;

/**
 * Parent exception for any permissions or limitation exceptions. Each client must process
 * this exception by default.
 * <p/>
 * This is runtime exception and can be thrown by any method at any time.
 * <p/>
 * This exception must contains localized error message.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerSecurityException extends GWTRuntimeException {
    /**
     * This is GWT serialization only constructor.
     */
    @Deprecated
    public PlayerSecurityException() {
    }

    /**
     * Creates new exception with specified message.
     *
     * @param message the related description of exception.
     */
    public PlayerSecurityException(String message) {
        super(message);
    }

    /**
     * Creates new exception with message template and template arguments.
     * <p/>
     * This constructor use template to create a exception message. Template message can contains
     * the following construction {@code {NUMBER}} which will be replaced to
     * argument at specified index.
     * <p/>
     * This constructor is used to create localized exception.
     *
     * @param template  the template message
     * @param arguments the arguments which will be placed to template string.
     */
    public PlayerSecurityException(String template, Object... arguments) {
        super(processTemplate(template, arguments));
    }

    private static String processTemplate(String template, Object... arguments) {
        final StringBuilder b = new StringBuilder();
        int lastIndex = 0;
        do {
            int beginIndex = template.indexOf("{", lastIndex);
            if (beginIndex == -1) {
                break;
            }
            int endIndex = template.indexOf("}", beginIndex + 1);
            if (endIndex == -1) {
                break;
            }

            try {
                b.append(template.substring(lastIndex, beginIndex));

                int index = Integer.parseInt(template.substring(beginIndex + 1, endIndex));
                if (arguments == null) {
                    throw new IndexOutOfBoundsException("Template message '" + template +
                            "'  contains reference to index " + index + " but arguments are null");
                }

                if (index < 0) {
                    throw new IndexOutOfBoundsException("Template message '" + template + "' contains reference " +
                            "to negative index: " + index);
                } else if (index >= arguments.length) {
                    throw new IndexOutOfBoundsException("Template message '" + template + "' contains reference to " +
                            " not exist argument. Index: " + index + ". Arguments: " + arguments.length);
                }
                b.append(arguments[index]);
            } catch (NumberFormatException ex) {
                b.append(template.substring(beginIndex, endIndex + 1));
            }
            lastIndex = endIndex + 1;
        } while (true);

        if (lastIndex != template.length()) {
            b.append(template.substring(lastIndex));
        }
        return b.toString();
    }
}
