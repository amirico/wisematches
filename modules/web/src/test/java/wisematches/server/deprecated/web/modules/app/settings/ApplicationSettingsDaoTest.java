package wisematches.server.deprecated.web.modules.app.settings;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import wisematches.server.player.Player;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ApplicationSettingsDaoTest extends AbstractTransactionalDataSourceSpringContextTests {
	private ApplicationSettingsDao playerSettingsDao;

	protected String[] getConfigLocations() {
		return new String[]{"classpath:/config/test-application-settings.xml"};
	}

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();

		jdbcTemplate.execute("delete from user_settings where playerId=12");
	}

	@Override
	protected void onTearDown() throws Exception {
		super.onTearDown();

		jdbcTemplate.execute("delete from user_settings where playerId=12");
	}

	public void test_ChangePlayerSettings() {
		final Player p = createStrictMock(Player.class);
		expect(p.getId()).andReturn(12L).anyTimes();
		replay(p);

		assertNull(playerSettingsDao.getPlayerSettings(p, "playboard"));

		playerSettingsDao.setPlayerSettings(p, "profile", "test.key=test.value");
		assertEquals("test.key=test.value", playerSettingsDao.getPlayerSettings(p, "profile"));
		assertNull(playerSettingsDao.getPlayerSettings(p, "dashboard"));

		playerSettingsDao.setPlayerSettings(p, "dashboard", "test.key=test.value2");
		assertEquals("test.key=test.value", playerSettingsDao.getPlayerSettings(p, "profile"));
		assertEquals("test.key=test.value2", playerSettingsDao.getPlayerSettings(p, "dashboard"));
		assertNull(playerSettingsDao.getPlayerSettings(p, "gameboard"));

		playerSettingsDao.setPlayerSettings(p, "profile", "test.key=test.value3");
		playerSettingsDao.setPlayerSettings(p, "dashboard", "test.key=test.value4");
		assertEquals("test.key=test.value3", playerSettingsDao.getPlayerSettings(p, "profile"));
		assertEquals("test.key=test.value4", playerSettingsDao.getPlayerSettings(p, "dashboard"));
		assertNull(playerSettingsDao.getPlayerSettings(p, "gameboard"));
	}

	public void setPlayerSettingsDao(ApplicationSettingsDao playerSettingsDao) {
		this.playerSettingsDao = playerSettingsDao;
	}
}
