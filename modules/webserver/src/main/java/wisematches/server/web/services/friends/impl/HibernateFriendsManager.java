package wisematches.server.web.services.friends.impl;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.personality.Personality;
import wisematches.server.web.services.friends.FriendRelation;
import wisematches.server.web.services.friends.FriendsListener;
import wisematches.server.web.services.friends.FriendsManager;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateFriendsManager extends HibernateDaoSupport implements FriendsManager {
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
		HibernateTemplate template = getHibernateTemplate();
		HibernateFriendRelation relation = template.get(HibernateFriendRelation.class, new HibernateFriendRelation.PK(person, friend));
		if (relation == null) {
			relation = new HibernateFriendRelation(person, friend, comment);
			template.save(relation);

			for (FriendsListener listener : listeners) {
				listener.friendAdded(person, friend, relation);
			}
		} else {
			relation.setComment(comment);
			template.update(relation);
		}
	}

	@Override
	public void removeFriend(Personality person, Personality friend) {
		HibernateTemplate template = getHibernateTemplate();
		HibernateFriendRelation relation = template.get(HibernateFriendRelation.class, new HibernateFriendRelation.PK(person, friend));
		if (relation != null) {
			template.delete(relation);

			for (FriendsListener listener : listeners) {
				listener.friendRemoved(person, friend, relation);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Long> getFriendsIds(Personality person) {
		return getHibernateTemplate().find("select friend from wisematches.server.web.services.friends.impl.HibernateFriendRelation where person=?", person.getId());
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<FriendRelation> getFriendsList(Personality person) {
		return getHibernateTemplate().find("from wisematches.server.web.services.friends.impl.HibernateFriendRelation where person=?", person.getId());
	}
}
