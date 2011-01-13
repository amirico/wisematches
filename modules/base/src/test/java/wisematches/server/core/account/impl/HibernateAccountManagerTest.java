package wisematches.server.core.account.impl;

import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import wisematches.kernel.player.Player;
import wisematches.server.core.account.*;
import wisematches.server.player.locks.LockAccountManager;

import java.util.Date;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateAccountManagerTest extends AbstractTransactionalDataSourceSpringContextTests {
	@Autowired
	private AccountManager accountManager;

	private LockAccountManager lockAccountManager;
	private LockAccountManager oldLockAccountManager;

	private static final String TEST_USERNAME = "test_username";
	private static final String TEST_PASSWORD = "test_password";
	private static final String TEST_EMAIL = "mail@test.tst";

	private static final int DEFAULT_PLAYER_RATING = 1000;

	public HibernateAccountManagerTest() {
	}

	protected String[] getConfigLocations() {
		return new String[]{"classpath:/config/test-server-base-config.xml"};
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();

		lockAccountManager = createMock(LockAccountManager.class);

		if (accountManager instanceof Advised) {
			final Advised adviced = (Advised) accountManager;
			final HibernateAccountManager accountManager = (HibernateAccountManager) adviced.getTargetSource().getTarget();
			oldLockAccountManager = accountManager.getLockAccountManager();
			accountManager.setLockAccountManager(lockAccountManager);
			accountManager.setDefaultPlayerRating(DEFAULT_PLAYER_RATING);
		}
	}

	public void test_findByUsername() {
		final int i = jdbcTemplate.update("insert into user_user(username, password, email) values (?, ? , ?)", new Object[]{TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL});
		assertEquals(1, i);

		final PlayerImpl player = (PlayerImpl) accountManager.findByUsername(TEST_USERNAME);
		assertNotNull(player);
		assertEquals(TEST_EMAIL, player.getEmail());

		final int i1 = jdbcTemplate.update("delete from user_user where id=" + player.getId());
		assertEquals(1, i1);
	}

	public void test_findByEmail() {
		final int i = jdbcTemplate.update("insert into user_user(username, password, email) values (?, ? , ?)", new Object[]{TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL});
		assertEquals(1, i);

		final PlayerImpl player = (PlayerImpl) accountManager.findByEmail(TEST_EMAIL);
		assertNotNull(player);
		assertEquals(TEST_EMAIL, player.getEmail());

		final int i1 = jdbcTemplate.update("delete from user_user where id=" + player.getId());
		assertEquals(1, i1);
	}

	public void test_createDeletePlayer() throws AccountException {
		final int count = countRowsInTable("user_user");

		reset(lockAccountManager);
		expect(lockAccountManager.isUsernameLocked(TEST_USERNAME)).andReturn(null);
		replay(lockAccountManager);

		final AccountListener accountListener = createStrictMock(AccountListener.class);
		accountListener.accountCreated(isA(Player.class));
		replay(accountListener);

		accountManager.addAccountListener(accountListener);
		accountManager.addAccountListener(accountListener); // Twise added - nothing should be happend.
		try {
			final PlayerImpl user = (PlayerImpl) accountManager.createPlayer(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
			assertTrue(user.getId() > 0);
			assertEquals(DEFAULT_PLAYER_RATING, user.getRating());
			verify(lockAccountManager);

			assertNotNull(user.getPlayerProfile());
			assertEquals(user.getId(), user.getPlayerProfile().getPlayerId());

			assertNotNull(user.getPlayerNotifications());
			assertEquals(user.getId(), user.getPlayerNotifications().getPlayerId());

			final int newCount = countRowsInTable("user_user");
			assertEquals("User was not found in the DB", count + 1, newCount);
			assertEquals("Player profile was not created", 1, jdbcTemplate.queryForInt("select count(*) from user_profile where playerId = " + user.getId()));
			assertEquals("Player notification were not created", 1, jdbcTemplate.queryForInt("select count(*) from user_notification where playerId = " + user.getId()));

			try {
				reset(lockAccountManager);
				expect(lockAccountManager.isUsernameLocked(TEST_USERNAME)).andReturn(null);
				replay(lockAccountManager);

				accountManager.createPlayer(TEST_USERNAME, TEST_PASSWORD, "2" + TEST_EMAIL);
				fail("Exception must be here");
			} catch (DublicateAccountException ex) {
				assertTrue(ex.isDublicateUsername());
				assertFalse(ex.isDublicateEmail());
			}
			verify(lockAccountManager);

			try {
				reset(lockAccountManager);
				expect(lockAccountManager.isUsernameLocked(TEST_USERNAME + "2")).andReturn(null);
				replay(lockAccountManager);

				accountManager.createPlayer(TEST_USERNAME + "2", TEST_PASSWORD, TEST_EMAIL);
				fail("Exception must be here");
			} catch (DublicateAccountException ex) {
				assertFalse(ex.isDublicateUsername());
				assertTrue(ex.isDublicateEmail());
			}
			verify(lockAccountManager);

			reset(lockAccountManager);
			expect(lockAccountManager.isUsernameLocked(TEST_USERNAME + "2")).andReturn("reason");
			replay(lockAccountManager);

			try {
				accountManager.createPlayer(TEST_USERNAME + "2", TEST_PASSWORD + "2", TEST_EMAIL + "2");
				fail("Exception must be here");
			} catch (InadmissibleUsernameException ex) {
				assertEquals("reason", ex.getReason());
			}
			verify(lockAccountManager);

			verify(accountListener);

			reset(accountListener);
			accountListener.accountDeleted(user);
			replay(accountListener);

			accountManager.deletePlayer(user);

			verify(accountListener);

			assertEquals("User was not found in the DB", count, countRowsInTable("user_user"));
			assertEquals("Player profile was not removed", 0, jdbcTemplate.queryForInt("select count(*) from user_profile where playerId = " + user.getId()));
			assertEquals("Player notification were not removed", 0, jdbcTemplate.queryForInt("select count(*) from user_notification where playerId = " + user.getId()));
		} finally {
			accountManager.removeAccountListener(accountListener);
		}
	}

	public void test_authentificate1() throws AccountLockedException {
		Player p = createNiceMock(Player.class);
		expect(p.getId()).andReturn(13L);
		replay(p);

/*
        expect(lockAccountManager.isAccountLocked(p)).andReturn(true);
        expect(lockAccountManager.getLockAccountInfo(p)).andReturn(new LockAccountInfo(p, "pub", "priv", new Date()));
        expect(lockAccountManager.isAccountLocked(p)).andReturn(false);
*/
		replay(lockAccountManager);

		try {
			accountManager.authentificate(p);
			fail("Exception must be here");
		} catch (AccountLockedException ex) {
			assertNotNull(ex.getLockAccountInfo());
		}

		accountManager.authentificate(p);
	}

	public void test_authentificate2() throws AccountLockedException, AccountNotFountException {
		final int i = jdbcTemplate.update("insert into user_user(username, password, email) values (?, ? , ?)",
				new Object[]{TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL});
		assertEquals(1, i);

		final Date unlockDate = new Date(0);

/*
        expect(lockAccountManager.isAccountLocked(isA(Player.class))).andReturn(true);
        expect(lockAccountManager.getLockAccountInfo(isA(Player.class))).andAnswer(new IAnswer<LockAccountInfo>() {
            public LockAccountInfo answer() throws Throwable {
                final Player player = (Player) getCurrentArguments()[0];
                return new LockAccountInfo(player, "pub", "priv", unlockDate);
            }
        });
        expect(lockAccountManager.isAccountLocked(isA(Player.class))).andReturn(false);
*/
		replay(lockAccountManager);

		try {
			accountManager.authentificate(TEST_USERNAME, TEST_PASSWORD);
			fail("Exception must be here");
		} catch (AccountLockedException ex) {
			assertNotNull(ex.getLockAccountInfo());
		}

		try {
			accountManager.authentificate(TEST_USERNAME, TEST_EMAIL);
			fail("Exception must be here");
		} catch (AccountNotFountException ex) {
			;
		}

		final Player player = accountManager.authentificate(TEST_USERNAME, TEST_PASSWORD);
		assertNotNull(player);

		final int i1 = jdbcTemplate.update("delete from user_user where id=" + player.getId());
		assertEquals(1, i1);

		verify(lockAccountManager);
	}

	public void test_getPlayersCount() {
		final int i = jdbcTemplate.update("insert into user_user(username, password, email) values (?, ? , ?)", new Object[]{
				TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL
		});
		assertEquals(1, i);

		final int count = countRowsInTable("user_user");
		assertEquals(count, accountManager.getRegistredPlayersCount());
	}

	@Override
	protected void onTearDown() throws Exception {
		jdbcTemplate.update("delete from user_user where username=? AND password = ? AND email = ?", new Object[]{TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL});

		if (accountManager instanceof Advised) {
			final Advised adviced = (Advised) accountManager;
			final HibernateAccountManager accountManager = (HibernateAccountManager) adviced.getTargetSource().getTarget();
			accountManager.setLockAccountManager(oldLockAccountManager);
		}
	}
}
