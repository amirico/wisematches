/*
package wisematches.server.web.services.tbr.notify.impl;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import org.easymock.IMockBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import wisematches.playground.RatingManager;
import wisematches.playground.propose.impl.DefaultGameProposal;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.server.web.services.notify.NotificationCreator;
import wisematches.server.web.services.tbr.notify.impl.publish.mail.MailNotificationPublisher;
import wisematches.server.web.services.notify.NotificationCondition;
import wisematches.server.web.services.notify.NotificationManager;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static org.easymock.EasyMock.*;

*/
/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 *//*

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
    MailNotificationPublisher notificationPublisher;

    @Autowired
    Configuration notificationFreemarkerConfig;

    @Autowired
    NotificationTransformer notificationTransformer;

    public MailNotificationFunctionalTest() {
    }

    @Test
    public void test() throws BoardLoadingException, InterruptedException, TemplateModelException, GameMoveException, ExecutionException {
        final MemberPlayer p1 = createMockPlayer(1001, Language.RU);
        final MemberPlayer p2 = createMockPlayer(1002, Language.EN);

        final PlayerManager playerManager = createMock(PlayerManager.class);
        expect(playerManager.getPlayer(1001)).andReturn(p1).anyTimes();
        expect(playerManager.getPlayer(1002)).andReturn(p2).anyTimes();
        replay(playerManager);

        final RatingManager ratingManager = createMock(RatingManager.class);
        expect(ratingManager.getRating(Personality.person(1001))).andReturn((short) 1234).anyTimes();
        expect(ratingManager.getRating(Personality.person(1002))).andReturn((short) 2122).anyTimes();
        replay(ratingManager);

        notificationFreemarkerConfig.setSharedVariable("playerManager", playerManager);
        notificationFreemarkerConfig.setSharedVariable("ratingManager", ratingManager);

        final NotificationSettings condition = new NotificationSettings();
        Collection<NotificationDescription> descriptions = notificationManager.getDescriptions();
        for (NotificationDescription description : descriptions) {
            condition.setEnabled(description.getName(), true);
        }
        notificationManager.setNotificationCondition(p1, condition);
        notificationManager.setNotificationCondition(p2, condition);

*/
/*
		final TilesBank tilesBank = new TilesBank(new TilesBankInfoEditor(Language.EN).add('A', 100, 1).createTilesBankInfo());
		final Dictionary dictionary = createNiceMock(Dictionary.class);

		final ScribbleBoard b1 = new ScribbleBoard(new ScribbleSettings("mock1", Language.RU, 3), Arrays.asList(p1, p2), tilesBank, dictionary);
		b1.makeMove(new PassTurnMove(b1.getPlayerTurn().getPlayerId()));

		final ScribbleBoard b2 = new ScribbleBoard(new ScribbleSettings("mock2", Language.EN, 5), Arrays.asList(p1, p2), tilesBank, dictionary);
		b2.makeMove(new PassTurnMove(b2.getPlayerTurn().getPlayerId()));

		notificationPublisher.raiseNotification("game.state.started", p1, NotificationSender.GAME, Collections.<String, Object>singletonMap("context", b1)).get();
		notificationPublisher.raiseNotification("game.state.started", p2, NotificationSender.GAME, Collections.<String, Object>singletonMap("context", b2)).get();

		notificationPublisher.raiseNotification("game.move.your", p1, NotificationSender.GAME, Collections.<String, Object>singletonMap("context", b1)).get();
		notificationPublisher.raiseNotification("game.move.your", p2, NotificationSender.GAME, Collections.<String, Object>singletonMap("context", b2)).get();

		notificationPublisher.raiseNotification("game.move.opponent", p1, NotificationSender.GAME, Collections.<String, Object>singletonMap("context", b1)).get();
		notificationPublisher.raiseNotification("game.move.opponent", p2, NotificationSender.GAME, Collections.<String, Object>singletonMap("context", b2)).get();

		notificationPublisher.raiseNotification("game.timeout.day", p1, NotificationSender.GAME, Collections.<String, Object>singletonMap("context", b1)).get();
		notificationPublisher.raiseNotification("game.timeout.day", p2, NotificationSender.GAME, Collections.<String, Object>singletonMap("context", b2)).get();

		notificationPublisher.raiseNotification("game.timeout.half", p1, NotificationSender.GAME, Collections.<String, Object>singletonMap("context", b1)).get();
		notificationPublisher.raiseNotification("game.timeout.half", p2, NotificationSender.GAME, Collections.<String, Object>singletonMap("context", b2)).get();

		notificationPublisher.raiseNotification("game.timeout.hour", p1, NotificationSender.GAME, Collections.<String, Object>singletonMap("context", b1)).get();
		notificationPublisher.raiseNotification("game.timeout.hour", p2, NotificationSender.GAME, Collections.<String, Object>singletonMap("context", b2)).get();

		b1.resign(b1.getPlayerHand(p1.getId()));
		b2.resign(b2.getPlayerHand(p1.getId()));
		notificationPublisher.raiseNotification("game.state.finished", p1, NotificationSender.GAME, Collections.<String, Object>singletonMap("context", b1)).get();
		notificationPublisher.raiseNotification("game.state.finished", p2, NotificationSender.GAME, Collections.<String, Object>singletonMap("context", b2)).get();

		final Message m = new HibernateMessage(p2, "asdqwe adf", p1);
		notificationPublisher.raiseNotification("game.message", p1, NotificationSender.SUPPORT, Collections.<String, Object>singletonMap("context", m)).get();
		notificationPublisher.raiseNotification("game.message", p2, NotificationSender.SUPPORT, Collections.<String, Object>singletonMap("context", m)).get();
*//*


        final DefaultGameProposal<ScribbleSettings> proposal1 = new DefaultGameProposal<ScribbleSettings>(12, "comment", new ScribbleSettings("mock1", Language.RU, 3), p1, new Personality[]{p2});
        notificationPublisher.raiseNotification("game.challenge.initiated", p1, NotificationSender.SUPPORT, Collections.<String, Object>singletonMap("context", proposal1)).get();
        notificationPublisher.raiseNotification("game.challenge.initiated", p2, NotificationSender.SUPPORT, Collections.<String, Object>singletonMap("context", proposal1)).get();

        final DefaultGameProposal<ScribbleSettings> proposal2 = new DefaultGameProposal<ScribbleSettings>(12, "comment", new ScribbleSettings("mock1", Language.RU, 3), p1, new Personality[]{p2});
        notificationPublisher.raiseNotification("game.challenge.rejected", p1, NotificationSender.SUPPORT, Collections.<String, Object>singletonMap("context", proposal2)).get();
        notificationPublisher.raiseNotification("game.challenge.rejected", p2, NotificationSender.SUPPORT, Collections.<String, Object>singletonMap("context", proposal2)).get();

        final DefaultGameProposal<ScribbleSettings> proposal3 = new DefaultGameProposal<ScribbleSettings>(12, "comment", new ScribbleSettings("mock1", Language.RU, 3), p1, new Personality[]{p2});
        notificationPublisher.raiseNotification("game.challenge.terminated", p1, NotificationSender.SUPPORT, Collections.<String, Object>singletonMap("context", proposal3)).get();
        notificationPublisher.raiseNotification("game.challenge.terminated", p2, NotificationSender.SUPPORT, Collections.<String, Object>singletonMap("context", proposal3)).get();

*/
/*
		notificationPublisher.raiseNotification("account.created", p1.getAccount(), NotificationSender.ACCOUNTS, null).get();
		notificationPublisher.raiseNotification("account.created", p2.getAccount(), NotificationSender.ACCOUNTS, null).get();

		final Map<String, Object> asd = new HashMap<String, Object>();
		asd.put("confirmationUrl", "a/a/w");
		asd.put("recoveryToken", "eeee");
		notificationPublisher.raiseNotification("account.recovery", p1.getAccount(), NotificationSender.ACCOUNTS, asd);
		notificationPublisher.raiseNotification("account.recovery", p2.getAccount(), NotificationSender.ACCOUNTS, asd);

		notificationPublisher.raiseNotification("account.updated", p1.getAccount(), NotificationSender.ACCOUNTS, null);
		notificationPublisher.raiseNotification("account.updated", p2.getAccount(), NotificationSender.ACCOUNTS, null);
*//*

    }

    private MemberPlayer createMockPlayer(long i, Language en) {
        IMockBuilder<Account> mockBuilder = createMockBuilder(Account.class);
        mockBuilder.withConstructor(i);

        final Account mock = mockBuilder.createMock("mock" + i);
        expect(mock.getLanguage()).andReturn(en).anyTimes();
        expect(mock.getNickname()).andReturn("mock" + i).anyTimes();
        expect(mock.getEmail()).andReturn("support@localhost").anyTimes();
        expect(mock.getMembership()).andReturn(Membership.BASIC).anyTimes();
        replay(mock);
        return new MemberPlayer(mock);
    }
}
*/
