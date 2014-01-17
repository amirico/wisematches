package wisematches.server.web.servlet.mvc.playground.message.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageForm {
    private long pid;
    private String message;
    private boolean reply;

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

    public boolean isReply() {
        return reply;
    }

    public void setReply(boolean reply) {
        this.reply = reply;
    }
}
