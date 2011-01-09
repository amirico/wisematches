package wisematches.server.web.modules.app.services;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.web.modules.app.settings.ApplicationSettingsDao;
import wisematches.server.web.rpc.GenericSecureRemoteService;

/**
 */
public class ApplicationSettingsServiceImpl extends GenericSecureRemoteService { //implements ApplicationSettingsService {
/*    private ApplicationSettingsDao playerSettingsDao;

    private static final Logger log = Logger.getLogger(ApplicationSettingsServiceImpl.class);

    @Transactional(readOnly = true)
    public String loadSettings(String frameViewId) {
        final String settings = playerSettingsDao.getPlayerSettings(getPlayer(), frameViewId);
        if (log.isDebugEnabled()) {
            log.debug("Load settings for " + frameViewId + ": " + settings);
        }
        return settings;
    }

    @Transactional
    public void saveSettings(String frameViewId, String settings) {
        if (log.isDebugEnabled()) {
            log.debug("Store settings for " + frameViewId + ": " + settings);
        }
        playerSettingsDao.setPlayerSettings(getPlayer(), frameViewId, settings);
    }

    public void setPlayerSettingsDao(ApplicationSettingsDao playerSettingsDao) {
        this.playerSettingsDao = playerSettingsDao;
    }*/
}