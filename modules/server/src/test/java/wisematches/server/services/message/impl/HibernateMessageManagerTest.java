package wisematches.server.services.message.impl;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Personality;
import wisematches.core.personality.member.Membership;
import wisematches.playground.restriction.impl.RestrictionDescription;
import wisematches.playground.restriction.impl.RestrictionManagerImpl;
import wisematches.server.services.message.Message;
import wisematches.server.services.message.MessageDirection;
import wisematches.server.services.message.MessageManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/accounts-config.xml",
		"classpath:/config/playground-junit-config.xml"
})
public class HibernateMessageManagerTest {
	@Autowired
	MessageManager messageManager;

	public HibernateMessageManagerTest() {
	}

	@Test
	public void testMessage() {
		final Personality p1 = Personality.person(998);
		final Personality p2 = Personality.person(999);

		Assert.assertEquals(0, messageManager.getTodayMessagesCount(p1, MessageDirection.RECEIVED));
		Assert.assertEquals(0, messageManager.getTodayMessagesCount(p1, MessageDirection.SENT));
		Assert.assertEquals(0, messageManager.getTodayMessagesCount(p2, MessageDirection.SENT));
		Assert.assertEquals(0, messageManager.getTodayMessagesCount(p2, MessageDirection.RECEIVED));

		Assert.assertEquals(0, messageManager.getTodayMessagesCount(p1, MessageDirection.RECEIVED));
		Assert.assertEquals(0, messageManager.getTodayMessagesCount(p1, MessageDirection.SENT));
		Assert.assertEquals(0, messageManager.getTodayMessagesCount(p2, MessageDirection.SENT));
		Assert.assertEquals(0, messageManager.getTodayMessagesCount(p2, MessageDirection.RECEIVED));

		Assert.assertEquals(0, messageManager.getNewMessagesCount(p1));
		Assert.assertEquals(0, messageManager.getNewMessagesCount(p2));

		messageManager.sendMessage(p1, p2, "B1");
		Assert.assertEquals(0, messageManager.getNewMessagesCount(p1));
		Assert.assertEquals(1, messageManager.getNewMessagesCount(p2));

		messageManager.sendMessage(p1, p2, "B2");
		Assert.assertEquals(0, messageManager.getNewMessagesCount(p1));
		Assert.assertEquals(2, messageManager.getNewMessagesCount(p2));

		messageManager.sendMessage(p2, p1, "B3");
		Assert.assertEquals(1, messageManager.getNewMessagesCount(p1));
		Assert.assertEquals(2, messageManager.getNewMessagesCount(p2));

		Assert.assertEquals(2, messageManager.getMessages(p1, MessageDirection.SENT).size());
		Assert.assertEquals(1, messageManager.getNewMessagesCount(p1));
		Assert.assertEquals(2, messageManager.getNewMessagesCount(p2));

		Assert.assertEquals(1, messageManager.getMessages(p2, MessageDirection.SENT).size());
		Assert.assertEquals(1, messageManager.getNewMessagesCount(p1));
		Assert.assertEquals(2, messageManager.getNewMessagesCount(p2));

		Assert.assertEquals(1, messageManager.getMessages(p1, MessageDirection.RECEIVED).size());
		Assert.assertEquals(1, messageManager.getNewMessagesCount(p1)); // READ_COMMITTED isolation.
		Assert.assertEquals(2, messageManager.getNewMessagesCount(p2));  // READ_COMMITTED isolation.

		Assert.assertEquals(2, messageManager.getMessages(p2, MessageDirection.RECEIVED).size());
		Assert.assertEquals(1, messageManager.getNewMessagesCount(p1));  // READ_COMMITTED isolation.
		Assert.assertEquals(2, messageManager.getNewMessagesCount(p2));  // READ_COMMITTED isolation.

		Assert.assertEquals(1, messageManager.getTodayMessagesCount(p2, MessageDirection.SENT));
		Assert.assertEquals(2, messageManager.getTodayMessagesCount(p1, MessageDirection.SENT));
		Assert.assertEquals(2, messageManager.getTodayMessagesCount(p2, MessageDirection.RECEIVED));
		Assert.assertEquals(1, messageManager.getTodayMessagesCount(p1, MessageDirection.RECEIVED));
	}

