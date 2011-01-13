package wisematches.server.core.account.impl;

import org.hibernate.SessionFactory;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import wisematches.kernel.notification.PlayerNotifications;
import wisematches.kernel.player.Gender;
import wisematches.kernel.player.Player;
import wisematches.kernel.player.PlayerProfile;
import wisematches.server.core.account.AccountException;
import wisematches.server.core.account.PlayerListener;
import wisematches.server.core.account.PlayerManager;

import java.util.Date;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class HibernatePlayerManagerTest extends AbstractTransactionalDataSourceSpringContextTests {
	private SessionFactory sessionFactory;
	private PlayerManager playerManager;

	private static final String TEST_USERNAME = "test_username";
	private static final String TEST_PASSWORD = "test_password";
	private static final String TEST_EMAIL = "mail@test.tst";

	private static final Object[] PARAMS = {TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL};

	public HibernatePlayerManagerTest() {
	}

	protected String[] getConfigLocations() {
		return new String[]{"classpath:/config/test-server-base-config.xml"};
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void test_getPlayerById() throws AccountException {
		final Date date = new Date();
		final int timeZone = 0;

		final int i = jdbcTemplate.update("insert into user_user(username, password, email) values (?, ? , ?)", PARAMS);
		assertEquals(1, i);

		final long id = jdbcTemplate.queryForLong("select id from user_user where username = ? and password = ? and email = ?", PARAMS);
		jdbcTemplate.update("insert into user_profile(playerId, realName, countryCode, city, timezone, dateOfBirth, gender, homepage, additionalInfo) " +
				"values (?, ?, ?, ? ,? ,?, ?, ?, ?)", new Object[]{
				id, "Real Name", "ru", "City", timeZone, date, 0, "this is homepage", "additional info"
		});

		final Player player = playerManager.getPlayer(id);
		assertEquals(id, player.getId());
		assertEquals(TEST_USERNAME, player.getUsername());
		assertEquals(TEST_PASSWORD, player.getPassword());
		assertEquals(TEST_EMAIL, player.getEmail());
		assertNotNull(player.getPlayerProfile());

		final PlayerProfile profile = player.getPlayerProfile();

		assertEquals(date.getTime() / 1000 * 1000, profile.getDateOfBirth().getTime() / 1000 * 1000); //remove milliseconds
		assertEquals("additional info", profile.getAdditionalInfo());
		assertEquals("City", profile.getCity());
		assertEquals("ru", profile.getCountryCode());
		assertEquals(Gender.MALE, profile.getGender());
		assertEquals("this is homepage", profile.getHomepage());
		assertEquals("Real Name", profile.getRealName());
		assertEquals(timeZone, profile.getTimezone());
	}

	public void test_updatePlayer() throws AccountException {
		final Date date = new Date();

		final int i = jdbcTemplate.update("insert into user_user(username, password, email) values (?, ? , ?)", PARAMS);
		assertEquals(1, i);

		final long id = jdbcTemplate.queryForLong("select id from user_user where username = ? and password = ? and email = ?", PARAMS);
		jdbcTemplate.update("insert into user_profile(playerId, realName, countryCode, city, timezone, dateOfBirth, gender, homepage, additionalInfo) " +
				"values (?, ?, ?, ? ,? ,?, ?, ?, ?)", new Object[]{
				id, "Real Name", "ru", "City", 3, date, 0, "this is homepage", "additional info"
		});

		final Player player = playerManager.getPlayer(id);
		final PlayerProfile profile = player.getPlayerProfile();

		Date d = new Date(System.currentTimeMillis() + 1000);
		player.setEmail(TEST_EMAIL + "2");
		player.setLastSigninDate(d);
		profile.setAdditionalInfo(null);
		profile.setCity(null);
		profile.setCountryCode(null);
		profile.setDateOfBirth(null);
		profile.setGender(null);
		profile.setHomepage(null);
		profile.setRealName(null);
		profile.setTimezone(0);

		final PlayerListener playerListener = createStrictMock(PlayerListener.class);
		playerListener.playerUpdated(player);
		playerListener.playerUpdated(player);
		replay(playerListener);

		playerManager.addPlayerListener(playerListener);
		playerManager.addPlayerListener(playerListener); // Twise adding - nothing should be happend
		try {
			playerManager.updatePlayer(player);

			String email = (String) jdbcTemplate.queryForObject("select email from user_user where username = ? and password = ? ", new Object[]{TEST_USERNAME, TEST_PASSWORD}, String.class);
			assertEquals(TEST_EMAIL + "2", email);

			player.setEmail(TEST_EMAIL);
			playerManager.updatePlayer(player);

			verify(playerListener);
		} finally {
			// Because we are using configuration to test we should remove listener. In other case next tests will be failed.
			playerManager.removePlayerListener(playerListener);
		}
	}

	public void test_notifications() {
		final int i = jdbcTemplate.update("insert into user_user(username, password, email) values (?, ? , ?)", PARAMS);
		final long id = jdbcTemplate.queryForLong("select id from user_user where username = ? and password = ? and email = ?", PARAMS);
		jdbcTemplate.update("insert into user_notification(playerId, notifications) " +
				"values (?, ?)", new Object[]{
				id, ""
		});

		Player player = playerManager.getPlayer(id);
		PlayerNotifications notifications = player.getPlayerNotifications();

		assertTrue(notifications.isNotificationEnabled(MockPlayerNotification1.V1));
		assertTrue(notifications.isNotificationEnabled(MockPlayerNotification2.V1));

		notifications.addDisabledNotification(MockPlayerNotification1.V1);
		playerManager.updatePlayer(player);
		assertFalse(notifications.isNotificationEnabled(MockPlayerNotification1.V1));
		assertTrue(notifications.isNotificationEnabled(MockPlayerNotification2.V1));

		notifications.addDisabledNotification(MockPlayerNotification2.V1);
		playerManager.updatePlayer(player);
		assertFalse(notifications.isNotificationEnabled(MockPlayerNotification1.V1));
		assertFalse(notifications.isNotificationEnabled(MockPlayerNotification2.V1));

		// Test again
		sessionFactory.getCurrentSession().clear();
		player = playerManager.getPlayer(id);
		notifications = player.getPlayerNotifications();
		assertFalse(notifications.isNotificationEnabled(MockPlayerNotification1.V1));
		assertFalse(notifications.isNotificationEnabled(MockPlayerNotification2.V1));
		assertTrue(notifications.isNotificationEnabled(MockPlayerNotification1.V2));
		assertTrue(notifications.isNotificationEnabled(MockPlayerNotification2.V2));

		notifications.removeDisabledNotification(MockPlayerNotification1.V1);
		playerManager.updatePlayer(player);
		assertTrue(notifications.isNotificationEnabled(MockPlayerNotification1.V1));
		assertFalse(notifications.isNotificationEnabled(MockPlayerNotification2.V1));
		assertTrue(notifications.isNotificationEnabled(MockPlayerNotification1.V2));
		assertTrue(notifications.isNotificationEnabled(MockPlayerNotification2.V2));

		// Test again
		sessionFactory.getCurrentSession().clear();  //clear cache
		player = playerManager.getPlayer(id);
		notifications = player.getPlayerNotifications();

		assertTrue(notifications.isNotificationEnabled(MockPlayerNotification1.V1));
		assertFalse(notifications.isNotificationEnabled(MockPlayerNotification2.V1));
		assertTrue(notifications.isNotificationEnabled(MockPlayerNotification1.V2));
		assertTrue(notifications.isNotificationEnabled(MockPlayerNotification2.V2));
	}

	@Override
	protected void onTearDown() throws Exception {
		final long id = jdbcTemplate.queryForLong("select id from user_user where username = ? and password = ? and email = ?", PARAMS);
		if (id != 0) {
			jdbcTemplate.update("delete from user_user where id = ?", new Object[]{id});
			jdbcTemplate.update("delete from user_profile where playerId = ?", new Object[]{id});
			jdbcTemplate.update("delete from user_notification where playerId = ?", new Object[]{id});
		}
	}
}
