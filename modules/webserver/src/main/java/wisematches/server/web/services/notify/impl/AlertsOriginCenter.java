package wisematches.server.web.services.notify.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountListener;
import wisematches.personality.account.AccountManager;
import wisematches.playground.abuse.AbuseReportListener;
import wisematches.playground.abuse.AbuseReportManager;
import wisematches.playground.message.Message;
import wisematches.server.web.services.dictionary.ChangeSuggestion;
import wisematches.server.web.services.dictionary.DictionarySuggestionListener;
import wisematches.server.web.services.dictionary.DictionarySuggestionManager;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AlertsOriginCenter {
	private String hostName;
	private MailSender mailSender;
	private AccountManager accountManager;
	private AbuseReportManager abuseReportManager;
	private DictionarySuggestionManager dictionarySuggestionManager;

	private final TheAccountListener accountListener = new TheAccountListener();
	private final TheDictionaryListener dictionaryListener = new TheDictionaryListener();
	private final TheAbuseReportListener abuseReportListener = new TheAbuseReportListener();

	private static final Log log = LogFactory.getLog("wisematches.server.alerts.center");

	public AlertsOriginCenter() {
	}

	protected void raiseAlarm(String system, String subj, String msg) {
		try {
			final SimpleMailMessage message = new SimpleMailMessage();
			message.setTo("alert@" + hostName);
			message.setFrom("no-replay@" + hostName);
			message.setSentDate(new Date());
			message.setSubject("[" + system + "] " + subj);
			message.setText(msg);

			mailSender.send(message);
		} catch (Exception ex) {
			log.error("Alerts can't be sent: system=[" + system + "], subj=[" + subj + "], msg=[" + msg + "]");
		}
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
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
		public void changeRequestRaised(ChangeSuggestion request) {
			raiseAlarm("DIC", "Suggestion raised: " + request.getWord() + " [" + request.getSuggestionType() + "]",
					request.getDefinition() + "\n" + request.getAttributes());
		}

		@Override
		public void changeRequestApproved(ChangeSuggestion request) {
			raiseAlarm("DIC", "Suggestion approved: " + request.getWord() + " [" + request.getSuggestionType() + "]",
					request.getDefinition() + "\n" + request.getAttributes());
		}

		@Override
		public void changeRequestRejected(ChangeSuggestion request) {
			raiseAlarm("DIC", "Suggestion rejected: " + request.getWord() + " [" + request.getSuggestionType() + "]",
					request.getDefinition() + "\n" + request.getAttributes());
		}
	}
}
