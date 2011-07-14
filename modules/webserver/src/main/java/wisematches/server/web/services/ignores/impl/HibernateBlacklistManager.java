package wisematches.server.web.services.ignores.impl;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.server.web.services.ignores.BlacklistManager;
import wisematches.server.web.services.ignores.BlacklistRecord;

import java.util.Collection;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateBlacklistManager extends HibernateDaoSupport implements BlacklistManager {
	public HibernateBlacklistManager() {
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

		getHibernateTemplate().bulkUpdate("delete from wisematches.server.web.services.ignores.BlacklistRecord " +
				"where person=? and whom=?", person.getId(), whom.getId());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public boolean isIgnored(Personality person, Personality whom) {
		if (person == null) {
			throw new NullPointerException("Person can't be null");
		}
		if (whom == null) {
			throw new NullPointerException("Whom can't be null");
		}

		@SuppressWarnings("unchecked")
		final List<Long> longs = (List<Long>) getHibernateTemplate().find(
				"select count(*) from wisematches.server.web.services.ignores.BlacklistRecord " +
						"where person=? and whom=?", person.getId(), whom.getId());
		return DataAccessUtils.uniqueResult(longs) == 1;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public Collection<BlacklistRecord> getBlacklist(final Personality person) {
		if (person == null) {
			throw new NullPointerException("Person can't be null");
		}
		return (Collection<BlacklistRecord>) getHibernateTemplate().find(
				"from wisematches.server.web.services.ignores.BlacklistRecord where person=?", person.getId());
	}
}
