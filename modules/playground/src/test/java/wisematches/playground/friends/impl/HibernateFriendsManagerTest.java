package wisematches.playground.friends.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.personality.proprietary.robot.RobotPlayer;
import wisematches.playground.friends.FriendRelation;
import wisematches.playground.friends.FriendsListener;

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
		"classpath:/config/accounts-config.xml",
		"classpath:/config/playground-junit-config.xml"
})
public class HibernateFriendsManagerTest {
	@Autowired
	private HibernateFriendsManager friendsManager;

	public HibernateFriendsManagerTest() {
	}

	@Test
	public void test() {
		final FriendsListener listener = createStrictMock(FriendsListener.class);
		listener.friendAdded(same(RobotPlayer.DULL), same(RobotPlayer.EXPERT), isA(FriendRelation.class));
		listener.friendAdded(same(RobotPlayer.DULL), same(RobotPlayer.TRAINEE), isA(FriendRelation.class));
		listener.friendAdded(same(RobotPlayer.EXPERT), same(RobotPlayer.TRAINEE), isA(FriendRelation.class));
		listener.friendRemoved(same(RobotPlayer.DULL), same(RobotPlayer.EXPERT), isA(FriendRelation.class));
		listener.friendRemoved(same(RobotPlayer.EXPERT), same(RobotPlayer.TRAINEE), isA(FriendRelation.class));
		listener.friendRemoved(same(RobotPlayer.DULL), same(RobotPlayer.TRAINEE), isA(FriendRelation.class));
		replay(listener);

		friendsManager.addFriendsListener(listener);

		friendsManager.addFriend(RobotPlayer.DULL, RobotPlayer.EXPERT, "asd");
		friendsManager.addFriend(RobotPlayer.DULL, RobotPlayer.TRAINEE, "qwe");
		friendsManager.addFriend(RobotPlayer.EXPERT, RobotPlayer.TRAINEE, "zxc");

		final List<FriendRelation> fl1 = new ArrayList<FriendRelation>(friendsManager.getFriendsList(RobotPlayer.DULL));
		assertEquals(2, fl1.size());
		assertEquals(2, friendsManager.getFriendsIds(RobotPlayer.DULL).size());
		assertRelation(fl1.get(0), RobotPlayer.DULL, RobotPlayer.TRAINEE, "qwe");
		assertRelation(fl1.get(1), RobotPlayer.DULL, RobotPlayer.EXPERT, "asd");

		final List<FriendRelation> fl2 = new ArrayList<FriendRelation>(friendsManager.getFriendsList(RobotPlayer.EXPERT));
		assertEquals(1, fl2.size());
		assertEquals(1, friendsManager.getFriendsIds(RobotPlayer.EXPERT).size());
		assertRelation(fl2.get(0), RobotPlayer.EXPERT, RobotPlayer.TRAINEE, "zxc");

		final List<FriendRelation> fl3 = new ArrayList<FriendRelation>(friendsManager.getFriendsList(RobotPlayer.TRAINEE));
		assertEquals(0, fl3.size());
		assertEquals(0, friendsManager.getFriendsIds(RobotPlayer.TRAINEE).size());

		friendsManager.removeFriend(RobotPlayer.DULL, RobotPlayer.EXPERT);
		assertEquals(1, friendsManager.getFriendsList(RobotPlayer.DULL).size());

		friendsManager.removeFriend(RobotPlayer.EXPERT, RobotPlayer.TRAINEE);
		assertEquals(0, friendsManager.getFriendsList(RobotPlayer.EXPERT).size());

		friendsManager.removeFriend(RobotPlayer.DULL, RobotPlayer.DULL);
		assertEquals(1, friendsManager.getFriendsList(RobotPlayer.DULL).size());

		friendsManager.addFriend(RobotPlayer.DULL, RobotPlayer.TRAINEE, "hdfgh");
		final List<FriendRelation> fl4 = new ArrayList<FriendRelation>(friendsManager.getFriendsList(RobotPlayer.DULL));
		assertEquals(1, fl4.size());
		assertRelation(fl1.get(0), RobotPlayer.DULL, RobotPlayer.TRAINEE, "hdfgh");

		friendsManager.removeFriend(RobotPlayer.DULL, RobotPlayer.TRAINEE);
		assertEquals(0, friendsManager.getFriendsList(RobotPlayer.DULL).size());

		verify(listener);
	}

	private void assertRelation(FriendRelation f1, RobotPlayer person, RobotPlayer friend, String comment) {
		assertEquals(person.getId(), f1.getPerson());
		assertEquals(friend.getId(), f1.getFriend());
		assertEquals(comment, f1.getComment());
	}
}