	@Test
	public void testNotifications() {
		final Personality p1 = Personality.person(998);
		final Personality p2 = Personality.person(999);

		messageManager.sendNotification(p1, "B1");
		messageManager.sendNotification(p1, "B2");
		messageManager.sendNotification(p2, "B3");

		Assert.assertEquals(2, messageManager.getMessages(p1, MessageDirection.RECEIVED).size());
		Assert.assertEquals(1, messageManager.getMessages(p2, MessageDirection.RECEIVED).size());

		final Object[] objects = messageManager.getMessages(p1, MessageDirection.RECEIVED).toArray();
		final Message m1 = (Message) objects[0];
		Assert.assertEquals(998, m1.getRecipient());
		Assert.assertEquals("B1", m1.getText());

		final Message m2 = (Message) objects[1];
		Assert.assertEquals(998, m2.getRecipient());
		Assert.assertEquals("B2", m2.getText());

		final Message m3 = (Message) messageManager.getMessages(p2, MessageDirection.RECEIVED).toArray()[0];
		Assert.assertEquals(999, m3.getRecipient());
		Assert.assertEquals("B3", m3.getText());

		messageManager.removeMessage(p2, m3.getId(), MessageDirection.RECEIVED);
		Assert.assertEquals(2, messageManager.getMessages(p1, MessageDirection.RECEIVED).size());
		Assert.assertEquals(0, messageManager.getMessages(p2, MessageDirection.RECEIVED).size());
		Assert.assertEquals(1, messageManager.getTodayMessagesCount(p2, MessageDirection.RECEIVED));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testCleanup() {
		final Map<Membership, Comparable<Integer>> a1 = new HashMap<>();
		a1.put(Membership.DEFAULT, 20);
		a1.put(Membership.SILVER, 40);
		a1.put(Membership.GOLD, 40);
		a1.put(Membership.PLATINUM, 40);

		final Map<Membership, Comparable<Integer>> a2 = new HashMap<>();
		a2.put(Membership.DEFAULT, 10);
		a2.put(Membership.SILVER, 20);
		a2.put(Membership.GOLD, 20);
		a2.put(Membership.PLATINUM, 20);

		final RestrictionManagerImpl r = new RestrictionManagerImpl();
		r.setRestrictions(Arrays.asList(
				new RestrictionDescription<>("messages.hist.private", 0, a1),
				new RestrictionDescription<>("messages.hist.notice", 0, a2)));

		final SQLQuery query = EasyMock.createMock(SQLQuery.class);
		EasyMock.expect(query.executeUpdate()).andReturn(1);
		EasyMock.replay(query);

		final Session session = EasyMock.createMock(Session.class);
		EasyMock.expect(session.createSQLQuery(EasyMock.isA(String.class))).andReturn(query);
		EasyMock.replay(session);

		final SessionFactory sessionFactory = EasyMock.createMock(SessionFactory.class);
		EasyMock.expect(sessionFactory.getCurrentSession()).andReturn(session);
		EasyMock.replay(sessionFactory);
/*
		expect(template.bulkUpdate("DELETE m FROM player_message as m INNER JOIN account_personality as a ON a.id=m.recipient and " +
                "((m.notification and " +
                "(a.membership = 'GUEST' and created < DATE_SUB(curdate(), INTERVAL 0 DAY)) or " +
                "(a.membership = 'DEFAULT' and created < DATE_SUB(curdate(), INTERVAL 20 DAY)) or " +
                "(a.membership = 'SECOND' and created < DATE_SUB(curdate(), INTERVAL 40 DAY)) or " +
                "(a.membership = 'FIRST' and created < DATE_SUB(curdate(), INTERVAL 40 DAY)) or " +
                "(a.membership = 'PLATINUM' and created < DATE_SUB(curdate(), INTERVAL 40 DAY))) or " +
                "(not m.notification and " +
                "(a.membership = 'GUEST' and created < DATE_SUB(curdate(), INTERVAL 0 DAY)) or " +
                "(a.membership = 'DEFAULT' and created < DATE_SUB(curdate(), INTERVAL 10 DAY)) or " +
                "(a.membership = 'SECOND' and created < DATE_SUB(curdate(), INTERVAL 20 DAY)) or " +
                "(a.membership = 'FIRST' and created < DATE_SUB(curdate(), INTERVAL 20 DAY)) or " +
                "(a.membership = 'PLATINUM' and created < DATE_SUB(curdate(), INTERVAL 20 DAY))))")).andReturn(0);
*/

		final HibernateMessageManager m = new HibernateMessageManager();
		m.setSessionFactory(sessionFactory);
		m.setRestrictionManager(r);

		m.cleanup();

		EasyMock.verify(sessionFactory, session, query);
	}
}
