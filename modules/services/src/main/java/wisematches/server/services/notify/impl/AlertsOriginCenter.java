package wisematches.server.services.notify.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import wisematches.core.personality.player.account.Account;
import wisematches.core.personality.player.account.AccountListener;
import wisematches.core.personality.player.account.AccountManager;
import wisematches.playground.dictionary.DictionaryReclaimListener;
import wisematches.playground.dictionary.DictionaryReclaimManager;
import wisematches.playground.dictionary.ReclaimResolution;
import wisematches.playground.dictionary.WordReclaim;
import wisematches.server.services.ServerDescriptor;
import wisematches.server.services.abuse.AbuseReportListener;
import wisematches.server.services.abuse.AbuseReportManager;
import wisematches.server.services.message.Message;
import wisematches.server.services.notify.NotificationSender;

import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AlertsOriginCenter {
	private JavaMailSender mailSender;
	private AccountManager accountManager;
	private ServerDescriptor serverDescriptor;
	private AbuseReportManager abuseReportManager;
	private DictionaryReclaimManager dictionaryReclaimManager;

	private final TheAccountListener accountListener = new TheAccountListener();
	private final TheDictionaryListener dictionaryListener = new TheDictionaryListener();
	private final TheAbuseReportListener abuseReportListener = new TheAbuseReportListener();

	private static final Logger log = LoggerFactory.getLogger("wisematches.alerts.OriginCenter");

	public AlertsOriginCenter() {
	}

	protected void raiseAlarm(String system, String subj, String msg) {
		try {
			final MimeMessage mimeMessage = mailSender.createMimeMessage();

			final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			message.setTo(serverDescriptor.getAlertsMailBox());
			message.setFrom(NotificationSender.SUPPORT.getMailAddress(serverDescriptor));
			message.setSentDate(new Date());
			message.setSubject("[" + system + "] " + subj);
			message.setText(msg);

			mailSender.send(mimeMessage);
		} catch (Exception ex) {
			log.error("Alerts can't be sent: system=[{}], subj=[{}], msg=[{}]", system, subj, msg);
		}
	}

	public void setServerDescriptor(ServerDescriptor serverDescriptor) {
		this.serverDescriptor = serverDescriptor;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setAccountManager(AccountManager accountManager) {
		if (this.accountManager != null) {
			this.accountManager.removeAccountListener(accountListener);
		}

		this.accountManager = accountManager;

		if (this.accountManager != null) {
			this.accountManager.addAccountListener(accountListener);
		}
	}

	public void setAbuseReportManager(AbuseReportManager abuseReportManager) {
		if (this.abuseReportManager != null) {
			this.abuseReportManager.removeAbuseReportListener(abuseReportListener);
		}

		this.abuseReportManager = abuseReportManager;

		if (this.abuseReportManager != null) {
			this.abuseReportManager.addAbuseReportListener(abuseReportListener);
		}
	}

	public void setDictionaryReclaimManager(DictionaryReclaimManager dictionaryReclaimManager) {
		if (this.dictionaryReclaimManager != null) {
			this.dictionaryReclaimManager.removeDictionaryReclaimListener(dictionaryListener);
		}

		this.dictionaryReclaimManager = dictionaryReclaimManager;

		if (this.dictionaryReclaimManager != null) {
			this.dictionaryReclaimManager.addDictionaryReclaimListener(dictionaryListener);
		}
	}

	private class TheAccountListener implements AccountListener {
		private TheAccountListener() {
		}

		@Override
		public void accountCreated(Account account) {
			raiseAlarm("ACC", "Account created: " + account.getNickname(), account.getNickname() + " (" + account.getEmail() + ")");
		}

		@Override
		public void accountRemove(Account account) {
		}

		@Override
		public void accountUpdated(Account oldAccount, Account newAccount) {
		}
	}

	private class TheAbuseReportListener implements AbuseReportListener {
		private TheAbuseReportListener() {
		}

		@Override
		public void abuseMessage(Message message) {
			raiseAlarm("ABUSE", "Abuse report: " + message.getId(),
					"" +
							"From: " + message.getSender() + "\n" +
							"To: " + message.getRecipient() + "\n" +
							"Message: " + message.getText());
		}
	}

	private class TheDictionaryListener implements DictionaryReclaimListener {
		private TheDictionaryListener() {
		}

		@Override
		public void wordReclaimRaised(WordReclaim reclaim) {
			raiseAlarm("DIC", "Reclaim raised: " + reclaim.getWord() + " [" + reclaim.getResolutionType() + "]",
					reclaim.getDefinition() + "\n" + reclaim.getAttributes());
		}

		@Override
		public void wordReclaimResolved(WordReclaim reclaim, ReclaimResolution resolution) {
			raiseAlarm("DIC", "Reclaim " + resolution.name().toLowerCase() + ": " +
					reclaim.getWord() + " [" + reclaim.getResolutionType() + "]",
					reclaim.getDefinition() + "\n" + reclaim.getAttributes());
		}

		@Override
		public void wordReclaimUpdated(WordReclaim reclaim) {
			raiseAlarm("DIC", "Reclaim updated: " + reclaim.getWord() + " [" + reclaim.getResolutionType() + "]",
					reclaim.getDefinition() + "\n" + reclaim.getAttributes());
		}
	}
}
