package wisematches.playground.scribble.settings.impl;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Player;
import wisematches.playground.scribble.settings.BoardSettings;
import wisematches.playground.scribble.settings.BoardSettingsManager;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateBoardSettingsManager implements BoardSettingsManager {
	private SessionFactory sessionFactory;

	private boolean checkWordsDefault = true;
	private boolean clearMemoryDefault = true;
	private boolean enableShareDefault = true;
	private boolean showCaptionsDefault = true;
	private boolean clearByClickDefault = true;

	private String tilesClassDefault = "tilesSetClassic";
	private final Lock lock = new ReentrantLock();
	private final Map<Player, HibernateBoardSettings> cache = new WeakHashMap<>();

	public HibernateBoardSettingsManager() {
	}

	@Override
	public BoardSettings getDefaultSettings() {
		return new BoardSettings(clearMemoryDefault, checkWordsDefault, clearByClickDefault, showCaptionsDefault, enableShareDefault, tilesClassDefault);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public BoardSettings getScribbleSettings(Player player) {
		if (player == null) {
			return getDefaultSettings();
		}

		lock.lock();
		try {
			return getHibernateBoardSettings(player).clone();
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY, readOnly = false)
	public void setScribbleSettings(Player player, BoardSettings settings) {
		lock.lock();
		try {
			HibernateBoardSettings s = getHibernateBoardSettings(player);
			s.update(settings);
			saveBoardSettings(s);
		} finally {
			lock.unlock();
		}
	}

	private HibernateBoardSettings getHibernateBoardSettings(Player player) {
		HibernateBoardSettings settings = cache.get(player);
		if (settings == null) {
			settings = loadBoardSettings(player);
			cache.put(player, settings);
		}
		if (settings == null) {
			settings = new HibernateBoardSettings(player.getId(),
					clearMemoryDefault, checkWordsDefault,
					clearByClickDefault, showCaptionsDefault, enableShareDefault, tilesClassDefault);
			cache.put(player, settings);
		}
		return settings;
	}

	private HibernateBoardSettings loadBoardSettings(Player player) {
		return (HibernateBoardSettings) sessionFactory.getCurrentSession().get(HibernateBoardSettings.class, player.getId());
	}

	private void saveBoardSettings(HibernateBoardSettings settings) {
		sessionFactory.getCurrentSession().saveOrUpdate(settings);
	}

	public boolean isCheckWordsDefault() {
		return checkWordsDefault;
	}

	public void setCheckWordsDefault(boolean checkWordsDefault) {
		this.checkWordsDefault = checkWordsDefault;
	}

	public boolean isClearMemoryDefault() {
		return clearMemoryDefault;
	}

	public void setClearMemoryDefault(boolean clearMemoryDefault) {
		this.clearMemoryDefault = clearMemoryDefault;
	}

	public String getTilesClassDefault() {
		return tilesClassDefault;
	}

	public void setTilesClassDefault(String tilesClassDefault) {
		this.tilesClassDefault = tilesClassDefault;
	}

	public boolean isClearByClickDefault() {
		return clearByClickDefault;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setClearByClickDefault(boolean clearByClickDefault) {
		this.clearByClickDefault = clearByClickDefault;
	}

	public boolean isShowCaptionsDefault() {
		return showCaptionsDefault;
	}

	public void setShowCaptionsDefault(boolean showCaptionsDefault) {
		this.showCaptionsDefault = showCaptionsDefault;
	}

	public boolean isEnableShareDefault() {
		return enableShareDefault;
	}

	public void setEnableShareDefault(boolean enableShareDefault) {
		this.enableShareDefault = enableShareDefault;
	}
}
