package wisematches.server.mail.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wisematches.personality.account.Account;
import wisematches.personality.account.Language;
import wisematches.server.mail.MailException;
import wisematches.server.mail.MailSender;
import wisematches.server.mail.MailService;

import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/application-settings.xml",
		"classpath:/config/message-source-config.xml",
		"classpath:/config/mail-sender-config.xml"
})
public class MailServiceTest {
	@Qualifier("mailService")
	@Autowired(required = true)
	private MailService mailService;

	public MailServiceTest() {
	}

	@Test
	public void mock() throws MailException {
//		asd();
	}

	public void asd() throws MailException {
		final Account p = createMock(Account.class);
		expect(p.getLanguage()).andReturn(Language.RU).anyTimes();
		expect(p.getEmail()).andReturn("support@127.0.0.1").anyTimes();
		expect(p.getNickname()).andReturn("Mock").anyTimes();
		replay(p);

		final Map<String, Object> model = new HashMap<String, Object>();

		Assert.assertNotNull(mailService);
		mailService.sendWarrantyMail(MailSender.ACCOUNTS, p, "account/recovery", model);
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
}
