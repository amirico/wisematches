package wisematches.server.web.mail.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import wisematches.server.player.Language;
import wisematches.server.player.Player;
import wisematches.server.web.mail.*;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FreeMarkerMailService implements MailService {
	private JavaMailSender mailSender;
	private MessageSource messageSource;
	private FreeMarkerConfig freeMarkerConfig;

	private String supportSenderAddress = "support@wisematches.net";
	private String supportRecipientAddress = "support@wisematches.net";

	private final Map<SenderAccount, InternetAddress> addressesCache = new HashMap<SenderAccount, InternetAddress>();

	protected static final Log log = LogFactory.getLog("wisematches.server.mail");

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public FreeMarkerMailService() {
	}

	@Override
	public void sendMail(SenderAccount from, Player to, String msgCode, Map<String, ?> model) {
		try {
			sendWarrantyMail(from, to, msgCode, model);
		} catch (MailException ex) {
			log.fatal("Mail can't be send: from=" + from + ", player=" + to + ", msgCode=" + msgCode + ", model=" + model, ex);
		}
	}

	@Override
	public void sendWarrantyMail(SenderAccount from, Player to, String msgCode, Map<String, ?> model) throws MailException {
		sendMail(preparePlayerMessage(from, to, msgCode, model));
	}

	@Override
	public void sendSupportRequest(String subject, String msgCode, Map<String, ?> model) throws MailException {
		sendMail(prepareSupportMessage(subject, msgCode, model));
	}

	protected void sendMail(MimeMessagePreparator preparator) throws MailException {
		try {
			mailSender.send(preparator);
		} catch (MailPreparationException ex) {
			throw new MailTemplateException("", ex);
		} catch (MailParseException ex) {
			throw new MailTemplateException("", ex);
		} catch (MailSendException ex) {
			throw new MailTransportException("", ex);
		} catch (MailAuthenticationException ex) {
			throw new MailTransportException("", ex);
		} catch (Exception ex) {
			throw new MailException("", ex);
		}
	}

	protected MimeMessagePreparator prepareSupportMessage(final String subject, final String msgCode, final Map<String, ?> model) {
		return new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				try {
					final Language language = Language.ENGLISH;
					final Template template = getTemplate(msgCode, language);

					final Map<String, Object> variables = new HashMap<String, Object>();
					if (model != null) {
						variables.putAll(model);
					}

					final String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, variables);
					final InternetAddress to = new InternetAddress(supportRecipientAddress);
					final InternetAddress from = new InternetAddress(supportSenderAddress);

					final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					message.setFrom(from);
					message.setTo(to);
					message.setSubject(subject);
					message.setText(text, true);
				} catch (Exception ex) {
					throw new MailTemplateException("Mail template can't be created", ex);
				}
			}
		};
	}

	protected MimeMessagePreparator preparePlayerMessage(final SenderAccount sender, final Player player, final String msgCode, final Map<String, ?> model) {
		return new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				try {
					final Language language = player.getLanguage();
					final Template template = getTemplate(msgCode, language);

					final Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("player", player);
					if (model != null) {
						variables.putAll(model);
					}

					final String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, variables);
					int firstLine = text.indexOf(LINE_SEPARATOR);
					if (firstLine < 0) {
						throw new MailTemplateException("Template does not have subject line at a top");
					}

					final InternetAddress to = new InternetAddress(player.getEmail(), player.getNickname(), "UTF-8");
					final InternetAddress from = getInternetAddress(sender, language);

					final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					message.setFrom(from);
					message.setTo(to);
					message.setSubject(text.substring(0, firstLine));
					message.setText(text.substring(firstLine + LINE_SEPARATOR.length()), true);
				} catch (MailTemplateException ex) {
					throw ex;
				} catch (Exception ex) {
					throw new MailTemplateException("Mail template can't be created", ex);
				}
			}
		};
	}

	protected Template getTemplate(String msgCode, Language language) throws IOException {
		final Configuration configuration = freeMarkerConfig.getConfiguration();
		return configuration.getTemplate(msgCode + ".ftl", language.locale(), "UTF-8");
	}

	protected InternetAddress getInternetAddress(SenderAccount sender, Language language) {
		return addressesCache.get(sender);
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setFreeMarkerConfig(FreeMarkerConfig freeMarkerConfig) {
		this.freeMarkerConfig = freeMarkerConfig;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;

		addressesCache.clear();
		for (SenderAccount sender : SenderAccount.values()) {
			for (Language language : Language.values()) {
				try {
					addressesCache.put(sender, new InternetAddress(
							messageSource.getMessage("mail.address." + sender.name().toLowerCase(), null,
									"support@wisematches.net", language.locale()),
							messageSource.getMessage("mail.personal." + sender.name().toLowerCase(), null,
									"WiseMatches Support", language.locale()),
							"UTF-8"));
				} catch (UnsupportedEncodingException ex) {
					log.fatal("JAVA SYSTEM ERROR - NOT UTF8!", ex);
				}
			}
		}
	}

	public void setSupportSenderAddress(String supportSenderAddress) {
		this.supportSenderAddress = supportSenderAddress;
	}

	public void setSupportRecipientAddress(String supportRecipientAddress) {
		this.supportRecipientAddress = supportRecipientAddress;
	}
}
