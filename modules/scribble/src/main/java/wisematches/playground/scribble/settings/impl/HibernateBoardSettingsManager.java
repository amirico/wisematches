package wisematches.playground.scribble.settings.impl;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
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
	private boolean showCaptionsDefault = true;
	private boolean clearByClickDefault = true;

	private String tilesClassDefault = "tilesSetClassic";
	private final Lock lock = new ReentrantLock();
	private final Map<Personality, HibernateBoardSettings> cache = new WeakHashMap<Personality, HibernateBoardSettings>();

	public HibernateBoardSettingsManager() {
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public BoardSettings getScribbleSettings(Personality personality) {
		if (personality == null) {
			return new BoardSettings(clearMemoryDefault, checkWordsDefault, clearByClickDefault, showCaptionsDefault, tilesClassDefault);
		}

		lock.lock();
		try {
			return getHibernateBoardSettings(personality).clone();
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY, readOnly = false)
	public void setScribbleSettings(Personality personality, BoardSettings settings) {
		lock.lock();
		try {
			HibernateBoardSettings s = getHibernateBoardSettings(personality);
			s.update(settings);
			saveBoardSettings(s);
		} finally {
			lock.unlock();
		}
	}

	private HibernateBoardSettings getHibernateBoardSettings(Personality personality) {
		HibernateBoardSettings settings = cache.get(personality);
		if (settings == null) {
			settings = loadBoardSettings(personality);
			cache.put(personality, settings);
		}
		if (settings == null) {
			settings = new HibernateBoardSettings(personality.getId(),
					clearMemoryDefault, checkWordsDefault,
					clearByClickDefault, showCaptionsDefault, tilesClassDefault);
			cache.put(personality, settings);
		}
		return settings;
	}

	private HibernateBoardSettings loadBoardSettings(Personality personality) {
		return (HibernateBoardSettings) sessionFactory.getCurrentSession().get(HibernateBoardSettings.class, personality.getId());
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
}
