package wisematches.server.web.mail.impl;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.easymock.IAnswer;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import wisematches.server.player.Language;
import wisematches.server.player.Player;
import wisematches.server.web.mail.FromTeam;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.Writer;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MailSenderImplTest {
	@Test
	public void testSendMailToPlayer() throws Exception {
		final Language language = Language.ENGLISH;

		final Player p = createMock(Player.class);
		expect(p.getEmail()).andReturn("player@test.ts");
		expect(p.getNickname()).andReturn("username");
		expect(p.getLanguage()).andReturn(language);
		replay(p);

		final InternetAddress to = InternetAddress.parse("username <player@test.ts>")[0];
		final InternetAddress from = FromTeam.BUG_REPORTER.getInternetAddress(language);

		final MimeMessage message = createMock("MimeMessage", MimeMessage.class);
		message.setRecipient(Message.RecipientType.TO, to);
		message.setFrom(from);
		message.setSubject("This is test message");
		message.setContent("Test text", "text/html");
		replay(message);

		final JavaMailSender mailSender = createMock("JavaMailSender", JavaMailSender.class);
		mailSender.send(isA(MimeMessagePreparator.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				MimeMessagePreparator p = (MimeMessagePreparator) getCurrentArguments()[0];
				p.prepare(message);
				return null;
			}
		});
		replay(mailSender);

		final VelocityEngine velocityEngine = createMock("VelocityEngine", VelocityEngine.class);
		expect(velocityEngine.mergeTemplate(eq("This is template"), isA(Context.class), isA(Writer.class))).andAnswer(new IAnswer<Boolean>() {
			public Boolean answer() throws Throwable {
				Context ctx = (Context) getCurrentArguments()[1];
				Writer writer = (Writer) getCurrentArguments()[2];
				final Object[] keys = ctx.getKeys();
				assertEquals("NUmber of keys in cintext", 4, keys.length);
				assertEquals("1", ctx.get("asd"));
				assertEquals("2", ctx.get("qwe"));
				assertEquals("3", ctx.get("rty"));
				assertEquals(p, ctx.get("player"));

				writer.write("Test text");
				return true;
			}
		});
		replay(velocityEngine);

		final MailSenderImpl s = new MailSenderImpl();
		s.setMailSender(mailSender);
		s.setVelocityEngine(velocityEngine);

		s.sendMail(FromTeam.BUG_REPORTER, p, "test.test1", "asd=1", "qwe=2", "rty=3");

		verify(message, mailSender, velocityEngine);
	}
}