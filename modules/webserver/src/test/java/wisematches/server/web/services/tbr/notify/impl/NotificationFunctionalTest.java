package wisematches.server.web.services.tbr.notify.impl;

import org.easymock.Capture;
import org.easymock.IMockBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.personality.player.Player;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageManager;
import wisematches.playground.propose.impl.DefaultGameProposal;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.impl.TilesBankInfoEditor;
import wisematches.server.web.services.tbr.notify.impl.publish.mail.MailNotificationPublisher;
import wisematches.server.web.services.tbr.notify.impl.publish.message.MessageNotificationPublisher;
import wisematches.server.web.services.notify.NotificationManager;

import java.util.Arrays;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Ignore
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
public class NotificationFunctionalTest {
    @Autowired
    ScribbleBoardManager boardManager;

    @Autowired
    MailNotificationPublisher mailNotificationPublisher;

    @Autowired
    MessageNotificationPublisher messageNotificationPublisher;

    @Autowired
    NotificationManager notificationManager;

    @Autowired
    NotificationPublisherCenter notificationPublisherCenter;

    public NotificationFunctionalTest() {
    }

    @Test
    public void test() throws BoardLoadingException, InterruptedException {
        final Player p1 = createMockPlayer(1029, Language.RU);
        final Player p2 = createMockPlayer(1030, Language.EN);

        final JavaMailSender mailSender = createStrictMock(JavaMailSender.class);
        mailSender.send(isA(MimeMessagePreparator.class));
        mailSender.send(isA(MimeMessagePreparator.class));
        mailSender.send(isA(MimeMessagePreparator.class));
        mailSender.send(isA(MimeMessagePreparator.class));
        mailSender.send(isA(MimeMessagePreparator.class));
        mailSender.send(isA(MimeMessagePreparator.class));
        mailSender.send(isA(MimeMessagePreparator.class));
        mailSender.send(isA(MimeMessagePreparator.class));

        final Capture<String> message1 = new Capture<String>();
        final Capture<String> message2 = new Capture<String>();

        final MessageManager messageManager = createStrictMock(MessageManager.class);
        messageManager.sendNotification(same(p1), capture(message1));
        messageManager.sendNotification(same(p1), capture(message2));
        replay(mailSender, messageManager);

        final TilesBank tilesBank = new TilesBank(new TilesBankInfoEditor(Language.EN).add('A', 100, 1).createTilesBankInfo());
        final Dictionary dictionary = createNiceMock(Dictionary.class);

        mailNotificationPublisher.setMailSender(mailSender);
        messageNotificationPublisher.setMessageManager(messageManager);

        final ScribbleBoard b1 = new ScribbleBoard(new ScribbleSettings("mock1", Language.RU, 3), Arrays.asList(p1, p2), tilesBank, dictionary);
        final ScribbleBoard b2 = new ScribbleBoard(new ScribbleSettings("mock2", Language.EN, 5), Arrays.asList(p1, p2), tilesBank, dictionary);

        notificationPublisherCenter.processNotification(p1, "game.state.started", b2);
        notificationPublisherCenter.processNotification(p1, "game.state.finished", b1);
        notificationPublisherCenter.processNotification(p1, "game.move.your", b2);
        notificationPublisherCenter.processNotification(p1, "game.move.opponent", b2);
        notificationPublisherCenter.processNotification(p1, "game.timeout.day", b2);
        notificationPublisherCenter.processNotification(p1, "game.timeout.half", b2);
        notificationPublisherCenter.processNotification(p1, "game.timeout.hour", b2);
        notificationPublisherCenter.processNotification(p1, "game.message", createMock(Message.class));
        notificationPublisherCenter.processNotification(p1, "game.challenge.initiated", new DefaultGameProposal<ScribbleSettings>(12, "comment", new ScribbleSettings("mock1", Language.RU, 3), p1, new Personality[]{p2}));
        notificationPublisherCenter.processNotification(p1, "game.challenge.rejected", new DefaultGameProposal<ScribbleSettings>(12, "comment", new ScribbleSettings("mock1", Language.RU, 3), p1, new Personality[]{p2}));
        notificationPublisherCenter.processNotification(p1, "game.challenge.terminated", new DefaultGameProposal<ScribbleSettings>(12, "comment", new ScribbleSettings("mock1", Language.RU, 3), p1, new Personality[]{p2}));

        Thread.sleep(1000);
        verify(mailSender, messageManager);
    }

    private MemberPlayer createMockPlayer(long i, Language en) {
        IMockBuilder<Account> mockBuilder = createMockBuilder(Account.class);
        mockBuilder.withConstructor(i);

        final Account mock = mockBuilder.createMock("mock" + i);
        expect(mock.getLanguage()).andReturn(en).anyTimes();
        expect(mock.getNickname()).andReturn("mock" + i).anyTimes();
        replay(mock);
        return new MemberPlayer(mock);
    }
}
