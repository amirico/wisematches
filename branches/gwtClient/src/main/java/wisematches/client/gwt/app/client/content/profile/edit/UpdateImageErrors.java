package wisematches.client.gwt.app.client.content.profile.edit;

/**
 * Enumeration contains set of errors which can be received from server.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum UpdateImageErrors {
    /**
     * Indicates that player session has been expired.
     */
    SESSION_EXPIRED,

    /**
     * Indicates that file is not attached or it's size is unknown
     */
    UNKNOWN_SIZE,

    /**
     * Indicates that file length is greate than expected.
     */
    TOO_LONG_FILE,

    /**
     * The type of specified image is unsupported.
     */
    UNSUPPORTED_IMAGE,

    /**
     * Indicates that some internal error is happend
     */
    INTERNAL_ERROR
}
