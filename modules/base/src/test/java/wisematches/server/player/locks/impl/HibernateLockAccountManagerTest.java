package wisematches.server.player.locks.impl;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import wisematches.server.player.Player;
import wisematches.server.player.locks.LockAccountInfo;
import wisematches.server.player.locks.LockAccountListener;
import wisematches.server.player.locks.LockAccountManager;
import wisematches.server.player.locks.LockUsernameListener;

import java.sql.Types;
import java.util.Date;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class HibernateLockAccountManagerTest extends AbstractTransactionalDataSourceSpringContextTests {
	private LockAccountListener accountListener;
	private LockUsernameListener usernameListener;
	private LockAccountManager lockAccountManager;
	private Player player;

	protected String[] getConfigLocations() {
		return new String[]{"classpath:/config/test-server-config.xml"};
	}

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();

		player = createMock(Player.class);
		expect(player.getId()).andReturn(13L).anyTimes();
		replay(player);

		accountListener = createStrictMock(LockAccountListener.class);
		usernameListener = createStrictMock(LockUsernameListener.class);

		lockAccountManager.addLockAccountListener(accountListener);
		lockAccountManager.addLockUsernameListener(usernameListener);
	}


	@Override
	protected void onTearDown() throws Exception {
		jdbcTemplate.update("delete from lock_account where playerId = ?", new Object[]{player.getId()});
		jdbcTemplate.update("delete from lock_username where username = ?", new Object[]{"test"});

		lockAccountManager.removeLockAccountListener(accountListener);
		lockAccountManager.removeLockUsernameListener(usernameListener);

		super.onTearDown();
	}

	public void test_lockUnlock() {
		SqlRowSet rowSet;

		final int count = countRowsInTable("lock_account");

		final Date createDate1 = new Date((System.currentTimeMillis() / 1000) * 1000); //remove milliseconds
		final Date unlockDate1 = new Date(createDate1.getTime() + 10000);

		reset(accountListener);
		accountListener.accountLocked(player, "You are locked", "Fuck off", unlockDate1);
		replay(accountListener);

		lockAccountManager.lockAccount(player, "You are locked", "Fuck off", unlockDate1);
		assertEquals(count + 1, countRowsInTable("lock_account"));
		verify(accountListener);

		rowSet = jdbcTemplate.queryForRowSet("select * from lock_account where playerId = " + player.getId());
		assertTrue(rowSet.next());

		assertEquals(13L, rowSet.getLong(1));
		assertEquals("You are locked", rowSet.getString(2));
		assertEquals("Fuck off", rowSet.getString(3));
		//creation time must be not great that 2 seconds
		assertTrue(Math.abs(createDate1.getTime() - rowSet.getTimestamp(4).getTime()) < 2000);

		//double lock must update parameters
		final Date createDate2 = new Date((System.currentTimeMillis() / 1000) * 1000); //remove milliseconds
		final Date unlockDate2 = new Date(createDate2.getTime() + 20000);
		reset(accountListener);
		accountListener.accountLocked(player, "You are locked 2", "Fuck off 2", unlockDate2);
		replay(accountListener);

		lockAccountManager.lockAccount(player, "You are locked 2", "Fuck off 2", unlockDate2);
		assertEquals(count + 1, countRowsInTable("lock_account"));
		verify(accountListener);

		rowSet = jdbcTemplate.queryForRowSet("select * from lock_account where playerId = " + player.getId());
		rowSet.next();
		assertEquals(13L, rowSet.getLong(1));
		assertEquals("You are locked 2", rowSet.getString(2));
		assertEquals("Fuck off 2", rowSet.getString(3));
		//creation time must be not great that 2 seconds
		assertTrue(Math.abs(createDate2.getTime() - rowSet.getTimestamp(4).getTime()) < 2000);
		assertEquals(unlockDate2, new Date(rowSet.getTimestamp(5).getTime()));

		reset(accountListener);
		accountListener.accountUnlocked(player);
		replay(accountListener);

		lockAccountManager.unlockAccount(player);
		assertEquals(count, countRowsInTable("lock_account"));

		//Double unlock dosn't have affect
		lockAccountManager.unlockAccount(player);
		assertEquals(count, countRowsInTable("lock_account"));
		verify(accountListener);
	}

	public void test_isAccountLocked() throws InterruptedException {
		final Date date = new Date(System.currentTimeMillis() + 2000);

		final int count = countRowsInTable("lock_account");

		accountListener.accountLocked(player, "t", "t", date);
		accountListener.accountUnlocked(player);
		replay(accountListener);

		lockAccountManager.lockAccount(player, "t", "t", date);
		assertTrue(lockAccountManager.isAccountLocked(player));
		assertEquals(count + 1, countRowsInTable("lock_account"));

		//Now wait while lock timeout expired
		Thread.sleep(2200);
		assertFalse(lockAccountManager.isAccountLocked(player));
		assertEquals(count, countRowsInTable("lock_account"));

		assertFalse(lockAccountManager.isAccountLocked(player));
		assertEquals(count, countRowsInTable("lock_account"));
		verify(accountListener);
	}

	public void test_getLockAccountInfo() throws InterruptedException {
		final int count = countRowsInTable("lock_account");
		System.err.println("Count: " + count);

		final Date lockDate = new Date((System.currentTimeMillis() / 1000) * 1000);
		final Date unlockDate = new Date(lockDate.getTime() + 2000);
		System.err.println("Lock date: " + lockDate.getTime() + ", unlockDate - " + unlockDate.getTime());

		int k = jdbcTemplate.update(
				"insert into lock_account(playerId, publicReason, privateReason, lockDate, unlockDate) " +
						"values (?, ?, ?, ?, ?)",
				new Object[]{player.getId(), "T1", "T2", lockDate, unlockDate},
				new int[]{Types.TINYINT, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.TIMESTAMP});
		System.err.println("Record inserted: " + k);
		assertEquals("Record inserted", 1, k);

		final SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select playerId, publicReason, privateReason, lockDate, unlockDate from lock_account");
		while (rowSet.next()) {
			final long aLong = rowSet.getLong(1);
			final String s1 = rowSet.getString(2);
			final String s2 = rowSet.getString(3);
			Date d1 = rowSet.getTimestamp(4);
			Date d2 = rowSet.getTimestamp(5);
			System.err.println("Player locked: " + aLong + ", " + s1 + ", " + s2 + ", " + d1.getTime() + ", " + d2.getTime());
		}

		final LockAccountInfo accountInfo = lockAccountManager.getLockAccountInfo(player);
		System.err.println("Lock info: " + accountInfo);
		assertNotNull(accountInfo);
		assertSame(player, accountInfo.getPlayer());
		assertEquals(13, accountInfo.getPlayerId());
		assertEquals("T1", accountInfo.getPublicReason());
		assertEquals("T2", accountInfo.getPrivateReason());
		//creation time must be not great that 2 seconds
		assertTrue(Math.abs(lockDate.getTime() - accountInfo.getLockDate().getTime()) < 2000);
		assertEquals(unlockDate.getTime() / 1000 * 1000, accountInfo.getUnlockDate().getTime());
		assertEquals(count + 1, jdbcTemplate.queryForInt("select count(*) from lock_account"));

		accountListener.accountUnlocked(player);
		replay(accountListener);

		Thread.sleep(2200);
		assertNull(lockAccountManager.getLockAccountInfo(player));
		assertEquals(count, countRowsInTable("lock_account"));

		assertNull(lockAccountManager.getLockAccountInfo(player));
		assertEquals(count, countRowsInTable("lock_account"));
		verify(accountListener);
	}

	public void test_username() {
		usernameListener.usernameLocked("test", "test reason");
		usernameListener.usernameLocked("test", "test reason2");
		usernameListener.usernameUnlocked("test");
		replay(usernameListener);

		lockAccountManager.addLockUsernameListener(usernameListener);

		final int count = countRowsInTable("lock_username");
		assertNull(lockAccountManager.isUsernameLocked("test"));

		lockAccountManager.lockUsername("test", "test reason");
		assertEquals("test reason", lockAccountManager.isUsernameLocked("test"));
		assertEquals(count + 1, countRowsInTable("lock_username"));

		lockAccountManager.lockUsername("test", "test reason2");
		assertEquals("test reason2", lockAccountManager.isUsernameLocked("test"));
		assertEquals(count + 1, countRowsInTable("lock_username"));

		lockAccountManager.unlockUsername("test2");
		assertEquals(count + 1, countRowsInTable("lock_username"));
		assertEquals("test reason2", lockAccountManager.isUsernameLocked("test"));

		lockAccountManager.unlockUsername("test");
		assertNotNull(lockAccountManager.isUsernameLocked("test"));
		assertEquals(count, countRowsInTable("lock_username"));

		lockAccountManager.unlockUsername("test");
		assertNotNull(lockAccountManager.isUsernameLocked("test"));
		assertEquals(count, countRowsInTable("lock_username"));

		verify(usernameListener);
	}

	public void setLockAccountManager(LockAccountManager lockAccountManager) {
		this.lockAccountManager = lockAccountManager;
	}
}
