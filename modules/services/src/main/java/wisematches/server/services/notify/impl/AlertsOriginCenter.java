package wisematches.server.services.notify.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import wisematches.core.personality.player.account.Account;
import wisematches.core.personality.player.account.AccountListener;
import wisematches.core.personality.player.account.AccountManager;
import wisematches.server.services.ServerDescriptor;
import wisematches.server.services.abuse.AbuseReportListener;
import wisematches.server.services.abuse.AbuseReportManager;
import wisematches.server.services.dictionary.DictionarySuggestionListener;
import wisematches.server.services.dictionary.DictionarySuggestionManager;
import wisematches.server.services.dictionary.WordSuggestion;
import wisematches.server.services.message.Message;
import wisematches.server.services.notify.NotificationSender;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AlertsOriginCenter {
	private MailSender mailSender;
	private AccountManager accountManager;
	private ServerDescriptor serverDescriptor;
	private AbuseReportManager abuseReportManager;
	private DictionarySuggestionManager dictionarySuggestionManager;

	private final TheAccountListener accountListener = new TheAccountListener();
	private final TheDictionaryListener dictionaryListener = new TheDictionaryListener();
	private final TheAbuseReportListener abuseReportListener = new TheAbuseReportListener();

	private static final Logger log = LoggerFactory.getLogger("wisematches.alerts.OriginCenter");

	public AlertsOriginCenter() {
	}

	protected void raiseAlarm(String system, String subj, String msg) {
		try {
			final SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(serverDescriptor.getAlertsMailBox());
			message.setFrom(NotificationSender.SUPPORT.getMailAddress(serverDescriptor));
			message.setSentDate(new Date());
			message.setSubject("[" + system + "] " + subj);
			message.setText(msg);

			mailSender.send(message);
		} catch (Exception ex) {
			log.error("Alerts can't be sent: system=[{}], subj=[{}], msg=[{}]", system, subj, msg);
		}
	}

	public void setServerDescriptor(ServerDescriptor serverDescriptor) {
		this.serverDescriptor = serverDescriptor;
	}

	public void setMailSender(MailSender mailSender) {
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

	public void setDictionarySuggestionManager(DictionarySuggestionManager dictionarySuggestionManager) {
		if (this.dictionarySuggestionManager != null) {
			this.dictionarySuggestionManager.removeDictionaryChangeListener(dictionaryListener);
		}

		this.dictionarySuggestionManager = dictionarySuggestionManager;

		if (this.dictionarySuggestionManager != null) {
			this.dictionarySuggestionManager.addDictionaryChangeListener(dictionaryListener);
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

	private class TheDictionaryListener implements DictionarySuggestionListener {
		private TheDictionaryListener() {
		}

		@Override
		public void changeRequestRaised(WordSuggestion request) {
			raiseAlarm("DIC", "Suggestion raised: " + request.getWord() + " [" + request.getSuggestionType() + "]",
					request.getDefinition() + "\n" + request.getAttributes());
		}

		@Override
		public void changeRequestApproved(WordSuggestion request) {
			raiseAlarm("DIC", "Suggestion approved: " + request.getWord() + " [" + request.getSuggestionType() + "]",
					request.getDefinition() + "\n" + request.getAttributes());
		}

		@Override
		public void changeRequestRejected(WordSuggestion request) {
			raiseAlarm("DIC", "Suggestion rejected: " + request.getWord() + " [" + request.getSuggestionType() + "]",
					request.getDefinition() + "\n" + request.getAttributes());
		}

		@Override
		public void changeRequestUpdated(WordSuggestion request) {
			raiseAlarm("DIC", "Suggestion updated: " + request.getWord() + " [" + request.getSuggestionType() + "]",
					request.getDefinition() + "\n" + request.getAttributes());
		}
	}
}
