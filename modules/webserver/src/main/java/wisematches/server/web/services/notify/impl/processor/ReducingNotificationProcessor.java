package wisematches.server.web.services.notify.impl.processor;

import wisematches.server.web.services.notify.NotificationTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ReducingNotificationProcessor extends FilteringNotificationProcessor {
    private Collection<Set<String>> groups = new ArrayList<Set<String>>();

    public ReducingNotificationProcessor() {
        super();
    }

    @Override
    public boolean publishNotification(NotificationTemplate template) throws Exception {
        return super.publishNotification(template);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setGroups(Collection<Set<String>> groups) {
        this.groups.clear();

        if (groups != null) {
            this.groups.addAll(groups);
        }
    }
}
