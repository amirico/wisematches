package wisematches.server.player.lock.impl;

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
import wisematches.server.player.AccountManager;
import wisematches.server.player.Player;
import wisematches.server.player.PlayerEditor;
import wisematches.server.player.lock.LockAccountInfo;
import wisematches.server.player.lock.LockAccountListener;
import wisematches.server.player.lock.LockAccountManager;
import wisematches.server.player.lock.LockUsernameListener;

import java.util.Date;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/test-server-base-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/server-base-config.xml"
})
public class HibernateLockAccountManagerTest {
	@Autowired
	private LockAccountManager lockAccountManager;

	@Autowired
	private AccountManager accountManager;

	private Player player;

	private LockAccountListener accountListener;
	private LockUsernameListener usernameListener;

	@Before
	public void createPlayer() throws Exception {
		player = accountManager.createPlayer(new PlayerEditor("asd@qwe.ru", "qwe", "asd").createPlayer());
	}

	@After
	public void removePlayer() throws Exception {
		accountManager.removePlayer(player);
	}

	@BeforeTransaction
	public void onSetUp() throws Exception {
		accountListener = createStrictMock(LockAccountListener.class);
		usernameListener = createStrictMock(LockUsernameListener.class);

		lockAccountManager.addLockAccountListener(accountListener);
		lockAccountManager.addLockUsernameListener(usernameListener);
	}

	@AfterTransaction
	public void onTearDown() throws Exception {
		lockAccountManager.removeLockAccountListener(accountListener);
		lockAccountManager.removeLockUsernameListener(usernameListener);
	}

	@Test
	public void testLockUnlockAccount() {
		final Date unlockDate = new Date(new Date().getTime() + 10000);

		reset(accountListener);
		accountListener.accountLocked(player, "You are locked", "Fuck off", unlockDate);
		replay(accountListener);

		assertFalse(lockAccountManager.isAccountLocked(player));
		lockAccountManager.lockAccount(player, "You are locked", "Fuck off", unlockDate);
		assertTrue(lockAccountManager.isAccountLocked(player));
		verify(accountListener);

		reset(accountListener);
		accountListener.accountUnlocked(player);
		replay(accountListener);

		lockAccountManager.unlockAccount(player);
		assertFalse(lockAccountManager.isAccountLocked(player));
		verify(accountListener);
	}

	@Test
	public void testIsAccountLocked() throws InterruptedException {
		final Date unlockDate = new Date(System.currentTimeMillis() + 1000);

		assertFalse(lockAccountManager.isAccountLocked(new PlayerEditor("asd", "qwe", "zc").createPlayer()));

		reset(accountListener);
		accountListener.accountLocked(player, "t", "t", unlockDate);
		accountListener.accountUnlocked(player);
		replay(accountListener);

		lockAccountManager.lockAccount(player, "t", "t", unlockDate);
		assertTrue(lockAccountManager.isAccountLocked(player));

		//Now wait while lock timeout expired
		Thread.sleep(1200);
		assertFalse(lockAccountManager.isAccountLocked(player));
	}

	@Test
	public void testGetLockAccountInfo() throws InterruptedException {
		final Date lockDate = new Date((System.currentTimeMillis() / 1000) * 1000);
		final Date unlockDate = new Date(lockDate.getTime() + 1000);

		reset(accountListener);
		accountListener.accountLocked(player, "t1", "t2", unlockDate);
		accountListener.accountUnlocked(player);
		replay(accountListener);

		lockAccountManager.lockAccount(player, "t1", "t2", unlockDate);
		assertNull(lockAccountManager.getLockAccountInfo(new PlayerEditor("asd", "qwe", "zc").createPlayer()));
		final LockAccountInfo info = lockAccountManager.getLockAccountInfo(player);
		assertEquals(player, info.getPlayer());
		assertEquals("t1", info.getPublicReason());
		assertEquals("t2", info.getPrivateReason());
		assertTrue(Math.abs(lockDate.getTime() - info.getLockDate().getTime()) < 2000);
		assertEquals(unlockDate, info.getUnlockDate());

		//Now wait while lock timeout expired
		Thread.sleep(1200);
		assertNull(lockAccountManager.getLockAccountInfo(player));
	}

	@Test
	public void test_username() {
		usernameListener.usernameLocked("test", "test reason");
		usernameListener.usernameLocked("test", "test reason2");
		usernameListener.usernameUnlocked("test");
		replay(usernameListener);

		lockAccountManager.addLockUsernameListener(usernameListener);

		assertNull(lockAccountManager.isUsernameLocked("test"));

		lockAccountManager.lockUsername("test", "test reason");
		assertEquals("test reason", lockAccountManager.isUsernameLocked("test"));

		lockAccountManager.lockUsername("test", "test reason2");
		assertEquals("test reason2", lockAccountManager.isUsernameLocked("test"));

		lockAccountManager.unlockUsername("test2");
		assertEquals("test reason2", lockAccountManager.isUsernameLocked("test"));

		lockAccountManager.unlockUsername("test");
		assertNotNull(lockAccountManager.isUsernameLocked("test"));

		lockAccountManager.unlockUsername("test");
		assertNotNull(lockAccountManager.isUsernameLocked("test"));

		verify(usernameListener);
	}
}
