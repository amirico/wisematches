package wisematches.server.web.services.notice.impl.publisher;

import wisematches.personality.account.Account;
import wisematches.personality.account.AccountManager;
import wisematches.server.mail.MailSender;
import wisematches.server.mail.MailService;
import wisematches.server.web.services.notice.NotificationDescription;
import wisematches.server.web.services.notice.publisher.Notification;
import wisematches.server.web.services.notice.publisher.NotificationPublisher;

import java.util.Collections;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MailNotificationPublisher implements NotificationPublisher {
	private MailService mailService;
	private AccountManager accountManager;

	public MailNotificationPublisher() {
	}

	@Override
	public void publishNotification(Notification notification) {
		final Account account = accountManager.getAccount(notification.getPersonality().getId());
		if (account == null) {
			return;
		}
		final NotificationDescription description = notification.getDescription();
		mailService.sendMail(MailSender.GAME, account, description.getName(), Collections.singletonMap("context", notification.getContext()));
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
}
