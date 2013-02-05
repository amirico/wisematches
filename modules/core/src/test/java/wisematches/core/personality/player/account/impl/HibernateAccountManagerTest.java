package wisematches.core.personality.player.account.impl;

import org.easymock.EasyMock;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Language;
import wisematches.core.personality.player.account.*;

import java.util.TimeZone;
import java.util.UUID;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/personality-junit-config.xml"
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
		final AccountListener l = EasyMock.createStrictMock(AccountListener.class);
		l.accountCreated(EasyMock.isA(HibernateAccountImpl.class));
		EasyMock.replay(l);

		accountManager.addAccountListener(l);
		try {
			createAccount();
			EasyMock.verify(l);
		} finally {
			accountManager.removeAccountListener(l);
		}
	}

	@Test
	public void testRemoveAccount() throws Exception {
		final AccountListener l = EasyMock.createStrictMock(AccountListener.class);
		l.accountRemove(EasyMock.isA(HibernateAccountImpl.class));
		EasyMock.replay(l);

		final Account player1 = createAccount();
		final Account player2 = accountManager.getAccount(player1.getId());

		Assert.assertEquals(player1, player2);
		accountManager.addAccountListener(l);
		try {
			accountManager.removeAccount(player1);
			accountManager.removeAccount(player2);

			Assert.assertNull(accountManager.getAccount(player1.getId()));
			EasyMock.verify(l);
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
			Assert.fail("DuplicateAccountException must be here");
		} catch (DuplicateAccountException ex) {
			Assert.assertEquals(1, ex.getFieldNames().size());
			Assert.assertTrue(ex.getFieldNames().contains("username"));
		}

		final AccountEditor editor2 = createMockEditor();
		editor2.setNickname(account.getNickname().toUpperCase());
		try {
			accountManager.createAccount(editor2.createAccount());
			Assert.fail("DuplicateAccountException must be here");
		} catch (DuplicateAccountException ex) {
			Assert.assertEquals(1, ex.getFieldNames().size());
			Assert.assertTrue(ex.getFieldNames().contains("username"));
		}
	}

	@Test
	public void testDuplicateEMail() throws Exception {
		final Account account = createAccount();

		final AccountEditor editor = createMockEditor();
		editor.setEmail(account.getEmail());
		try {
			accountManager.createAccount(editor.createAccount());
			Assert.fail("DuplicateAccountException must be here");
		} catch (DuplicateAccountException ex) {
			Assert.assertEquals(1, ex.getFieldNames().size());
			Assert.assertTrue(ex.getFieldNames().contains("email"));
		}

		final AccountEditor editor2 = createMockEditor();
		editor2.setEmail(account.getEmail().toUpperCase());
		try {
			accountManager.createAccount(editor2.createAccount());
			Assert.fail("DuplicateAccountException must be here");
		} catch (DuplicateAccountException ex) {
			Assert.assertEquals(1, ex.getFieldNames().size());
			Assert.assertTrue(ex.getFieldNames().contains("email"));
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
			Assert.fail("DuplicateAccountException must be here");
		} catch (DuplicateAccountException ex) {
			Assert.assertEquals(2, ex.getFieldNames().size());
			Assert.assertTrue(ex.getFieldNames().contains("username"));
			Assert.assertTrue(ex.getFieldNames().contains("email"));
		}

		final AccountEditor editor2 = createMockEditor();
		editor2.setNickname(account.getNickname().toUpperCase());
		editor2.setEmail(account.getEmail().toUpperCase());
		try {
			accountManager.createAccount(editor2.createAccount());
			Assert.fail("DuplicateAccountException must be here");
		} catch (DuplicateAccountException ex) {
			Assert.assertEquals(2, ex.getFieldNames().size());
			Assert.assertTrue(ex.getFieldNames().contains("username"));
			Assert.assertTrue(ex.getFieldNames().contains("email"));
		}
	}

	@Test
	public void testUpdateAccount() throws Exception {
		final AccountListener l = EasyMock.createStrictMock(AccountListener.class);
		l.accountUpdated(EasyMock.isA(Account.class), EasyMock.isA(HibernateAccountImpl.class));
		EasyMock.replay(l);

		final Account p = createAccount();

		final AccountEditor e = new AccountEditor(p);
		e.setEmail("modified_" + e.getEmail());
		e.setPassword("modified_" + e.getPassword());
		e.setLanguage(Language.RU);
		e.setTimeZone(TimeZone.getTimeZone("GMT+04:12"));

		accountManager.addAccountListener(l);
		try {
			accountManager.updateAccount(e.createAccount());
			EasyMock.verify(l);
		} finally {
			accountManager.removeAccountListener(l);
		}

		final Account player = accountManager.getAccount(p.getId());
		Assert.assertEquals(e.getEmail(), player.getEmail());
		Assert.assertEquals(e.getNickname(), player.getNickname());
		Assert.assertEquals(e.getPassword(), player.getPassword());
		Assert.assertEquals(e.getLanguage(), player.getLanguage());
		Assert.assertEquals(e.getTimeZone(), player.getTimeZone());
		Assert.assertEquals(TimeZone.getTimeZone("GMT+04:12"), player.getTimeZone());
	}

	private Account createAccount() throws Exception {
		final Account op = createMockEditor().createAccount();
		final Account mock = accountManager.createAccount(op);

		Assert.assertNotNull(mock);
		Assert.assertFalse(0 == mock.getId());
		Assert.assertEquals(op.getEmail(), mock.getEmail());
		Assert.assertEquals(op.getNickname(), mock.getNickname());
		Assert.assertEquals(op.getPassword(), mock.getPassword());
		Assert.assertEquals(Language.DEFAULT, mock.getLanguage());
		Assert.assertEquals(TimeZone.getDefault(), mock.getTimeZone());
		return mock;
	}

	private AccountEditor createMockEditor() {
		final String id = UUID.randomUUID().toString();
		return new AccountEditor(id + "@wm.net", id, id + "_password");
	}
}
