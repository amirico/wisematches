package wisematches.server.web.controllers.playground.form;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageForm {
    private long pid;
    private String message;
    private boolean replay;

    public MessageForm() {
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isReplay() {
        return replay;
    }

    public void setReplay(boolean replay) {
        this.replay = replay;
    }
}
