package wisematches.server.web.services.notify.mail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import wisematches.personality.Language;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.FreeMarkerNotificationPublisher;
import wisematches.server.web.services.notify.NotificationSender;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MailNotificationPublisher extends FreeMarkerNotificationPublisher {
	private String serverHostName;
	private JavaMailSender mailSender;

	private final Map<SenderKey, InternetAddress> addressesCache = new HashMap<SenderKey, InternetAddress>();

	private static final Log log = LogFactory.getLog("wisematches.server.notice.mail");

	public MailNotificationPublisher() {
	}

	@Override
	protected void raiseNotification(final String subject, final String text, final MemberPlayer player, final NotificationSender sender) throws Exception {
		final MimeMessagePreparator mm = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				final Language language = player.getLanguage();

				final InternetAddress to = new InternetAddress(player.getEmail(), player.getNickname(), "UTF-8");
				final InternetAddress from = getInternetAddress(sender, language);

				final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
				message.setFrom(from);
				message.setTo(to);
				message.setSubject(subject);
				message.setText(text, true);
			}
		};
		mailSender.send(mm);
	}

	protected InternetAddress getInternetAddress(NotificationSender sender, Language language) {
		return addressesCache.get(new SenderKey(sender, language));
	}

	private void validateAddressesCache() {
		addressesCache.clear();

		if (messageSource == null || serverHostName == null) {
			return;
		}

		for (NotificationSender sender : NotificationSender.values()) {
			for (Language language : Language.values()) {
				try {
					final String address = messageSource.getMessage("mail.address." + sender.getUserInfo(),
							null, sender.getUserInfo() + "@" + serverHostName, language.locale());

					final String personal = messageSource.getMessage("mail.personal." + sender.getUserInfo(),
							null, sender.name(), language.locale());

					addressesCache.put(new SenderKey(sender, language), new InternetAddress(address, personal, "UTF-8"));
				} catch (UnsupportedEncodingException ex) {
					log.fatal("JAVA SYSTEM ERROR - NOT UTF8!", ex);
				}
			}
		}
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		super.setMessageSource(messageSource);
		validateAddressesCache();
	}

	public void setServerHostName(String serverHostName) {
		this.serverHostName = serverHostName;
		validateAddressesCache();
	}

	private static final class SenderKey {
		private final Language language;
		private final NotificationSender sender;

		private SenderKey(NotificationSender sender, Language language) {
			this.sender = sender;
			this.language = language;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			SenderKey senderKey = (SenderKey) o;
			return language == senderKey.language && sender == senderKey.sender;
		}

		@Override
		public int hashCode() {
			int result = language.hashCode();
			result = 31 * result + sender.hashCode();
			return result;
		}
	}
}
