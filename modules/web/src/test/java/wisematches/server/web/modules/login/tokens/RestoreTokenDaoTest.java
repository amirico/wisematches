package wisematches.server.web.modules.login.tokens;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import wisematches.kernel.player.Player;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RestoreTokenDaoTest extends AbstractTransactionalDataSourceSpringContextTests {
    private RestoreTokenDao restoreTokenDao;

    protected String[] getConfigLocations() {
        return new String[]{"classpath:/config/test-web-server-config.xml"};
    }

    public void test_token() {
        int count = jdbcTemplate.queryForInt("select count(*) from tn_restore");

        Player p = createNiceMock(Player.class);
        expect(p.getId()).andReturn(13L).anyTimes();
        replay(p);

        final RestoreToken cookiesToken = restoreTokenDao.createToken(p);
        assertEquals(p.getId(), cookiesToken.getPlayerId());
        assertNotNull(cookiesToken.getToken());
        assertNotNull(cookiesToken.getDate());

        final RestoreToken token = restoreTokenDao.getToken(p);
        assertEquals(cookiesToken.getToken(), token.getToken());
        assertEquals(cookiesToken.getDate(), token.getDate());

        assertEquals(count + 1, jdbcTemplate.queryForInt("select count(*) from tn_restore"));

        restoreTokenDao.removeToken(cookiesToken);
        assertEquals(count, jdbcTemplate.queryForInt("select count(*) from tn_restore"));
    }

    public RestoreTokenDao getRestoreTokenDao() {
        return restoreTokenDao;
    }

    public void setRestoreTokenDao(RestoreTokenDao restoreTokenDao) {
        this.restoreTokenDao = restoreTokenDao;
    }
}
