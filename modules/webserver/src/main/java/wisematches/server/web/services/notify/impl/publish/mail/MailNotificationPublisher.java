package wisematches.server.web.services.notify.impl.publish.mail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import wisematches.personality.Language;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.publisher.NotificationOriginator;
import wisematches.server.web.services.notify.impl.Notification;
import wisematches.server.web.services.notify.impl.NotificationTransformerPublisher;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MailNotificationPublisher extends NotificationTransformerPublisher {
	private String serverHostName;
	private JavaMailSender mailSender;
	private MessageSource messageSource;

	private final Map<SenderKey, InternetAddress> addressesCache = new HashMap<SenderKey, InternetAddress>();

	private static final Log log = LogFactory.getLog("wisematches.server.notify.mail");

	public MailNotificationPublisher() {
	}

	@Override
	public String getPublisherName() {
		return "mail";
	}

	@Override
	protected void raiseNotification(final Notification notification) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Send mail notification '" + notification.getCode() + "' to " + notification.getAccount());
		}
		final MimeMessagePreparator mm = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				final Account account = notification.getAccount();
				final Language language = account.getLanguage();

				final InternetAddress to = new InternetAddress(account.getEmail(), account.getNickname(), "UTF-8");
				final InternetAddress from = getInternetAddress(notification.getOriginator(), language);

				final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
				message.setFrom(from);
				message.setTo(to);
				message.setSubject(notification.getSubject());
				message.setText(notification.getMessage(), true);
			}
		};
		mailSender.send(mm);
	}

	protected InternetAddress getInternetAddress(NotificationOriginator originator, Language language) {
		return addressesCache.get(new SenderKey(originator, language));
	}

	private void validateAddressesCache() {
		addressesCache.clear();

		if (messageSource == null || serverHostName == null) {
			return;
		}

		for (NotificationOriginator originator : NotificationOriginator.values()) {
			for (Language language : Language.values()) {
				try {
					final String address = messageSource.getMessage("mail.address." + originator.getUserInfo(),
							null, originator.getUserInfo() + "@" + serverHostName, language.locale());

					final String personal = messageSource.getMessage("mail.personal." + originator.getUserInfo(),
							null, originator.name(), language.locale());

					addressesCache.put(new SenderKey(originator, language), new InternetAddress(address, personal, "UTF-8"));
				} catch (UnsupportedEncodingException ex) {
					log.fatal("JAVA SYSTEM ERROR - NOT UTF8!", ex);
				}
			}
		}
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
		validateAddressesCache();
	}

	public void setHostName(String serverHostName) {
		this.serverHostName = serverHostName;
		validateAddressesCache();
	}

	private static final class SenderKey {
		private final Language language;
		private final NotificationOriginator originator;

		private SenderKey(NotificationOriginator originator, Language language) {
			this.originator = originator;
			this.language = language;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			SenderKey senderKey = (SenderKey) o;
			return language == senderKey.language && originator == senderKey.originator;
		}

		@Override
		public int hashCode() {
			int result = language.hashCode();
			result = 31 * result + originator.hashCode();
			return result;
		}
	}
}
