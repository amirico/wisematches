package wisematches.server.web.modules.login.tokens;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import wisematches.server.player.Player;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RememberTokenDaoTest extends AbstractTransactionalDataSourceSpringContextTests {
	private RememberTokenDao rememberTokenDao;

	protected String[] getConfigLocations() {
		return new String[]{"classpath:/config/test-web-server-config.xml"};
	}

	public void test_token() {
		int count = jdbcTemplate.queryForInt("select count(*) from tn_remember");

		Player p = createNiceMock(Player.class);
		expect(p.getId()).andReturn(13L).anyTimes();
		replay(p);

		final RememberToken rememberToken = rememberTokenDao.createToken(p, "123.127.129.255");
		assertEquals(p.getId(), rememberToken.getPlayerId());
		assertNotNull(rememberToken.getToken());
		assertNotNull(rememberToken.getDate());

		final RememberToken token = rememberTokenDao.getToken(p, "123.127.129.255");
		assertEquals(rememberToken.getToken(), token.getToken());
		assertEquals(rememberToken.getDate(), token.getDate());

		assertEquals(count + 1, jdbcTemplate.queryForInt("select count(*) from tn_remember"));

		rememberTokenDao.removeToken(rememberToken);
		assertEquals(count, jdbcTemplate.queryForInt("select count(*) from tn_remember"));


		assertNull(rememberTokenDao.getToken(p, "123.127.129.255"));
	}

	public RememberTokenDao getRememberTokenDao() {
		return rememberTokenDao;
	}

	public void setRememberTokenDao(RememberTokenDao rememberTokenDao) {
		this.rememberTokenDao = rememberTokenDao;
	}
}
