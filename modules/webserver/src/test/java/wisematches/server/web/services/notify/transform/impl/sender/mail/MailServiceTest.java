package wisematches.server.web.services.notify.transform.impl.sender.mail;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/application-settings.xml",
		"classpath:/config/notify-sender-config.xml",
		"classpath:/config/server-web-config.xml"
})
public class MailServiceTest {
	@Test
	public void asd() {
		throw new UnsupportedOperationException("Not implemented");
	}

/*
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
		mailService.sendWarrantyMail(SenderName.ACCOUNTS, p, "account/recovery", model);
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
*/
}
