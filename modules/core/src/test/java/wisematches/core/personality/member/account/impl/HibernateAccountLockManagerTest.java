package wisematches.core.personality.member.account.impl;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.personality.member.account.*;

import java.util.Date;
import java.util.UUID;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/accounts-config.xml"
})
public class HibernateAccountLockManagerTest {
	@Autowired
	private AccountLockManager accountLockManager;

	@Autowired
	private AccountManager accountManager;

	private Account player;

	@Autowired
	private SessionFactory sessionFactory;

	private AccountLockListener accountLockListener;
	private AccountNicknameLockListener listenerAccountNickname;

	public HibernateAccountLockManagerTest() {
	}

	@Before
	public void createAccount() throws Exception {
		UUID uuid = UUID.randomUUID();
		player = accountManager.createAccount(new AccountEditor(uuid + "@qwe.ru", uuid.toString(), "asd").createAccount());
	}

	@After
	public void removeAccount() throws Exception {
		accountManager.removeAccount(player);
	}

	@BeforeTransaction
	public void onSetUp() throws Exception {
		accountLockListener = EasyMock.createStrictMock(AccountLockListener.class);
		listenerAccountNickname = EasyMock.createStrictMock(AccountNicknameLockListener.class);

		accountLockManager.addAccountLockListener(accountLockListener);
		accountLockManager.addAccountNicknameLockListener(listenerAccountNickname);
	}

	@AfterTransaction
	public void onTearDown() throws Exception {
		accountLockManager.removeAccountLockListener(accountLockListener);
		accountLockManager.removeAccountNicknameLockListener(listenerAccountNickname);
	}

	@Test
	public void testLockUnlockAccount() {
		final Date unlockDate = new Date(new Date().getTime() + 10000);

		EasyMock.reset(accountLockListener);
		accountLockListener.accountLocked(player, "You are locked", "Fuck off", unlockDate);
		EasyMock.replay(accountLockListener);

		Assert.assertFalse(accountLockManager.isAccountLocked(player));
		accountLockManager.lockAccount(player, "You are locked", "Fuck off", unlockDate);
		Assert.assertTrue(accountLockManager.isAccountLocked(player));
		EasyMock.verify(accountLockListener);

		EasyMock.reset(accountLockListener);
		accountLockListener.accountUnlocked(player);
		EasyMock.replay(accountLockListener);

		accountLockManager.unlockAccount(player);
		Assert.assertFalse(accountLockManager.isAccountLocked(player));
		EasyMock.verify(accountLockListener);
	}

	@Test
	public void testIsAccountLocked() throws InterruptedException {
		final Date unlockDate = new Date(System.currentTimeMillis() + 1000);

		Assert.assertFalse(accountLockManager.isAccountLocked(new AccountEditor("asd", "qwe", "zc").createAccount()));

		EasyMock.reset(accountLockListener);
		accountLockListener.accountLocked(player, "t", "t", unlockDate);
		accountLockListener.accountUnlocked(player);
		EasyMock.replay(accountLockListener);

		accountLockManager.lockAccount(player, "t", "t", unlockDate);
		Assert.assertTrue(accountLockManager.isAccountLocked(player));

		//Now wait while lock timeout expired
		Thread.sleep(1200);
		Assert.assertFalse(accountLockManager.isAccountLocked(player));
	}

	@Test
	public void testGetLockAccountInfo() throws InterruptedException {
		final Date lockDate = new Date((System.currentTimeMillis() / 1000) * 1000);
		final Date unlockDate = new Date(lockDate.getTime() + 1000);

		EasyMock.reset(accountLockListener);
		accountLockListener.accountLocked(player, "t1", "t2", unlockDate);
		accountLockListener.accountUnlocked(player);
		EasyMock.replay(accountLockListener);

		accountLockManager.lockAccount(player, "t1", "t2", unlockDate);

		Assert.assertNull(accountLockManager.getAccountLockInfo(new AccountEditor("asd", "qwe", "zc").createAccount()));
		final AccountLockInfo lockInfo = accountLockManager.getAccountLockInfo(player);
		Assert.assertEquals(player, lockInfo.getAccount());
		Assert.assertEquals("t1", lockInfo.getPublicReason());
		Assert.assertEquals("t2", lockInfo.getPrivateReason());
		Assert.assertTrue(Math.abs(lockDate.getTime() - lockInfo.getLockDate().getTime()) < 2000);
		Assert.assertEquals(unlockDate, lockInfo.getUnlockDate());

		//Now wait while lock timeout expired
		Thread.sleep(1200);
		Assert.assertNull(accountLockManager.getAccountLockInfo(player));
	}

	@Test
	public void test_username() {
		listenerAccountNickname.usernameLocked("test", "test reason");
		listenerAccountNickname.usernameLocked("test", "test reason2");
		listenerAccountNickname.usernameUnlocked("test");
		EasyMock.replay(listenerAccountNickname);

		accountLockManager.addAccountNicknameLockListener(listenerAccountNickname);

		Assert.assertNull(accountLockManager.isNicknameLocked("test"));

		accountLockManager.lockNickname("test", "test reason");
		Assert.assertEquals("test reason", accountLockManager.isNicknameLocked("test"));

		accountLockManager.lockNickname("test", "test reason2");
		Assert.assertEquals("test reason2", accountLockManager.isNicknameLocked("test"));

		accountLockManager.unlockNickname("test2");
		Assert.assertEquals("test reason2", accountLockManager.isNicknameLocked("test"));

		accountLockManager.unlockNickname("test");
		Assert.assertNotNull(accountLockManager.isNicknameLocked("test"));

		accountLockManager.unlockNickname("test");
		Assert.assertNotNull(accountLockManager.isNicknameLocked("test"));

		EasyMock.verify(listenerAccountNickname);
	}
}
