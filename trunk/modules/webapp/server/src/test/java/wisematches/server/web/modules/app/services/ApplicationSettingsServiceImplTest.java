package wisematches.server.web.modules.app.services;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.*;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import wisematches.kernel.player.Player;
import wisematches.server.web.modules.app.settings.ApplicationSettingsDao;
import wisematches.server.web.rpc.RemoteServiceContextAccessor;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ApplicationSettingsServiceImplTest {
/*    @Test
    public void test_loadSettings() throws Exception {
        final ApplicationSettingsServiceImpl service = new ApplicationSettingsServiceImpl();

        final Player player = createMock(Player.class);
        RemoteServiceContextAccessor.expectPlayer(player);

        final ApplicationSettingsDao settingsDao = createMock(ApplicationSettingsDao.class);
        expect(settingsDao.getPlayerSettings(player, "testView")).andReturn("result");
        replay(settingsDao);

        service.setPlayerSettingsDao(settingsDao);
        assertEquals("result", service.loadSettings("testView"));

        verify(settingsDao);
    }

    @Test
    public void test_saveSettings() throws Exception {
        final ApplicationSettingsServiceImpl service = new ApplicationSettingsServiceImpl();

        final Player player = createMock(Player.class);
        RemoteServiceContextAccessor.expectPlayer(player);

        final ApplicationSettingsDao settingsDao = createMock(ApplicationSettingsDao.class);
        settingsDao.setPlayerSettings(player, "testView", "value");
        replay(settingsDao);

        service.setPlayerSettingsDao(settingsDao);
        service.saveSettings("testView", "value");

        verify(settingsDao);
    }

    @After
    public void thearDown() {
        RemoteServiceContextAccessor.destroy();
    }*/
}