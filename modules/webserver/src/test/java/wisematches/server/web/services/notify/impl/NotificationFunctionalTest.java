package wisematches.server.web.services.notify.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageManager;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.server.web.services.notify.NotificationManager;
import wisematches.server.web.services.notify.impl.publish.mail.MailNotificationPublisher;
import wisematches.server.web.services.notify.impl.publish.message.MessageNotificationPublisher;

import javax.mail.internet.MimeMessage;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
//@Ignore
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
        final JavaMailSender mailSender = createMock(JavaMailSender.class);
        mailSender.send(isA(MimeMessage.class));
        mailSender.send(isA(MimeMessage.class));
        mailSender.send(isA(MimeMessage.class));
        mailSender.send(isA(MimeMessage.class));
        mailSender.send(isA(MimeMessage.class));
        mailSender.send(isA(MimeMessage.class));
        mailSender.send(isA(MimeMessage.class));
        mailSender.send(isA(MimeMessage.class));

        final Personality p = Personality.person(1001);

        final MessageManager messageManager = createMock(MessageManager.class);
        messageManager.sendNotification(p, "asd");
        messageManager.sendNotification(p, "qwe");
        replay(mailSender, messageManager);

        mailNotificationPublisher.setMailSender(mailSender);
        messageNotificationPublisher.setMessageManager(messageManager);

        final ScribbleBoard b1 = boardManager.openBoard(53);
        final ScribbleBoard b2 = boardManager.openBoard(54);

        notificationPublisherCenter.processNotification(p, "game.started", b2);
        notificationPublisherCenter.processNotification(p, "game.finished", b1);
        notificationPublisherCenter.processNotification(p, "game.move.your", b2);
        notificationPublisherCenter.processNotification(p, "game.move.opponent", b2);
        notificationPublisherCenter.processNotification(p, "game.timeout.day", b2);
        notificationPublisherCenter.processNotification(p, "game.timeout.half", b2);
        notificationPublisherCenter.processNotification(p, "game.timeout.hour", b2);
        notificationPublisherCenter.processNotification(p, "game.message", createMock(Message.class));

        Thread.sleep(4000);
        verify(mailSender, messageManager);
    }
}
