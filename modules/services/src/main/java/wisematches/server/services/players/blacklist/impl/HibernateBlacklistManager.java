package wisematches.server.services.players.blacklist.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Player;
import wisematches.server.services.players.blacklist.BlacklistListener;
import wisematches.server.services.players.blacklist.BlacklistManager;
import wisematches.server.services.players.blacklist.BlacklistRecord;
import wisematches.server.services.players.blacklist.BlacklistedException;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateBlacklistManager implements BlacklistManager {
	private SessionFactory sessionFactory;
	private final Collection<BlacklistListener> listeners = new CopyOnWriteArraySet<BlacklistListener>();

	public HibernateBlacklistManager() {
	}

	@Override
	public void addBlacklistListener(BlacklistListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removeBlacklistListener(BlacklistListener l) {
		listeners.remove(l);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void addPlayer(final Player person, final Player whom, final String comment) {
		if (person == null) {
			throw new NullPointerException("Person can't be null");
		}
		if (whom == null) {
			throw new NullPointerException("Whom can't be null");
		}

		final Session session = sessionFactory.getCurrentSession();
		BlacklistRecord record = (BlacklistRecord) session.get(HibernateBlacklistRecord.class, new BlacklistRecordId(person, whom));
		if (record == null) {
			record = new HibernateBlacklistRecord(person, whom, comment);
			session.save(record);
		} else {
			record = new HibernateBlacklistRecord(person, whom, comment);
			session.merge(record);
		}

		for (BlacklistListener listener : listeners) {
			listener.playerAdded(record);
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void removePlayer(final Player person, final Player whom) {
		if (person == null) {
			throw new NullPointerException("Person can't be null");
		}
		if (whom == null) {
			throw new NullPointerException("Whom can't be null");
		}

		final Session session = sessionFactory.getCurrentSession();
		final BlacklistRecord record = (BlacklistRecord) session.get(HibernateBlacklistRecord.class, new BlacklistRecordId(person, whom));
		if (record != null) {
			session.delete(record);
			for (BlacklistListener listener : listeners) {
				listener.playerRemoved(record);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public boolean isBlacklisted(Player person, Player whom) {
		if (person == null) {
			throw new NullPointerException("Person can't be null");
		}
		if (whom == null) {
			throw new NullPointerException("Whom can't be null");
		}

		@SuppressWarnings("unchecked")
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("select count(*) from HibernateBlacklistRecord where person=:pid and whom=:whom");
		query.setParameter("pid", person.getId());
		query.setParameter("whom", whom.getId());
		return ((Long) query.uniqueResult()) == 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public void checkBlacklist(Player person, Player whom) throws BlacklistedException {
		if (isBlacklisted(person, whom)) {
			throw new BlacklistedException();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public Collection<BlacklistRecord> getBlacklist(final Player person) {
		if (person == null) {
			throw new NullPointerException("Person can't be null");
		}
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("from HibernateBlacklistRecord where person=:pid");
		query.setParameter("pid", person.getId());
		return query.list();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
