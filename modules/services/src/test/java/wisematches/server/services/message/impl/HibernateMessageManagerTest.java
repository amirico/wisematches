package wisematches.server.services.message.impl;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Membership;
import wisematches.core.Player;
import wisematches.core.personality.DefaultMember;
import wisematches.playground.restriction.impl.RestrictionDescription;
import wisematches.playground.restriction.impl.RestrictionManagerImpl;
import wisematches.server.services.message.Message;
import wisematches.server.services.message.MessageDirection;
import wisematches.server.services.message.MessageManager;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/properties-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/gameplay-config.xml",
		"classpath:/config/scribble-config.xml",
		"classpath:/config/wisematches-config.xml",
})
public class HibernateMessageManagerTest {
	@Autowired
	MessageManager messageManager;

	@Autowired
	SessionFactory sessionFactory;

	public HibernateMessageManagerTest() {
	}

	@Test
	public void testMessage() {
		final Player p1 = new DefaultMember(901, null, null, null, null, null);
		final Player p2 = new DefaultMember(902, null, null, null, null, null);

		assertEquals(0, messageManager.getTodayMessagesCount(p1, MessageDirection.RECEIVED));
		assertEquals(0, messageManager.getTodayMessagesCount(p1, MessageDirection.SENT));
		assertEquals(0, messageManager.getTodayMessagesCount(p2, MessageDirection.SENT));
		assertEquals(0, messageManager.getTodayMessagesCount(p2, MessageDirection.RECEIVED));

		assertEquals(0, messageManager.getTodayMessagesCount(p1, MessageDirection.RECEIVED));
		assertEquals(0, messageManager.getTodayMessagesCount(p1, MessageDirection.SENT));
		assertEquals(0, messageManager.getTodayMessagesCount(p2, MessageDirection.SENT));
		assertEquals(0, messageManager.getTodayMessagesCount(p2, MessageDirection.RECEIVED));

		assertEquals(0, messageManager.getNewMessagesCount(p1));
		assertEquals(0, messageManager.getNewMessagesCount(p2));

		messageManager.sendMessage(p1, p2, "B1");
		assertEquals(0, messageManager.getNewMessagesCount(p1));
		assertEquals(1, messageManager.getNewMessagesCount(p2));

		messageManager.sendMessage(p1, p2, "B2");
		assertEquals(0, messageManager.getNewMessagesCount(p1));
		assertEquals(2, messageManager.getNewMessagesCount(p2));

		messageManager.sendMessage(p2, p1, "B3");
		assertEquals(1, messageManager.getNewMessagesCount(p1));
		assertEquals(2, messageManager.getNewMessagesCount(p2));

		assertEquals(2, messageManager.getMessages(p1, MessageDirection.SENT).size());
		assertEquals(1, messageManager.getNewMessagesCount(p1));
		assertEquals(2, messageManager.getNewMessagesCount(p2));

		assertEquals(1, messageManager.getMessages(p2, MessageDirection.SENT).size());
		assertEquals(1, messageManager.getNewMessagesCount(p1));
		assertEquals(2, messageManager.getNewMessagesCount(p2));

		assertEquals(1, messageManager.getMessages(p1, MessageDirection.RECEIVED).size());
		assertEquals(1, messageManager.getNewMessagesCount(p1)); // READ_COMMITTED isolation.
		assertEquals(2, messageManager.getNewMessagesCount(p2));  // READ_COMMITTED isolation.

		assertEquals(2, messageManager.getMessages(p2, MessageDirection.RECEIVED).size());
		assertEquals(1, messageManager.getNewMessagesCount(p1));  // READ_COMMITTED isolation.
		assertEquals(2, messageManager.getNewMessagesCount(p2));  // READ_COMMITTED isolation.

		assertEquals(1, messageManager.getTodayMessagesCount(p2, MessageDirection.SENT));
		assertEquals(2, messageManager.getTodayMessagesCount(p1, MessageDirection.SENT));
		assertEquals(2, messageManager.getTodayMessagesCount(p2, MessageDirection.RECEIVED));
		assertEquals(1, messageManager.getTodayMessagesCount(p1, MessageDirection.RECEIVED));
	}

	@Test
	public void testNotifications() {
		final Player p1 = new DefaultMember(901, null, null, null, null, null);
		final Player p2 = new DefaultMember(902, null, null, null, null, null);

		messageManager.sendNotification(p1, "B1");
		messageManager.sendNotification(p1, "B2");
		messageManager.sendNotification(p2, "B3");

		assertEquals(2, messageManager.getMessages(p1, MessageDirection.RECEIVED).size());
		assertEquals(1, messageManager.getMessages(p2, MessageDirection.RECEIVED).size());

		final Object[] objects = messageManager.getMessages(p1, MessageDirection.RECEIVED).toArray();
		final Message m1 = (Message) objects[0];
		assertEquals(901, m1.getRecipient());
		assertEquals("B1", m1.getText());

		final Message m2 = (Message) objects[1];
		assertEquals(901, m2.getRecipient());
		assertEquals("B2", m2.getText());

		final Message m3 = (Message) messageManager.getMessages(p2, MessageDirection.RECEIVED).toArray()[0];
		assertEquals(902, m3.getRecipient());
		assertEquals("B3", m3.getText());

		messageManager.removeMessage(p2, m3.getId(), MessageDirection.RECEIVED);
		assertEquals(2, messageManager.getMessages(p1, MessageDirection.RECEIVED).size());
		assertEquals(0, messageManager.getMessages(p2, MessageDirection.RECEIVED).size());
		assertEquals(1, messageManager.getTodayMessagesCount(p2, MessageDirection.RECEIVED));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testCleanup() {
		final Map<Membership, Integer> a1 = new HashMap<>();
		a1.put(Membership.BASIC, 20);
		a1.put(Membership.SILVER, 40);
		a1.put(Membership.GOLD, 40);
		a1.put(Membership.PLATINUM, 40);

		final Map<Membership, Integer> a2 = new HashMap<>();
		a2.put(Membership.BASIC, 10);
		a2.put(Membership.SILVER, 20);
		a2.put(Membership.GOLD, 20);
		a2.put(Membership.PLATINUM, 20);

		final RestrictionManagerImpl r = new RestrictionManagerImpl();

		final List<RestrictionDescription<Integer>> a = new ArrayList<>();
		a.add(new RestrictionDescription.Integer("messages.hist.private", 0, a1));
		a.add(new RestrictionDescription.Integer("messages.hist.notice", 0, a2));

		r.setRestrictions(a);


		final HibernateMessageManager m = new HibernateMessageManager();
		m.setSessionFactory(sessionFactory);
		m.setRestrictionManager(r);

		m.cleanup(new Date());
	}
}
