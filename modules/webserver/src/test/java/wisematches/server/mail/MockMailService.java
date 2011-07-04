package wisematches.server.mail;

import wisematches.personality.account.Account;

import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MockMailService implements MailService {
	@Override
	public void sendMail(MailSender from, Account to, String msgCode, Map<String, ?> model) {
	}

	@Override
	public void sendWarrantyMail(MailSender from, Account to, String msgCode, Map<String, ?> model) throws MailException {
	}

	@Override
	public void sendSupportRequest(String subject, String msgCode, Map<String, ?> model) throws MailException {
	}
}
