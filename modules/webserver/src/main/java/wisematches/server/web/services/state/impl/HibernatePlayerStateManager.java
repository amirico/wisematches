package wisematches.server.web.services.state.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernatePlayerStateManager extends SessionRegistryStateManager {
	private SessionFactory sessionFactory;
	private final Map<Personality, Date> lastActivityMap = new HashMap<Personality, Date>();

	public HibernatePlayerStateManager() {
	}

	@Override
	protected void processPlayerOnline(Player player) {
		super.processPlayerOnline(player);
		lastActivityMap.put(player, new Date());
	}

	@Override
	protected void processPlayerAlive(Player player) {
		super.processPlayerAlive(player);
		lastActivityMap.put(player, new Date());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Date getLastActivityDate(Personality personality) {
		final Date lastActivityDate = lastActivityMap.get(personality);
		if (lastActivityDate == null) {
			final Session session = sessionFactory.getCurrentSession();
			HibernatePlayerActivity a = (HibernatePlayerActivity) session.get(HibernatePlayerActivity.class, personality.getId());
			if (a != null) { // DON'T PUT TO lastActivityMap. PLAYER IS OFFLINE AND CACHE WON'T BE CLEANED
				return a.getLastActivityDate();
			}
		}
		return lastActivityDate;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	protected void processPlayerOffline(Player player) {
		super.processPlayerOffline(player);
		final Date remove = lastActivityMap.remove(player);
		if (remove != null) {
			sessionFactory.getCurrentSession().saveOrUpdate(new HibernatePlayerActivity(player.getId(), remove));
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
