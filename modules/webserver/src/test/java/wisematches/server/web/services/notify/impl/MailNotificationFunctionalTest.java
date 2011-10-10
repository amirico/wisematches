package wisematches.server.web.services.notify.impl;

import freemarker.template.TemplateModelException;
import org.easymock.IMockBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Language;
import wisematches.personality.account.Account;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.GameMoveException;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.message.Message;
import wisematches.playground.message.impl.HibernateMessage;
import wisematches.playground.propose.impl.DefaultChallengeGameProposal;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.impl.TilesBankInfoEditor;
import wisematches.server.web.services.notify.NotificationDescription;
import wisematches.server.web.services.notify.NotificationManager;
import wisematches.server.web.services.notify.NotificationMask;
import wisematches.server.web.services.notify.NotificationMover;
import wisematches.server.web.services.notify.impl.publish.mail.MailNotificationPublisher;
import wisematches.server.web.services.notify.impl.transform.FreeMarkerNotificationTransformer;

import java.util.*;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/accounts-config.xml",
		"classpath:/config/playground-config.xml",
		"classpath:/config/scribble-junit-config.xml",
		"classpath:/config/application-settings.xml",
		"classpath:/config/notify-sender-config.xml",
		"classpath:/config/server-web-config.xml"
})
public class MailNotificationFunctionalTest {
	@Autowired
	NotificationManager notificationManager;

	@Autowired
	MailNotificationPublisher notificationPublisher;

	@Autowired
	FreeMarkerNotificationTransformer notificationTransformer;

	public MailNotificationFunctionalTest() {
	}

