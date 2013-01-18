package wisematches.core.personality.member.membership.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Personality;
import wisematches.core.expiration.ExpirationListener;
import wisematches.core.personality.member.Membership;
import wisematches.core.personality.member.membership.MembershipCard;
import wisematches.core.personality.member.membership.MembershipExpiration;
import wisematches.core.personality.member.membership.MembershipListener;
import wisematches.core.personality.member.membership.MembershipManager;
import wisematches.core.task.CleaningDayListener;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateMembershipManager implements MembershipManager, CleaningDayListener {
	private SessionFactory sessionFactory;

	private final Set<MembershipListener> listeners = new CopyOnWriteArraySet<>();
	private final Set<ExpirationListener<Personality, MembershipExpiration>> expirationListeners = new CopyOnWriteArraySet<>();

	private static final Log log = LogFactory.getLog("wisematches.server.membership");

	public HibernateMembershipManager() {
	}

	@Override
	public void addMembershipListener(MembershipListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removeMembershipListener(MembershipListener l) {
		listeners.remove(l);
	}

	@Override
	public void addExpirationListener(ExpirationListener<Personality, MembershipExpiration> l) {
		if (l != null) {
			expirationListeners.add(l);
		}
	}

	@Override
	public void removeExpirationListener(ExpirationListener<Personality, MembershipExpiration> l) {
		expirationListeners.remove(l);
	}

	@Override
	public Membership getMembership(Personality personality) {
		final MembershipCard card = getPlayerMembership(personality);
		if (card == null) {
			return Membership.DEFAULT;
		}
		if (card.isExpired()) {
			return Membership.DEFAULT;
		}
		return card.getMembership();
	}

	@Override
	public MembershipCard getPlayerMembership(Personality person) {
		if (person == null) {
			throw new NullPointerException("Person can't be null");
		}
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("from HibernateMembershipCard where player=:pid");
		query.setLong("pid", person.getId());
		return (MembershipCard) query.uniqueResult();
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public MembershipCard setPlayerMembership(Personality person, Membership membership, Date expiration) {
		if (person == null) {
			throw new NullPointerException("Person can't be null");
		}
		if (membership == null) {
			throw new NullPointerException("Membership can't be null");
		}
		if (expiration == null) {
			throw new NullPointerException("Expiration can't be null");
		}
		if (expiration.getTime() <= System.currentTimeMillis()) {
			throw new IllegalArgumentException("Expiration can't be in past");
		}

		final Session session = sessionFactory.getCurrentSession();
		HibernateMembershipCard old = null;
		HibernateMembershipCard card = (HibernateMembershipCard) getPlayerMembership(person);
		if (card == null) {
			card = new HibernateMembershipCard(person.getId(), membership, expiration);
			session.save(card);
		} else {
			old = card.clone();
			card.setExpiration(expiration);
			card.setMembership(membership);
			session.update(card);
		}
		for (MembershipListener listener : listeners) {
			listener.membershipCardUpdated(person, old, card);
		}
		return card;
	}

	@Override
	public MembershipCard removePlayerMembership(Personality person) {
		final MembershipCard card = getPlayerMembership(person);
		if (card != null) {
			sessionFactory.getCurrentSession().delete(card);

			for (MembershipListener listener : listeners) {
				listener.membershipCardUpdated(person, card, null);
			}
		}
		return card;
	}

	@Override
	public MembershipExpiration[] getExpirationPoints() {
		return MembershipExpiration.values();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void cleanup(Date today) {
		log.info("Cleanup memberships");

		final Session session = sessionFactory.getCurrentSession();
		final Query expQuery = session.createQuery("select player from HibernateMembershipCard where expiration<=:date");
		expQuery.setDate("date", new Date(today.getTime() + MembershipExpiration.DAY.getRemainedTime()));
		for (Object o : expQuery.list()) {
			final Personality p = Personality.person(((Number) o).longValue());
			for (ExpirationListener<Personality, MembershipExpiration> listener : expirationListeners) {
				listener.expirationTriggered(p, MembershipExpiration.DAY);
			}
		}

		final Query query = session.createQuery("from HibernateMembershipCard where expiration<=:date");
		query.setDate("date", today);
		for (Object o : query.list()) {
			final HibernateMembershipCard old = (HibernateMembershipCard) o;
			final Personality p = Personality.person(old.getPlayer());
			session.delete(old);

			log.info("Membership expired and was removed: " + old);

			for (MembershipListener listener : listeners) {
				listener.membershipCardUpdated(p, old, null);
			}
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
