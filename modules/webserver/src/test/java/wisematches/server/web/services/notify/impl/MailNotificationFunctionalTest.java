package wisematches.server.web.services.notify.impl;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import org.easymock.IMockBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Language;
import wisematches.personality.Membership;
import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.personality.player.PlayerManager;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.GameMoveException;
import wisematches.playground.PassTurnMove;
import wisematches.playground.RatingManager;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.message.Message;
import wisematches.playground.message.impl.HibernateMessage;
import wisematches.playground.propose.impl.DefaultGameProposal;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.impl.TilesBankInfoEditor;
import wisematches.server.web.services.notify.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
		"classpath:/config/notifications-config.xml",
		"classpath:/config/server-web-config.xml"
})
public class MailNotificationFunctionalTest {
	@Autowired
	NotificationManager notificationManager;

	@Autowired
	@Qualifier("mailNotificationPublisher")
	NotificationPublisher notificationPublisher;

	@Autowired
	Configuration notificationFreemarkerConfig;

	@Autowired
	NotificationTransformer notificationTransformer;

	public MailNotificationFunctionalTest() {
	}

	@Test
	public void test() throws BoardLoadingException, InterruptedException, TemplateModelException, GameMoveException, ExecutionException, PublicationException {
		final Account p1 = createMockPlayer(1001, Language.RU);
		final Account p2 = createMockPlayer(1002, Language.EN);

		final PlayerManager playerManager = createMock(PlayerManager.class);
		expect(playerManager.getPlayer(1001)).andReturn(new MemberPlayer(p1)).anyTimes();
		expect(playerManager.getPlayer(1002)).andReturn(new MemberPlayer(p2)).anyTimes();
		replay(playerManager);

		final RatingManager ratingManager = createMock(RatingManager.class);
		expect(ratingManager.getRating(Personality.person(1001))).andReturn((short) 1234).anyTimes();
		expect(ratingManager.getRating(Personality.person(1002))).andReturn((short) 2122).anyTimes();
		replay(ratingManager);

		notificationFreemarkerConfig.setSharedVariable("playerManager", playerManager);
		notificationFreemarkerConfig.setSharedVariable("ratingManager", ratingManager);

		final NotificationSettings condition = new NotificationSettings();
		Collection<NotificationDescriptor> descriptions = notificationManager.getDescriptors();
		for (NotificationDescriptor description : descriptions) {
			condition.setEnabled(description.getCode(), true);
		}
		notificationManager.setNotificationSettings(p1, condition);
		notificationManager.setNotificationSettings(p2, condition);

		final TilesBank tilesBank = new TilesBank(new TilesBankInfoEditor(Language.EN).add('A', 100, 1).createTilesBankInfo());
		final Dictionary dictionary = createNiceMock(Dictionary.class);

		final ScribbleBoard b1 = new ScribbleBoard(new ScribbleSettings("mock1", Language.RU, 3), Arrays.asList(p1, p2), tilesBank, dictionary);
		b1.makeMove(new PassTurnMove(b1.getPlayerTurn().getPlayerId()));

		final ScribbleBoard b2 = new ScribbleBoard(new ScribbleSettings("mock2", Language.EN, 5), Arrays.asList(p1, p2), tilesBank, dictionary);
		b2.makeMove(new PassTurnMove(b2.getPlayerTurn().getPlayerId()));

		notificationPublisher.publishNotification(new Notification("playground.game.started", p1, NotificationSender.GAME, b1));
		notificationPublisher.publishNotification(new Notification("playground.game.started", p2, NotificationSender.GAME, b2));

		notificationPublisher.publishNotification(new Notification("playground.game.turn", p1, NotificationSender.GAME, b1));
		notificationPublisher.publishNotification(new Notification("playground.game.turn", p2, NotificationSender.GAME, b2));

		notificationPublisher.publishNotification(new Notification("playground.game.expiration.day", "playground.game.expiring", p1, NotificationSender.GAME, b1));
		notificationPublisher.publishNotification(new Notification("playground.game.expiration.day", "playground.game.expiring", p2, NotificationSender.GAME, b2));

		notificationPublisher.publishNotification(new Notification("playground.game.expiration.half", "playground.game.expiring", p1, NotificationSender.GAME, b1));
		notificationPublisher.publishNotification(new Notification("playground.game.expiration.half", "playground.game.expiring", p2, NotificationSender.GAME, b2));

		notificationPublisher.publishNotification(new Notification("playground.game.expiration.hour", "playground.game.expiring", p1, NotificationSender.GAME, b1));
		notificationPublisher.publishNotification(new Notification("playground.game.expiration.hour", "playground.game.expiring", p2, NotificationSender.GAME, b2));

		b1.resign(b1.getPlayerHand(p1.getId()));
		b2.resign(b2.getPlayerHand(p1.getId()));
		notificationPublisher.publishNotification(new Notification("playground.game.finished", p1, NotificationSender.GAME, b1));
		notificationPublisher.publishNotification(new Notification("playground.game.finished", p2, NotificationSender.GAME, b2));

		final Message m = new HibernateMessage(p2, "asdqwe adf", p1);
		notificationPublisher.publishNotification(new Notification("playground.message.received", p1, NotificationSender.SUPPORT, m));
		notificationPublisher.publishNotification(new Notification("playground.message.received", p2, NotificationSender.SUPPORT, m));

		final DefaultGameProposal<ScribbleSettings> proposal = new DefaultGameProposal<ScribbleSettings>(12, "comment", new ScribbleSettings("mock1", Language.RU, 3), p1, new Personality[]{p2});
		notificationPublisher.publishNotification(new Notification("playground.challenge.initiated", p1, NotificationSender.SUPPORT, proposal));
		notificationPublisher.publishNotification(new Notification("playground.challenge.initiated", p2, NotificationSender.SUPPORT, proposal));

		notificationPublisher.publishNotification(new Notification("playground.challenge.rejected", "playground.challenge.finalized", p1, NotificationSender.SUPPORT, proposal));
		notificationPublisher.publishNotification(new Notification("playground.challenge.rejected", "playground.challenge.finalized", p2, NotificationSender.SUPPORT, proposal));

		notificationPublisher.publishNotification(new Notification("playground.challenge.repudiated", "playground.challenge.finalized", p1, NotificationSender.SUPPORT, proposal));
		notificationPublisher.publishNotification(new Notification("playground.challenge.repudiated", "playground.challenge.finalized", p2, NotificationSender.SUPPORT, proposal));

		notificationPublisher.publishNotification(new Notification("playground.challenge.terminated", "playground.challenge.finalized", p1, NotificationSender.SUPPORT, proposal));
		notificationPublisher.publishNotification(new Notification("playground.challenge.terminated", "playground.challenge.finalized", p2, NotificationSender.SUPPORT, proposal));

		notificationPublisher.publishNotification(new Notification("playground.challenge.expiration.day", "playground.challenge.expiring", p1, NotificationSender.SUPPORT, proposal));
		notificationPublisher.publishNotification(new Notification("playground.challenge.expiration.day", "playground.challenge.expiring", p2, NotificationSender.SUPPORT, proposal));

		notificationPublisher.publishNotification(new Notification("playground.challenge.expiration.days", "playground.challenge.expiring", p1, NotificationSender.SUPPORT, proposal));
		notificationPublisher.publishNotification(new Notification("playground.challenge.expiration.days", "playground.challenge.expiring", p2, NotificationSender.SUPPORT, proposal));

		notificationPublisher.publishNotification(new Notification("account.created", p1, NotificationSender.ACCOUNTS));
		notificationPublisher.publishNotification(new Notification("account.created", p2, NotificationSender.ACCOUNTS));

		final Map<String, Object> asd = new HashMap<String, Object>();
		asd.put("confirmationUrl", "a/a/w");
		asd.put("recoveryToken", "eeee");
		notificationPublisher.publishNotification(new Notification("account.recovery", p1, NotificationSender.ACCOUNTS, asd));
		notificationPublisher.publishNotification(new Notification("account.recovery", p2, NotificationSender.ACCOUNTS, asd));

		notificationPublisher.publishNotification(new Notification("account.updated", p1, NotificationSender.ACCOUNTS));
		notificationPublisher.publishNotification(new Notification("account.updated", p2, NotificationSender.ACCOUNTS));
	}

	private Account createMockPlayer(long i, Language en) {
		IMockBuilder<Account> mockBuilder = createMockBuilder(Account.class);
		mockBuilder.withConstructor(i);

		final Account mock = mockBuilder.createMock("mock" + i);
		expect(mock.getLanguage()).andReturn(en).anyTimes();
		expect(mock.getNickname()).andReturn("mock" + i).anyTimes();
		expect(mock.getEmail()).andReturn("support@localhost").anyTimes();
		expect(mock.getMembership()).andReturn(Membership.BASIC).anyTimes();
		replay(mock);
		return mock;
	}
}