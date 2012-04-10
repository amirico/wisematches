package wisematches.server.web.services.notify.impl.publisher;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.junit.Test;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationCreator;
import wisematches.server.web.services.notify.NotificationPublisher;
import wisematches.server.web.services.notify.NotificationTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static junit.framework.Assert.*;
import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MatchingNotificationProcessorTest {
    public MatchingNotificationProcessorTest() {
    }

    @Test
    public void testPublishNotification() throws Exception {
        final Account account = createMock(Account.class);
        final Map<String, Object> model = new HashMap<String, Object>();

        final Capture<NotificationTemplate> capture = new Capture<NotificationTemplate>(CaptureType.ALL);

        final NotificationPublisher publisher = createStrictMock(DefaultNotificationPublisher.class);
        expect(publisher.publishNotification(capture(capture))).andReturn(true);
        expect(publisher.publishNotification(capture(capture))).andReturn(false);
        replay(publisher);

        final MatchingNotificationPublisher p = new MatchingNotificationPublisher();
        p.setNotificationPublisher(publisher);
        p.setAllowedNotifications(new HashSet<String>(Arrays.asList("game.state.finished", "game.challenge.initiated")));

        assertFalse(p.publishNotification(new NotificationTemplate("asd", "asd", account, NotificationCreator.ACCOUNTS)));
        assertTrue(p.publishNotification(new NotificationTemplate("game.state.finished", "asd", account, NotificationCreator.ACCOUNTS)));
        assertFalse(p.publishNotification(new NotificationTemplate("game.challenge.initiated", "asd", account, NotificationCreator.ACCOUNTS, model)));
        assertFalse(p.publishNotification(new NotificationTemplate("qwe", "asd", account, NotificationCreator.ACCOUNTS)));

        assertTemplate("game.state.finished", "asd", account, NotificationCreator.ACCOUNTS, null, capture.getValues().get(0));
        assertTemplate("game.challenge.initiated", "asd", account, NotificationCreator.ACCOUNTS, model, capture.getValues().get(1));

        verify(publisher);
    }

    private void assertTemplate(String code, String template, Account account, NotificationCreator creator, Object model, NotificationTemplate notificationTemplate) {
        assertEquals(code, notificationTemplate.getCode());
        assertEquals(template, notificationTemplate.getTemplate());
        assertEquals(account, notificationTemplate.getRecipient());
        assertEquals(creator, notificationTemplate.getCreator());
        assertEquals(model, notificationTemplate.getContext());
    }
}
