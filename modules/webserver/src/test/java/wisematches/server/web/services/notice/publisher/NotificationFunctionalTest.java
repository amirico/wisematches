package wisematches.server.web.services.notice.publisher;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.message.Message;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.server.web.services.notice.NotificationManager;

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
		"classpath:/config/mail-sender-config.xml",
		"classpath:/config/server-web-config.xml"
})
public class NotificationFunctionalTest {
	@Autowired
	ScribbleBoardManager boardManager;

	@Autowired
	NotificationManager notificationManager;

	@Autowired
	NotificationPublishCenter publishCenter;

	public NotificationFunctionalTest() {
	}

	@Test
	public void asd() throws BoardLoadingException, InterruptedException {
		final Personality p = Personality.person(1029);

		final ScribbleBoard b1 = boardManager.openBoard(53);
		final ScribbleBoard b2 = boardManager.openBoard(54);

		publishCenter.processNotification(new Notification(p, notificationManager.getDescription("game.started"), b2));
		publishCenter.processNotification(new Notification(p, notificationManager.getDescription("game.finished"), b1));
		publishCenter.processNotification(new Notification(p, notificationManager.getDescription("game.move.your"), b2));
		publishCenter.processNotification(new Notification(p, notificationManager.getDescription("game.move.opponent"), b2));
		publishCenter.processNotification(new Notification(p, notificationManager.getDescription("game.timeout.day"), b2));
		publishCenter.processNotification(new Notification(p, notificationManager.getDescription("game.timeout.half"), b2));
		publishCenter.processNotification(new Notification(p, notificationManager.getDescription("game.timeout.hour"), b2));
		publishCenter.processNotification(new Notification(p, notificationManager.getDescription("game.message"), new Message(p, "Mock body")));

		Thread.sleep(1000000);
	}
}
