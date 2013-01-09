package wisematches.server.web.services.notify.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountListener;
import wisematches.personality.account.AccountManager;
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
	private DictionarySuggestionManager dictionarySuggestionManager;

	private final TheAccountListener accountListener = new TheAccountListener();
	private final TheDictionaryListener dictionaryListener = new TheDictionaryListener();

	private static final Log log = LogFactory.getLog("wisematches.server.alerts.center");

	public AlertsOriginCenter() {
	}

	protected void raiseAlarm(String system, String subj, String msg) {
		try {
			final SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(system + "@" + hostName);
			message.setFrom("alert@" + hostName);
			message.setSentDate(new Date());
			message.setSubject(subj);
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
			raiseAlarm("account", "Account created: " + account.getNickname(), account.getNickname() + " (" + account.getEmail() + ")");
		}

		@Override
		public void accountRemove(Account account) {
		}

		@Override
		public void accountUpdated(Account oldAccount, Account newAccount) {
		}
	}

	private class TheDictionaryListener implements DictionarySuggestionListener {
		private TheDictionaryListener() {
		}

		@Override
		public void changeRequestRaised(ChangeSuggestion request) {
			raiseAlarm("dictionary", "Suggestion raised: " + request.getWord() + " [" + request.getSuggestionType() + "]",
					request.getDefinition() + "\n" + request.getAttributes());
		}

		@Override
		public void changeRequestApproved(ChangeSuggestion request) {
			raiseAlarm("dictionary", "Suggestion approved: " + request.getWord() + " [" + request.getSuggestionType() + "]",
					request.getDefinition() + "\n" + request.getAttributes());
		}

		@Override
		public void changeRequestRejected(ChangeSuggestion request) {
			raiseAlarm("dictionary", "Suggestion rejected: " + request.getWord() + " [" + request.getSuggestionType() + "]",
					request.getDefinition() + "\n" + request.getAttributes());
		}
	}
}
