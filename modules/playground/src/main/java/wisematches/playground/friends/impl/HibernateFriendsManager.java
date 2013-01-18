package wisematches.playground.friends.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import wisematches.core.Personality;
import wisematches.playground.friends.FriendRelation;
import wisematches.playground.friends.FriendsListener;
import wisematches.playground.friends.FriendsManager;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateFriendsManager implements FriendsManager {
	private SessionFactory sessionFactory;
	private final Collection<FriendsListener> listeners = new CopyOnWriteArraySet<FriendsListener>();

	public HibernateFriendsManager() {
	}

	@Override
	public void addFriendsListener(FriendsListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removeFriendsListener(FriendsListener l) {
		listeners.remove(l);
	}

	@Override
	public void addFriend(Personality person, Personality friend, String comment) {
		final Session session = sessionFactory.getCurrentSession();
		HibernateFriendRelation relation = (HibernateFriendRelation) session.get(HibernateFriendRelation.class, new HibernateFriendRelation.PK(person, friend));
		if (relation == null) {
			relation = new HibernateFriendRelation(person, friend, comment);
			session.save(relation);

			for (FriendsListener listener : listeners) {
				listener.friendAdded(person, friend, relation);
			}
		} else {
			relation.setComment(comment);
			session.update(relation);
		}
	}

	@Override
	public void removeFriend(Personality person, Personality friend) {
		final Session session = sessionFactory.getCurrentSession();
		HibernateFriendRelation relation = (HibernateFriendRelation) session.get(HibernateFriendRelation.class, new HibernateFriendRelation.PK(person, friend));
		if (relation != null) {
			session.delete(relation);

			for (FriendsListener listener : listeners) {
				listener.friendRemoved(person, friend, relation);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Long> getFriendsIds(Personality person) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("select friend from wisematches.playground.friends.impl.HibernateFriendRelation where person=:pid");
		query.setLong("pid", person.getId());
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<FriendRelation> getFriendsList(Personality person) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("from wisematches.playground.friends.impl.HibernateFriendRelation where person=:pid");
		query.setLong("pid", person.getId());
		return query.list();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
