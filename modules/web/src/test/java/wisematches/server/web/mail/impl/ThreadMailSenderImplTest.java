package wisematches.server.web.mail.impl;

import org.easymock.IAnswer;
import org.junit.Test;
import wisematches.server.player.Player;
import wisematches.server.web.mail.FromTeam;
import wisematches.server.web.mail.MailSender;
import wisematches.server.web.mail.ToTeam;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ThreadMailSenderImplTest {
	@Test
	public void test() {
		final MailSender originalMailSender = createStrictMock(MailSender.class);
		originalMailSender.sendMail(eq(FromTeam.ACCOUNTS), isA(Player.class), eq("resource"), eq(createMap(null, null)));
		originalMailSender.sendMail(eq(FromTeam.ABSTRACT), isA(Player.class), eq("resource2"), eq(createMap("asd", "qwe")));
		originalMailSender.sendSystemMail(eq(FromTeam.ACCOUNTS), eq(EnumSet.of(ToTeam.BUGS_TEAM)), eq("subject"), eq("template"), eq(createMap(null, null)));
		originalMailSender.sendSystemMail(eq(FromTeam.BUG_REPORTER), eq(EnumSet.of(ToTeam.SUPPORT_TEAM)), eq("subject2"), eq("template2"), eq(createMap("asd", "qwe")));
		originalMailSender.sendMail(eq(FromTeam.ACCOUNTS), isA(Player.class), eq("resource"), eq(createMap(null, null)));
		expectLastCall().andThrow(new UnsupportedOperationException("Not implemented"));
		replay(originalMailSender);

		final ExecutorService executor = createStrictMock(ExecutorService.class);
		executor.execute(isA(Runnable.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				final Runnable r = (Runnable) getCurrentArguments()[0];
				r.run();
				return null;
			}
		}).times(5);
		executor.shutdown();
		replay(executor);

		final ThreadMailSenderImpl impl = new ThreadMailSenderImpl();
		impl.setExecutorService(executor);
		impl.setOriginalMailSender(originalMailSender);

		impl.sendMail(FromTeam.ACCOUNTS, createNiceMock(Player.class), "resource", new HashMap<String, String>());
		impl.sendMail(FromTeam.ABSTRACT, createNiceMock(Player.class), "resource2", "asd=qwe");
		impl.sendSystemMail(FromTeam.ACCOUNTS, EnumSet.of(ToTeam.BUGS_TEAM), "subject", "template", new HashMap<String, String>());
		impl.sendSystemMail(FromTeam.BUG_REPORTER, EnumSet.of(ToTeam.SUPPORT_TEAM), "subject2", "template2", "asd=qwe");

		//this method should be executed. No any exception should be thrown.
		impl.sendMail(FromTeam.ACCOUNTS, createNiceMock(Player.class), "resource", new HashMap<String, String>());
		impl.destroy();

		verify(originalMailSender, executor);
	}

	private HashMap<String, ?> createMap(String key, Object value) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (key != null) {
			map.put(key, value);
		}
		return map;
	}
}