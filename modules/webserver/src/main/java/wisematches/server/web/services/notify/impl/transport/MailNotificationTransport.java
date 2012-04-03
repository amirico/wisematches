package wisematches.server.web.services.notify.impl.transport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import wisematches.personality.Language;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationCreator;
import wisematches.server.web.services.notify.NotificationMessage;
import wisematches.server.web.services.notify.NotificationTransport;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MailNotificationTransport implements NotificationTransport {
	private String serverHostName;
	private JavaMailSender mailSender;
	private MessageSource messageSource;

	private final Map<SenderKey, InternetAddress> addressesCache = new HashMap<SenderKey, InternetAddress>();

	private static final Log log = LogFactory.getLog("wisematches.server.notify.mail");

	public MailNotificationTransport() {
	}

	@Override
	public void sendNotification(final NotificationMessage message) {
		if (log.isDebugEnabled()) {
			log.debug("Send mail notification '" + message.getCode() + "' to " + message.getAccount());
		}
		final MimeMessagePreparator mm = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				final Account account = message.getAccount();
				final Language language = account.getLanguage();

				final InternetAddress to = new InternetAddress(account.getEmail(), account.getNickname(), "UTF-8");
				final InternetAddress from = getInternetAddress(message.getCreator(), language);

				final MimeMessageHelper msg = new MimeMessageHelper(mimeMessage, false, "UTF-8");
				msg.setFrom(from);
				msg.setTo(to);
				msg.setSubject(message.getSubject());
				msg.setText(message.getMessage(), true);
			}
		};
		mailSender.send(mm);
	}

	protected InternetAddress getInternetAddress(NotificationCreator creator, Language language) {
		return addressesCache.get(new SenderKey(creator, language));
	}

	private void validateAddressesCache() {
		addressesCache.clear();

		if (messageSource == null || serverHostName == null) {
			return;
		}

		for (NotificationCreator creator : NotificationCreator.values()) {
			for (Language language : Language.values()) {
				try {
					final String address = messageSource.getMessage("mail.address." + creator.getUserInfo(),
							null, creator.getUserInfo() + "@" + serverHostName, language.locale());

					final String personal = messageSource.getMessage("mail.personal." + creator.getUserInfo(),
							null, creator.name(), language.locale());

					addressesCache.put(new SenderKey(creator, language), new InternetAddress(address, personal, "UTF-8"));
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
		private final NotificationCreator creator;

		private SenderKey(NotificationCreator creator, Language language) {
			this.creator = creator;
			this.language = language;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			SenderKey senderKey = (SenderKey) o;
			return language == senderKey.language && creator == senderKey.creator;
		}

		@Override
		public int hashCode() {
			int result = language.hashCode();
			result = 31 * result + creator.hashCode();
			return result;
		}
	}
}