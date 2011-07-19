package wisematches.playground.blacklist.impl;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.playground.blacklist.BlacklistListener;
import wisematches.playground.blacklist.BlacklistManager;
import wisematches.playground.blacklist.BlacklistRecord;
import wisematches.playground.blacklist.BlacklistedException;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateBlacklistManager extends HibernateDaoSupport implements BlacklistManager {
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

		final HibernateTemplate template = getHibernateTemplate();
		BlacklistRecord record = template.get(BlacklistRecord.class, new BlacklistRecordId(person, whom));
		if (record == null) {
			record = new BlacklistRecord(person, whom, comment);
			template.save(record);
		} else {
			record = new BlacklistRecord(person, whom, comment);
			template.merge(record);
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

		final HibernateTemplate template = getHibernateTemplate();
		BlacklistRecord record = template.get(BlacklistRecord.class, new BlacklistRecordId(person, whom));
		if (record != null) {
			template.delete(record);
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
		final List<Long> longs = (List<Long>) getHibernateTemplate().find(
				"select count(*) from wisematches.playground.blacklist.BlacklistRecord " +
						"where person=? and whom=?", person.getId(), whom.getId());
		return DataAccessUtils.uniqueResult(longs) == 1;
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
		return (Collection<BlacklistRecord>) getHibernateTemplate().find(
				"from wisematches.playground.blacklist.BlacklistRecord where person=?", person.getId());
	}
}