	@Test
	public void test() throws BoardLoadingException, InterruptedException, TemplateModelException, GameMoveException {
		final MemberPlayer p1 = createMockPlayer(1001, Language.RU);
		final MemberPlayer p2 = createMockPlayer(1002, Language.EN);

		final PlayerManager playerManager = createMock(PlayerManager.class);
		expect(playerManager.getPlayer(1001)).andReturn(p1).anyTimes();
		expect(playerManager.getPlayer(1002)).andReturn(p2).anyTimes();
		replay(playerManager);

		notificationTransformer.getFreeMarkerConfig().setSharedVariable("playerManager", playerManager);

		final NotificationMask mask = new NotificationMask();
		Collection<NotificationDescription> descriptions = notificationManager.getDescriptions();
		for (NotificationDescription description : descriptions) {
			mask.setEnabled(description.getName(), true);
		}
		notificationManager.setNotificationMask(p1, mask);
		notificationManager.setNotificationMask(p2, mask);

		final TilesBank tilesBank = new TilesBank(new TilesBankInfoEditor(Language.EN).add('A', 100, 1).createTilesBankInfo());
		final Dictionary dictionary = createNiceMock(Dictionary.class);

		final ScribbleBoard b1 = new ScribbleBoard(new ScribbleSettings("mock1", Language.RU, 3), Arrays.asList(p1, p2), tilesBank, dictionary);
		final ScribbleBoard b2 = new ScribbleBoard(new ScribbleSettings("mock2", Language.EN, 5), Arrays.asList(p1, p2), tilesBank, dictionary);

		notificationPublisher.raiseNotification("game.state.started", p1, NotificationMover.GAME, Collections.<String, Object>singletonMap("context", b1));
		notificationPublisher.raiseNotification("game.state.started", p2, NotificationMover.GAME, Collections.<String, Object>singletonMap("context", b2));

		notificationPublisher.raiseNotification("game.move.your", p1, NotificationMover.GAME, Collections.<String, Object>singletonMap("context", b1));
		notificationPublisher.raiseNotification("game.move.your", p2, NotificationMover.GAME, Collections.<String, Object>singletonMap("context", b2));

		notificationPublisher.raiseNotification("game.move.opponent", p1, NotificationMover.GAME, Collections.<String, Object>singletonMap("context", b1));
		notificationPublisher.raiseNotification("game.move.opponent", p2, NotificationMover.GAME, Collections.<String, Object>singletonMap("context", b2));

		notificationPublisher.raiseNotification("game.timeout.day", p1, NotificationMover.GAME, Collections.<String, Object>singletonMap("context", b1));
		notificationPublisher.raiseNotification("game.timeout.day", p2, NotificationMover.GAME, Collections.<String, Object>singletonMap("context", b2));

		notificationPublisher.raiseNotification("game.timeout.half", p1, NotificationMover.GAME, Collections.<String, Object>singletonMap("context", b1));
		notificationPublisher.raiseNotification("game.timeout.half", p2, NotificationMover.GAME, Collections.<String, Object>singletonMap("context", b2));

		notificationPublisher.raiseNotification("game.timeout.hour", p1, NotificationMover.GAME, Collections.<String, Object>singletonMap("context", b1));
		notificationPublisher.raiseNotification("game.timeout.hour", p2, NotificationMover.GAME, Collections.<String, Object>singletonMap("context", b2));

		b1.resign(b1.getPlayerHand(p1.getId()));
		b2.resign(b2.getPlayerHand(p1.getId()));
		notificationPublisher.raiseNotification("game.state.finished", p1, NotificationMover.GAME, Collections.<String, Object>singletonMap("context", b1));
		notificationPublisher.raiseNotification("game.state.finished", p2, NotificationMover.GAME, Collections.<String, Object>singletonMap("context", b2));

		final Message m = new HibernateMessage(p2, "asdqwe adf", p1);
		notificationPublisher.raiseNotification("game.message", p1, NotificationMover.SUPPORT, Collections.<String, Object>singletonMap("context", m));
		notificationPublisher.raiseNotification("game.message", p2, NotificationMover.SUPPORT, Collections.<String, Object>singletonMap("context", m));

		DefaultChallengeGameProposal<ScribbleSettings> proposal1 = new DefaultChallengeGameProposal<ScribbleSettings>(12, new ScribbleSettings("mock1", Language.RU, 3), "comment", p1, Arrays.<Player>asList(p2));
		notificationPublisher.raiseNotification("game.challenge.received", p1, NotificationMover.SUPPORT, Collections.<String, Object>singletonMap("context", proposal1));
		notificationPublisher.raiseNotification("game.challenge.received", p2, NotificationMover.SUPPORT, Collections.<String, Object>singletonMap("context", proposal1));

		DefaultChallengeGameProposal<ScribbleSettings> proposal2 = new DefaultChallengeGameProposal<ScribbleSettings>(12, new ScribbleSettings("mock1", Language.RU, 3), "comment", p1, Arrays.<Player>asList(p2));
		notificationPublisher.raiseNotification("game.challenge.rejected", p1, NotificationMover.SUPPORT, Collections.<String, Object>singletonMap("context", proposal2));
		notificationPublisher.raiseNotification("game.challenge.rejected", p2, NotificationMover.SUPPORT, Collections.<String, Object>singletonMap("context", proposal2));

		notificationPublisher.raiseNotification("account.created", p1.getAccount(), NotificationMover.ACCOUNTS, null);
		notificationPublisher.raiseNotification("account.created", p2.getAccount(), NotificationMover.ACCOUNTS, null);

		final Map<String, Object> asd = new HashMap<String, Object>();
		asd.put("confirmationUrl", "a/a/w");
		asd.put("recoveryToken", "eeee");
		notificationPublisher.raiseNotification("account.recovery", p1.getAccount(), NotificationMover.ACCOUNTS, asd);
		notificationPublisher.raiseNotification("account.recovery", p2.getAccount(), NotificationMover.ACCOUNTS, asd);

		notificationPublisher.raiseNotification("account.updated", p1.getAccount(), NotificationMover.ACCOUNTS, null);
		notificationPublisher.raiseNotification("account.updated", p2.getAccount(), NotificationMover.ACCOUNTS, null);

		Thread.sleep(10000);
	}

	private MemberPlayer createMockPlayer(long i, Language en) {
		IMockBuilder<Account> mockBuilder = createMockBuilder(Account.class);
		mockBuilder.withConstructor(i);

		final Account mock = mockBuilder.createMock("mock" + i);
		expect(mock.getLanguage()).andReturn(en).anyTimes();
		expect(mock.getNickname()).andReturn("mock" + i).anyTimes();
		expect(mock.getEmail()).andReturn("support@localhost").anyTimes();
		replay(mock);
		return new MemberPlayer(mock);
	}
}
