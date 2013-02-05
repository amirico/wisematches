package wisematches.server.services.players.friends.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import wisematches.core.Player;
import wisematches.server.services.players.friends.FriendRelation;
import wisematches.server.services.players.friends.FriendsListener;
import wisematches.server.services.players.friends.FriendsManager;

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
	public void addFriend(Player person, Player friend, String comment) {
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
	public void removeFriend(Player person, Player friend) {
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
	public Collection<Long> getFriendsIds(Player person) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("select friend from HibernateFriendRelation where person=:pid");
		query.setParameter("pid", person.getId());
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<FriendRelation> getFriendsList(Player person) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("from HibernateFriendRelation where person=:pid");
		query.setParameter("pid", person.getId());
		return query.list();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
