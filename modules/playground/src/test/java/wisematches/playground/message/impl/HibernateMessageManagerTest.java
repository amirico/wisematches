package wisematches.playground.message.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Membership;
import wisematches.personality.Personality;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageManager;
import wisematches.playground.restriction.RestrictionDescription;
import wisematches.playground.restriction.impl.RestrictionManagerImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/config/database-junit-config.xml",
        "classpath:/config/accounts-config.xml",
        "classpath:/config/playground-config.xml"
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

        assertEquals(0, messageManager.getTodayMessagesCount(p1));
        assertEquals(0, messageManager.getTodayMessagesCount(p2));

        messageManager.sendMessage(p1, p2, "B1");
        messageManager.sendMessage(p1, p2, "B2");
        messageManager.sendMessage(p2, p1, "B3");

        assertEquals(1, messageManager.getMessages(p1).size());
        assertEquals(2, messageManager.getMessages(p2).size());
        assertEquals(1, messageManager.getTodayMessagesCount(p2));
        assertEquals(2, messageManager.getTodayMessagesCount(p1));
    }

    @Test
    public void testNotifications() {
        final Personality p1 = Personality.person(998);
        final Personality p2 = Personality.person(999);

        messageManager.sendNotification(p1, "B1");
        messageManager.sendNotification(p1, "B2");
        messageManager.sendNotification(p2, "B3");

        assertEquals(2, messageManager.getMessages(p1).size());
        assertEquals(1, messageManager.getMessages(p2).size());

        final Object[] objects = messageManager.getMessages(p1).toArray();
        final Message m1 = (Message) objects[0];
        assertEquals(998, m1.getRecipient());
        assertEquals("B1", m1.getText());

        final Message m2 = (Message) objects[1];
        assertEquals(998, m2.getRecipient());
        assertEquals("B2", m2.getText());

        final Message m3 = (Message) messageManager.getMessages(p2).toArray()[0];
        assertEquals(999, m3.getRecipient());
        assertEquals("B3", m3.getText());

        messageManager.removeMessage(m3.getId());
        assertEquals(2, messageManager.getMessages(p1).size());
        assertEquals(0, messageManager.getMessages(p2).size());

        messageManager.clearMessages(p1);
        assertEquals(0, messageManager.getMessages(p1).size());
        assertEquals(0, messageManager.getMessages(p2).size());
    }


    @Test
    public void testCleanup() {
        final Map<Membership, Comparable> a1 = new HashMap<Membership, Comparable>();
        a1.put(Membership.GUEST, 0);
        a1.put(Membership.BASIC, 20);
        a1.put(Membership.SILVER, 40);
        a1.put(Membership.GOLD, 40);
        a1.put(Membership.PLATINUM, 40);

        final Map<Membership, Comparable> a2 = new HashMap<Membership, Comparable>();
        a2.put(Membership.GUEST, 0);
        a2.put(Membership.BASIC, 10);
        a2.put(Membership.SILVER, 20);
        a2.put(Membership.GOLD, 20);
        a2.put(Membership.PLATINUM, 20);

        final RestrictionManagerImpl r = new RestrictionManagerImpl();
        r.setRestrictions(Arrays.asList(
                new RestrictionDescription("messages.hist.private", a1),
                new RestrictionDescription("messages.hist.notice", a2)));

/*
        final HibernateTemplate template = createMock(HibernateTemplate.class);
        expect(template.bulkUpdate("DELETE m FROM player_message as m INNER JOIN account_personality as a ON a.id=m.recipient and " +
                "((m.notification and " +
                "(a.membership = 'GUEST' and created < DATE_SUB(curdate(), INTERVAL 0 DAY)) or " +
                "(a.membership = 'BASIC' and created < DATE_SUB(curdate(), INTERVAL 20 DAY)) or " +
                "(a.membership = 'SILVER' and created < DATE_SUB(curdate(), INTERVAL 40 DAY)) or " +
                "(a.membership = 'GOLD' and created < DATE_SUB(curdate(), INTERVAL 40 DAY)) or " +
                "(a.membership = 'PLATINUM' and created < DATE_SUB(curdate(), INTERVAL 40 DAY))) or " +
                "(not m.notification and " +
                "(a.membership = 'GUEST' and created < DATE_SUB(curdate(), INTERVAL 0 DAY)) or " +
                "(a.membership = 'BASIC' and created < DATE_SUB(curdate(), INTERVAL 10 DAY)) or " +
                "(a.membership = 'SILVER' and created < DATE_SUB(curdate(), INTERVAL 20 DAY)) or " +
                "(a.membership = 'GOLD' and created < DATE_SUB(curdate(), INTERVAL 20 DAY)) or " +
                "(a.membership = 'PLATINUM' and created < DATE_SUB(curdate(), INTERVAL 20 DAY))))")).andReturn(0);
        replay(template);
*/

        final HibernateMessageManager m = new HibernateMessageManager();
        m.setRestrictionManager(r);
//        m.setHibernateTemplate(template);

        m.cleanup();

//        verify(template);
    }
}
