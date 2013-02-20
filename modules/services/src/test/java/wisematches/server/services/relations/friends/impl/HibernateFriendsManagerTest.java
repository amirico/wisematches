package wisematches.server.services.relations.friends.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Player;
import wisematches.core.personality.DefaultMember;
import wisematches.server.services.relations.friends.FriendRelation;
import wisematches.server.services.relations.friends.FriendsListener;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/scribble-config.xml",
		"classpath:/config/playground-junit-config.xml"
})
public class HibernateFriendsManagerTest {
	@Autowired
	private HibernateFriendsManager friendsManager;

	public HibernateFriendsManagerTest() {
	}

	@Test
	public void test() {
		final Player p1 = new DefaultMember(901, null, null, null, null, null);
		final Player p2 = new DefaultMember(902, null, null, null, null, null);
		final Player p3 = new DefaultMember(903, null, null, null, null, null);

		final FriendsListener listener = createStrictMock(FriendsListener.class);
		listener.friendAdded(same(p1), same(p3), isA(FriendRelation.class));
		listener.friendAdded(same(p1), same(p2), isA(FriendRelation.class));
		listener.friendAdded(same(p3), same(p2), isA(FriendRelation.class));
		listener.friendRemoved(same(p1), same(p3), isA(FriendRelation.class));
		listener.friendRemoved(same(p3), same(p2), isA(FriendRelation.class));
		listener.friendRemoved(same(p1), same(p2), isA(FriendRelation.class));
		replay(listener);

		friendsManager.addFriendsListener(listener);

		friendsManager.addFriend(p1, p3, "asd");
		friendsManager.addFriend(p1, p2, "qwe");
		friendsManager.addFriend(p3, p2, "zxc");

		final List<FriendRelation> fl1 = new ArrayList<FriendRelation>(friendsManager.getFriendsList(p1));
		assertEquals(2, fl1.size());
		assertEquals(2, friendsManager.getFriendsIds(p1).size());
		assertRelation(fl1.get(0), p1, p2, "qwe");
		assertRelation(fl1.get(1), p1, p3, "asd");

		final List<FriendRelation> fl2 = new ArrayList<FriendRelation>(friendsManager.getFriendsList(p3));
		assertEquals(1, fl2.size());
		assertEquals(1, friendsManager.getFriendsIds(p3).size());
		assertRelation(fl2.get(0), p3, p2, "zxc");

		final List<FriendRelation> fl3 = new ArrayList<FriendRelation>(friendsManager.getFriendsList(p2));
		assertEquals(0, fl3.size());
		assertEquals(0, friendsManager.getFriendsIds(p2).size());

		friendsManager.removeFriend(p1, p3);
		assertEquals(1, friendsManager.getFriendsList(p1).size());

		friendsManager.removeFriend(p3, p2);
		assertEquals(0, friendsManager.getFriendsList(p3).size());

		friendsManager.removeFriend(p1, p1);
		assertEquals(1, friendsManager.getFriendsList(p1).size());

		friendsManager.addFriend(p1, p2, "hdfgh");
		final List<FriendRelation> fl4 = new ArrayList<FriendRelation>(friendsManager.getFriendsList(p1));
		assertEquals(1, fl4.size());
		assertRelation(fl1.get(0), p1, p2, "hdfgh");

		friendsManager.removeFriend(p1, p2);
		assertEquals(0, friendsManager.getFriendsList(p1).size());

		verify(listener);
	}

	private void assertRelation(FriendRelation f1, Player person, Player friend, String comment) {
		assertEquals(person.getId().longValue(), f1.getPerson());
		assertEquals(friend.getId().longValue(), f1.getFriend());
		assertEquals(comment, f1.getComment());
	}
}
