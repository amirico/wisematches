package wisematches.server.personality.account.impl;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.personality.account.*;

import java.util.TimeZone;
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

	public HibernateAccountManagerTest() {
	}

	@Test
	public void testCreateAccount() throws Exception {
		final AccountListener l = createStrictMock(AccountListener.class);
		l.accountCreated(isA(HibernateAccountImpl.class));
		replay(l);

		accountManager.addAccountListener(l);
		try {
			createAccount();
			verify(l);
		} finally {
			accountManager.removeAccountListener(l);
		}
	}

	@Test
	public void testRemoveAccount() throws Exception {
		final AccountListener l = createStrictMock(AccountListener.class);
		l.accountRemove(isA(HibernateAccountImpl.class));
		replay(l);

		final Account player1 = createAccount();
		final Account player2 = accountManager.getAccount(player1.getId());

		assertEquals(player1, player2);
		accountManager.addAccountListener(l);
		try {
			accountManager.removeAccount(player1);
			accountManager.removeAccount(player2);

			assertNull(accountManager.getAccount(player1.getId()));
			verify(l);
		} finally {
			accountManager.removeAccountListener(l);
		}
	}

	@Test
	public void testDuplicateUsername() throws Exception {
		final Account account = createAccount();

		final AccountEditor editor = createMockEditor();
		editor.setNickname(account.getNickname());
		try {
			accountManager.createAccount(editor.createAccount());
			fail("DuplicateAccountException must be here");
		} catch (DuplicateAccountException ex) {
			assertEquals(1, ex.getFieldNames().size());
			assertTrue(ex.getFieldNames().contains("username"));
		}
	}

	@Test
	public void testDuplicateEMail() throws Exception {
		final Account account = createAccount();

		final AccountEditor editor = createMockEditor();
		editor.setEmail(account.getEmail());
		try {
			accountManager.createAccount(editor.createAccount());
			fail("DuplicateAccountException must be here");
		} catch (DuplicateAccountException ex) {
			assertEquals(1, ex.getFieldNames().size());
			assertTrue(ex.getFieldNames().contains("email"));
		}
	}

	@Test
	public void testDuplicateBoth() throws Exception {
		final Account account = createAccount();

		final AccountEditor editor = createMockEditor();
		editor.setNickname(account.getNickname());
		editor.setEmail(account.getEmail());
		try {
			accountManager.createAccount(editor.createAccount());
			fail("DuplicateAccountException must be here");
		} catch (DuplicateAccountException ex) {
			assertEquals(2, ex.getFieldNames().size());
			assertTrue(ex.getFieldNames().contains("username"));
			assertTrue(ex.getFieldNames().contains("email"));
		}
	}

	@Test
	public void testUpdateAccount() throws Exception {
		final AccountListener l = createStrictMock(AccountListener.class);
		l.accountUpdated(isA(Account.class), isA(HibernateAccountImpl.class));
		replay(l);

		final Account p = createAccount();

		final AccountEditor e = new AccountEditor(p);
		e.setEmail("modified_" + e.getEmail());
		e.setPassword("modified_" + e.getPassword());
		e.setLanguage(Language.RU);
		e.setMembership(Membership.PLATINUM);
		e.setTimeZone(TimeZone.getTimeZone("GMT+04:12"));

		accountManager.addAccountListener(l);
		try {
			accountManager.updateAccount(e.createAccount());
			verify(l);
		} finally {
			accountManager.removeAccountListener(l);
		}

		final Account player = accountManager.getAccount(p.getId());
		assertEquals(e.getEmail(), player.getEmail());
		assertEquals(e.getNickname(), player.getNickname());
		assertEquals(e.getPassword(), player.getPassword());
		assertEquals(e.getLanguage(), player.getLanguage());
		assertEquals(e.getMembership(), player.getMembership());
		assertEquals(e.getTimeZone(), player.getTimeZone());
		assertEquals(TimeZone.getTimeZone("GMT+04:12"), player.getTimeZone());
	}

	private Account createAccount() throws Exception {
		final Account op = createMockEditor().createAccount();
		final Account mock = accountManager.createAccount(op);

		assertNotNull(mock);
		assertFalse(0 == mock.getId());
		assertEquals(op.getEmail(), mock.getEmail());
		assertEquals(op.getNickname(), mock.getNickname());
		assertEquals(op.getPassword(), mock.getPassword());
		assertEquals(Language.DEFAULT, mock.getLanguage());
		assertEquals(Membership.BASIC, mock.getMembership());
		assertEquals(TimeZone.getDefault(), mock.getTimeZone());
		return mock;
	}

	private AccountEditor createMockEditor() {
		final String id = UUID.randomUUID().toString();
		return new AccountEditor(id + "@wm.net", id, id + "_password");
	}
}
