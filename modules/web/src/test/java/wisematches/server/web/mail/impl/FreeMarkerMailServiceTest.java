package wisematches.server.web.mail.impl;

import freemarker.template.TemplateException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import wisematches.server.player.Language;
import wisematches.server.player.Player;
import wisematches.server.web.mail.SenderAccount;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FreeMarkerMailServiceTest {
	private JavaMailSender javaMailSender;
	private FreeMarkerMailService markerMailService;

	private static StaticMessageSource messageSource = new StaticMessageSource();

	@BeforeClass
	public static void initMessageSource() {
		messageSource.addMessage("mail.address.support", Language.RUSSIAN.locale(), "support@mock.wm");
		messageSource.addMessage("mail.personal.support", Language.RUSSIAN.locale(), "Mock Support");
	}

	@Before
	public void setUp() throws IOException, TemplateException {
		javaMailSender = createMock(JavaMailSender.class);

		final FreeMarkerConfigurer config = new FreeMarkerConfigurer();
		config.setDefaultEncoding("UTF-8");
		config.setTemplateLoaderPath("classpath:/i18n/server/mail");

		markerMailService = new FreeMarkerMailService();
		markerMailService.setMailSender(javaMailSender);
		markerMailService.setFreeMarkerConfig(config.createConfiguration());
		markerMailService.setMessageSource(messageSource);
		markerMailService.setSupportSenderAddress("sender@mock.wm");
		markerMailService.setSupportRecipientAddress("recipient@mock.wm");
	}

	@Test
	public void testSendWarrantyMail() throws Exception {
		final Player p = createMock(Player.class);
		replay(p);

		javaMailSender.send(isA(MimeMessagePreparator.class));
		replay(javaMailSender);

		markerMailService.sendWarrantyMail(SenderAccount.SUPPORT, p, "mock", null);
		verify(p, javaMailSender);
	}

	@Test
	public void testSendSupportMail() throws Exception {
		final Player p = createMock(Player.class);
		replay(p);

		javaMailSender.send(isA(MimeMessagePreparator.class));
		replay(javaMailSender);

		markerMailService.sendSupportRequest("Mock Subject", "mock", null);
		verify(p, javaMailSender);
	}

	@Test
	public void testPrepareSupportMessage() throws Exception {
		final MimeMessage message = createMock(MimeMessage.class);
		message.setFrom(new InternetAddress("sender@mock.wm"));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress("recipient@mock.wm"));
		message.setSubject("Mock Subject");
		message.setContent("En Subject" + System.getProperty("line.separator") +
				System.getProperty("line.separator") + "En body", "text/html");
		replay(message);

		MimeMessagePreparator preparator = markerMailService.prepareSupportMessage("Mock Subject", "mock", null);
		preparator.prepare(message);

		verify(message);
	}

	@Test
	public void testPreparePlayerMessage() throws Exception {
		final Player p = createMock(Player.class);
		expect(p.getLanguage()).andReturn(Language.RUSSIAN);
		expect(p.getNickname()).andReturn("Mock Player");
		expect(p.getEmail()).andReturn("player@mock.wm");
		replay(p);

		final MimeMessage message = createMock(MimeMessage.class);
		message.setFrom(new InternetAddress("support@mock.wm", "Mock Support", "UTF-8"));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress("player@mock.wm", "Mock Player", "UTF-8"));
		message.setSubject("Ru Subject");
		message.setContent(System.getProperty("line.separator") + "Ru body", "text/html");
		replay(message);

		MimeMessagePreparator preparator = markerMailService.preparePlayerMessage(SenderAccount.SUPPORT, p, "mock", null);
		assertNotNull(preparator);
		preparator.prepare(message);

		verify(p, message);
	}

	@Test
	public void testGetTemplate() throws Exception {
		assertNotNull(markerMailService.getTemplate("mock", Language.ENGLISH));
	}
}
