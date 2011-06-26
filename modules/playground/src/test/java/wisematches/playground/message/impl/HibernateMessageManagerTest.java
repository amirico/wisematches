package wisematches.playground.message.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageManager;

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
    public void test() {
        final Personality p1 = Personality.person(998);
        final Personality p2 = Personality.person(999);

        messageManager.sendMessage(p1, "S1", "B1");
        messageManager.sendMessage(p1, "S2", "B2");
        messageManager.sendMessage(p2, "S3", "B3");

        assertEquals(2, messageManager.getMessages(p1).size());
        assertEquals(1, messageManager.getMessages(p2).size());

        final Object[] objects = messageManager.getMessages(p1).toArray();
        final Message m1 = (Message) objects[0];
        assertEquals(998, m1.getRecipient());
        assertEquals("S1", m1.getSubject());
        assertEquals("B1", m1.getBody());

        final Message m2 = (Message) objects[1];
        assertEquals(998, m2.getRecipient());
        assertEquals("S2", m2.getSubject());
        assertEquals("B2", m2.getBody());

        final Message m3 = (Message) messageManager.getMessages(p2).toArray()[0];
        assertEquals(999, m3.getRecipient());
        assertEquals("S3", m3.getSubject());
        assertEquals("B3", m3.getBody());

        messageManager.removeMessage(m3.getId());
        assertEquals(2, messageManager.getMessages(p1).size());
        assertEquals(0, messageManager.getMessages(p2).size());

        messageManager.clearMessages(p1);
        assertEquals(0, messageManager.getMessages(p1).size());
        assertEquals(0, messageManager.getMessages(p2).size());
    }
}
