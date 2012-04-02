package wisematches.server.web.services.notify.publisher;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationCreator;
import wisematches.server.web.services.notify.NotificationDescriptor;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationMessage {
    private final String subject;
    private final String message;
    private final Account account;
    private final NotificationCreator creator;
    private final NotificationDescriptor descriptor;

    public NotificationMessage(String subject, String message, Account account, NotificationCreator creator, NotificationDescriptor descriptor) {
        this.descriptor = descriptor;
        this.creator = creator;
        this.account = account;
        this.message = message;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public Account getAccount() {
        return account;
    }

    public NotificationCreator getCreator() {
        return creator;
    }

    public NotificationDescriptor getDescriptor() {
        return descriptor;
    }
}
