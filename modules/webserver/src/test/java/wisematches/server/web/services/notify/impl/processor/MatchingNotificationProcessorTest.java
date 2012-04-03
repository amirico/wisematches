package wisematches.server.web.services.notify.impl.processor;

import org.junit.Test;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationCreator;
import wisematches.server.web.services.notify.NotificationProcessor;
import wisematches.server.web.services.notify.NotificationTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MatchingNotificationProcessorTest {
    public MatchingNotificationProcessorTest() {
    }

    @Test
    public void asd() throws Exception {
        final Account account = createMock(Account.class);
        final Map<String, Object> model = new HashMap<String, Object>();

        final NotificationProcessor processor = createStrictMock(DefaultNotificationProcessor.class);
        expect(processor.publishNotification(new NotificationTemplate("game.state.finished", "asd", account, NotificationCreator.ACCOUNTS, model))).andReturn(true);
        expect(processor.publishNotification(new NotificationTemplate("game.state.initiated", "asd", account, NotificationCreator.ACCOUNTS, model))).andReturn(false);
        replay(processor);

        final MatchingNotificationProcessor p = new MatchingNotificationProcessor();
        p.setNotificationProcessor(processor);
        p.setAllowedNotifications(new HashSet<String>(Arrays.asList("game.state.finished", "game.challenge.initiated")));

        p.publishNotification(new NotificationTemplate("asd", "asd", account, NotificationCreator.ACCOUNTS));
        p.publishNotification(new NotificationTemplate("game.state.finished", "asd", account, NotificationCreator.ACCOUNTS));
        p.publishNotification(new NotificationTemplate("game.challenge.initiated", "asd", account, NotificationCreator.ACCOUNTS));
        p.publishNotification(new NotificationTemplate("qwe", "asd", account, NotificationCreator.ACCOUNTS));
        verify(processor);
    }
}
