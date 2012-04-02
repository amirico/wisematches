package wisematches.server.web.services.notify.impl.publish.reducer;

import org.junit.Test;
import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.NotificationCreator;
import wisematches.server.web.services.notify.publisher.NotificationPublisher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FilteringNotificationPublisherTest {
    public FilteringNotificationPublisherTest() {
    }

    @Test
    public void test() {
        final Account account = createMock(Account.class);
        final MemberPlayer player = new MemberPlayer(account);
        final Map<String, Object> model = new HashMap<String, Object>();

        final NotificationPublisher publisher = createStrictMock(NotificationPublisher.class);

        final FilteringNotificationPublisher p = new FilteringNotificationPublisher();
        p.setNotificationPublisher(publisher);
        p.setAcceptedNotifications(new HashSet<String>(Arrays.asList("game.state.finished", "game.challenge.initiated")));

        expect(publisher.raiseNotification("game.state.finished", account, NotificationCreator.ACCOUNTS, model)).andReturn(null);
        expect(publisher.raiseNotification("game.challenge.initiated", account, NotificationCreator.ACCOUNTS, model)).andReturn(null);
        replay(publisher);

        p.raiseNotification("asd", player, NotificationCreator.ACCOUNTS, model);
        p.raiseNotification("game.state.finished", player, NotificationCreator.ACCOUNTS, model);
        p.raiseNotification("game.challenge.initiated", player, NotificationCreator.ACCOUNTS, model);
        p.raiseNotification("qwe", player, NotificationCreator.ACCOUNTS, model);
        verify(publisher);
    }
}
