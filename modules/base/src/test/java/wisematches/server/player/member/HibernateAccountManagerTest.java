package wisematches.server.player.member;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.player.*;
import wisematches.server.player.member.impl.HibernatePlayerImpl;

import java.util.UUID;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/test-server-base-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/server-base-config.xml"
})
public class HibernateAccountManagerTest {
	@Autowired
	private AccountManager accountManager;

	@Autowired
	private SessionFactory sessionFactory;

	@Test
	public void testCreateAccount() throws Exception {
		final AccountListener l = createStrictMock(AccountListener.class);
		l.accountCreated(isA(HibernatePlayerImpl.class));
		replay(l);

		accountManager.addAccountListener(l);
		try {
			createAccount();
			verify(l);
		} finally {
			accountManager.removeAccountListener(l);
		}
		sessionFactory.getCurrentSession().flush();
	}

	@Test
	public void testRemoveAccount() throws Exception {
		final AccountListener l = createStrictMock(AccountListener.class);
		l.accountRemove(isA(HibernatePlayerImpl.class));
		replay(l);

		final Player player1 = createAccount();
		final Player player2 = accountManager.getPlayer(player1.getId());

		assertEquals(player1, player2);
		accountManager.addAccountListener(l);
		try {
			accountManager.removePlayer(player1);
			accountManager.removePlayer(player2);

			assertNull(accountManager.getPlayer(player1.getId()));
			verify(l);
		} finally {
			accountManager.removeAccountListener(l);
		}
		sessionFactory.getCurrentSession().flush();
	}

	@Test
	public void testDuplicateUsername() throws Exception {
		final Player account = createAccount();

		final PlayerEditor editor = createMockEditor();
		editor.setNickname(account.getNickname());
		try {
			accountManager.createPlayer(editor.createPlayer());
			fail("DuplicateAccountException must be here");
		} catch (DuplicateAccountException ex) {
			assertEquals(1, ex.getFieldNames().size());
			assertTrue(ex.getFieldNames().contains("username"));
		}
		sessionFactory.getCurrentSession().flush();
	}

	@Test
	public void testDuplicateEMail() throws Exception {
		final Player account = createAccount();

		final PlayerEditor editor = createMockEditor();
		editor.setEmail(account.getEmail());
		try {
			accountManager.createPlayer(editor.createPlayer());
			fail("DuplicateAccountException must be here");
		} catch (DuplicateAccountException ex) {
			assertEquals(1, ex.getFieldNames().size());
			assertTrue(ex.getFieldNames().contains("email"));
		}
		sessionFactory.getCurrentSession().flush();
	}

	@Test
	public void testDuplicateBoth() throws Exception {
		final Player account = createAccount();

		final PlayerEditor editor = createMockEditor();
		editor.setNickname(account.getNickname());
		editor.setEmail(account.getEmail());
		try {
			accountManager.createPlayer(editor.createPlayer());
			fail("DuplicateAccountException must be here");
		} catch (DuplicateAccountException ex) {
			assertEquals(2, ex.getFieldNames().size());
			assertTrue(ex.getFieldNames().contains("username"));
			assertTrue(ex.getFieldNames().contains("email"));
		}
		sessionFactory.getCurrentSession().flush();
	}

	@Test
	public void testUpdateAccount() throws Exception {
		final PlayerListener l = createStrictMock(PlayerListener.class);
		l.playerUpdated(isA(Player.class), isA(HibernatePlayerImpl.class));
		replay(l);

		final Player p = createAccount();

		final PlayerEditor e = new PlayerEditor(p);
		e.setEmail("modified_" + e.getEmail());
		e.setPassword("modified_" + e.getPassword());
		e.setLanguage(Language.RUSSIAN);
		e.setMembership(Membership.PLATINUM);
		e.setRating(56456);

		accountManager.addPlayerListener(l);
		try {
			accountManager.updatePlayer(e.createPlayer());
			verify(l);
		} finally {
			accountManager.removePlayerListener(l);
		}

		final Player player = accountManager.getPlayer(p.getId());
		assertEquals(e.getEmail(), player.getEmail());
		assertEquals(e.getNickname(), player.getNickname());
		assertEquals(e.getPassword(), player.getPassword());
		assertEquals(e.getLanguage(), player.getLanguage());
		assertEquals(e.getMembership(), player.getMembership());
		assertEquals(e.getRating(), player.getRating());
		sessionFactory.getCurrentSession().flush();
	}

	private Player createAccount() throws Exception {
		final Player op = createMockEditor().createPlayer();
		final Player mock = accountManager.createPlayer(op);
		sessionFactory.getCurrentSession().flush();

		assertNotNull(mock);
		assertFalse(0 == mock.getId());
		assertEquals(op.getEmail(), mock.getEmail());
		assertEquals(op.getNickname(), mock.getNickname());
		assertEquals(op.getPassword(), mock.getPassword());
		assertEquals(Language.DEFAULT, mock.getLanguage());
		assertEquals(Membership.BASIC, mock.getMembership());
		assertEquals(1200, mock.getRating());
		return mock;
	}

	private PlayerEditor createMockEditor() {
		final String id = UUID.randomUUID().toString();
		return new PlayerEditor(id + "@wisematches.net", id, id + "_password");
	}
}
