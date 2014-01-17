package wisematches.server.services.state.impl;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Language;
import wisematches.core.Player;
import wisematches.core.personality.DefaultVisitor;
import wisematches.core.secure.PlayerContainer;
import wisematches.server.services.state.PlayerStateListener;

import java.util.Date;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/properties-config.xml",
		"classpath:/config/database-config.xml"
})
public class HibernatePlayerStateManagerTest {
	@Autowired
	private SessionFactory sessionFactory;

	public HibernatePlayerStateManagerTest() {
	}

	@Test
	public void test() throws InterruptedException {
		final Player player1 = new DefaultVisitor(Language.RU);
		final Player player2 = new DefaultVisitor(Language.EN);

		final PlayerStateListener listener = createStrictMock(PlayerStateListener.class);

		final HibernatePlayerStateManager stateManager = new HibernatePlayerStateManager();
		stateManager.setSessionFactory(sessionFactory);

		stateManager.addPlayerStateListener(listener);

		listener.playerOnline(player1);
		listener.playerOnline(player2);
		listener.playerAlive(player1);
		listener.playerAlive(player1);
		listener.playerOffline(player2);
		listener.playerOffline(player1);
		replay(listener);

		Date lastActivity1 = stateManager.getLastActivityDate(player1);
		if (lastActivity1 == null) {
			lastActivity1 = new Date();
		}

		Date lastActivity2 = stateManager.getLastActivityDate(player2);
		if (lastActivity2 == null) {
			lastActivity2 = new Date();
		}

		assertFalse(stateManager.isPlayerOnline(player1));

		Thread.sleep(100);
		stateManager.registerNewSession("S1", createContainer(player1));
		assertTrue(stateManager.isPlayerOnline(player1));
		assertTrue(lastActivity1.before(lastActivity1 = stateManager.getLastActivityDate(player1)));

		Thread.sleep(100);
		stateManager.registerNewSession("S2", createContainer(player1));
		assertFalse(stateManager.isPlayerOnline(player2));
		assertTrue(lastActivity1.before(lastActivity1 = stateManager.getLastActivityDate(player1)));

		Thread.sleep(100);
		stateManager.registerNewSession("S3", createContainer(player2));
		assertTrue(stateManager.isPlayerOnline(player2));
		assertTrue(lastActivity2.before(lastActivity2 = stateManager.getLastActivityDate(player2)));

		Thread.sleep(100);
		stateManager.registerNewSession("S4", createContainer(player2));
		assertTrue(lastActivity2.before(lastActivity2 = stateManager.getLastActivityDate(player2)));

		Thread.sleep(100);
		stateManager.refreshLastRequest("S5"); // what is it?
		assertEquals(lastActivity1, stateManager.getLastActivityDate(player1));
		assertEquals(lastActivity2, stateManager.getLastActivityDate(player2));

		Thread.sleep(100);
		stateManager.refreshLastRequest("S1");
		assertTrue(lastActivity1.before(lastActivity1 = stateManager.getLastActivityDate(player1)));

		Thread.sleep(100);
		stateManager.refreshLastRequest("S1");
		assertTrue(lastActivity1.before(lastActivity1 = stateManager.getLastActivityDate(player1)));

		Thread.sleep(100);
		stateManager.removeSessionInformation("S5");
		assertTrue(stateManager.isPlayerOnline(player1));
		assertTrue(stateManager.isPlayerOnline(player2));
		assertEquals(lastActivity1, stateManager.getLastActivityDate(player1));
		assertEquals(lastActivity2, stateManager.getLastActivityDate(player2));

		Thread.sleep(100);
		stateManager.removeSessionInformation("S2");
		assertTrue(stateManager.isPlayerOnline(player1));
		assertEquals(lastActivity1, stateManager.getLastActivityDate(player1));
		assertEquals(lastActivity2, stateManager.getLastActivityDate(player2));

		Thread.sleep(100);
		stateManager.removeSessionInformation("S3");
		assertTrue(stateManager.isPlayerOnline(player2));
		assertEquals(lastActivity1, stateManager.getLastActivityDate(player1));
		assertEquals(lastActivity2, stateManager.getLastActivityDate(player2));

		Thread.sleep(100);
		stateManager.removeSessionInformation("S4");
		assertFalse(stateManager.isPlayerOnline(player2));
		assertEquals(lastActivity1, stateManager.getLastActivityDate(player1));
		assertEquals(lastActivity2, stateManager.getLastActivityDate(player2));

		Thread.sleep(100);
		stateManager.removeSessionInformation("S1");
		assertFalse(stateManager.isPlayerOnline(player1));
		assertEquals(lastActivity1, stateManager.getLastActivityDate(player1));
		assertEquals(lastActivity2, stateManager.getLastActivityDate(player2));

		verify(listener);
	}

	private PlayerContainer createContainer(Player player) {
		PlayerContainer container = createMock(PlayerContainer.class);
		expect(container.getPlayer()).andReturn(player).anyTimes();
		replay(container);
		return container;
	}
}
