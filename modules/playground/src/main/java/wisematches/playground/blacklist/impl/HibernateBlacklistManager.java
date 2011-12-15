package wisematches.playground.blacklist.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.playground.blacklist.BlacklistListener;
import wisematches.playground.blacklist.BlacklistManager;
import wisematches.playground.blacklist.BlacklistRecord;
import wisematches.playground.blacklist.BlacklistedException;

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
	public void addPlayer(final Personality person, final Personality whom, final String comment) {
		if (person == null) {
			throw new NullPointerException("Person can't be null");
		}
		if (whom == null) {
			throw new NullPointerException("Whom can't be null");
		}

		final Session session = sessionFactory.getCurrentSession();
		BlacklistRecord record = (BlacklistRecord) session.get(BlacklistRecord.class, new BlacklistRecordId(person, whom));
		if (record == null) {
			record = new BlacklistRecord(person, whom, comment);
			session.save(record);
		} else {
			record = new BlacklistRecord(person, whom, comment);
			session.merge(record);
		}

		for (BlacklistListener listener : listeners) {
			listener.playerAdded(record);
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void removePlayer(final Personality person, final Personality whom) {
		if (person == null) {
			throw new NullPointerException("Person can't be null");
		}
		if (whom == null) {
			throw new NullPointerException("Whom can't be null");
		}

		final Session session = sessionFactory.getCurrentSession();
		final BlacklistRecord record = (BlacklistRecord) session.get(BlacklistRecord.class, new BlacklistRecordId(person, whom));
		if (record != null) {
			session.delete(record);
			for (BlacklistListener listener : listeners) {
				listener.playerRemoved(record);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public boolean isBlacklisted(Personality person, Personality whom) {
		if (person == null) {
			throw new NullPointerException("Person can't be null");
		}
		if (whom == null) {
			throw new NullPointerException("Whom can't be null");
		}

		@SuppressWarnings("unchecked")
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("select count(*) from wisematches.playground.blacklist.BlacklistRecord where person=? and whom=?");
		query.setParameter(0, person.getId());
		query.setParameter(1, whom.getId());
		return ((Long) query.uniqueResult()) == 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public void checkBlacklist(Personality person, Personality whom) throws BlacklistedException {
		if (isBlacklisted(person, whom)) {
			throw new BlacklistedException();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public Collection<BlacklistRecord> getBlacklist(final Personality person) {
		if (person == null) {
			throw new NullPointerException("Person can't be null");
		}
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("from wisematches.playground.blacklist.BlacklistRecord where person=?");
		query.setParameter(0, person.getId());
		return query.list();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
