package wisematches.server.web.services.notice;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Notification {
    private String name;
    private Date lastRaised;

    public Notification() {
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return lastRaised != null;
    }

    public Date getLastRaised() {
        return lastRaised;
    }
}
