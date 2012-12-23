package wisematches.server.web.services.notify.impl.publisher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import wisematches.personality.Language;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationMessage;
import wisematches.server.web.services.notify.NotificationPublisher;
import wisematches.server.web.services.notify.NotificationScope;
import wisematches.server.web.services.notify.NotificationSender;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MailNotificationPublisher implements NotificationPublisher {
    private String serverHostName;
    private JavaMailSender mailSender;
    private MessageSource messageSource;

    private final Map<SenderKey, InternetAddress> addressesCache = new HashMap<>();

    private static final Log log = LogFactory.getLog("wisematches.server.notify.mail");

    public MailNotificationPublisher() {
    }

    @Override
    public String getName() {
        return "email";
    }

    @Override
    public NotificationScope getNotificationScope() {
        return NotificationScope.EXTERNAL;
    }

    @Override
    public void sendNotification(final NotificationMessage message) throws TransmissionException {
        if (log.isDebugEnabled()) {
            log.debug("Send mail notification '" + message.getCode() + "' to " + message.getAccount());
        }
        final MimeMessagePreparator mm = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                final Account account = message.getAccount();
                final Language language = account.getLanguage();

                final InternetAddress to = new InternetAddress(account.getEmail(), account.getNickname(), "UTF-8");
                final InternetAddress from = getInternetAddress(message.getSender(), language);

                final MimeMessageHelper msg = new MimeMessageHelper(mimeMessage, false, "UTF-8");
                msg.setFrom(from);
                msg.setTo(to);
                msg.setSubject(message.getSubject());
                msg.setText(message.getMessage(), true);
            }
        };
        try {
            mailSender.send(mm);
        } catch (MailException ex) {
            throw new TransmissionException(ex);
        }
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